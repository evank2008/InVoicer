package inv;

import java.awt.Color;
import java.time.LocalDate;

import javax.swing.JLabel;

//this class should show a table of the past invoices
//payment status, date sent, all the info about the invoice
//let you push a button to say that an invoice has been paid, input check data
public class RecordsPanel extends MenuPanel {
	public RecordsPanel() {
		super();
		add(new JLabel("Records Panel"));
		this.getComponent(0).setForeground(Color.white);
	}

	public static void newRecord(Client client, String service, double amount, LocalDate serviceDate,
			LocalDate billDate) {
		//make a record
	}
}
