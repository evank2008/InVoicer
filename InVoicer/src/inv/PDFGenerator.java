package inv;

import java.awt.Font;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.filechooser.FileSystemView;
import javax.swing.text.DateFormatter;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;



public class PDFGenerator {
//top right number is 'invoice number' which can just be year+month of service date
	//below that, invoice genration date
	public static void main(String[] args) {
		Client c = new Client();
		c.name="J. P. Client Co.";
		Contact jim = new Contact("Jim Contact","jim@client.net","Director of Jim");
		Contact clark = new Contact("Clark Contact","clark@client.net","Beer Drinker");
		c.contactList.add(jim);
		c.contactList.add(clark);
		try {
			generatePdf(c,"Stabbing",500,20.5,LocalDate.EPOCH,LocalDate.now());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static boolean generatePdf(Client client, String service, double amount, double hourly, LocalDate serviceDate, LocalDate billDate) throws IOException{
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
		String invoiceNum = LocalDate.now().getYear()+"-"+LocalDate.now().getMonthValue(); //2025-12
		String path = FileSystemView.getFileSystemView().getDefaultDirectory().getPath()+ "/invoice.pdf";
		
			PdfWriter writer = new PdfWriter(path);
			PdfDocument pDoc = new PdfDocument(writer);
			Document doc = new Document(pDoc);
			
			PdfFont roman = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN, PdfEncodings.MACROMAN);
			
			pDoc.setDefaultPageSize(PageSize.LETTER);
			
			Table table = new Table(new float[] {300,250});//header
			table.addCell(new Cell().add("Jason Keri MD").setBold().setFontSize(24).setBorder(Border.NO_BORDER));
			table.addCell(new Cell().setBorder(Border.NO_BORDER));
			table.addCell(new Cell().add("Geriatric Psychiatry").setItalic().setBold().setFontSize(24).setBorder(Border.NO_BORDER));
			table.addCell(new Cell().add("INVOICE\n"+invoiceNum).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setFont(roman));
			table.addCell(new Cell().add("8677 Villa La Jolla Dr #205\nLa Jolla, CA 92037\n(619) 299-4374\nJK@SeniorMedicalAssociates.com").setBold().setHeight(80).setBorder(Border.NO_BORDER));
			table.addCell(new Cell().add(billDate.format(DateTimeFormatter.ofPattern("MM/d/uuuu"))).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT).setVerticalAlignment(VerticalAlignment.BOTTOM));
			doc.add(table);
			
			Table contactTable=new Table(new float[] {500,500});
			contactTable.setBorder(new SolidBorder(Color.BLACK,1));
			//contactTable.addCell(new Cell().add("text").setBorder(Border.NO_BORDER));
			contactTable.addCell(new Cell().add("To:").setBorder(Border.NO_BORDER));
			contactTable.addCell(new Cell().add("For: "+client.name).setBorder(Border.NO_BORDER));
			for(Contact con:client.contactList) {
				contactTable.addCell(new Cell().add(con.name+"\n"+con.emailAddress).setBorder(Border.NO_BORDER).setHeight(50));
				contactTable.addCell(new Cell().setBorder(Border.NO_BORDER));
			}
			doc.add(contactTable);
			
			Table bodyTable = new Table(new float[] {200,700,300});
			
			bodyTable.addCell(new Cell().add("").setHeight(20).setBorder(Border.NO_BORDER));
			bodyTable.addCell(new Cell().add("").setHeight(20).setBorder(Border.NO_BORDER));
			bodyTable.addCell(new Cell().add("").setHeight(20).setBorder(Border.NO_BORDER));
			
			bodyTable.addCell(new Cell().add("Date").setTextAlignment(TextAlignment.CENTER).setFont(roman));
			bodyTable.addCell(new Cell().add("Description").setTextAlignment(TextAlignment.CENTER).setFont(roman));
			bodyTable.addCell(new Cell().add("Amount").setTextAlignment(TextAlignment.CENTER).setFont(roman));
			
			bodyTable.addCell(new Cell().add("").setHeight(50));
			bodyTable.addCell(new Cell().add("").setHeight(50));
			bodyTable.addCell(new Cell().add("").setHeight(50));

			bodyTable.addCell(new Cell().add(serviceDate.format(DateTimeFormatter.ofPattern("MMM uuuu"))));
			bodyTable.addCell(new Cell().add(service));
			bodyTable.addCell(new Cell().add(""));
			
			bodyTable.addCell(new Cell().add("").setHeight(bodyTable.getCell(0, 0).getHeight()));
			bodyTable.addCell(new Cell().add(""));
			bodyTable.addCell(new Cell().add(""));
			
			if(hourly>0) {
			bodyTable.addCell(new Cell().add(""));
			bodyTable.addCell(new Cell().add("Hourly Rate").setBold().setTextAlignment(TextAlignment.RIGHT));
			
			String formatted;
			if (hourly == Math.floor(hourly)) {
			    formatted = String.valueOf((int) hourly);
			} else {
			    formatted = String.format("%.2f", hourly);
			}
			
			bodyTable.addCell(new Cell().add("$"+formatted));
			
			}
			
			bodyTable.addCell(new Cell());
			
			
			bodyTable.addCell(new Cell().add("TOTAL").setTextAlignment(TextAlignment.RIGHT).setFont(roman));
			String formatted;
			if (amount == Math.floor(amount)) {
			    formatted = String.valueOf((int) amount);
			} else {
			    formatted = String.format("%.2f", amount);
			}
			bodyTable.addCell(new Cell().add("$"+formatted));

			doc.add(bodyTable);
			
			doc.close();
		
		return true;
	}
}
