package inv;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.github.lgooddatepicker.components.DatePicker;

//this class should show a table of the past invoices
//payment status, date sent, all the info about the invoice
//TODO add button to delete record
public class RecordsPanel extends MenuPanel {
	JPanel buttonPanel;
	JButton inputButton, viewButton;
	JButton deleteButton;
	JPanel bufferPanel, buffer2Panel;

	//will hold two buttons - 1 to input check data, 1 to view check info of selected row
	JTable table;
	JScrollPane tablePane;
	static ArrayList<Record> recordsList = new ArrayList<Record>();
	public RecordsPanel() {
		super();
		updateTable();
		setLayout(new BorderLayout());
		bufferPanel = new JPanel();
		buffer2Panel = new JPanel();
		
		buttonPanel = new JPanel();
		buttonPanel.setPreferredSize(new Dimension((int)(Invoicer.WIDTH/1.1), Invoicer.HEIGHT/10));
		buttonPanel.setMaximumSize(buttonPanel.getPreferredSize());
		buttonPanel.setBackground(new Color(20,85,122));
		
		inputButton = new JButton("Input Check");
		viewButton = new JButton("View Check");
		deleteButton = new JButton("Delete");
		if(Invoicer.onMac) {
			inputButton.setForeground(Color.black);
			viewButton.setForeground(Color.black);
			deleteButton.setForeground(Color.black);
		} else {
			inputButton.setForeground(Color.white);
			viewButton.setForeground(Color.white);
			deleteButton.setForeground(Color.white);
		}
		inputButton.setBackground(new Color(40,160,230));
		inputButton.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,Invoicer.HEIGHT/25));
		viewButton.setBackground(new Color(40,160,230));
		viewButton.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,Invoicer.HEIGHT/25));
		deleteButton.setBackground(new Color(40,160,230));
		deleteButton.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,Invoicer.HEIGHT/25));
		inputButton.setPreferredSize(new Dimension(buttonPanel.getPreferredSize().width/3-20,buttonPanel.getPreferredSize().height-10));
		viewButton.setPreferredSize(new Dimension(buttonPanel.getPreferredSize().width/3-20,buttonPanel.getPreferredSize().height-10));
		deleteButton.setPreferredSize(new Dimension(buttonPanel.getPreferredSize().width/3-20,buttonPanel.getPreferredSize().height-10));
		
		inputButton.addActionListener(e->{
			int row = table.getSelectedRow();
			if(row==-1) {
				JOptionPane.showMessageDialog(null, "Select an invoice");
			} else {
				//check if selected row has already been filled
				if(!table.getValueAt(row, 6).equals("Unpaid")) {
					if(JOptionPane.showConfirmDialog(null, "Are you sure you want to override the current check?")==0) {
						new CheckInputFrame(recordsList.get(row).check);
					}
				} else {
					new CheckInputFrame(recordsList.get(row).check);
				}
			
			}
		});
		viewButton.addActionListener(e->{
			int row = table.getSelectedRow();
			if(row==-1) {
				JOptionPane.showMessageDialog(null, "Select an invoice");
			} else if(table.getValueAt(row, 6).equals("Unpaid")) {
				JOptionPane.showMessageDialog(null, "No check saved");
			} else {
				new CheckViewFrame(recordsList.get(row));
			}
		});
		deleteButton.addActionListener(e->{
			int row = table.getSelectedRow();
			if(row==-1) {
				JOptionPane.showMessageDialog(null, "Select an invoice");
			} else if(JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this record?")==0) {
				recordsList.remove(row);
				updateTable();
			}
		});
		
		buttonPanel.add(inputButton);
		bufferPanel.setPreferredSize(new Dimension(10,10));
		bufferPanel.setBackground(new Color(20,85,122));
		buttonPanel.add(bufferPanel);
		buttonPanel.add(viewButton);
		buffer2Panel.setPreferredSize(new Dimension(10,10));
		buffer2Panel.setBackground(new Color(20,85,122));
		buttonPanel.add(buffer2Panel);
		buttonPanel.add(deleteButton);
		
		add(buttonPanel,BorderLayout.NORTH);
		
		table.setRowHeight(Invoicer.HEIGHT*32/700);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoCreateRowSorter(true);
		tablePane = new JScrollPane(table);
		tablePane.getVerticalScrollBar().setUnitIncrement(6);
		table.setFillsViewportHeight(true);
		
		table.setFont(table.getFont().deriveFont((float)(table.getFont().getSize()*1.4)));
		table.setBackground(new Color(31,31,31));
		table.setForeground(Color.white);
		table.setGridColor(Color.white);
		table.setSelectionBackground(new Color(20,85,122));
		table.setSelectionForeground(Color.white);
		
		add(tablePane,BorderLayout.CENTER);
		
	}

	public void newRecord(Client client, String service, double amount, LocalDate serviceDate,
			LocalDate billDate) {
		//make a record and add to recordslist
		new Record(client.name, client.doctor, service, amount, serviceDate, billDate);
		updateTable();
	}
	void updateTable() {
		//fills table with all data from recordslist
		String[][] array = new String[recordsList.size()][8];
		for(int i = 0; i<recordsList.size();i++) {
			array[i]=recordsList.get(i).toStrArray();
		}
		String[] titles = {"Date Sent","Client","Doctor","Date of Service","Service","Amount","Check","Notes"};
	
		if(table==null) {
			table = new JTable(new DefaultTableModel(array,titles)) {public boolean isCellEditable(int row, int column) {
				if(column==7) return true;
				return false;
			}};
			table.getModel().addTableModelListener(e->{
			recordsList.get(e.getFirstRow()).notes=(String)table.getValueAt(e.getFirstRow(), 7);
			});
			}
		else table.setModel(new DefaultTableModel(array,titles));
		table.getModel().addTableModelListener(e->{	
		recordsList.get(e.getFirstRow()).notes=(String)table.getValueAt(e.getFirstRow(), 7);
		});
		
		this.paintAll(getGraphics());
	}
