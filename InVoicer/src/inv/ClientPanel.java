package inv;

import java.util.ArrayList;

import javax.swing.*;

//this class should display the clients, let you add/remove clients, input data about clients
//have a row of clientboxes on the left side
//buttons on the right side?
//TODO: add buttons to create and edit
public class ClientPanel extends MenuPanel {
	public ClientPanel() {
		super();
		add(new JLabel("Client Panel"));
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
	}
}