import java.awt.Color;

import javax.swing.*;

public class InVoicer {
	
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
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setVisible(true);
		frame.setLayout(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//one panel for controls, one for display
		JPanel controlPanel = new JPanel();
		JButton addB = new JButton("Create");
		controlPanel.add(addB);
		JButton editB = new JButton("Edit");
		controlPanel.add(editB);
			controlPanel.setSize(600, 80);
			controlPanel.setBorder(BorderFactory.createLineBorder(Color.black));
			frame.add(controlPanel);
		JPanel displayPanel = new JPanel();
		displayPanel.setSize(600, 520);
		displayPanel.setBorder(BorderFactory.createLineBorder(Color.red));
		frame.add(displayPanel);

		
		frame.setSize(600,640);
		
	}
}