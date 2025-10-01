import javax.swing.JPanel;
import javax.swing.JTable;

public class DisplayPanel extends JPanel{
	JTable table;
	public DisplayPanel() {
		super();
		//now display all the invoices
		//easy right?
		loadListAsTable();
		//now just show the table
		this.add(table);
		InVoicer.frame.pack();
		System.out.println("should display");
		
		
	}
	void loadListAsTable() {
		String[][] array = new String[InVoicer.invoiceList.size()][7];
		for(int i = 0; i<InVoicer.invoiceList.size();i++) {
			array[i]=InVoicer.invoiceList.get(i).toArray();
		}
		//array is now 2d array of unboxed data of ever invoice
		//initial date, service, client, amount, payment status, and last time updated
		table = new JTable(array,new String[] {"Date Created","Service","Client","Amount","Payment Status","Updated"});
	
	}
}
