import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
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
	{
		
		setDefaultRenderer(Object.class, new DefaultTableCellRenderer()
		{
			
		    @Override
		    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		    {
		        final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		        Color col;
		        /*set color to:
		        white if unselected and unpaid
		        light gray if unselected and paid
		        light blue sort if selected and unpaid
		        ??? evil mystery color(gray) if selected and paid
		        */
		        if(isSelected) {
		        	if(getValueAt(row, 4).equals("Unpaid")) {
		        		col=new Color(115,166,220); //light blue thing
		        		
		        	} else {
		        		col=Color.gray;
		        	}
		        } else {
		        	if(getValueAt(row, 4).equals("Unpaid")) {
		        		col=new Color(255,253,208); //creamy
		        	} else {
		        		col=Color.LIGHT_GRAY;
		        	}
		        }
		        c.setBackground(col);
		        return c;
		    }
		});  
		
		//set custom renderer for client column to add the button
		getColumnModel().getColumn(2).setCellRenderer(new ArrowCellRenderer());

	}

}
