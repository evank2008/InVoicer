import java.time.LocalDateTime;

public class Invoice {
//This class controls the object of each individual invoice created
//store data about date history, price, client, paid/unpaid, etc
	LocalDateTime dateCreated;
	LocalDateTime datePaid;
	String client;
	double amount;
	boolean paidStatus;
	
}
