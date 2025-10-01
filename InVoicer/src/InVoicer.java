import java.awt.Color;
import java.util.ArrayList;

import javax.swing.*;

public class InVoicer {
	//TODO: figure out how to use a jtable
	
	//this program will create a window where you can input and view
	//transaction histories that are paid or unpaid
	//button to filter by properties
	//way to interact with each entry to manipulate properties
		//"paid" button etc
		//box that tracks when last updated
		//give the paid ones a darker background
	//store the data in a file and load it on opening
	//when making a new invoice give a lil dropdown menu of all past clients or 
	//an entry text box
	static ArrayList<Invoice> invoiceList = new ArrayList<Invoice>();
	static int width=600;
	static int height=640;
	static JFrame frame = new JFrame();

	public static void main(String[] args) {
		loadList();
		frame.setVisible(true);
		frame.setLayout(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//one panel for controls, one for display
		JPanel controlPanel = new JPanel();
		JButton addB = new JButton("Create");
		controlPanel.add(addB);
		JButton editB = new JButton("Edit");
		controlPanel.add(editB);
			controlPanel.setSize(width, height/8);
			controlPanel.setBorder(BorderFactory.createLineBorder(Color.black));
			frame.add(controlPanel);
			
		JPanel displayPanel = new DisplayPanel();
		displayPanel.setSize(600, 520);
		displayPanel.setBorder(BorderFactory.createLineBorder(Color.red));
		frame.add(displayPanel);
		
//new JTable(new String[][] {{"pizza","pozza"},{"hod dog","had dog"}},new String[] {"big","if"});
		
		frame.setSize(width,height);
		
	}
	
	static void loadList() {
		//temporary test invoice
		Invoice i = new Invoice(600,"John Cliente");
		Invoice j = new Invoice(4567.88,"Sammy Rich");
		invoiceList.add(i);
		invoiceList.add(j);
	}
}