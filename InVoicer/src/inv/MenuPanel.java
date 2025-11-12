package inv;

import javax.swing.*;

//this class is a parent of records, clients, and creator
public abstract class MenuPanel extends JPanel {
//TODO: add a simon button in the top right?
	public MenuPanel() {
		super();
		add(new JLabel("Menu Panel"));
	}

}
