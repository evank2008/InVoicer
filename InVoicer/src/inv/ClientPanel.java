package inv;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.LineBorder;

//this class should display the clients, let you add/remove clients, input data about clients
//have a row of clientboxes on the left side
//buttons on the right side?
//TODO:line 91, replace the jlabels with jpanels that hold a label and a textfield to allow editing
public class ClientPanel extends MenuPanel {
	static ArrayList<ClientBox> clientList = new ArrayList<ClientBox>();
	static JPanel boxPanel = new JPanel();
	JScrollPane scrollPane = new JScrollPane(boxPanel);
	//maybe put these in a jscrollpane?
	//make a jscrollpane and a second jpanel
	JPanel buttonPanel = new JPanel();
	JPanel bufferPanel = new JPanel();
public ClientPanel() {
		super();
		//add(new JLabel("Client Panel"));
		//two panels - one with the clientboxes one with the control buttons

		boxPanel.setPreferredSize(new Dimension((int)(Invoicer.WIDTH/1.1), Invoicer.HEIGHT*9/10));
		buttonPanel.setPreferredSize(new Dimension((int)(Invoicer.WIDTH/1.1), Invoicer.HEIGHT/10));
		
		boxPanel.setBackground(new Color(31,31,31));
		boxPanel.setBorder(new LineBorder(new Color(201,201,201),1));
		buttonPanel.setBackground(new Color(20,85,122));
		bufferPanel.setBackground(Color.green);
		
		JButton addButton = new JButton("Add Client");
		//40 160 230
		addButton.setForeground(Color.white);
		addButton.setBackground(new Color(40,160,230));
		addButton.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,Invoicer.HEIGHT/20));
		addButton.addActionListener(e->addClient());
		addButton.setPreferredSize(new Dimension(buttonPanel.getPreferredSize().width-10,buttonPanel.getPreferredSize().height-10));
		
		buttonPanel.add(addButton);
		
		add(buttonPanel);
		//add(boxPanel);
		add(scrollPane);
	}
void addClient() {
	ClientBox cbox = new ClientBox(new Client());
	clientList.add(cbox);
	boxPanel.add(cbox);
	boxPanel.paintAll(boxPanel.getGraphics());
	
	//after adding clientbox to clientlist, should actually draw the new boxpanel? or make that its own function that reads through the entire list and sets it up
	//i think adding every clientbox should be one function and adding a single one should happen here
	
}
}

class Client {
	String name="New Client";
	String doctor="Doctorguy";
	double expectedAmt=0.00; //expected monthly billing amount in dollars
	ArrayList<Contact> contactList = new ArrayList<Contact>();

}
class Contact {
	String name;
	String emailAddress;
	String role="";
	public Contact(String name, String address) {
		this.name=name;
		emailAddress=address;
	}
	public Contact(String name, String address, String role) {
		this(name, address);
		this.role=role;
	}
}
class ClientBox extends JPanel{
	//this displays a client's data in a box
	Client client;
	JLabel nameLabel;
	JLabel amountLabel;
	JLabel doctorLabel;
	JTextField nameField, amountField, doctorField;
	JPanel namePanel, amountPanel, doctorPanel;
	JButton contactsButton;
	public ClientBox(Client client) {
		super();
		this.client=client;
		this.setPreferredSize(new Dimension(ClientPanel.boxPanel.getPreferredSize().width*39/40,(int)(ClientPanel.boxPanel.getPreferredSize().height/7.6)));
		this.setBorder(new LineBorder(new Color(201,201,201),2,true));
		this.setBackground(new Color(36,36,36));
		this.setLayout(new GridBagLayout());
		this.setFocusable(true);
		GridBagConstraints gbc = new GridBagConstraints();
		//what is displayed?
		/*
		 * maybe just all the data i guess
		 * name, amount, doctor, contacts
		 */
		nameLabel=new JLabel("  ");
		nameLabel.setForeground(Color.white);
		nameLabel.setFont(nameLabel.getFont().deriveFont((float)(this.getPreferredSize().height*7/24)));
		
		nameField=new JTextField(client.name);
		nameField.setFont(nameLabel.getFont());
		nameField.setForeground(Color.white);
		nameField.setBackground(new Color(34,34,34));
		nameField.setCaretColor(Color.white);
		nameField.setPreferredSize(new Dimension(nameField.getPreferredSize().width*5/2,nameField.getPreferredSize().height));
		nameField.addActionListener(e->{
			this.requestFocusInWindow();
			client.name=nameField.getText();
		});
		
		namePanel=new JPanel();
		namePanel.add(nameLabel);
		namePanel.add(nameField);
		namePanel.setOpaque(false);
		
		amountLabel=new JLabel("       Expected Monthly Amount: $"+client.expectedAmt);
		amountLabel.setForeground(Color.white);
		amountLabel.setFont(nameLabel.getFont().deriveFont((float)(this.getPreferredSize().height/4)));
		
		doctorLabel=new JLabel(client.doctor);
		doctorLabel.setForeground(Color.white);
		doctorLabel.setFont(nameLabel.getFont().deriveFont((float)(this.getPreferredSize().height/5)));
		
		JButton contactsButton = new JButton("Contacts");
		//40 160 230
		contactsButton.setForeground(Color.white);
		contactsButton.setBackground(new Color(40,160,230));
		contactsButton.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,this.getPreferredSize().height/5));
		contactsButton.addActionListener(e->{});//TODO: made button show contacts or something idk
		contactsButton.setPreferredSize(new Dimension(this.getPreferredSize().width/6,this.getPreferredSize().height/3));

		gbc.anchor = GridBagConstraints.WEST;
		 gbc.gridx = 0;
         gbc.gridy = 0;
         gbc.weightx = 0.8; // relative width
         gbc.weighty = 0.5;
         add(namePanel, gbc);

         // Row 0, Col 1 (Top Right)
         gbc.anchor = GridBagConstraints.CENTER;
         gbc.gridx = 1;
         gbc.gridy = 0;
         gbc.weightx = 0.2;
         add(doctorLabel, gbc);

         // Row 1, Col 0 (Bottom Left)
         gbc.anchor = GridBagConstraints.WEST;
         gbc.gridx = 0;
         gbc.gridy = 1;
         gbc.weightx = 0.8;
         gbc.weighty = 0.5;
         add(amountLabel, gbc);

         // Row 1, Col 1 (Bottom Right)
         gbc.anchor = GridBagConstraints.PAGE_START;
         gbc.gridx = 1;
         gbc.gridy = 1;
         gbc.weightx = 0.2;
         add(contactsButton, gbc);
         //TODO: make contnactsbutton
         
		//add(nameLabel,BorderLayout.NORTH);
		//add(amountLabel,BorderLayout.WEST);
		//at the end
		ClientPanel.clientList.add(this);
	}
}