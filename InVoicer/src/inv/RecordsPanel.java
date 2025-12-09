package inv;

import java.awt.BorderLayout;
import java.awt.Color;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

//this class should show a table of the past invoices
//payment status, date sent, all the info about the invoice
//let you push a button to say that an invoice has been paid, input check data
//not sure exactly how many columns should be in the table
//TODO figure that out
//bill sent date, service date, service, client name, amount, payment status/check data, notes

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
	JTable table;
	JScrollPane tablePane;
	ArrayList<Record> recordsList = new ArrayList<Record>();
	public RecordsPanel() {
		super();
	}

	public static void newRecord(Client client, String service, double amount, LocalDate serviceDate,
			LocalDate billDate) {
		//make a record and add to recordslist
	}
	void updateTable() {
		//fills table with all data from recordslist
		
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
		check.paymentStatus=false;
		notes="";
	}
	public Record(String name, String serv, double amt, LocalDate sDate, LocalDate billDate,boolean status, String notes) {
		this( name,  serv,  amt,  sDate,  billDate);
		check.paymentStatus=status;
		this.notes=notes;
	}

}
class Check {
	//what??
	//idk bro
	//i think it needs some sort of number
	boolean paymentStatus=false;
	public String toString() {
		if(paymentStatus) {
			return "todo add check data";
		} else {
			return "Unpaid";
		}
	}
}
