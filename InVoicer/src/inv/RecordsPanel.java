package inv;

import javax.swing.JLabel;

//this class should show a table of the past invoices
//payment status, date sent, all the info about the invoice
//let you push a button to say that an invoice has been paid, input check data
public class RecordsPanel extends MenuPanel {
	public RecordsPanel() {
		super();
		add(new JLabel("Records Panel"));
	}
}
