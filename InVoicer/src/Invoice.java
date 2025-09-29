import java.time.LocalDateTime;

public class Invoice {
//This class controls the object of each individual invoice created
//store data about date history, price, client, paid/unpaid, etc
	LocalDateTime dateCreated;
	LocalDateTime datePaid;
	LocalDateTime lastUpdated;
	String client;
	double amount;
	boolean paidStatus;
	public Invoice() {
		dateCreated=LocalDateTime.now();
		paidStatus=false;
	}
	void pay() {
		paidStatus=true;
		datePaid=LocalDateTime.now();
		update();
	}
	void update() {
		lastUpdated=LocalDateTime.now();
	}
}
