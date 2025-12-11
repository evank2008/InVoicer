package inv;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
//main class for running and holding the central frame
//TODO: line 25 add icons to the tabs
//		add file saving/reading system
//		maybe just make it save on close? with the jframe
public class Invoicer {
	String errorMsg="";
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
		loadFile();
	}
	void saveAllData() {
		ClientPanel.updateClientData();
		//commas should separate things within categories
		//should be the same amount of commas for each bracket i think? is that wrong
		//split repeatable things with <>s
		//take clients for instance you would first split by <client>
		//a client's tofilestring is name,doctor,amount<contactList>contactstuffwithcommas separated by <contact>
		//first split by <contactList> then split first one by commas and the second by <contact> and then by commas, system works

		BufferedWriter bw;
		
		try {
			bw = new BufferedWriter(new FileWriter(
					new File(FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "/invoicerData.txt")));
				String s=clp.toFileString();
				bw.write(s);
				if(s.equals("Empty")) {bw.close();return;};
				bw.newLine();
				bw.append(rp.toFileString());
				bw.close();
				System.out.println("saved successfully");
		} catch (IOException e) {
			Arrays.asList(e.getStackTrace()).forEach(j->errorMsg+=j.toString()+"\n");
			JOptionPane.showMessageDialog(null, "Error occurred while saving: "+errorMsg);
			e.printStackTrace();
		}
	
	}
	void loadFile() {
		String path=FileSystemView.getFileSystemView().getDefaultDirectory().getPath()+ "/invoicerData.txt";
		File dataFile = new File(path);
		if(!dataFile.exists()) {
			JOptionPane.showMessageDialog(null,"Welcome to the Invoicer! Your data file will be saved to "+path+". Enjoy!");
			return;
		}
		try {
		BufferedReader br = new BufferedReader(new FileReader(dataFile));
		String s = br.readLine();
		if(s.equals("Empty")) return;
		clp.loadData(s);
		rp.loadData(br.readLine());
		String recordsDataLine=br.readLine();
		} catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Error occurred while loading: "+e.toString());
			e.printStackTrace();
		}
	}
}
