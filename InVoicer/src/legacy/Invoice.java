package legacy;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Invoice {
//This class controls the object of each individual invoice created
//store data about date history, price, client, paid/unpaid, etc
	static ArrayList<String> clientList = new ArrayList<String>();
	LocalDate dateCreated;
	LocalDate datePaid;
	String client;
	String serviceRendered;
	double amount;
	Boolean paidStatus = null;

	public Invoice() {
		dateCreated = LocalDate.now();
		paidStatus = false;
	}

	public Invoice(double amt, String clnt) {
		this();
		amount = amt;
		client = clnt;
	}

	public Invoice(double amt, String clnt, String service) {
		this(amt, clnt);
		serviceRendered = service;
	}

	public Invoice(String str) {
		String[] strs = str.split(",");
		// data in order that the vars are declared at the top
		// made, service, client, amount, status
		// uuuu-mm-dd
		dateCreated = dateFromStr(strs[0]);
		serviceRendered = strs[1].equals("null") ? "" : strs[1];
		client = strs[2];
		amount = Double.parseDouble(strs[3]);
		if (strs[4].equals("Unpaid"))
			paidStatus = false;
		else {
			datePaid = dateFromStr(strs[4]);
			paidStatus = true;
		}
	}

	static LocalDate dateFromStr(String str) {
		String[] spl = str.split("-");
		LocalDate date = LocalDate.of(Integer.parseInt(spl[0]), Integer.parseInt(spl[1]), Integer.parseInt(spl[2]));
		return date;
	}

	void pay() {
		paidStatus = true;
		datePaid = LocalDate.now();
	}

	// returns an array of this Invoice's initial date, service, client, amount,
	// payment status, and last time updated.
	String[] toArray() {
		return new String[] { dateCreated.format(DateTimeFormatter.ISO_DATE), serviceRendered, client, "" + amount,
				paidStatus ? datePaid.format(DateTimeFormatter.ISO_DATE) : "Unpaid", };
	}
}
