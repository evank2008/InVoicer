package inv;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.*;
//main class for running and holding the central frame
//TODO: line 25 add icons to the tabs
//		add file saving/reading system
public class Invoicer {
	static JFrame frame;
	static ClientPanel clp;
	static CreatorPanel crp;
	static RecordsPanel rp;
	static SelectionScreen ss;
	static boolean onMac=System.getProperty("os.name").substring(0,3).toLowerCase().equals("mac");
	public static final int WIDTH = 800, HEIGHT = 700;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		clp = new ClientPanel();
		crp = new CreatorPanel();
		rp = new RecordsPanel();
		ss = new SelectionScreen();
		new Invoicer();
	}

	public Invoicer() {

		frame = new JFrame("Invoicer");
		frame.setSize(WIDTH, HEIGHT);
		frame.setVisible(true);
		
		ss.addTab("Clients", clp);
		ss.addTab("Invoice", crp);
		ss.addTab("Records", rp);
		
		clp.setBackground(new Color(36,36,36));
		crp.setBackground(new Color(36,36,36));
		rp.setBackground(new Color(36,36,36));
		
		//ss.setSize(WIDTH,HEIGHT);
		frame.add(ss);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}

}
