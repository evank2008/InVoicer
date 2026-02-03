package inv;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JButton;

public class SettingsPanel extends MenuPanel{

	JButton nameFieldsButton;
	public SettingsPanel() {
		super();
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		add(CreatorPanel.bufferPanel());
		nameFieldsButton = new JButton("Calibrate Client Name Fields");
		if(Invoicer.onMac) {
			nameFieldsButton.setForeground(Color.black);
		} else {
			nameFieldsButton.setForeground(Color.white);
		}
		
		nameFieldsButton.setBackground(new Color(40,160,230));
		nameFieldsButton.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,Invoicer.HEIGHT/20));
		nameFieldsButton.addActionListener(e->{
			Invoicer.ss.setSelectedIndex(0);
			Invoicer.clp.calibrateNameFields();
		});
		nameFieldsButton.setPreferredSize(new Dimension(Invoicer.WIDTH/5,Invoicer.HEIGHT/30));
		nameFieldsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(nameFieldsButton);
		
	}
}
