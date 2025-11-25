package inv;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class TableExtender {

	public static void extend(JTable table) {
		//assume data is string
		//System.out.println("table with "+table.getColumnCount()+" columns and "+table.getRowCount()+" rows");
		String[][]data=new String[table.getRowCount()+1][table.getColumnCount()];
		for(int i=0;i<table.getRowCount();i++) {
			for(int j = 0;j<table.getColumnCount();j++) {
				data[i][j]=(String)table.getValueAt(i, j);
			}
		}
		String[] columnNames = new String[table.getColumnCount()];
		for(int i = 0;i<table.getColumnCount();i++) {
			data[table.getRowCount()][i]="";
			columnNames[i]=table.getColumnName(i);
		}
		table.setModel(new DefaultTableModel(data,columnNames));
	}
}
