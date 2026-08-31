package inv;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;

public class SettingsPanel extends MenuPanel{

	JButton nameFieldsButton, promptButton;
	String promptAddendum;
	boolean promptOpen;
	public SettingsPanel() {
		super();
		promptOpen=false;
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		add(CreatorPanel.bufferPanel());
		nameFieldsButton = new JButton("Calibrate Client Name Fields");
		if(Invoicer.onMac) {
			nameFieldsButton.setForeground(Color.black);
		} else {
			nameFieldsButton.setForeground(Color.white);
		}
		
		nameFieldsButton.setBackground(new Color(40,160,230));
		nameFieldsButton.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,Invoicer.HEIGHT/20));
		nameFieldsButton.addActionListener(e->{
			Invoicer.ss.setSelectedIndex(0);
			Invoicer.clp.calibrateNameFields();
		});
		nameFieldsButton.setPreferredSize(new Dimension(Invoicer.WIDTH/5,Invoicer.HEIGHT/30));
		nameFieldsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(nameFieldsButton);
		
		add(CreatorPanel.bufferPanel());
		
		promptButton = new JButton("Edit Prompt Addendum");
		if(Invoicer.onMac) {
			promptButton.setForeground(Color.black);
		} else {
			promptButton.setForeground(Color.white);
		}
		
		promptButton.setBackground(new Color(40,160,230));
		promptButton.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,Invoicer.HEIGHT/20));
		promptButton.addActionListener(e->{
			
			if(promptOpen) return; 
			promptOpen=true;
			JDialog dialog = new JDialog(Invoicer.frame, "Editing Prompt Addendum", false);
			dialog.setSize(400,200);
			dialog.getContentPane().setBackground(new Color(62,62,62));
			dialog.setLocationRelativeTo(RecordsPanel.aFrame);

			//use jtable like in contactsframe
			String[][] data = new String[1][1];
			data[0][0]=promptAddendum==null?"":promptAddendum;
			String[] columnName = {"Prompt Addendum"};
			JTable table = new JTable(new DefaultTableModel(data,columnName));

			table.setRowHeight(Invoicer.clp.getSize().height/8);
			table.getTableHeader().setReorderingAllowed(false);
			
			table.setFont(table.getFont().deriveFont((float)(table.getFont().getSize()*2)));
			table.setBackground(new Color(31,31,31));
			table.setForeground(Color.white);
			table.setGridColor(Color.white);
			table.setSelectionBackground(new Color(20,85,122));
			table.setSelectionForeground(Color.white);	
			
			dialog.add(table);
			
			dialog.addWindowListener(new java.awt.event.WindowAdapter() {
				@Override
				public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				     if (table.isEditing()) {
				            table.getCellEditor().stopCellEditing();
				        }
				     promptOpen=false;
					promptAddendum=(String) table.getValueAt(0, 0);
					if(promptAddendum.isBlank()) promptAddendum=null;
				}
			});
			
			dialog.setVisible(true);
			
		});
		promptButton.setPreferredSize(new Dimension(Invoicer.WIDTH/5,Invoicer.HEIGHT/30));
		promptButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(promptButton);
		
		add(CreatorPanel.bufferPanel());
		
		String path;
		if(Invoicer.onMac)path = FileSystemView.getFileSystemView().getDefaultDirectory().getPath()+ "/InVoicer/invoicerData.txt";
		else path = FileSystemView.getFileSystemView().getDefaultDirectory().getPath()+ "\\InVoicer\\invoicerData.txt";
		JLabel label = new JLabel("Directory: "+path);
		label.setForeground(Color.white);
		label.setFont(nameFieldsButton.getFont().deriveFont(20f));
		label.setAlignmentX(Component.CENTER_ALIGNMENT);

		add(label);
		
	}
	void loadData(String data) {
		if(data!=null&&!data.equals("null")) promptAddendum=data;
	}
	public String toFileString() {
		return promptAddendum==null?"null":promptAddendum;
	}
}
