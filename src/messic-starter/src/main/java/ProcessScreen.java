
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;

public class ProcessScreen extends JFrame {
	
	public ProcessScreen() {
		JLabel background=new JLabel();
		add(background, BorderLayout.CENTER);
		background.setBorder(new LineBorder(new Color(0x888888), 3));

		
		// frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setMinimumSize(new Dimension(360, 220));
		setSize(400, 200);
		setBackground(new Color(0x444444));
		// frame.pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
}
