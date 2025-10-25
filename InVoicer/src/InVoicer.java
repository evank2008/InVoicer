import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class InVoicer {

	/*TODO: 
	 * option to select preexisting clients
	 * optional: notes section? just add another column or smthn
	 */
	//this program will create a window where you can input and view
	//transaction histories that are paid or unpaid
	//button to filter by properties
	//way to interact with each entry to manipulate properties
		//"paid" button etc
		//box that tracks when last updated
		//give the paid ones a darker background
	//store the data in a file and load it on opening
	//when making a new invoice give a lil dropdown menu of all past clients or 
	//an entry text box
	static ArrayList<Invoice> invoiceList = new ArrayList<Invoice>();
	static JFrame frame;
	DynamicTable jt;
	JPanel tPanel;
	//tpanel holds the table
	JPanel jn;
	//jn holds the top buttons
	boolean saveStatus = true;
	public static void main(String[] args) {
		
		new InVoicer();
	}
	public InVoicer() {
		loadListFromFile();
		frame = new JFrame("InVoicer");
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);	
		jt.setRowHeight(32);
		jt.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		jt.getTableHeader().setReorderingAllowed(false);
		jt.setAutoCreateRowSorter(true);
		frame.setLayout(new BorderLayout());
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        if(saveStatus) System.exit(0);
		        else {
		        	switch(JOptionPane.showConfirmDialog(null, "Some changes may not have been saved. Save before closing?")) {
		        	case 0:
		        		saveTable();
		        		System.exit(0);
		        		break;
		        	case 1:
		        		System.exit(0);
		        		break;
		        	case 2:
		        		break;
		        	}
		        }
		    }
		});
		jn = new JPanel();
		
		JButton jb = new JButton("New");
		jb.addActionListener(e->{
			DefaultTableModel tm = (DefaultTableModel) jt.getModel();
			tm.addRow(new Invoice().toArray());
		});
		jt.getModel().addTableModelListener((e)->{
			saveStatus=false;
			frame.setTitle("*InVoicer");
		});
		jn.add(jb);
		
		JButton payB = new JButton("Pay");
		payB.addActionListener(e->{
			int row = jt.getSelectedRow();
			
			String date=LocalDate.now().format(DateTimeFormatter.ISO_DATE);
			jt.setValueAt(date,row,4);
			
			jt.repaint();
			
			//invoiceList.get(row).pay();
			//updateTable();
		});
		jn.add(payB);
		
		JButton unpayB = new JButton("Unpay");
		unpayB.addActionListener(e->{
			int row = jt.getSelectedRow();
			jt.setValueAt("Unpaid",row,4);	
			jt.repaint();
		});
		jn.add(unpayB);
		
		JButton sb = new JButton("Save");
		sb.addActionListener(e->{
			saveTable();
		});
		jn.add(sb);
		
		frame.add(jn, BorderLayout.NORTH);
		
		JScrollPane sp = new JScrollPane(jt);
		jt.setFillsViewportHeight(true);
		tPanel = new JPanel();
		tPanel.setLayout(new BorderLayout());
		tPanel.add(sp);
		frame.add(tPanel,BorderLayout.AFTER_LAST_LINE);
		
		
		frame.pack();
	}
	
	 void loadListFromFile() {
		 try {
		 String path = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();
		 BufferedReader br = new BufferedReader(new FileReader(new File(path+"/invoiceData.csv")));
		 String line;
		 while((line = br.readLine())!=null) {
			invoiceList.add(new Invoice(line));
		 }
		 } catch(FileNotFoundException e) {
			 //oh well haha i guess theres no data
			 //TODO: handle lack of data better, maybe show a funny image or smthn
			 //or just leave it blank that works too
			 /*
			 Invoice in = new Invoice(600,"John Cliente");
				Invoice j = new Invoice(4567.88,"Sammy Rich");
				invoiceList.add(in);
				invoiceList.add(j);
				*/
		 }
		 catch(Exception e) {
			 e.printStackTrace();
		 }
		
		updateTable();
	}
	 void updateTable() {
		 //this method fills in the table with all the data from invoiceList
		 String[][] array = new String[InVoicer.invoiceList.size()][6];
			for(int i = 0; i<InVoicer.invoiceList.size();i++) {
				array[i]=InVoicer.invoiceList.get(i).toArray();
			}
			//array is now 2d array of unboxed data of every invoice
			//initial date, service, client, amount, payment status, and last time updated
			String[] titles = {"Date Created","Service","Client","Amount","Payment Status"};
			jt = new DynamicTable(new DefaultTableModel(array,titles));
			//double the header height
			//here please
			
	 }
	 void saveTable() {
		 if(saveStatus) {
			 System.out.println("already saved");
			 return;
		 }
		 BufferedWriter bw;
		 try {
		 bw = new BufferedWriter(new FileWriter(new File(FileSystemView.getFileSystemView().getDefaultDirectory().getPath()+"/invoiceData.csv")));
		 bw.write("");
		 for(int i = 0; i<jt.getRowCount();i++) {
			 String s = "";
			 for(int j = 0; j<5;j++) {
				 s+=jt.getValueAt(i, j);
				 if(j!=4) s+=",";
			 }
			 bw.append(s);
			 bw.newLine();
		 }
		 bw.close();
		 System.out.println("saved successfully");
		 saveStatus=true;
		 frame.setTitle("InVoicer");
		 } catch (IOException e) {
			 	JOptionPane.showMessageDialog(null, "Error occurred while saving.");
				e.printStackTrace();
			}
	 }
	 void updateList() {
		 //updates invoiceList with the current table data
		 invoiceList.clear();
		 for(int i = 0; i<jt.getRowCount();i++) {
			 String s = "";
			 for(int j = 0; j<5;j++) {
				 s+=jt.getValueAt(i, j);
				 if(j!=4) s+=",";
			 }
			 invoiceList.add(new Invoice(s));
		 }
		 System.out.println(invoiceList.toString());
	 }

}