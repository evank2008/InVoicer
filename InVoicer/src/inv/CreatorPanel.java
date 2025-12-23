package inv;

import java.awt.*;
import java.time.LocalDate;
import java.util.Date;

import javax.swing.*;

import com.github.lgooddatepicker.components.DatePicker;

//this class should allow you to create an invoice
//TODO: pdf generation
//have a button thats says 'advance all dates for clients of this doctor by a month'
public class CreatorPanel extends MenuPanel {
	DatePicker serviceDatePicker, billDatePicker;
	JLabel clientLabel, serviceDateLabel, billDateLabel, serviceFieldLabel, amountLabel, hourlyLabel;
	JLabel errorLabel, successLabel;
	JTextField serviceField;
	JTextField amountField;
	JTextField hourlyField;
	JButton generateButton;
	//two date pickers
	JComboBox<Client> clientPicker;
	public CreatorPanel() {
		super();
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		
		clientPicker = new JComboBox<Client>(new DefaultComboBoxModel<Client>());
		clientPicker.setBackground(new Color(200,200,200));
		clientPicker.setForeground(Color.black);
		updateClientPicker();
		clientPicker.setMaximumSize(new Dimension(Invoicer.WIDTH*8/10,Invoicer.HEIGHT/12));
		
		
		clientLabel = new JLabel("Client");
		clientLabel.setForeground(Color.white);
		clientLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, Invoicer.HEIGHT/25));
		clientLabel.setAlignmentX(LEFT_ALIGNMENT);
		clientPicker.setFont(new Font(Font.SANS_SERIF, Font.BOLD, Invoicer.HEIGHT/30));
		
		add(clientLabel);
		add(clientPicker);
		
		
		add(bufferPanel());
		
		serviceFieldLabel = new JLabel("Service Performed");
		serviceFieldLabel.setForeground(Color.white);
		serviceFieldLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, Invoicer.HEIGHT/30));
		serviceField = new JTextField();
		serviceField.setMaximumSize(new Dimension(Invoicer.WIDTH*8/10,Invoicer.HEIGHT/20));
		serviceField.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, Invoicer.HEIGHT/35));

		add(serviceFieldLabel);
		add(serviceField);
		add(bufferPanel());
		
		amountLabel = new JLabel("Billing Amount");
		amountLabel.setForeground(Color.white);
		amountLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, Invoicer.HEIGHT/30));
		amountField = new JTextField();
		amountField.setMaximumSize(new Dimension(Invoicer.WIDTH*8/10,Invoicer.HEIGHT/20));
		amountField.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, Invoicer.HEIGHT/35));

		add(amountLabel);
		add(amountField);
		add(bufferPanel());
		
		hourlyLabel = new JLabel("Hourly Rate (Optional)");
		hourlyLabel.setForeground(Color.white);
		hourlyLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, Invoicer.HEIGHT/30));
		hourlyField = new JTextField();
		hourlyField.setMaximumSize(new Dimension(Invoicer.WIDTH*8/10,Invoicer.HEIGHT/20));
		hourlyField.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, Invoicer.HEIGHT/35));

		add(hourlyLabel);
		add(hourlyField);
		add(bufferPanel());
		
		serviceDatePicker = new DatePicker();
		serviceDatePicker.setMaximumSize(new Dimension(Invoicer.WIDTH*8/10,Invoicer.HEIGHT/20));
		serviceDateLabel = new JLabel("Service Date");
		serviceDateLabel.setForeground(Color.white);
		serviceDateLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, Invoicer.HEIGHT/30));
		
		add(serviceDateLabel);
		add(serviceDatePicker);
		add(bufferPanel());
		
		billDatePicker = new DatePicker();
		billDatePicker.setMaximumSize(new Dimension(Invoicer.WIDTH*8/10,Invoicer.HEIGHT/20));
		billDatePicker.setDateToToday();
		billDateLabel = new JLabel("Billing Date");
		billDateLabel.setForeground(Color.white);
		billDateLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, Invoicer.HEIGHT/30));
		
		add(billDateLabel);
		add(billDatePicker);
		add(bufferPanel());
		add(bufferPanel());
		
		clientPicker.addItemListener(e->{
			//autofill the other boxes with client data
			Client client = (Client)e.getItem();
			autofill(client);
		});
		
		generateButton = new JButton("Generate PDF");
		if(Invoicer.onMac) {
			generateButton.setForeground(Color.black);
		} else {
			generateButton.setForeground(Color.white);
		}
		generateButton.setBackground(new Color(40,160,230));
		generateButton.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,Invoicer.HEIGHT/20));
		generateButton.addActionListener(e->{
			//Client client, String service, double amount, Date serviceDate, Date billDate
			try {
				errorLabel.setVisible(false);
				successLabel.setVisible(false);
				Client client = (Client) clientPicker.getSelectedItem();
				String service = serviceField.getText();
				double amount = Double.parseDouble((amountField.getText()));
				double hourly=0;
				try {
					hourly=Double.parseDouble((hourlyField.getText()));
				}catch(Exception exc) {
					hourlyField.setText("0");
					hourly=0;
				}
				LocalDate serviceDate = serviceDatePicker.getDate();
				if(serviceDate==null||amount<0||service.equals("")) throw new Exception();
				LocalDate billDate = billDatePicker.getDate();
				
				generatePDF(client,service,amount,hourly,serviceDate,billDate);
				//this is for autofill purposes
				client.serviceDate=serviceDate;
				client.service=service;
				client.hourly=hourly;
				//now to clear the fields
				autofill(new Client());
				
			} catch (Exception ex) {
				errorLabel.setVisible(true);
				//ex.printStackTrace();
			}
		});
		generateButton.setPreferredSize(new Dimension(Invoicer.WIDTH*8/10,Invoicer.HEIGHT/10));
		add(generateButton);
		
		errorLabel = new JLabel("Some fields not filled out properly");
		errorLabel.setForeground(Color.red);
		errorLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, Invoicer.HEIGHT/35));

		add(errorLabel);
		
		successLabel = new JLabel("Generated pdf! TODO make this say file save loc");
		successLabel.setForeground(Color.green);
		successLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, Invoicer.HEIGHT/35));

		add(successLabel);
		
		successLabel.setVisible(false);
		errorLabel.setVisible(false);

	}
	private void autofill(Client client) {
		//TODO this
		amountField.setText(""+client.expectedAmt);
		serviceDatePicker.setDate(client.serviceDate.equals(LocalDate.EPOCH)?null:client.serviceDate);
		serviceField.setText(client.service.equals("null")?null:client.service);
		hourlyField.setText(client.hourly==0?null:""+client.hourly);
	}
	public void updateClientPicker() {
		((DefaultComboBoxModel<Client>) clientPicker.getModel()).removeAllElements();
		for(ClientBox c: ClientPanel.clientList) {
			((DefaultComboBoxModel<Client>) clientPicker.getModel()).addElement(c.client);
		}
	}
	public static JPanel bufferPanel() {
		JPanel bufferPanel = new JPanel();
		bufferPanel.setPreferredSize(new Dimension(Invoicer.WIDTH*6/10,Invoicer.HEIGHT/50));
		bufferPanel.setMaximumSize(new Dimension(Invoicer.WIDTH*6/10,Invoicer.HEIGHT/40));
		bufferPanel.setBackground(new Color(36,36,36));
		return bufferPanel;
	}
	void generatePDF(Client client, String service, double amount, double hourly, LocalDate serviceDate, LocalDate billDate) throws Exception {
		//TODO this part where you generate a pdf
		//hepl
		//maybe let you select contacts to be subjects? idk
		if(PDFGenerator.generatePdf(client, service, amount, hourly, serviceDate, billDate)) {
		//on success:
		successLabel.setVisible(true);
		//dont really need to record an hourly rate so i shall eschew it
		Invoicer.rp.newRecord(client,service,amount,serviceDate,billDate);
		} else throw new Exception("Error occurred generating PDF");
	}
}

