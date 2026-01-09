package inv;

import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.time.LocalDate;
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
	JPanel autogenPanel;
	JPanel bufferPanel;
	JLabel genLabel;
	Thread t;
	public MailPanel() {
		super();
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		docBox = new JComboBox<String>(new DefaultComboBoxModel<String>());
		updateDoctorList();
		
		parseButton = new JButton("Doctor Autogen");
		parseButton.setToolTipText("Make a PDF for each of this\n"
				+ "doctor's clients that have had one made before and move all\n"
				+ "their dates up by a month");
		
		parseButton.addActionListener(e->{
			//do parsing stuff
			genLabel.setText(autoGen());
			t=new Thread(()->{
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				genLabel.setText("");
			});
			t.start();
		});
		
		docBox.setMaximumSize(new Dimension(Invoicer.WIDTH,Invoicer.HEIGHT/12));
		
		genLabel = new JLabel();
		genLabel.setForeground(Color.green);
		genLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		
		
		autogenPanel = new JPanel();
		autogenPanel.setMaximumSize(new Dimension(Invoicer.WIDTH/3,Invoicer.HEIGHT/5));
		autogenPanel.setBackground(new Color(36,36,36));
		
		JPanel bufferPanel = new JPanel();
		bufferPanel.setPreferredSize(new Dimension(Invoicer.WIDTH/3,Invoicer.HEIGHT/15));
		bufferPanel.setOpaque(false);
		
		autogenPanel.add(docBox);	
		autogenPanel.add(bufferPanel);
		autogenPanel.add(parseButton);
		autogenPanel.add(genLabel);
		
		bufferPanel = new JPanel();
		bufferPanel.setBackground(new Color(36,36,36));
		bufferPanel.setMaximumSize(new Dimension(Invoicer.WIDTH/3,Invoicer.HEIGHT/30));
		
		add(bufferPanel);
		add(autogenPanel);
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
	String autoGen() {
		if(docBox.getSelectedItem()==null) return "null selection";
		int i=0;
		for(ClientBox cb: ClientPanel.clientList) {
			Client c = cb.client;
			//only gen if correct doctor and also if the client has an autogen
			if((!c.doctor.equals(docBox.getSelectedItem()))||c.service.equals("null")||!c.isActive) continue;
			try {
				if(c.serviceDate.isAfter(LocalDate.now().plusMonths(1))) return "<html>Client "+c.name+" has a<br>service date after current date</html>";
				Invoicer.crp.generatePDF(c, c.service, c.expectedAmt, c.hourly, c.serviceDate, LocalDate.now());
				i++;
				//now increment the dates by a month
				c.serviceDate=c.serviceDate.plusMonths(1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "error occurred: "+e.getMessage();
			}
		}
		return "Generated "+i+" invoices.";
	}
}
