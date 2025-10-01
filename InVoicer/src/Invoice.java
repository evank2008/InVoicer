import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Invoice {
//This class controls the object of each individual invoice created
//store data about date history, price, client, paid/unpaid, etc
	LocalDate dateCreated;
	LocalDate datePaid;
	LocalDate lastUpdated;
	String client;
	String serviceRendered;
	double amount;
	boolean paidStatus;
	public Invoice() {
		dateCreated=LocalDate.now();
		paidStatus=false;
		update();
	}
	public Invoice(double amt, String clnt) {
		this();
		amount=amt;
		client=clnt;
	}
	public Invoice(double amt, String clnt, String service) {
		this(amt,clnt);
		serviceRendered=service;
	}
	void pay() {
		paidStatus=true;
		datePaid=LocalDate.now();
		update();
	}
	void update() {
		lastUpdated=LocalDate.now();
	}
	//returns an array of this Invoice's initial date, service, client, amount, payment status, and last time updated. 
	String[] toArray() {
		return new String[] {
				dateCreated.format(DateTimeFormatter.ISO_DATE),
				serviceRendered,
				client,
				""+amount,
				paidStatus?datePaid.format(DateTimeFormatter.ISO_DATE):"Unpaid",
				lastUpdated.format(DateTimeFormatter.ISO_DATE)
				};
	}
}