public String toFileString() {
	String s = "";
	for(Record record:recordsList) {
		s+=record.toFileString();
	}
	if(s.isEmpty()) return "Empty";
	return s;
}
public void loadData(String fileData) {
	if(fileData.equals("Empty")) return;
	String[] recordsArr = fileData.split("<record>");
	for(String recordStr:recordsArr) {
		//Record(String name, String doctor, String serv, double amt, 
		//LocalDate sDate, LocalDate billDate, String notes, Check chk) 
		String[] fields = recordStr.split("<break>");
		String name = fields[0];
		String doc=fields[1];
		String service = fields[2];
		double amt = Double.parseDouble(fields[3]);
		LocalDate serviceDate=LocalDate.parse(fields[4]);
		LocalDate billDate=LocalDate.parse(fields[5]);
		String notes = fields[6].equals("null")?"":fields[6];
		Check check = new Check(fields[7]);
		new Record(name,doc,service,amt,serviceDate,billDate,notes,check);
	}
	updateTable();
}
}
class Record {
	//a record is one entry in the table
	//per the specs, should store "sent invoice, payment status, check data"
	//will store invoice data
	//TODO uhhh take in check data?
	//check will store payment data ahahhaa
	String clientName;
	String docName;
	String service;
	double amount;
	LocalDate serviceDate;
	LocalDate billDate;
	Check check;
	String notes;
	
