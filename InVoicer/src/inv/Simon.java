package inv;
import java.awt.*;
import java.awt.geom.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.*;

public class Simon extends JPanel implements MouseListener{
	//TODO play sounds on flash/click?
	Integer[] sequence = new Integer[31];
	JFrame frame;
	Area greenButton, redButton, yellowButton, blueButton, sub;
	int topLeft=0,topRight=0,bottomLeft=0,bottomRight=0;
	//0 for dark(pressed), 1 for neutral, 2 for lit up
	public static int WIDTH=Invoicer.WIDTH-20,HEIGHT=Invoicer.HEIGHT-20;
	int currentClick=0;
	final int tl=1,tr=2,bl=3,br=4;
	boolean clickEnabled=false;
	boolean started = false;
	int currentLevel;
	int clickProgress;
	int highScore=-1;
	JSlider slider;
	Clip redTone, blueTone, greenTone, yellowTone;
	Clip[] sounds;
	
	public Simon() {
		super();
		//frame = new JFrame("Simon");
		//frame.setSize(WIDTH,HEIGHT);
		setBackground(new Color(36,36,36));
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//frame.add(this);
		//frame.setVisible(true);
		//frame.repaint();
		
	    Ellipse2D innerCircle = new Ellipse2D.Double(WIDTH*275/800, HEIGHT*275/800-20, WIDTH*250/800, HEIGHT*250/800);
	    Rectangle2D vBar = new Rectangle2D.Double(WIDTH/2-10, HEIGHT*125/800-20,20, HEIGHT*550/800);
	    Rectangle2D hBar=new Rectangle2D.Double(WIDTH*125/800, HEIGHT/2-30,WIDTH*550/800, 20);
		
	    sub = new Area(innerCircle);
	    sub.add(new Area(vBar));
	    sub.add(new Area(hBar));
	    
	    
	    greenButton= new Area(new Arc2D.Double(WIDTH*125/800, HEIGHT*125/800-20, WIDTH*550/800, HEIGHT*550/800, 90, 90,Arc2D.PIE));
	    redButton= new Area(new Arc2D.Double(WIDTH*125/800, HEIGHT*125/800-20, WIDTH*550/800, HEIGHT*550/800, 90, -90,Arc2D.PIE));
	    yellowButton= new Area(new Arc2D.Double(WIDTH*125/800, HEIGHT*125/800-20, WIDTH*550/800, HEIGHT*550/800, 180, 90,Arc2D.PIE));
	    blueButton= new Area(new Arc2D.Double(WIDTH*125/800, HEIGHT*125/800-20, WIDTH*550/800, HEIGHT*550/800, -90, 90,Arc2D.PIE));
		
	    greenButton.subtract(sub);
	    redButton.subtract(sub);
	    yellowButton.subtract(sub);
	    blueButton.subtract(sub);
	    
		addMouseListener(this);
		slider = new JSlider(0,850,500);
		slider.setBackground(new Color(36,36,36));
		JPanel bufferPanel = new JPanel();
		bufferPanel.setOpaque(false);
		bufferPanel.setPreferredSize(new Dimension(WIDTH,HEIGHT*6/7));
		slider.setPreferredSize(new Dimension(WIDTH/2,HEIGHT/20));
		JLabel sneakyLabel = new JLabel("Speed");
		sneakyLabel.setForeground(getBackground());
		add(bufferPanel);
		add(sneakyLabel);
		
		add(slider);
		JLabel label = new JLabel("Speed");
		
		label.setForeground(Color.white);
		add(label);
		//frame.paintAll(frame.getGraphics());
		this.paintAll(getGraphics());
		
		//load sounds
		redTone=loadClip("red.wav");
		blueTone=loadClip("blue.wav");
		yellowTone=loadClip("yellow.wav");
		greenTone=loadClip("green.wav");
		
		sounds = new Clip[]{greenTone,redTone,yellowTone,blueTone};
			for(Clip c: sounds) {
				c.setFramePosition(c.getFrameLength()-2);
				c.start();
				
			}
		
	}
	Clip loadClip(String path) {
        try {
        	java.net.URL is = this.getClass().getResource(path);
        	//File file = new File(path);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(is);

            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            return clip;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
	@Override
	public void paintComponent(Graphics g1) {
		super.paintComponent(g1);
		Graphics2D g = (Graphics2D) g1;
		g.setColor(new Color(67,67,67));
		g.fillOval(WIDTH/8, HEIGHT/8-20, WIDTH*3/4, HEIGHT*3/4);
		
		Color[][] colors = {{Color.green.darker().darker(),Color.green.darker(),Color.green},{Color.red.darker().darker(),Color.red.darker(),Color.red},{Color.yellow.darker().darker(),Color.yellow.darker(),Color.yellow},{Color.blue.darker().darker(),Color.blue.darker(),Color.blue}};
		
		g.setColor(colors[0][topLeft]);
		g.fill(greenButton);
		g.setColor(colors[1][topRight]);
		g.fill(redButton);
		g.setColor(colors[2][bottomLeft]);
		g.fill(yellowButton);
		g.setColor(colors[3][bottomRight]);
		g.fill(blueButton);
		
		if(highScore!=-1) {
			g.setColor(Color.white);
			g.setFont(getFont().deriveFont(HEIGHT/18f));
			//g.drawString("High Score: "+highScore, WIDTH/2-HEIGHT*5/36, HEIGHT*9/10);
			g.drawString("High Score", WIDTH/40, HEIGHT/40+20);
			g.drawString(""+highScore, WIDTH/40+HEIGHT*2/18, HEIGHT/40+20+HEIGHT/18+HEIGHT/40);
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		if(!clickEnabled) return;
		// TODO Auto-generated method stub
		
		//check which of the 4 colors it is, 
		//if it is one of them set their respective variable to 0 for pressed 
		//and set currentClick to their value
		if(greenButton.contains(getMousePosition())) {
			topLeft=0;
			currentClick=tl;
		} else if(redButton.contains(getMousePosition())) {
			topRight=0;
			currentClick=tr;
		} else if(yellowButton.contains(getMousePosition())) {
			bottomLeft=0;
			currentClick=bl;
		} else if(blueButton.contains(getMousePosition())) {
			bottomRight=0;
			currentClick=br;
		}
		repaint();
	}
	void click(int loc) {
		if(sequence[clickProgress]!=loc) {
			//you lose
			topLeft=0;topRight=0;bottomLeft=0;bottomRight=0;
			started=false;
			clickEnabled=false;
			if(clickProgress>highScore) highScore=clickProgress;
			if(currentLevel-1>highScore) highScore=currentLevel-1;
			repaint();
		} else {
			clickProgress++;
			if(clickProgress==31) {
				//you win!
				topLeft=topRight=bottomLeft=bottomRight=2;
				started=false;
				clickEnabled=false;
				highScore=-1;
				repaint();
				return;
			}
			if(clickProgress==currentLevel) {
				//win this round
				clickProgress=0;
				currentLevel++;
				showSequence(currentLevel);
			}
		}
	}
	void start() {
		started=true;
		for(int i = 0; i<31;i++) {
			sequence[i]=new Random().nextInt(4)+1;
		}
		currentLevel=1;
		clickProgress=0;
		topLeft=topRight=bottomLeft=bottomRight=1;
		repaint();
		showSequence(currentLevel);
	}
	void showSequence(int count) {
		clickEnabled=false;
		slider.setEnabled(false);
		int[] i = {0};
		boolean[] flashTime= {true};
		Timer timer = new Timer(1000-slider.getValue(),e->{
			if(flashTime[0]) {
				flash(sequence[i[0]]);
			} else {
				unflash(sequence[i[0]]);
				i[0]++;
			}
			flashTime[0]=!flashTime[0];
			if(i[0]>count-1) {
				clickEnabled=true;
				((Timer) e.getSource()).stop();
				slider.setEnabled(true);
			}
			
		});
		timer.start();
	}
	void flash(int loc) {
		//1 to 4
		switch(loc) {
		case tl:
			topLeft=2;
			break;
		case tr:
			topRight=2;
			break;
		case bl:
			bottomLeft=2;
			break;
		case br: 
			bottomRight=2;
			break;
		}
		sounds[loc-1].stop();
		sounds[loc-1].setFramePosition(0);
		sounds[loc-1].start();
		repaint();
	}
	void unflash(int loc) {

		switch(loc) {
		case tl:
			topLeft=1;
			break;
		case tr:
			topRight=1;
			break;
		case bl:
			bottomLeft=1;
			break;
		case br: 
			bottomRight=1;
			break;
		}
		repaint();
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		if(!started) {
			start();
			return;
		}
		if(!clickEnabled) {
			return;
		}
		// TODO Auto-generated method stub
		//if currentClick!=0 then count a click on a certain button
		if(currentClick!=0) {
				click(currentClick);
		}
		if(!started) return;
		//set currentclick to 0
		currentClick=0;
		topLeft=topRight=bottomLeft=bottomRight=1;
		repaint();
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
