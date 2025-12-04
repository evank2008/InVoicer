package inv;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.*;
//main class for running and holding the central frame
//TODO: line 25 add icons to the tabs
//		add file saving/reading system
//		maybe just make it save on close? with the jframe
public class Invoicer {
	static JFrame frame;
	static ClientPanel clp;
	static CreatorPanel crp;
	static RecordsPanel rp;
	static SelectionScreen ss;
	static boolean onMac=System.getProperty("os.name").substring(0,3).toLowerCase().equals("mac");
	public static final int WIDTH = 700, HEIGHT = 700;

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
		
		ss.addChangeListener(e-> {
	        if(e.getSource()==crp) {
	        	System.out.println("crp cslected");
	        }
	    });
		
		//ss.setSize(WIDTH,HEIGHT);
		frame.add(ss);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				saveAllData();
				System.exit(0);
			}
		});
		
	}
	void saveAllData() {
		ClientPanel.updateClientData();
		//TODO: make this save to files etc
		//also probably update the data for the other two panels when you get around to them
		//cant really write the data aving code until all the data fields are set up anyway
	}

}
