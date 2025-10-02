import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class DisplayPanel extends JPanel{
	JTable table;
	public DisplayPanel() {
		super();
		//now display all the invoices
		//easy right?
		//loadListAsTable();
		//now just show the table
		this.add(table);
		InVoicer.frame.pack();
		System.out.println("should display");
		
		
	}
	
}
