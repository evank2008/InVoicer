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
//let you push a button to say that an invoice has been paid, input check data
//TODO view check data button
public class RecordsPanel extends MenuPanel {
	JPanel buttonPanel;
	JButton inputButton, viewButton;
	JPanel bufferPanel;

	//will hold two buttons - 1 to input check data, 1 to view check info of selected row
	JTable table;
	JScrollPane tablePane;
	static ArrayList<Record> recordsList = new ArrayList<Record>();
	public RecordsPanel() {
		super();
		setLayout(new BorderLayout());
		loadRecordsFile();
		bufferPanel = new JPanel();
		
		buttonPanel = new JPanel();
		buttonPanel.setPreferredSize(new Dimension((int)(Invoicer.WIDTH/1.1), Invoicer.HEIGHT/10));
		buttonPanel.setMaximumSize(buttonPanel.getPreferredSize());
		buttonPanel.setBackground(new Color(20,85,122));
		
		inputButton = new JButton("Input Check");
		viewButton = new JButton("View Check");
		if(Invoicer.onMac) {
			inputButton.setForeground(Color.black);
			viewButton.setForeground(Color.black);
		} else {
			inputButton.setForeground(Color.white);
			viewButton.setForeground(Color.white);
		}
		inputButton.setBackground(new Color(40,160,230));
		inputButton.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,Invoicer.HEIGHT/20));
		viewButton.setBackground(new Color(40,160,230));
		viewButton.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,Invoicer.HEIGHT/20));
		inputButton.setPreferredSize(new Dimension(buttonPanel.getPreferredSize().width/2-20,buttonPanel.getPreferredSize().height-10));
		viewButton.setPreferredSize(new Dimension(buttonPanel.getPreferredSize().width/2-20,buttonPanel.getPreferredSize().height-10));
		
		inputButton.addActionListener(e->{
			int row = table.getSelectedRow();
			if(row==-1) {
				JOptionPane.showMessageDialog(null, "Select an invoice");
			} else {
				//check if selected row has already been filled
				if(!table.getValueAt(row, 5).equals("Unpaid")) {
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
			} else if(table.getValueAt(row, 5).equals("Unpaid")) {
				JOptionPane.showMessageDialog(null, "No check saved");
			} else {
				new CheckViewFrame(recordsList.get(row));
			}
				
			
		});
		
		buttonPanel.add(inputButton);
		bufferPanel.setPreferredSize(new Dimension(10,10));
		bufferPanel.setBackground(new Color(20,85,122));
		buttonPanel.add(bufferPanel);
		buttonPanel.add(viewButton);
		
		add(buttonPanel,BorderLayout.NORTH);
		
		table.setRowHeight(Invoicer.HEIGHT*32/700);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoCreateRowSorter(true);
		tablePane = new JScrollPane(table);
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
		new Record(client.name, service, amount, serviceDate, billDate);
		updateTable();
	}
	void loadRecordsFile() {
		//TODO this
		updateTable();
	}
	void updateTable() {
		//fills table with all data from recordslist
		String[][] array = new String[recordsList.size()][7];
		for(int i = 0; i<recordsList.size();i++) {
			array[i]=recordsList.get(i).toStrArray();
		}
		String[] titles = {"Date Sent","Client","Date of Service","Service","Amount","Check","Notes"};
	
		if(table==null) {
			table = new JTable(new DefaultTableModel(array,titles)) {public boolean isCellEditable(int row, int column) {
				if(column==6) return true;
				return false;
			}};
			table.getModel().addTableModelListener(e->{
			recordsList.get(e.getFirstRow()).notes=(String)table.getValueAt(e.getFirstRow(), 6);
			});
			}
		else table.setModel(new DefaultTableModel(array,titles));
		table.getModel().addTableModelListener(e->{	
		recordsList.get(e.getFirstRow()).notes=(String)table.getValueAt(e.getFirstRow(), 6);
		});
		
		this.paintAll(getGraphics());
	}
	public String toFileString() {
		String s="";
		for(Record r: recordsList) {
			s+=r.toFileString();
		}
		return s;
	}

	public void loadData(String line) {
		System.out.println("Records loading data");
		System.out.println(line==null?"Null":line);
		if(line==null) return;
	}
}
class Record {
	//a record is one entry in the table
	//per the specs, should store "sent invoice, payment status, check data"
	//will store invoice data
	//TODO uhhh take in check data?
	//check will store payment data ahahhaa
	String clientName;
	String service;
	double amount;
	LocalDate serviceDate;
	LocalDate billDate;
	Check check;
	String notes;
	