	public Record(String name, String doctor, String serv, double amt, LocalDate sDate, LocalDate billDate) {
		//fresh record off the creator panel
		docName=doctor;
		clientName=name;
		service=serv;
		amount=amt;
		serviceDate=sDate;
		this.billDate=billDate;
		notes="";
		check=new Check();
		RecordsPanel.recordsList.add(this);
	}
	public Record(String name, String doctor, String serv, double amt, LocalDate sDate, LocalDate billDate, String notes, Check chk) {
		//should probably be used only when loading from save file
		docName=doctor;
		clientName=name;
		service=serv;
		amount=amt;
		serviceDate=sDate;
		this.billDate=billDate;
		check=chk;
		this.notes=notes;
		RecordsPanel.recordsList.add(this);

	}
	public String[] toStrArray() {
		//when adding doctor maybe change updateTable()
		String[] arr=new String[8];
		//"Date Sent","Client","Date of Service","Service","Amount","Check","Notes"
		arr[0]=billDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
		arr[1]=clientName;
		arr[2]=docName;
		arr[3]=serviceDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
		arr[4]=service;
		arr[5]=""+amount;
		arr[6]=check.toString();
		arr[7]=notes;
		return arr;
	}
	public String toFileString() {
		//make sure to end it with <record>
		/*
	String clientName;
	String docName;
	String service;
	double amount;
	LocalDate serviceDate;
	LocalDate billDate;
	Check check;
	String notes;
	*/
		//should probably place notes before check
		
		String s=clientName+"<break>"+docName+"<break>"+service+"<break>"+amount+"<break>"+serviceDate
				+"<break>"+billDate+"<break>"+(notes.isEmpty()?"null":notes)+"<break>"+check.toFileString()
				+"<record>"; 
		return s;
	}
}
class Check {
	LocalDate checkDate;
	String checkId;
	double amount;
	boolean paymentStatus;
	
	public Check() {
		paymentStatus=false;
	}
	
	public Check(double amt, LocalDate chkDate, String chkId) {
		paymentStatus=true;

		amount=amt;
		checkDate=chkDate;
		checkId=chkId;
	}
	public Check(String fileData) {
		
		//for loading from file
		if(fileData.equals("Unpaid")) {paymentStatus=false; return;}
		paymentStatus=true;
		String[] arr = fileData.split("<chbreak>");
		checkDate=LocalDate.parse(arr[0]);
		checkId=arr[1];
		amount=Double.parseDouble(arr[2]);
	}
	void fill(double amt, LocalDate chkDate, String chkId) {
		paymentStatus=true;
		amount=amt;
		checkDate=chkDate;
		checkId=chkId;
	}
	public String toString() {
		if(paymentStatus) {
			return checkId;
		} else {
			return "Unpaid";
		}
	}
	public String toFileString() {
		if(!paymentStatus) return "Unpaid";
		//do not use <break> in here
		String s = checkDate+"<chbreak>"+checkId+"<chbreak>"+amount;
		return s;
		
	}
	
}
class CheckInputFrame extends JFrame {
	Check check;
	JPanel panel;
	DatePicker checkDatePicker;
	JTextField checkIdField, amountField;
	JLabel checkDateLabel, checkIdLabel, amountLabel;
	JButton generateButton;
	JLabel errorLabel;
public CheckInputFrame(Check chk) {
	super("Input Check Data");
	check=chk;
	panel=new JPanel();
	setSize(Invoicer.WIDTH*8/10,Invoicer.HEIGHT*85/100);
	setVisible(true);
	add(panel);
	panel.setBackground(new Color(50,50,50));
	panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
	Font labelFont = new Font(Font.SANS_SERIF, Font.PLAIN, Invoicer.HEIGHT/30);
	Font fieldFont = new Font(Font.SANS_SERIF, Font.PLAIN, Invoicer.HEIGHT/35);
	Dimension fieldDim = new Dimension(Invoicer.WIDTH*8/10,Invoicer.HEIGHT/20);
	//(String invNum, LocalDate invDate, double amt, LocalDate chkDate, String chkId
	
	
	panel.add(bufferPanel());
	
	amountLabel = new JLabel("Amount");
	amountLabel.setForeground(Color.white);
	amountLabel.setFont(labelFont);
	amountField = new JTextField();
	amountField.setMaximumSize(fieldDim);
	amountField.setFont(fieldFont);
	
	panel.add(amountLabel);
	panel.add(amountField);
	panel.add(bufferPanel());
	
	checkDatePicker = new DatePicker();
	checkDatePicker.setMaximumSize(new Dimension(Invoicer.WIDTH*8/10,Invoicer.HEIGHT/20));
	checkDateLabel = new JLabel("Check Date");
	checkDateLabel.setForeground(Color.white);
	checkDateLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, Invoicer.HEIGHT/30));
	
	panel.add(checkDateLabel);
	panel.add(checkDatePicker);
	panel.add(bufferPanel());
	
	checkIdLabel = new JLabel("Check Number");
	checkIdLabel.setForeground(Color.white);
	checkIdLabel.setFont(labelFont);
	checkIdField = new JTextField();
	checkIdField.setMaximumSize(fieldDim);
	checkIdField.setFont(fieldFont);
	
	panel.add(checkIdLabel);
	panel.add(checkIdField);
	panel.add(bufferPanel());
	
	//
	generateButton = new JButton("Input Check Data");
	if(Invoicer.onMac) {
		generateButton.setForeground(Color.black);
	} else {
		generateButton.setForeground(Color.white);
	}
	generateButton.setBackground(new Color(40,160,230));
	generateButton.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,Invoicer.HEIGHT/20));
	generateButton.addActionListener(e->{
		//(String invNum, LocalDate invDate, double amt, LocalDate chkDate, String chkId
		try {
			LocalDate chkDate = checkDatePicker.getDate();	
			String chkId = checkIdField.getText();
			double amount = Double.parseDouble((amountField.getText()));
			if(chkId==null||chkId.equals("")) throw new Exception();
			//now populate the check
			check.fill(amount, chkDate, chkId);
			Invoicer.rp.updateTable();
			this.dispose();
		} catch (Exception ex) {
			errorLabel.setVisible(true);
			//ex.printStackTrace();
		}
	});
	generateButton.setPreferredSize(new Dimension(Invoicer.WIDTH*8/10,Invoicer.HEIGHT/10));
	panel.add(generateButton);
	
	errorLabel = new JLabel("Some fields not filled out properly");
	errorLabel.setForeground(Color.red);
	errorLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, Invoicer.HEIGHT/35));
	errorLabel.setVisible(false);
	
	panel.add(bufferPanel());
	panel.add(errorLabel);
	
}
JPanel bufferPanel() {
	JPanel buffPanel = new JPanel();
	buffPanel.setPreferredSize(new Dimension(Invoicer.WIDTH*6/10,Invoicer.HEIGHT/50));
	buffPanel.setMaximumSize(new Dimension(Invoicer.WIDTH*6/10,Invoicer.HEIGHT/40));
	buffPanel.setBackground(new Color(50,50,50));
	return buffPanel;
}
}
class CheckViewFrame extends JFrame {
	Check check;
	Record rec;
	JPanel panel;
	//(String invNum, LocalDate invDate, double amt, LocalDate chkDate, String chkId

