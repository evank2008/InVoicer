import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class DynamicTable extends JTable{
	public DynamicTable(DefaultTableModel defaultTableModel) {
		super(defaultTableModel);
	}

	@Override
	public boolean isCellEditable(int row, int column) {  
		if(column==0||column==4) return false;
		if(!getValueAt(row, 4).equals("Unpaid")) return false;
        return true;               
}
}