	public Record(String name, String serv, double amt, LocalDate sDate, LocalDate billDate) {
		//fresh record off the creator panel
		clientName=name;
		service=serv;
		amount=amt;
		serviceDate=sDate;
		this.billDate=billDate;
		notes="";
		check=new Check();
		
		RecordsPanel.recordsList.add(this);
	}
	public Record(String name, String serv, double amt, LocalDate sDate, LocalDate billDate,boolean status, String notes, Check chk) {
		//should probably be used only when loading from save file
		this( name,  serv,  amt,  sDate,  billDate);
		check=chk;
		this.notes=notes;
	}
	public String[] toStrArray() {
		String[] arr=new String[7];
		//"Date Sent","Client","Date of Service","Service","Amount","Check","Notes"
		arr[0]=billDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
		arr[1]=clientName;
		arr[2]=serviceDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
		arr[3]=service;
		arr[4]=""+amount;
		arr[5]=check.toString();
		arr[6]=notes;
		return arr;
	}
	public String toFileString() {
		/*String clientName;
	String service;
	double amount;
	LocalDate serviceDate;
	LocalDate billDate;
	Check check;
	String notes;*/
		return ","+clientName+","+service+","+amount+","+serviceDate+","+billDate+","+notes+","+check.toFileString()+"<record>";
	}
}
class Check {
	LocalDate checkDate;
	String checkId;
	double amount;
	String invoiceNum;
	LocalDate invoiceDate;
	boolean paymentStatus;
	
	public Check() {
		paymentStatus=false;
	}
	public Check(String invNum, LocalDate invDate, double amt, LocalDate chkDate, String chkId) {
		paymentStatus=true;
		invoiceNum=invNum;
		invoiceDate=invDate;
		amount=amt;
		checkDate=chkDate;
		checkId=chkId;
	}
	void fill(String invNum, LocalDate invDate, double amt, LocalDate chkDate, String chkId) {
		paymentStatus=true;
		invoiceNum=invNum;
		invoiceDate=invDate;
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
		return checkDate.format(DateTimeFormatter.ISO_LOCAL_DATE)+","+checkId+","+amount+","+invoiceNum+","+invoiceDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
		
	}
}
class CheckInputFrame extends JFrame {
	Check check;
	JPanel panel;
	DatePicker checkDatePicker, invoiceDatePicker;
	JTextField checkIdField, amountField, invoiceNumField;
	JLabel checkDateLabel, invoiceDateLabel, checkIdLabel, amountLabel, invoiceNumLabel;
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
	invoiceNumLabel = new JLabel("Invoice Number");
	invoiceNumLabel.setForeground(Color.white);
	invoiceNumLabel.setFont(labelFont);
	invoiceNumField = new JTextField();
	invoiceNumField.setMaximumSize(fieldDim);
	invoiceNumField.setFont(fieldFont);
	
	panel.add(bufferPanel());
	panel.add(invoiceNumLabel);
	panel.add(invoiceNumField);
	panel.add(bufferPanel());
	
	invoiceDatePicker = new DatePicker();
	invoiceDatePicker.setMaximumSize(new Dimension(Invoicer.WIDTH*8/10,Invoicer.HEIGHT/20));
	invoiceDateLabel = new JLabel("Invoice Date");
	invoiceDateLabel.setForeground(Color.white);
	invoiceDateLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, Invoicer.HEIGHT/30));
	
	panel.add(invoiceDateLabel);
	panel.add(invoiceDatePicker);
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
			String invNum = invoiceNumField.getText();
			LocalDate invDate = invoiceDatePicker.getDate();
			LocalDate chkDate = checkDatePicker.getDate();	
			String chkId = checkIdField.getText();
			double amount = Double.parseDouble((amountField.getText()));
			if(invNum==null||chkId==null||invNum.equals("")||chkId.equals("")) throw new Exception();
			//now populate the check
			check.fill(invNum, invDate, amount, chkDate, chkId);
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

	JLabel checkDateLabel, idLabel, amountLabel, invoiceNumLabel, invoiceDateLabel;
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
		
		
		invoiceNumLabel = new JLabel("Invoice Number: "+check.invoiceNum);
		invoiceNumLabel.setForeground(Color.white);
		invoiceNumLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, Invoicer.HEIGHT/30));
		panel.add(invoiceNumLabel);
		panel.add(bufferPanel());
		
		invoiceDateLabel = new JLabel("Invoice Date: "+check.invoiceDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
		invoiceDateLabel.setForeground(Color.white);
		invoiceDateLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, Invoicer.HEIGHT/30));
		panel.add(invoiceDateLabel);
		panel.add(bufferPanel());
		
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