	JLabel checkDateLabel, idLabel, amountLabel;
	public CheckViewFrame(Record rec) {
		super("View Check Data");
		panel=new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));

		check=rec.check;
		this.rec=rec;
		setSize(Invoicer.WIDTH*4/5,Invoicer.HEIGHT*60/100);
		setVisible(true);
		add(panel);
		panel.add(bufferPanel());
		panel.setBackground(new Color(40,40,40));
		
		amountLabel = new JLabel("Amount: "+check.amount);
		amountLabel.setForeground(Color.white);
		amountLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, Invoicer.HEIGHT/30));
		panel.add(amountLabel);
		panel.add(bufferPanel());
	
		checkDateLabel = new JLabel("Check Date: "+check.checkDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
		checkDateLabel.setForeground(Color.white);
		checkDateLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, Invoicer.HEIGHT/30));
		panel.add(checkDateLabel);
		panel.add(bufferPanel());
		
		idLabel = new JLabel("Check Number: "+check.checkId);
		idLabel.setForeground(Color.white);
		idLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, Invoicer.HEIGHT/30));
		panel.add(idLabel);
		panel.add(bufferPanel());
		
}
	JPanel bufferPanel() {
		JPanel buffPanel = new JPanel();
		buffPanel.setPreferredSize(new Dimension(Invoicer.WIDTH*6/10,Invoicer.HEIGHT/50));
		buffPanel.setMaximumSize(new Dimension(Invoicer.WIDTH*6/10,Invoicer.HEIGHT/40));
		buffPanel.setBackground(new Color(40,40,40));
		return buffPanel;
	}
}
