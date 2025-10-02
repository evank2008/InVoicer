import java.awt.BorderLayout;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class InVoicer {

	/*TODO: 
	 * be able to add new invoices(with a button?)
	 * option to select preexisting clients
	 * save data to invoiceData.csv
	 * button to sort the table based on amount, date, client
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
	JTable jt;
	JPanel tPanel;
	public static void main(String[] args) {
		
		new InVoicer();
	}
	public InVoicer() {
		loadListFromFile();
		frame = new JFrame("InVoicer");
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		jt.setRowHeight(32);
		jt.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		frame.setLayout(new BorderLayout());
		
		JPanel jn = new JPanel();
		JButton jb = new JButton("p");
		jb.addActionListener(e->{
			DefaultTableModel tm = (DefaultTableModel) jt.getModel();
			tm.addRow(new String[]{"goosh","boo"});
		});
		jn.add(jb);
		
		frame.add(jn, BorderLayout.NORTH);
		
		JScrollPane sp = new JScrollPane(jt);
		jt.setFillsViewportHeight(true);
		tPanel = new JPanel();
		tPanel.add(sp);
		frame.add(tPanel,BorderLayout.AFTER_LAST_LINE);
		
		frame.pack();
//new JTable(new String[][] {{"pizza","pozza"},{"hod dog","had dog"}},new String[] {"big","if"})
	}
	
	 void loadListFromFile() {
		//temporary test invoice
		//TODO: make this method read out of a file
		 try {
		 String path = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();
		 BufferedReader br = new BufferedReader(new FileReader(new File(path+"/invoiceData.csv")));
		 String line;
		 
		 while((line = br.readLine())!=null) {
			invoiceList.add(new Invoice(line));
		 }
		 } catch(FileNotFoundException e) {
			 //oh well haha i guess theres no data
		 }
		 catch(Exception e) {
			 e.printStackTrace();
		 }
		Invoice in = new Invoice(600,"John Cliente");
		Invoice j = new Invoice(4567.88,"Sammy Rich");
		invoiceList.add(in);
		invoiceList.add(j);
		updateTable();
	}
	 void updateTable() {
		 String[][] array = new String[InVoicer.invoiceList.size()][6];
			for(int i = 0; i<InVoicer.invoiceList.size();i++) {
				array[i]=InVoicer.invoiceList.get(i).toArray();
			}
			//array is now 2d array of unboxed data of every invoice
			//initial date, service, client, amount, payment status, and last time updated
			String[] titles = {"Date Created","Service","Client","Amount","Payment Status","Updated"};
			jt = new JTable(new DefaultTableModel(array,titles));
			
	 }

}