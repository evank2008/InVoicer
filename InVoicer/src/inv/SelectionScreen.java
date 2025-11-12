package inv;

import javax.swing.*;

public class SelectionScreen extends JTabbedPane {
	//this one is shown on startup
	//should basically be three big buttons that send you to records, client data, or invoice generation
	JLabel label = new JLabel("select");
	
	public SelectionScreen() {
		super();
		//add(label);
		
	}
}
