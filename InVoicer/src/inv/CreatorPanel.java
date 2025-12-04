package inv;

import java.awt.*;

import javax.swing.*;

//this class should allow you to create an invoice
//autopull from existing clients ideally but give customization options
//then generate a pdf of it

/*
 try {
		cb.client.expectedAmt=Double.parseDouble((cb.amountField.getText()));
		} catch(Exception e) {
			cb.client.expectedAmt=0.0;
			cb.amountField.setText("Error!");
		}
 */
public class CreatorPanel extends MenuPanel {
	JLabel clientLabel, serviceDateLabel, billDateLabel, serviceFieldLabel, amountLabel;
	JTextField serviceField;
	JTextField amountField;
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
		

	}
	public void updateClientPicker() {
		((DefaultComboBoxModel<Client>) clientPicker.getModel()).removeAllElements();
		for(ClientBox c: ClientPanel.clientList) {
			System.out.println(c.client);
			System.out.println(ClientPanel.clientList.size());
			((DefaultComboBoxModel<Client>) clientPicker.getModel()).addElement(c.client);
		}
		
	}
	JPanel bufferPanel() {
		JPanel bufferPanel = new JPanel();
		bufferPanel.setPreferredSize(new Dimension(Invoicer.WIDTH*6/10,Invoicer.HEIGHT/50));
		bufferPanel.setMaximumSize(new Dimension(Invoicer.WIDTH*6/10,Invoicer.HEIGHT/40));
		bufferPanel.setBackground(new Color(36,36,36));
		return bufferPanel;
	}
}

