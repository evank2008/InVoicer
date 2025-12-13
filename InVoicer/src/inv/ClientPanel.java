package inv;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import legacy.Invoice;

//this class should display the clients, let you add/remove clients, input data about clients
//TODO: option to delete clients?

public class ClientPanel extends MenuPanel {
	static ArrayList<ClientBox> clientList = new ArrayList<ClientBox>();
	static JPanel boxPanel = new JPanel();
	JScrollPane scrollPane = new JScrollPane(boxPanel);
	
	JPanel buttonPanel = new JPanel();
	Dimension clientBoxSize = new Dimension(Invoicer.WIDTH*19/20,Invoicer.HEIGHT*2/15);
public ClientPanel() {
		super();
		//two panels - one with the clientboxes one with the control buttons
		//scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));

		buttonPanel.setPreferredSize(new Dimension((int)(Invoicer.WIDTH/1.1), Invoicer.HEIGHT/10));
		buttonPanel.setMaximumSize(buttonPanel.getPreferredSize());
		
		boxPanel.setBackground(new Color(31,31,31));
		boxPanel.setBorder(new LineBorder(new Color(201,201,201),1));
		
		boxPanel.setLayout(new BoxLayout(boxPanel,BoxLayout.Y_AXIS));

		
		buttonPanel.setBackground(new Color(20,85,122));
		
		JButton addButton = new JButton("Add Client");
		if(Invoicer.onMac) {
			addButton.setForeground(Color.black);
		} else {
			addButton.setForeground(Color.white);
		}
		
		addButton.setBackground(new Color(40,160,230));
		addButton.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,Invoicer.HEIGHT/20));
		addButton.addActionListener(e->{
			addClient();
			boxPanel.revalidate();
			boxPanel.repaint();
		});
		addButton.setPreferredSize(new Dimension(buttonPanel.getPreferredSize().width-10,buttonPanel.getPreferredSize().height-10));
		
		buttonPanel.add(addButton);
		
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);		
		add(buttonPanel);
		add(scrollPane);
	}
void addClient() {
	addClient(new Client());
}
void addClient(Client c) {
	ClientBox cbox = new ClientBox(c);
	clientList.add(cbox);
	
	
	JPanel buffer = new JPanel();
	buffer.setPreferredSize(new Dimension(25,Invoicer.HEIGHT/40));
	buffer.setMaximumSize(new Dimension(25,Invoicer.HEIGHT/100));
	buffer.setBackground(new Color(31,31,31));
	boxPanel.add(buffer);
	boxPanel.add(cbox);
	
	
	
	
//	boxPanel.paintAll(boxPanel.getGraphics());
	//i think adding every clientbox should be one function and adding a single one should happen here
	
}
static void updateClientData() {
	//parse each clientbox's textfields and store their data in their client's variables
	//notably, doesn't save data on contacts
	for(ClientBox cb:clientList) {
		cb.client.doctor=cb.doctorField.getText();
		cb.client.name=cb.nameField.getText();
		try {
		cb.client.expectedAmt=Double.parseDouble((cb.amountField.getText()));
		} catch(Exception e) {
			cb.client.expectedAmt=0.0;
			cb.amountField.setText("Error!");
		}
		
	}
	//also update creator panel picker
	Invoicer.crp.updateClientPicker();
}
public String toFileString() {
	String s = "";
	for(ClientBox c: ClientPanel.clientList) {
		s+=(c.client.toFileString())+"<client>";
	}
	if(s.equals("")) return "Empty";
	return s;
}

public void loadData(String dataLine) {
String[] clients = dataLine.split("<client>");
if(clients[0].length()==0) return;
for(String clientStr: clients) {
	Client client = new Client();
	String[] clientArr = clientStr.split("<contactList>");
	String[] clientFields=clientArr[0].split("<break>");
		client.name=clientFields[0];
		client.doctor=clientFields[1];
		client.expectedAmt=Double.parseDouble(clientFields[2]);
		
	if (clientArr.length > 1 && !clientArr[1].isEmpty()) {
		String[] contacts = clientArr[1].split("<contact>");
		for(String contactStr: contacts) {
			//"<name>"+name+"<name><role>"+role+"<role><email>"+emailAddress+"<email>"
			String name=contactStr.split("<name>")[0];
			String role=contactStr.split("<role>")[1];
			String email=contactStr.split("<email>")[1];
			Contact contact = new Contact(name.equals("null")?"":name, (email.equals("null")?"":email), role.equals("null")?"":role);
			client.contactList.add(contact);
			}
		}
	addClient(client);
}
this.repaint();
boxPanel.revalidate();
boxPanel.repaint();
}
}

