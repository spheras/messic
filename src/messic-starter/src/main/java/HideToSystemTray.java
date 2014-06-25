import java.awt.*;
import java.awt.event.*;
import javax.swing.JFrame;
import javax.swing.UIManager;

/**
 * @author Mohammad Faisal ermohammadfaisal.blogspot.com facebook.com/m.faisal6621
 */
public class HideToSystemTray
    extends JFrame
{
    TrayIcon trayIcon;

    SystemTray tray;

    HideToSystemTray()
    {
        super( "SystemTray test" );
        System.out.println( "creating instance" );
        try
        {
            System.out.println( "setting look and feel" );
            UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
        }
        catch ( Exception e )
        {
            System.out.println( "Unable to set LookAndFeel" );
        }
        if ( SystemTray.isSupported() )
        {
            System.out.println( "system tray supported" );
            tray = SystemTray.getSystemTray();

            Image image = Toolkit.getDefaultToolkit().getImage( "/media/faisal/DukeImg/Duke256.png" );
            ActionListener exitListener = new ActionListener()
            {
                public void actionPerformed( ActionEvent e )
                {
                    System.out.println( "Exiting...." );
                    System.exit( 0 );
                }
            };
            PopupMenu popup = new PopupMenu();
            MenuItem defaultItem = new MenuItem( "Exit" );
            defaultItem.addActionListener( exitListener );
            popup.add( defaultItem );
            defaultItem = new MenuItem( "Open" );
            defaultItem.addActionListener( new ActionListener()
            {
                public void actionPerformed( ActionEvent e )
                {
                    setVisible( true );
                    setExtendedState( JFrame.NORMAL );
                }
            } );
            popup.add( defaultItem );
            trayIcon = new TrayIcon( image, "SystemTray Demo", popup );
            trayIcon.setImageAutoSize( true );
        }
        else
        {
            System.out.println( "system tray not supported" );
        }
        addWindowStateListener( new WindowStateListener()
        {
            public void windowStateChanged( WindowEvent e )
            {
                if ( e.getNewState() == ICONIFIED )
                {
                    try
                    {
                        tray.add( trayIcon );
                        setVisible( false );
                        System.out.println( "added to SystemTray" );
                    }
                    catch ( AWTException ex )
                    {
                        System.out.println( "unable to add to tray" );
                    }
                }
                if ( e.getNewState() == 7 )
                {
                    try
                    {
                        tray.add( trayIcon );
                        setVisible( false );
                        System.out.println( "added to SystemTray" );
                    }
                    catch ( AWTException ex )
                    {
                        System.out.println( "unable to add to system tray" );
                    }
                }
                if ( e.getNewState() == MAXIMIZED_BOTH )
                {
                    tray.remove( trayIcon );
                    setVisible( true );
                    System.out.println( "Tray icon removed" );
                }
                if ( e.getNewState() == NORMAL )
                {
                    tray.remove( trayIcon );
                    setVisible( true );
                    System.out.println( "Tray icon removed" );
                }
            }
        } );
        setIconImage( Toolkit.getDefaultToolkit().getImage( "Duke256.png" ) );

        setVisible( true );
        setSize( 300, 200 );
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
    }

    public static void main( String[] args )
    {
        new HideToSystemTray();
    }
}