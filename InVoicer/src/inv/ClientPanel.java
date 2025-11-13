package inv;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.LineBorder;

//this class should display the clients, let you add/remove clients, input data about clients
//have a row of clientboxes on the left side
//buttons on the right side?
//TODO: add buttons to create and edit
public class ClientPanel extends MenuPanel {
	static ArrayList<ClientBox> clientList = new ArrayList<ClientBox>();
	static JPanel boxPanel = new JPanel();
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
		add(boxPanel);
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
	static ArrayList<String> doctorList = new ArrayList<String>();
	int doctor; //position in list
	double expectedAmt=0; //expected monthly billing amount in dollars
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
	public ClientBox(Client client) {
		super();
		this.client=client;
		this.setPreferredSize(new Dimension(ClientPanel.boxPanel.getPreferredSize().width*39/40,ClientPanel.boxPanel.getPreferredSize().height/5));
		this.setBorder(new LineBorder(new Color(201,201,201),2,true));
		this.setBackground(new Color(36,36,36));
		
		//at the end
		ClientPanel.clientList.add(this);
	}
}