

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

public class BasicDraggableWindow extends JDialog {
	private JPanel contentPane;
	private JPanel main;
	private JPanel closePanel;
	private Point initialClick;
	private ImageIcon closeNormal;
	private ImageIcon closeHover;
	private ImageIcon closePress;
	private JLabel closeLabel;

	
	private void initialize() {
		// don't show a frame or title bar
		setUndecorated(true);

		// Create JPanel and set it as the content pane
		contentPane = new JPanel();
		setContentPane(contentPane);

		// If main has not already been created, create it.
		// Explained later
		if (main == null) {
			main = new JPanel();
		}

		// Create panel for close button
		closePanel = new JPanel(new BorderLayout());
		closePanel.setBackground( new Color(0x8888FF) );

		// Create point to catch initial mouse click coordinates
		initialClick = new Point();
	}

	public JPanel getContentPane() {
		return main;
	}

	public Component add(Component comp) {
		return main.add(comp);
	}

	public void setLayout(LayoutManager manager) {
		if (main == null) {
			main = new JPanel();
			main.setLayout(new FlowLayout());
		} else {
			main.setLayout(manager);
		}

		if (!(getLayout() instanceof BorderLayout)) {
			super.setRootPaneCheckingEnabled(false);
			super.setLayout(new BorderLayout());
			super.setRootPane(super.getRootPane());
			super.setRootPaneCheckingEnabled(true);
		}
	}

	private void showWindow() {
		// If not set, default to FlowLayout
		if (main.getLayout() == null) {
			setLayout(new FlowLayout());
		}

		// close "button" - show this image by default
		closeNormal = new ImageIcon(getClass().getResource("close.png"));

		// close "button" - show this when the mouse enter is detected
		closeHover = new ImageIcon(getClass().getResource("close-hover.png"));

		// close "button" - show this image when mouse press is detected
		closePress = new ImageIcon(getClass().getResource("close-hover.png"));
		closeLabel = new JLabel(closeNormal);

		// Put the label with the image on the far right
		closePanel.add(closeLabel, BorderLayout.EAST);

		// Add the two panels to the content pane
		contentPane.setLayout(new BorderLayout());
		contentPane.add(closePanel, BorderLayout.NORTH);
		contentPane.add(main, BorderLayout.CENTER);

		// set raised beveled border for window
		contentPane.setBorder(BorderFactory.createLineBorder(new Color(0x8888FF),2));

		// Set position somewhere near the middle of the screen
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(screenSize.width / 2 - (getWidth() / 2), screenSize.height
				/ 2 - (getHeight() / 2));
		
		installListeners();
	}

	private void installListeners() {
		// Get point of initial mouse click
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				initialClick = e.getPoint();
				getComponentAt(initialClick);
			}
		});

		// Move window when mouse is dragged
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				// get location of Window
				int thisX = getLocation().x;
				int thisY = getLocation().y;

				// Determine how much the mouse moved since the initial click
				int xMoved = (thisX + e.getX()) - (thisX + initialClick.x);
				int yMoved = (thisY + e.getY()) - (thisY + initialClick.y);

				// Move window to this position
				int X = thisX + xMoved;
				int Y = thisY + yMoved;
				setLocation(X, Y);
			}
		});

		// Close "button" (image) listeners
		closeLabel.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				closeLabel.setIcon(closePress);
			}

			public void mouseReleased(MouseEvent e) {
				closeLabel.setIcon(closeNormal);
			}

			public void mouseEntered(MouseEvent e) {
				closeLabel.setIcon(closeHover);
			}

			public void mouseExited(MouseEvent e) {
				closeLabel.setIcon(closeNormal);
			}

			public void mouseClicked(MouseEvent e) {
				close();
			}
		});
	}

	// close and dispose
	public void close() {
		setVisible(false);
		dispose();
	}
	
	public static void main( String[] args )
    {
        BasicDraggableWindow bdw=new BasicDraggableWindow();
        bdw.initialize();
        bdw.showWindow();
        bdw.setSize( 600, 400 );
        
        JLabel label = new JLabel();

        label.setHorizontalAlignment( SwingConstants.CENTER );

        
        ImageIcon ii = new ImageIcon(Main.class.getResource( "/tumblr_n24lc8Wmhg1qk57pio1_500.gif" ) );
        label.setIcon( ii );
        bdw.add( label, BorderLayout.CENTER );
        label.setBorder( new LineBorder( new Color( 0x888888 ), 3 ) );

        bdw.setVisible( true );

    }
}
