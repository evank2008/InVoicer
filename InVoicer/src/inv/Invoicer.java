package inv;

import javax.swing.*;
//main class for running and holding the central frame

public class Invoicer {
	JFrame frame;
	public static final int WIDTH = 600, HEIGHT = 600;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Invoicer();
	}

	public Invoicer() {

		frame = new JFrame("Invoicer");
		frame.setSize(WIDTH, HEIGHT);
		frame.setVisible(true);

	}

}
