package legacy;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;
import javax.swing.table.*;

public class ArrowCellRenderer extends JPanel implements TableCellRenderer, MouseListener {
	private JLabel label;
	private JButton button;

	public ArrowCellRenderer() {
		setLayout(new BorderLayout());
		label = new JLabel();
		button = new JButton("▼");
		button.setPreferredSize(new Dimension(20, 20));
		// button.setMargin(new Insets(0, 1, 0, 1));
		button.setFocusable(true);
		button.setContentAreaFilled(false);
		button.addActionListener(e -> {
			System.out.println("bazongal");
		});

		add(label, BorderLayout.CENTER);
		add(button, BorderLayout.EAST);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		label.setText(value == null ? "" : value.toString());
		Color col;
		if (isSelected) {
			if (InVoicer.jt.getValueAt(row, 4).equals("Unpaid")) {
				col = new Color(115, 166, 220); // light blue thing

			} else {
				col = Color.gray;
			}
			setBackground(col);
			label.setForeground(table.getSelectionForeground());
		} else {

			if (InVoicer.jt.getValueAt(row, 4).equals("Unpaid")) {
				col = new Color(255, 253, 208); // creamy
			} else {
				col = Color.LIGHT_GRAY;
			}

		}
		setBackground(col);
		label.setForeground(table.getForeground());

		return this;
	}

	@Override
	public void mouseClicked(MouseEvent e) {

		// current issue: not properly detectign when button is pressed.
		// only works if you click the right side of the first date cell, not on the
		// button

		System.out.println("click");
		// TODO Auto-generated method stub
		Point p = e.getPoint();
		if (button.getBounds().contains(p)) {
			System.out.println("button pressed?");
			button.doClick();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
