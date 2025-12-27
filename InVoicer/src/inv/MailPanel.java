package inv;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.*;

public class MailPanel extends MenuPanel{
	//this class should handle the email api and also the shortcut for autogenning every client's pdf for one doctor
	
	//TODO: have a button thats says 'advance all dates for clients of this doctor by a month'

	//add a jcombobox where you can select a doctor and theres a button below it
	//it for-eaches each client and generates a pdf where you advance all their dates by a month
	
	//what is the mail going to be? idk lol
	//maybe keep a record of all pdfs generated while the program is running and show a 'recently created pdfs' thing
	//and you can click them to send the email to the emails in the client
	static ArrayList<String> doctorList = new ArrayList<String>();
	static JComboBox<String> docBox;
	JButton parseButton;
	JLabel parseLabel;
	public MailPanel() {
		super();
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		docBox = new JComboBox<String>(new DefaultComboBoxModel<String>());
		updateDoctorList();
		
		parseButton = new JButton("make");
		parseLabel = new JLabel("Make a PDF for each of this\n"
							+ "doctor's clients and move all\n"
							+ "their service dates by a month");
		parseLabel.setForeground(Color.white);
		
		docBox.setMaximumSize(new Dimension(Invoicer.WIDTH,Invoicer.HEIGHT/12));
		
		add(docBox);	
		add(parseLabel);
		add(parseButton);
	}
	public static void updateDoctorList() {
		doctorList.clear();
		for(ClientBox cb: ClientPanel.clientList) {
			String doc = cb.client.doctor;
			if(!doctorList.contains(doc) ) {
				doctorList.add(doc);
			}
		}
		((DefaultComboBoxModel<String>)docBox.getModel()).removeAllElements();
		((DefaultComboBoxModel<String>)docBox.getModel()).addAll(doctorList);
		
	}
}
