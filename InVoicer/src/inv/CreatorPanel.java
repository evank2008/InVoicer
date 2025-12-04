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
		clientPicker.setBackground(Color.white);
		clientPicker.setForeground(Color.black);
		updateClientPicker();
		clientPicker.setMaximumSize(new Dimension(Invoicer.WIDTH*8/10,Invoicer.HEIGHT/12));
		
		JButton button = new JButton("update");
		button.addActionListener(e->{
			updateClientPicker();
		});
		
		add(button);
		clientLabel = new JLabel("Client");
		clientLabel.setForeground(Color.white);
		clientLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, Invoicer.HEIGHT/20));
		
		add(clientLabel);
		add(clientPicker);
		
	}
	public void updateClientPicker() {
		((DefaultComboBoxModel<Client>) clientPicker.getModel()).removeAllElements();
		for(ClientBox c: ClientPanel.clientList) {
			System.out.println(c.client);
			System.out.println(ClientPanel.clientList.size());
			((DefaultComboBoxModel<Client>) clientPicker.getModel()).addElement(c.client);
		}
		
	}
}