class Client {
	String name="New Client";
	String doctor="Doctorguy";
	double expectedAmt=0.00; //expected monthly billing amount in dollars
	ArrayList<Contact> contactList = new ArrayList<Contact>();
public String toString() {
	return name;
}
public String toFileString() {
	String s = name+"<break>"+doctor+"<break>"+expectedAmt+"<contactList>";
	
	for(Contact c:contactList) {
		s+=c.toFileString();
	}
	return s+"<contactList>";
}
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
	public String[] toArray() {
		return new String[]{name,role,emailAddress};
	}
	public String toFileString() {
		//make sure to have a comma around everything incase of empties
		return (name.isEmpty()?"null":name)+"<name><role>"+(role.isEmpty()?"null":role)+"<role><email>"+(emailAddress.isEmpty()?"null":emailAddress)+"<email><contact>";
	}
}
class ClientBox extends JPanel{
	Dimension clientBoxSize = new Dimension(Invoicer.WIDTH*18/20,Invoicer.HEIGHT*2/15);

	//this displays a client's data in a box
	Client client;
	JLabel nameLabel;
	JLabel amountLabel;
	JTextField nameField, amountField, doctorField;
	JPanel namePanel, amountPanel;
	JButton contactsButton;
	Color darkWhite = new Color(220,220,220);
	public ClientBox(Client client) {
		super();

		this.client=client;
		this.setPreferredSize(clientBoxSize);
		this.setMaximumSize(clientBoxSize);
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
		nameLabel.setForeground(darkWhite);
		nameLabel.setFont(nameLabel.getFont().deriveFont((float)(this.getPreferredSize().height*7/24)));
		
		nameField=new JTextField(client.name,20);
		nameField.setFont(nameLabel.getFont());
		nameField.setForeground(darkWhite);
		nameField.setBackground(new Color(34,34,34));
		nameField.setCaretColor(Color.white);
	//	nameField.setPreferredSize(new Dimension(nameField.getPreferredSize().width*5/2,nameField.getPreferredSize().height));
		nameField.addActionListener(e->{
			this.requestFocusInWindow();
			ClientPanel.updateClientData();

		});
		
		namePanel=new JPanel();
		namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.X_AXIS));
		nameLabel.setAlignmentY(Component.TOP_ALIGNMENT);
		nameField.setAlignmentY(Component.TOP_ALIGNMENT);
		namePanel.add(nameLabel);
		namePanel.add(nameField);
		namePanel.setOpaque(false);
		namePanel.setMinimumSize(namePanel.getPreferredSize());

		
		amountLabel=new JLabel("       Expected Monthly Amount: $");
		amountLabel.setForeground(darkWhite);
		amountLabel.setFont(nameLabel.getFont().deriveFont((float)(this.getPreferredSize().height/4)));
		
		amountField=new JTextField(""+client.expectedAmt,10);
		amountField.setFont(amountLabel.getFont());
		amountField.setForeground(darkWhite);
		amountField.setBackground(new Color(34,34,34));
		amountField.setCaretColor(Color.white);
		//amountField.setPreferredSize(new Dimension(amountField.getPreferredSize().width*5/2,amountLabel.getPreferredSize().height/2));
		amountField.addActionListener(e->{
			this.requestFocusInWindow();
			ClientPanel.updateClientData();

		});
		//amountPanel is tricky to work with
		//adding anything but the label breaks all the gui
		//even just a regular panel with only the label in it
		amountPanel=new JPanel();
		amountPanel.setLayout(new GridBagLayout());
		GridBagConstraints apc = new GridBagConstraints();
		apc.anchor = GridBagConstraints.WEST;

		apc.gridx = 0;
		amountPanel.add(amountLabel, apc);

		apc.gridx = 1;
		apc.weightx = 1.0;
		apc.fill = GridBagConstraints.HORIZONTAL;
		amountPanel.add(amountField, apc);
		amountPanel.setOpaque(false);
		
		doctorField=new JTextField(client.doctor,12);
		//doctorField.setMaximumSize(new Dimension(Invoicer.WIDTH/5,Invoicer.HEIGHT));
		doctorField.setForeground(darkWhite);
		doctorField.setFont(nameLabel.getFont().deriveFont((float)(this.getPreferredSize().height/5)));
		doctorField.setBackground(new Color(34,34,34));
		doctorField.setCaretColor(Color.white);
		doctorField.addActionListener(e->{
			this.requestFocusInWindow();
			ClientPanel.updateClientData();
		});

		
		contactsButton = new JButton("Contacts");
		//40 160 230
		contactsButton.setForeground(Invoicer.onMac?Color.black:darkWhite);
		contactsButton.setBackground(new Color(40,160,230));
		contactsButton.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,this.getPreferredSize().height/5));
		contactsButton.addActionListener(e->{
			ClientPanel.updateClientData();
			ContactsFrame frame = new ContactsFrame(client);
		});//TODO: made button show contacts or something idk
		contactsButton.setPreferredSize(new Dimension(this.getPreferredSize().width/6,this.getPreferredSize().height/3));

		gbc.anchor = GridBagConstraints.WEST;
		 gbc.gridx = 0;
         gbc.gridy = 0;
         gbc.weightx = 0.8; // relative width
         gbc.weighty = 0;
         gbc.fill = GridBagConstraints.HORIZONTAL;
         add(namePanel, gbc);

         // Row 0, Col 1 (Top Right)
         gbc.anchor = GridBagConstraints.CENTER;
         gbc.gridx = 1;
         gbc.gridy = 0;
         gbc.weightx = 0.2;
         add(doctorField, gbc);

         // Row 1, Col 0 (Bottom Left)
         gbc.anchor = GridBagConstraints.WEST;
         gbc.gridx = 0;
         gbc.gridy = 1;
         gbc.weightx = 0.8;
         gbc.weighty = 1;
         add(amountPanel, gbc);
         //:(

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
	}
}
	class ContactsFrame extends JFrame {
		//how should this look?
		//its basically just a list of Contacts[name, email address, role]
		//i think it should have a slightly different layout compared to clientpanel
		//more of a bare-bones vibe?
		//one button or two at top for add/delete then just a jtable
		ArrayList<Contact> contactList;
		JPanel buttonPanel;
		JPanel tablePanel;
		JTable table;
		JButton addButton;
		Client client;
		TableModelListener listener = ((e)->{
			updateClientContacts();
		});
		
		public ContactsFrame(Client client) {
			super(client.name+" Contacts");
			this.client=client;
			contactList=client.contactList;
			setSize(Invoicer.WIDTH*2/3,Invoicer.HEIGHT*80/100);
			setVisible(true);
			setLayout(new BorderLayout());
			
			addWindowListener(new java.awt.event.WindowAdapter() {
				@Override
				public void windowClosing(java.awt.event.WindowEvent windowEvent) {
					updateClientContacts();
				}
			});
			
			buttonPanel=new JPanel();
			buttonPanel.setBackground(new Color(36,36,36));
			Dimension d = this.getSize();
			d.height/=8;
			buttonPanel.setPreferredSize(d);
			add(buttonPanel, BorderLayout.NORTH);
			
			addButton=new JButton("Add");
			addButton.addActionListener(e->{
				if(table.getCellEditor()!=null)table.getCellEditor().stopCellEditing();
				DefaultTableModel t=(DefaultTableModel) table.getModel();
				t.addRow(new Object[] {"","",""});
				//TableExtender.extend(table);
				table.getModel().addTableModelListener(listener);
				});
			addButton.setPreferredSize(new Dimension(buttonPanel.getPreferredSize().width-80,buttonPanel.getPreferredSize().height-10));
			addButton.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,buttonPanel.getPreferredSize().height*5/12));
			if(Invoicer.onMac) {
				addButton.setForeground(Color.black);
			} else {
				addButton.setForeground(Color.white);
			}
			
			addButton.setBackground(new Color(40,160,230));
			
			buttonPanel.add(addButton);
			
			tablePanel=new JPanel();
			tablePanel.setBackground(new Color(36,36,36));
			Dimension t = new Dimension(d);
			t.height=this.getHeight()*7/8;
			tablePanel.setPreferredSize(d);
			add(tablePanel);
			
			
			String[][] data = new String[client.contactList.size()][3];
			for(int i = 0; i<client.contactList.size();i++) {
				data[i]=client.contactList.get(i).toArray();
				}
			 
			/*
			String[][] data = new String[2][3];
			for(int i = 0; i<2;i++) {
				data[i]= new String[]{"Jeremy Willis","Reconnaissance Field Agent","awesomesauce@aol.com"};
			}
			*/
			
			String[] titles = { "Name","Title","E-Mail Address"};
			table = new JTable(new DefaultTableModel(data,titles));

			table.setRowHeight(t.height/8);
			table.getTableHeader().setReorderingAllowed(false);
			table.getModel().addTableModelListener(listener);
			table.setFont(table.getFont().deriveFont((float)(table.getFont().getSize()*1.2)));
			table.setBackground(new Color(31,31,31));
			table.setForeground(Color.white);
			table.setGridColor(Color.white);
			table.setSelectionBackground(new Color(20,85,122));
			table.setSelectionForeground(Color.white);		
			JScrollPane pane = new JScrollPane(table);
			pane.getViewport().setBackground(new Color(50,50,50));
			tablePanel.add(pane);
			
		}
		void updateClientContacts() {

			// updates client's contactlist with the current table data
			client.contactList.clear();
			for (int i = 0; i < table.getRowCount(); i++) {
				client.contactList.add(new Contact((String)table.getValueAt(i, 0),(String)table.getValueAt(i, 2),(String)table.getValueAt(i, 1)));
			}		
		}
	}
