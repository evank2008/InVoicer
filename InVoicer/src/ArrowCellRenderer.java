import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

public class ArrowCellRenderer extends JPanel implements TableCellRenderer {
    private JLabel label;
    private JButton button;

    public ArrowCellRenderer() {
        setLayout(new BorderLayout());
        label = new JLabel();
        button = new JButton("▼"); 
        button.setPreferredSize(new Dimension(20,20));
       // button.setMargin(new Insets(0, 1, 0, 1));
       // button.setFocusable(false);
        button.setContentAreaFilled(false);
        
        add(label, BorderLayout.CENTER);
        add(button, BorderLayout.EAST);
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        label.setText(value == null ? "" : value.toString());
        Color col;
        if (isSelected) {
        	if(InVoicer.jt.getValueAt(row, 4).equals("Unpaid")) {
        		col=new Color(115,166,220); //light blue thing
        		
        	} else {
        		col=Color.gray;
        	}
            setBackground(col);
            label.setForeground(table.getSelectionForeground());
        } else {

        	if(InVoicer.jt.getValueAt(row, 4).equals("Unpaid")) {
        		col=new Color(255,253,208); //creamy
        	} else {
        		col=Color.LIGHT_GRAY;
        	}
        
        }
        setBackground(col);
            label.setForeground(table.getForeground());

        return this;
    }

}
