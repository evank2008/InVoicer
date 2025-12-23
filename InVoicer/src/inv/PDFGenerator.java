package inv;

import java.time.LocalDate;

import javax.swing.filechooser.FileSystemView;



public class PDFGenerator {
//top right number is 'invoice number' which can just be year+month of service date
	//below that, invoice genration date
	public static void main(String[] args) {
		Client c = new Client();
		c.name="John M. Client";
		Contact jim = new Contact("Jim Contact","jim@client.net","Director of Jim");
		Contact clark = new Contact("Clark Contact","clark@client.net","Beer Drinker");
		c.contactList.add(jim);
		c.contactList.add(clark);
		generatePdf(c,"Stabbing",500,20,LocalDate.EPOCH,LocalDate.now());
	}
	public static boolean generatePdf(Client client, String service, double amount, double hourly, LocalDate serviceDate, LocalDate billDate){
		//TODO figure this one out
		//ingredients that are variable:
		/*
		 * top right invoice number(current month)
		 * below that invoice generation date(current date)
		 * then the contacts list
		 * to the right of that is the client name
		 * in the table, you have the service date 
		 * and service
		 * hourly rate(optional)
		 * total amount
		 * 
		 */
		String path = FileSystemView.getFileSystemView().getDefaultDirectory().getPath()+ "/invoice.pdf";
		
		return true;
	}
}
