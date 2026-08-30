package inv;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;

import com.anthropic.client.AnthropicClient;
import com.anthropic.client.okhttp.AnthropicOkHttpClient;
import com.anthropic.models.messages.Base64ImageSource;
import com.anthropic.models.messages.ContentBlockParam;
import com.anthropic.models.messages.ImageBlockParam;
import com.anthropic.models.messages.Message;
import com.anthropic.models.messages.MessageCreateParams;
import com.anthropic.models.messages.MessageParam;
import com.anthropic.models.messages.Model;
import com.anthropic.models.messages.TextBlockParam;
import com.github.lgooddatepicker.components.DatePicker;

import openize.heic.decoder.HeicImage;
import openize.heic.decoder.PixelFormat;
import openize.io.IOFileStream;
import openize.io.IOMode;

//this class should show a table of the past invoices
//payment status, date sent, all the info about the invoice
public class RecordsPanel extends MenuPanel {
	JPanel buttonPanel;
	JButton inputButton, viewButton;
	JButton deleteButton;
	JPanel bufferPanel, buffer2Panel, buffer3Panel;
	JButton analyzeButton;
	static AnalysisFrame aFrame;

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
		buffer3Panel = new JPanel();
		
		buttonPanel = new JPanel();
		buttonPanel.setPreferredSize(new Dimension((int)(Invoicer.WIDTH/1.1), Invoicer.HEIGHT/10));
		buttonPanel.setMaximumSize(buttonPanel.getPreferredSize());
		buttonPanel.setBackground(new Color(20,85,122));
		
