package inv;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.*;

//this class should display the clients, let you add/remove clients, input data about clients
//have a row of clientboxes on the left side
//buttons on the right side?
//TODO: add buttons to create and edit
public class ClientPanel extends MenuPanel {
	static ArrayList<ClientBox> clientList = new ArrayList<ClientBox>();
	JPanel boxPanel = new JPanel();
	//maybe put these in a jscrollpane?
	JPanel buttonPanel = new JPanel();
	JPanel bufferPanel = new JPanel();
public ClientPanel() {
		super();
		//add(new JLabel("Client Panel"));
		//two panels - one with the clientboxes one with the control buttons
		boxPanel.setPreferredSize(new Dimension((int)(Invoicer.WIDTH/1.1), Invoicer.HEIGHT*9/10));
		//bufferPanel.setPreferredSize(new Dimension((int)(Invoicer.WIDTH/2.5), Invoicer.HEIGHT*39/40));
		buttonPanel.setPreferredSize(new Dimension((int)(Invoicer.WIDTH/1.1), Invoicer.HEIGHT/10));
		
		boxPanel.setBackground(Color.red);
		buttonPanel.setBackground(Color.blue);
		bufferPanel.setBackground(Color.green);
		
		JButton addButton = new JButton("Add Client");
		addButton.setFont(addButton.getFont().deriveFont((float)(Invoicer.HEIGHT/20)));
		addButton.addActionListener(e->addClient());
		addButton.setPreferredSize(new Dimension(buttonPanel.getPreferredSize().width-10,buttonPanel.getPreferredSize().height-10));
		
		buttonPanel.add(addButton);
		
		add(buttonPanel);
		add(boxPanel);
	}
void addClient() {
	//TODO: this
	System.out.println("client add");
}
}

class Client {
	String name;
	static ArrayList<String> doctorList = new ArrayList<String>();
	int doctor; //position in list
	double expectedAmt; //expected monthly billing amount in dollars
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
		
		//at the end
		ClientPanel.clientList.add(this);
	}
}