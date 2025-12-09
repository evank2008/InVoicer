package inv;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

//this class should show a table of the past invoices
//payment status, date sent, all the info about the invoice
//let you push a button to say that an invoice has been paid, input check data

//ideal layout:
/*
 * 1. load data from file
 * 2. place data in recordslist
 * 3. call updatetable to fill table with recordslist data
 */
//maybe have a separate file for each record
/*
 * JScrollPane sp = new JScrollPane(jt);
		jt.setFillsViewportHeight(true);
		tPanel = new JPanel();
		tPanel.setLayout(new BorderLayout());
		tPanel.add(sp);
		frame.add(tPanel, BorderLayout.AFTER_LAST_LINE);
 */
/*
 * 
 */
public class RecordsPanel extends MenuPanel {
	JPanel buttonPanel;
	JButton inputButton, viewButton;
	JPanel bufferPanel;

	//will hold two buttons - 1 to input check data, 1 to view check info of selected row
	JTable table;
	JScrollPane tablePane;
	static ArrayList<Record> recordsList = new ArrayList<Record>();
	public RecordsPanel() {
		super();
		loadRecordsFile();
		bufferPanel = new JPanel();
		
		buttonPanel = new JPanel();
		buttonPanel.setPreferredSize(new Dimension((int)(Invoicer.WIDTH/1.1), Invoicer.HEIGHT/10));
		buttonPanel.setMaximumSize(buttonPanel.getPreferredSize());
		buttonPanel.setBackground(new Color(20,85,122));
		
		inputButton = new JButton("Input Check");
		viewButton = new JButton("View Check");
		if(Invoicer.onMac) {
			inputButton.setForeground(Color.black);
			viewButton.setForeground(Color.black);
		} else {
			inputButton.setForeground(Color.white);
			viewButton.setForeground(Color.white);
		}
		inputButton.setBackground(new Color(40,160,230));
		inputButton.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,Invoicer.HEIGHT/20));
		viewButton.setBackground(new Color(40,160,230));
		viewButton.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,Invoicer.HEIGHT/20));
		inputButton.setPreferredSize(new Dimension(buttonPanel.getPreferredSize().width/2-20,buttonPanel.getPreferredSize().height-10));
		viewButton.setPreferredSize(new Dimension(buttonPanel.getPreferredSize().width/2-20,buttonPanel.getPreferredSize().height-10));
		
		buttonPanel.add(inputButton);
		bufferPanel.setPreferredSize(new Dimension(10,10));
		bufferPanel.setBackground(new Color(20,85,122));
		buttonPanel.add(bufferPanel);
		buttonPanel.add(viewButton);
		
		add(buttonPanel);
		
		table.setRowHeight(32);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoCreateRowSorter(true);
		tablePane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		
		add(tablePane);
		
	}

	public void newRecord(Client client, String service, double amount, LocalDate serviceDate,
			LocalDate billDate) {
		//make a record and add to recordslist
		new Record(client.name, service, amount, serviceDate, billDate);
		updateTable();
	}
	void loadRecordsFile() {
		//TODO this
		updateTable();
	}
	void updateTable() {
		//fills table with all data from recordslist
		String[][] array = new String[recordsList.size()][7];
		for(int i = 0; i<recordsList.size();i++) {
			array[i]=recordsList.get(i).toStrArray();
		}
		String[] titles = {"Date Sent","Client","Date of Service","Service","Amount","Check","Notes"};
	
		table = new JTable(new DefaultTableModel(array,titles));
	}
}
class Record {
	//a record is one entry in the table
	//per the specs, should store "sent invoice, payment status, check data"
	//will store invoice data
	//TODO uhhh take in check data?
	//check will store payment data ahahhaa
	String clientName;
	String service;
	double amount;
	LocalDate serviceDate;
	LocalDate billDate;
	Check check;
	String notes;
	
	public Record(String name, String serv, double amt, LocalDate sDate, LocalDate billDate) {
		//fresh record off the creator panel
		clientName=name;
		service=serv;
		amount=amt;
		serviceDate=sDate;
		this.billDate=billDate;
		notes="";
		check=new Check();
		
		RecordsPanel.recordsList.add(this);
	}
	public Record(String name, String serv, double amt, LocalDate sDate, LocalDate billDate,boolean status, String notes, Check chk) {
		//should probably be used only when loading from save file
		this( name,  serv,  amt,  sDate,  billDate);
		check=chk;
		this.notes=notes;
	}
	public String[] toStrArray() {
		String[] arr=new String[7];
		//"Date Sent","Client","Date of Service","Service","Amount","Check","Notes"
		arr[0]=billDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
		arr[1]=clientName;
		arr[2]=serviceDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
		arr[3]=service;
		arr[4]=""+amount;
		arr[5]=check.toString();
		arr[6]=notes;
		return arr;
	}

}
class Check {
	LocalDate checkDate;
	String checkId;
	double amount;
	String invoiceNum;
	LocalDate invoiceDate;
	boolean paymentStatus;
	
	public Check() {
		paymentStatus=false;
	}
	public Check(String invNum, LocalDate invDate, double amt, LocalDate chkDate, String chkId) {
		paymentStatus=true;
		invoiceNum=invNum;
		invoiceDate=invDate;
		amount=amt;
		checkDate=chkDate;
		checkId=chkId;
	}
	void fill(String invNum, LocalDate invDate, double amt, LocalDate chkDate, String chkId) {
		paymentStatus=true;
		invoiceNum=invNum;
		invoiceDate=invDate;
		amount=amt;
		checkDate=chkDate;
		checkId=chkId;
	}
	public String toString() {
		if(paymentStatus) {
			return checkId;
		} else {
			return "Unpaid";
		}
	}
}