		inputButton = new JButton("Input Check");
		viewButton = new JButton("View Check");
		deleteButton = new JButton("Delete");
		analyzeButton = new JButton("Scan Checks");
		analyzeButton.setToolTipText("Shift-click on a ClientBox to add a check alias");
		if(Invoicer.onMac) {
			inputButton.setForeground(Color.black);
			viewButton.setForeground(Color.black);
			deleteButton.setForeground(Color.black);
			analyzeButton.setForeground(Color.black);
			
		} else {
			inputButton.setForeground(Color.white);
			viewButton.setForeground(Color.white);
			deleteButton.setForeground(Color.white);
			analyzeButton.setForeground(Color.white);
		}
		inputButton.setBackground(new Color(40,160,230));
		inputButton.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,Invoicer.HEIGHT/27));
		viewButton.setBackground(new Color(40,160,230));
		viewButton.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,Invoicer.HEIGHT/27));
		deleteButton.setBackground(new Color(40,160,230));
		deleteButton.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,Invoicer.HEIGHT/27));
		inputButton.setPreferredSize(new Dimension(buttonPanel.getPreferredSize().width/4-20,buttonPanel.getPreferredSize().height-10));
		viewButton.setPreferredSize(new Dimension(buttonPanel.getPreferredSize().width/4-20,buttonPanel.getPreferredSize().height-10));
		deleteButton.setPreferredSize(new Dimension(buttonPanel.getPreferredSize().width/4-20,buttonPanel.getPreferredSize().height-10));
		
		
		analyzeButton.setBackground(new Color(40,160,230));
		analyzeButton.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,Invoicer.HEIGHT/27));
		analyzeButton.setPreferredSize(new Dimension(buttonPanel.getPreferredSize().width/4-20,buttonPanel.getPreferredSize().height-10));
		
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
			int[] rows = table.getSelectedRows();
			if(rows.length==0) {
				JOptionPane.showMessageDialog(null, "Select an invoice");
			} else if(JOptionPane.showConfirmDialog(null, "Are you sure you want to delete th"+(rows.length==1?"is":"ese")+" record"+(rows.length==1?"":"s")+"?")==0) {
				ArrayList<Record> deleteList = new ArrayList<Record>();
				for(int i=rows.length-1;i>=0;i--) {
					deleteList.add(recordsList.get(table.convertRowIndexToModel(rows[i])));
				}
				for(Record r: deleteList) recordsList.remove(r);
				updateTable();
				Invoicer.saveAllData();
			}
		});
		analyzeButton.addActionListener(e->{
			ArrayList<Record> unpaidList = new ArrayList<Record>();
			for(Record r: recordsList) {
				if(!r.check.paymentStatus) {
					unpaidList.add(r);
				}
			}
		/*	if(unpaidList.size()==0) {
				JOptionPane.showMessageDialog(null,"No unpaid checks remaining");
			} else {*/
				if(aFrame!=null) aFrame.dispose();
				aFrame = new AnalysisFrame(unpaidList);
			//}
			
		});
		
		buttonPanel.add(inputButton);
		bufferPanel.setPreferredSize(new Dimension(10,10));
		bufferPanel.setOpaque(false);
		buttonPanel.add(bufferPanel);
		buttonPanel.add(viewButton);
		buffer2Panel.setPreferredSize(new Dimension(10,10));
		buffer2Panel.setOpaque(false);
		buttonPanel.add(buffer2Panel);
		buttonPanel.add(deleteButton);
		
		buffer3Panel.setPreferredSize(new Dimension(10,10));
		buffer3Panel.setOpaque(false);
		buttonPanel.add(buffer3Panel);
		buttonPanel.add(analyzeButton);
		
		JLabel imageLabel = new JLabel(new ImageIcon(getClass().getResource("ai_icon_dual_final.png")));
		buttonPanel.add(imageLabel);
		
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
		
		File f = new File("/Volumes/HPBMCIASH/claude api key.txt");
		if(f.exists())
			try {
				AnalysisFrame.apiKey = new Scanner(f).nextLine();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		else AnalysisFrame.apiKey = System.getenv("CLAUDE_API_KEY");
		AnalysisFrame.client = AnthropicOkHttpClient.builder().apiKey(AnalysisFrame.apiKey).build();
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
		
		//this.paintAll(getGraphics());
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

public void nameChange(String currentName, String newName) {
	if(currentName.equals(newName)) return;

	//when client name is changed, change name of all records to reflect new name
	for (Record r: recordsList) {
		if(r.clientName.equals(currentName)) r.clientName=newName;
	}
	updateTable();
}
}
class Record {
	//a record is one entry in the table
	//per the specs, should store "sent invoice, payment status, check data"
	//will store invoice data
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
	public String toString() {
		return toFileString();
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
			Invoicer.saveAllData();
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

class AnalysisFrame extends JFrame {//TODO use servicedate to match
	static AnthropicClient client;
	static String apiKey;
	JPanel panel, leftPanel, rightPanel, topLeftPanel, bottomLeftPanel;
	File imageFile;
	File imageFileConverted;
	JLabel imageDisplay;
	ImageIcon scaledIcon;
	JButton scanButton;
	File output;
	String prompt = "Attached is an image of multiple check stubs. "
			+ "Return a list in CSV format, where each line represents one check stub."
			+ "Each entry in a line should follow the following format: "
			+ "name,invoice date,check date,service date,amount,id. don't include a period at the end."
			+ "for name, enter the client name, which is the name of the sender of the check."
			+ " This may displayed in the top left of a stub, "
			+ "or under 'location', or 'please post payment for'. "
			+ "For invoice date, enter the date listed under"
			+ "the text that reads Invoice Date, second from the left on the stubs."
			+ "This date should be exactly as it is written on the stub, for example,"
			+ " 10/15/2025. For check date, enter the date listed under the text that reads Check Date"
			+ " in mm/dd/yyyy format. If there is no such text, the check date is likely the date least associated with the invoice date."
			+ " The check date might be written on the check in a format "
			+ "that spells out the month name. In your response, write it in the mm/dd/yyyy format "
			+ "identically to the format for invoice date. This is the date when the check was sent. For service date, look at the invoice number"
			+ " on the check, which may be in the format of '2026-8' or '08/26' or something similar, composed of a month and a year."
			+ " Using this invoice number, write a date corresponding to the given month and year, with the day being 1."
			+ " For example, an invoice number of 2026-8 would lead to an entry of 8/1/2026. For amount, this should be a simple number with no commas,"
			+ " though it is okay to have a decimal point and the number of cents at the end. "
			+ "The amount can be found under Net Paid Amt on the check stub."
			+ " The id is the seven-digit number at the top right of the stub."
			+ " In the case that a single check stub has multiple lines under Invoice "
			+ "Date and Net Paid Amt, the returned list should contain a separate"
			+ " line for each listed date/amount pairing, with the name and id being identical."
			+ " Your response should contain nothing but the information that would be in a"
			+ " CSV file, with no commentary before or after. Make sure all dates are in mm/dd/yyyy format.";
	ArrayList<Record> unpaidList;

	public AnalysisFrame(ArrayList<Record> upList) {
		super("Check Analysis");
		setSize(400,400);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		panel = new JPanel();
		panel.setBackground(new Color(36,36,36));
		JPanel centerPanel = new JPanel();
		centerPanel.setOpaque(false);
		
		this.unpaidList=upList;
	     
			JLabel imageLabel = new JLabel("imagefilename");
			imageLabel.setForeground(new Color(36,36,36));
			imageDisplay = new JLabel();
			
			
			JButton chooserButton = new JButton("Choose Image");
			chooserButton.addActionListener(e->{
				JFileChooser jfc = new JFileChooser();
				FileFilter ff = new FileFilter() {
					public boolean accept(File f) {
						String name = f.getName().toLowerCase();					
						return name.endsWith(".png")||name.endsWith(".jpg")||name.endsWith(".jpeg")||f.isDirectory()||name.endsWith(".heic");
					}

					@Override
					public String getDescription() {
						// TODO Auto-generated method stub
						return "Image Files (.png, .jpg, .heic)";
					}
				};
				jfc.setFileFilter(ff);
				if(jfc.showOpenDialog(null)==JFileChooser.APPROVE_OPTION) {
					imageFile = jfc.getSelectedFile();
				String name = imageFile.getName().toLowerCase();
				if(name.endsWith(".png")||name.endsWith(".jpg")||name.endsWith(".jpeg")||name.endsWith(".heic")) {	
				imageLabel.setText(imageFile.getName());
				imageLabel.setForeground(Color.white);
				imageFileConverted = !isHeicFile(imageFile)?imageFile:heicToJpg(imageFile);
				ImageIcon unscaledIcon=new ImageIcon(imageFileConverted.getPath());
				int unscaledWidth = unscaledIcon.getIconWidth();
				int unscaledHeight = unscaledIcon.getIconHeight();
				double ratio;
				if(unscaledWidth>unscaledHeight) {
					ratio = 200d/unscaledWidth;
				} else ratio = 200d/unscaledHeight;
				scaledIcon = new ImageIcon(unscaledIcon.getImage().getScaledInstance((int)(unscaledWidth*ratio), (int)(unscaledHeight*ratio), Image.SCALE_SMOOTH));
				imageDisplay.setIcon(scaledIcon);
				}} else return;
			});
			
			scanButton = new JButton("Scan");
			scanButton.addActionListener(e->{
				if(imageFileConverted==null) {
					JOptionPane.showMessageDialog(null, "No image selected");
					return;
					}
				JDialog dialog = new JDialog(this, "Scanning", true);
				dialog.setSize(400,200);
				String[] strs = {"Scanning","Scanning.","Scanning..","Scanning..."};
				JLabel label = new JLabel(strs[0]);
				label.setForeground(Color.black);

				Timer animator = new Timer(500,new ActionListener() {
			        int i = -1;
			        @Override
			        public void actionPerformed(ActionEvent e) {
			            i = (i + 1) % 4;
			            label.setText(strs[i]);
			        }
			    });
				Thread dialogThread = new Thread(()->dialog.setVisible(true));
				
				dialog.add(label);
				dialog.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				
				ArrayList<Response>[] result = new ArrayList[1];
				
				Thread scanThread = new Thread(()-> {
				result[0] = scanCheck(imageFileConverted);
				//this takes a while because it pings an api
				
				animator.stop();
				dialog.dispose();
				System.out.println("done");
				//System.out.println(result[0].get(0).amount());
				});
				
				
				animator.start();
				scanThread.start();
				dialogThread.run();
				
				System.out.println("done scanning");
				//these below have to be two separate loops to prioritize perfection
				HashMap<Record, Response> perfectMatches = new HashMap<Record, Response>();
				//HashMap<Record,Response> imperfectMatches = new HashMap<Record, Response>();
				ArrayList<Record> toDel = new ArrayList<Record>();
				ArrayList<Response> toDelRes = new ArrayList<Response>();
				for(Response res: result[0]) {
					for(Record rec: unpaidList) {
						String cn = getClientName(res.name());
						if(cn==null) continue;
						//System.out.println("no continue");
						if(cn.equalsIgnoreCase(rec.clientName)) {
							//now we might populate the record
							
							if(rec.serviceDate.getYear()==res.serviceDate().getYear()&&rec.serviceDate.getMonth()==res.serviceDate().getMonth()&&rec.amount==res.amount()) {
							//rec.check.fill(res.amount(), res.checkDate(), res.checkNumber());
								System.out.println("perfect match");
								perfectMatches.put(rec,res);
								toDel.add(rec);
								toDelRes.add(res);
							break;
							}
						}
					}
				}//now all the perfect matches have been made
				//might be some with imperfect matches?
				
				for(Record rec: toDel) {
					unpaidList.remove(rec);
				}
				for(Response res: toDelRes) {
					result[0].remove(res);
				}/*
				toDel.clear();
				toDelRes.clear();
				for(Response res: result[0]) {
					for(Record rec: unpaidList) {
						String cn = getClientName(res.name());
						if(cn==null) continue;
						if(cn.equalsIgnoreCase(rec.clientName)) {
						imperfectMatches.put(rec, res);
						toDel.add(rec);
						toDelRes.add(res);
						break;
						}
					}}
				
				for(Record rec: toDel) {
					unpaidList.remove(rec);
				}
				for(Response res: toDelRes) {
					result[0].remove(res);
				}*/
				//TODO let you edit the failed check responses and send them back into records search
				//show all the perfect matches at once
				//add optional permanent edits to the prompt(in settings?)
				
				
				//now have unpaidList of records with no matches
				//and result[0] with responses which found no matches
				//and perfectMatches and imperfectMatches, maps which store matches
				System.out.println("perfect:");
				System.out.println(perfectMatches);
				//System.out.println(imperfectMatches);
				System.out.println(result[0]);
				//issues removing from unpaidlist??
				if(perfectMatches.size()==0) {
					JOptionPane.showMessageDialog(null,"No perfect matches found. Maybe try adding check aliases to clients?");
				} else {
					confirmScan(perfectMatches);
				}
				int choice = JOptionPane.showConfirmDialog(null, result[0].size()+" failed matches. View all?", null, JOptionPane.YES_NO_OPTION);
				if(choice==JOptionPane.YES_OPTION) {
					int i = 0;
					for(Response r: result[0]) {
						JOptionPane optionPane = new JOptionPane(r, JOptionPane.INFORMATION_MESSAGE);
				        JDialog d = optionPane.createDialog(null);
				        d.setLocation(d.getLocation().x+20*i,d.getLocation().y+20*i);
				        d.setModal(false); 
				        d.setVisible(true);
				        i++;
					}
				}
			});
			
			if(Invoicer.onMac) {
				scanButton.setForeground(Color.black);
				chooserButton.setForeground(Color.black);
				
			} else {
				scanButton.setForeground(Color.white);
				chooserButton.setForeground(Color.white);
				
			}
			
			scanButton.setBackground(new Color(40,160,230));
			scanButton.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,Invoicer.HEIGHT/27));
			scanButton.setPreferredSize(new Dimension(150,50));
			chooserButton.setBackground(new Color(40,160,230));
			chooserButton.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,Invoicer.HEIGHT/27));
			chooserButton.setPreferredSize(new Dimension(250,50));
			
		add(panel);
		centerPanel.setPreferredSize(new Dimension(400,200));
		panel.add(centerPanel);
		centerPanel.add(chooserButton);
		centerPanel.add(imageDisplay);
		centerPanel.add(imageLabel);
		JLabel tip = new JLabel("<html>Shift-click on a client to input a check alias.<br>This is how their name is written on the check.</html>");
		tip.setForeground(Color.white);
		panel.add(tip);
		panel.add(scanButton);
		
		
		
		setVisible(true);
		//to fill a check
		//check.fill(amount, chkDate, chkId);
		//Invoicer.rp.updateTable();
		//Invoicer.saveAllData();
	}
	void confirmScan(HashMap<Record,Response> pm) {
		int matches = pm.size();
		String[] options = {"View All","Yes","No"};
		int choice = JOptionPane.showOptionDialog(null, matches+" perfect matches found. Apply them?", "", 0, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
		
		switch(choice) {
		case 0:
			//view all	
			String s = "";
			for(Record r: pm.keySet()) {
				Response res = pm.get(r);
				s+=res+"\n";
			}
			JOptionPane.showMessageDialog(null,s);
			confirmScan(pm);
			break;
		case 1:
			//yes
			for(Record rec: pm.keySet()) {
				fillMatch(rec, pm.get(rec));
			}
			Invoicer.rp.updateTable();
			JOptionPane.showMessageDialog(null, "Successfully filled "+matches+" records");
			break;
		case 2:
			//no
			break;
		}
	}
	void fillMatch(Record rec, Response res) {
		if(rec.check.paymentStatus) throw new RuntimeException("Check already filled: "+rec.check);
		rec.check.fill(res.amount(), res.checkDate(), res.checkNumber());
		
	}
	String getClientName(String alias) {
		for(ClientBox cb: Invoicer.clp.clientList) {
			Client c = cb.client;
			if(c.checkAlias==null) continue;
			if(c.checkAlias.equalsIgnoreCase(alias)) {return c.name;}
		}
		return null;
	}
	ArrayList<Response> scanCheck(File image) {
		//this is where the magic happens
		//return number of how many checks couldn't be identified?
		//MISSION HILLS,10/15/2025,11/10/2025,1000.00,3718123
		String response=null;
		ArrayList<Response> list = new ArrayList<Response>();
		try {
			 response = parseMessage(callAI(prompt, image));
			//System.out.println(response);
			String[] lines = response.split("\n");
			for(String line: lines) {
				//unpaidList exists
				String[] splitLine = line.split(",");
				String name = splitLine[0]; //the name on the check may be differet from the client name
					String[] splitInvDate = splitLine[1].split("/");
					String[] splitChkDate = splitLine[2].split("/");
					String[] splitServDate = splitLine[3].split("/");
				LocalDate invoiceDate = LocalDate.of(Integer.parseInt(splitInvDate[2]), Integer.parseInt(splitInvDate[0]), Integer.parseInt(splitInvDate[1]));
				LocalDate checkDate = LocalDate.of(Integer.parseInt(splitChkDate[2]), Integer.parseInt(splitChkDate[0]), Integer.parseInt(splitChkDate[1]));
				LocalDate serviceDate = LocalDate.of(Integer.parseInt(splitServDate[2]), Integer.parseInt(splitServDate[0]), Integer.parseInt(splitServDate[1]));
				double amount = Double.parseDouble(splitLine[4]);
				String checkNum = splitLine[5];
			/*System.out.println("name: "+name);
				System.out.println("invd "+invoiceDate);
				System.out.println("chkd: "+checkDate);
				System.out.println("amt: "+amount);
				System.out.println("num: "+checkNum);*/
				//maybe create a Response record object to store the data
				Response r = new Response(name, invoiceDate, checkDate, serviceDate, amount, checkNum);
				list.add(r);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch(Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error parsing AI response. Check console.");
			System.out.println(response);
			return null;
		}
		return list;
		
	}
	String callAI(String prompt, File img) throws IOException{
		if(img==null) return null;
		if(isHeicFile(img)) return callAI(prompt, heicToJpg(img));
		long maxTokens=512;
		boolean isJpeg = isJpeg(img.getPath());
		String b64;
			
								
				byte[] bytes = Files.readAllBytes(img.toPath());
				b64 = Base64.getEncoder().encodeToString(bytes);

			
		MessageCreateParams params = MessageCreateParams.builder()
				.system("Respond only in plain text.")
				.model(Model.CLAUDE_HAIKU_4_5)
				//.addUserMessage(prompt)
				.addMessage(MessageParam.builder()
						.role(MessageParam.Role.USER)
						.content(MessageParam.Content.ofBlockParams(List.of(
								ContentBlockParam.ofImage(ImageBlockParam.builder().source(Base64ImageSource.builder()
										.data(b64)
										.mediaType(isJpeg?Base64ImageSource.MediaType.IMAGE_JPEG:Base64ImageSource.MediaType.IMAGE_PNG)
										.build()
										).build()
						), ContentBlockParam.ofText(TextBlockParam.builder().text(prompt).build())
								))).build())
				.maxTokens(maxTokens)
				.build();
		
		Message msg = client.messages().create(params);
		return msg.toString();
			}
	boolean isJpeg(String filePath) {
		byte[] header = new byte[8];
		try {
			if(new BufferedInputStream(new FileInputStream(filePath)).read(header,0,8)<2) {
				throw new RuntimeException("GigaError - small file");
			}
			if(header[0]==(byte)0xFF&&header[1]==(byte)0xD8) {
				return true;
			}
			byte[] pngMagic = {(byte) 0x89 ,0x50 ,0x4E ,0x47 ,0x0D ,0x0A ,0x1A ,0x0A};
			if(Arrays.equals(header, pngMagic)) return false;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		throw new RuntimeException("Super GigaError");
	}
	    public static boolean isHeicFile(File file) {
	    	 Set<String> HEIC_BRANDS = Set.of("heic", "heix", "mif1", "msf1");
	        if (file == null || !file.exists() || file.isDirectory()) {
	            return false;
	        }

	        byte[] header = new byte[12];
	        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
	            int bytesRead = bis.read(header, 0, 12);
	            if (bytesRead < 12) {
	                return false;
	            }
	        } catch (IOException e) {
	            return false; // Handle or log exception according to your application logic
	        }

	        // Extract "ftyp" signature from bytes 4-7
	        String ftyp = new String(header, 4, 4, StandardCharsets.US_ASCII);
	        if (!"ftyp".equals(ftyp)) {
	            return false;
	        }

	        // Extract the major brand from bytes 8-11
	        String majorBrand = new String(header, 8, 4, StandardCharsets.US_ASCII).toLowerCase();
	        
	        return HEIC_BRANDS.contains(majorBrand);
	    }
	String parseMessage(String rawMessage) {
		return rawMessage.split("text=")[2].split(", type=text")[0];
	}
	File heicToJpg(File heicFile) {
		
		JDialog dialog = new JDialog(RecordsPanel.aFrame, "HEIC Convertion", true);
		dialog.setSize(300,150);
		dialog.setLocationRelativeTo(RecordsPanel.aFrame);
		JLabel message = new JLabel("            Converting image... (0%)");
		dialog.add(message);
		dialog.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		Thread t = new Thread(()->{
			String filename = heicFile.getName().substring(0,heicFile.getName().lastIndexOf("."))+".jpeg";
			output = new File(FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "/InVoicer/"+filename);
			output.deleteOnExit();
		
			IOFileStream fs = new IOFileStream(heicFile.getAbsolutePath(), IOMode.READ);
			 HeicImage image = HeicImage.load(fs);
			 
			 int width = (int) image.getWidth();
	            int height = (int) image.getHeight();
	            message.setText("            Converting image... (10%)");
	            // 3. Extract the raw pixel blocks as ARGB integers
	            int[] pixels = image.getInt32Array(PixelFormat.Argb32);
	            message.setText("            Converting image... (30%)");
	            // 4. Create an empty blank standard Java BufferedImage 
	            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	            message.setText("            Converting image... (40%)");
	            // 5. Blast the raw pixel block arrays onto the buffered object canvas
	            bufferedImage.setRGB(0, 0, width, height, pixels, 0, width);
	            message.setText("            Converting image... (50%)");
	            // 3. Write out using standard ImageIO
	            try {
	            	message.setText("            Converting image... (60%)");
	            	ImageIO.write(bufferedImage, "JPG", output);
					message.setText("            Converting image... (100%)");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					dialog.dispose();
				}
	    		dialog.dispose();
		});
		t.start();
		dialog.setVisible(true);
		return output;
	}
}
record Response(String name, LocalDate invoiceDate, LocalDate checkDate, LocalDate serviceDate, double amount, String checkNumber) {

}

