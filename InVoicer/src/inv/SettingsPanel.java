package inv;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.lang.reflect.Field;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;

import com.anthropic.models.messages.Model;

public class SettingsPanel extends MenuPanel{
//todo allow choice on model
	JButton nameFieldsButton, promptButton, viewPromptButton;
	String promptAddendum;
	boolean promptOpen;
	JComboBox<Model> modelPicker;
	Model defaultModel = Model.CLAUDE_OPUS_5;
	
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
		
		viewPromptButton = new JButton("View Default Prompt");
		if(Invoicer.onMac) {
			viewPromptButton.setForeground(Color.black);
		} else {
			viewPromptButton.setForeground(Color.white);
		}
		
		viewPromptButton.setBackground(new Color(40,160,230));
		viewPromptButton.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,Invoicer.HEIGHT/20));
		viewPromptButton.addActionListener(e->{
			String wrappedString = AnalysisFrame.prompt.replaceAll("(.{1," + 120 + "})\\s+", "$1\n");
			JOptionPane.showMessageDialog(null, wrappedString, "Default Prompt", JOptionPane.PLAIN_MESSAGE);
		});
		viewPromptButton.setPreferredSize(new Dimension(Invoicer.WIDTH/5,Invoicer.HEIGHT/30));
		viewPromptButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(viewPromptButton);
		add(CreatorPanel.bufferPanel());
		
		modelPicker = new JComboBox<Model>(new DefaultComboBoxModel<Model>());
		modelPicker.setBackground(new Color(200,200,200));
		modelPicker.setForeground(Color.black);
		modelPicker.setMaximumSize(new Dimension(Invoicer.WIDTH*4/10,Invoicer.HEIGHT/12));
		if(Invoicer.onMac) {
			modelPicker.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));

		} else {
			modelPicker.setFont(new Font(Font.SANS_SERIF, Font.BOLD, Invoicer.HEIGHT/30));
		}
		JLabel labele = new JLabel("Model: ");
		labele.setFont(modelPicker.getFont());
		labele.setForeground(Color.white);
		add(labele);
		add(modelPicker);
		modelPicker.setSelectedItem(defaultModel);
		((DefaultComboBoxModel<Model>) modelPicker.getModel()).removeAllElements();
		for (Field field : Model.class.getDeclaredFields()) {
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers()) && field.getType() == Model.class) {
                try {
                    Model modelInstance = (Model) field.get(null);
        			((DefaultComboBoxModel<Model>) modelPicker.getModel()).addElement(modelInstance);

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
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
		String[] dataSplit = data.split("<break>");
		if(dataSplit[0]!=null&&!dataSplit[0].equals("null")) promptAddendum=dataSplit[0];
		if(dataSplit.length>1&&!dataSplit[1].equals("null")) modelPicker.setSelectedItem(Model.of(dataSplit[1]));
		else modelPicker.setSelectedItem(defaultModel);
	}
	public String toFileString() {
		String data="";
		data+= promptAddendum==null?"null":promptAddendum;
		data+="<break>";
		data+=modelPicker.getSelectedItem().toString();
		
		
		return data;
	}
	public Model getSelectedModel() {
		
        
        return (Model) modelPicker.getSelectedItem();
	}
}
