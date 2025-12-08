package inv;

import java.awt.BorderLayout;
import java.awt.Color;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

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
	//not quite sure what 'sent invoice' means yet - pdf or data?
	Client client;
	String service;
	double amount;
	LocalDate serviceDate;
	LocalDate billDate;
	boolean paymentStatus;
	String notes;
	//TODO uhhh take in check data?
}
