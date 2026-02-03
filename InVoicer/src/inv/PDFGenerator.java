package inv;

import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
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
	
	public static boolean generatePdf(Client client, String service, double amount, double hourly, LocalDate serviceDate, LocalDate billDate) throws IOException, Exception{
		
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
		String lineBreakCode = "~";
		String invoiceNum = LocalDate.now().getYear()+"-"+LocalDate.now().getMonthValue(); //2025-12
		String path = FileSystemView.getFileSystemView().getDefaultDirectory().getPath()+ "/InVoicer/"+client.name+" "+invoiceNum;
		int i = 1;
		if(new File(path+".pdf").exists()) {
			while(new File(path+"("+i+").pdf").exists()) {
				i++;
			}
			path+="("+i+").pdf";
		} else {
		path=path+".pdf";
		}
		
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
			
			ArrayList<Contact> contactsNoBlanks = new ArrayList<Contact>(client.contactList);
			contactsNoBlanks.removeIf((c)->{
				return c.name.isEmpty();
			});
			
			contactTable.addCell(new Cell().add("For: "+client.name).setBorder(Border.NO_BORDER));
			for(Contact con:contactsNoBlanks) {
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
			
			String serviceWithBreaks = service.replaceAll(lineBreakCode, "\n");
			bodyTable.addCell(new Cell().add(serviceWithBreaks));
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
			MailMaker.newMail(new File(path),client.contactList);
		return true;
	}
}
class MailMaker {
	 static String subject = "INVOICE";
	 static String body = "Hello";
	public static void newMail(File file,List<Contact> cList) throws Exception {
		if(!Invoicer.onMac) {
		JOptionPane.showMessageDialog(null, "this would be an email with your pdf: "+file.getName()+" if you were on mac");
	} else {
		ArrayList<Contact> contactsNoBlanks = new ArrayList<Contact>(cList);
		contactsNoBlanks.removeIf((c)->{
			return c.name.isEmpty();
		});
		//make the body
		String names=contactsNoBlanks.get(0).name.split(" ")[0];
		if(contactsNoBlanks.size()==2) names+=" and "+contactsNoBlanks.get(1).name.split(" ")[0];

		if(contactsNoBlanks.size()>2) {
		for(int i = 1;i<contactsNoBlanks.size()-1;i++) {
			names+=", "+contactsNoBlanks.get(i).name.split(" ")[0];
		}
		names+=", and "+contactsNoBlanks.get(contactsNoBlanks.size()-1).name.split(" ")[0];
		}
		body = "Hi "+names+",\n\n" +
		"Attached is my latest invoice. \n \n"
		+ "Thank you,\n\n"
		+ "Jason Keri, M.D. \n"
		+ "Diplomate, American Board of Psychiatry & Neurology, With Added Qualification in Geriatric Psychiatry\n"
		+ "Associate Clinical Professor, UCSD Health Sciences\n"
		+ "Co-Founder, Senior Medical Associates, Inc.";
		System.out.println(body);
		//do the mail		
	        StringBuilder script = new StringBuilder();
	        
	        script.append("tell application \"Mail\"\n")
	              .append("activate\n")
	              .append("set newMessage to make new outgoing message with properties ")
	              .append("{subject:\""+subject+"\", content:\""+body+"\" & return & return, visible:true}\n")
	              .append("tell newMessage\n");

	        for (Contact c : cList) {
	        	
	            script.append("make new to recipient at end of to recipients ")
	                  .append("with properties {address:\"")
	                  .append(c.emailAddress)
	                  .append("\"}\n");
	        	

	        }

	        script.append("make new attachment with properties {file name:POSIX file \"")
	              .append(file.getAbsolutePath())
	              .append("\"} at after the last paragraph\n")
	              .append("end tell\n")
	              .append("end tell");

	        try {
				new ProcessBuilder("osascript", "-e", script.toString()).start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	        
	}
	}
}
