package inv;

import java.time.LocalDate;

public class PDFGenerator {
//top right number is 'invoice number' which can just be year+month of service date
	//below that, invoice genration date
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
		return true;
	}
}
