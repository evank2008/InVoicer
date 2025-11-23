package inv;

import javax.swing.*;

public class SelectionScreen extends JTabbedPane {
	//this one is shown on startup
	//should basically be three big buttons that send you to records, client data, or invoice generation
	
	public SelectionScreen() {
		super();
		this.addChangeListener(e->{
			ClientPanel.updateClientData();
		});;
		
	}
}
