package org.messic.starter;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class ProcessMonitorSwing
    extends JFrame
{

    private JPanel contentPaneStarted;

    private JTextField textFieldURL;

    /**
     * Launch the application.
     */
    public static void main( String[] args )
    {
        EventQueue.invokeLater( new Runnable()
        {
            public void run()
            {
                try
                {
                    ProcessMonitorSwing frame = new ProcessMonitorSwing();
                    frame.setVisible( true );
                }
                catch ( Exception e )
                {
                    e.printStackTrace();
                }
            }
        } );
    }

    /**
     * Create the frame.
     * 
     * @throws Exception
     */
    public ProcessMonitorSwing()
        throws Exception
    {
        setTitle( "Messic Monitor" );
        ArrayList<Image> icons = new ArrayList<Image>();
        icons.add( Toolkit.getDefaultToolkit().getImage( ProcessMonitorSwing.class.getResource( "/logotipo-32x32.png" ) ) );
        icons.add( Toolkit.getDefaultToolkit().getImage( ProcessMonitorSwing.class.getResource( "/logotipo-64x64.png" ) ) );
        icons.add( Toolkit.getDefaultToolkit().getImage( ProcessMonitorSwing.class.getResource( "/logotipo-128x128.png" ) ) );
        icons.add( Toolkit.getDefaultToolkit().getImage( ProcessMonitorSwing.class.getResource( "/logotipo-256x256.png" ) ) );
        setIconImages( icons );
        setResizable( false );
        final Properties ml = Util.getMultilanguage();

        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        setBounds( 100, 100, 506, 462 );
        contentPaneStarted = new JPanel();
        contentPaneStarted.setBorder( new EmptyBorder( 5, 5, 5, 5 ) );
        setContentPane( contentPaneStarted );
        contentPaneStarted.setLayout( null );

        JLabel lblMessicMonitor = new JLabel( ml.getProperty( "messic-title" ) );
        lblMessicMonitor.setBounds( 12, 0, 438, 36 );
        lblMessicMonitor.setFont( new Font( "Dialog", Font.BOLD, 30 ) );
        lblMessicMonitor.setHorizontalAlignment( SwingConstants.CENTER );
        contentPaneStarted.add( lblMessicMonitor );

        JLabel lblMessicServiceIs = new JLabel( ml.getProperty( "messic-service-status-title" ) );
        lblMessicServiceIs.setHorizontalAlignment( SwingConstants.RIGHT );
        lblMessicServiceIs.setFont( new Font( "Dialog", Font.BOLD, 14 ) );
        lblMessicServiceIs.setBounds( 12, 48, 252, 17 );
        contentPaneStarted.add( lblMessicServiceIs );

        final boolean running = Util.isMessicRunning();
        final JLabel lblRunning =
            new JLabel( ( running ? ml.getProperty( "messic-service-status-started" )
                            : ml.getProperty( "messic-service-status-stopped" ) ) );
        if ( running )
        {
            lblRunning.setForeground( Color.GREEN );
        }
        else
        {
            lblRunning.setForeground( Color.RED );
        }
        lblRunning.setHorizontalAlignment( SwingConstants.LEFT );
        lblRunning.setFont( new Font( "Dialog", Font.BOLD, 16 ) );
        lblRunning.setBounds( 276, 48, 95, 17 );
        contentPaneStarted.add( lblRunning );

        final JButton btnStop =
            new JButton(
                         ( running ? ml.getProperty( "messic-service-stop" ) : ml.getProperty( "messic-service-start" ) ) );

        btnStop.setBounds( 379, 44, 95, 25 );
        contentPaneStarted.add( btnStop );
        // }

        JButton button = new JButton( ml.getProperty( "messic-password-reset" ) );
        button.setForeground( Color.WHITE );
        button.setBackground( Color.RED );
        button.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                ProcessMonitorSwing.this.setCursor( Cursor.getPredefinedCursor( Cursor.WAIT_CURSOR ) );

                Util.resetAdminPassword( ml );
                ProcessMonitorSwing.this.setCursor( Cursor.getDefaultCursor() );
            }
        } );
        button.setBounds( 82, 318, 355, 25 );
        contentPaneStarted.add( button );

        JLabel label = new JLabel( ml.getProperty( "messic-password-reset-explanation" ) );
        label.setForeground( Color.DARK_GRAY );
        label.setHorizontalAlignment( SwingConstants.CENTER );
        label.setBounds( 12, 351, 480, 70 );
        contentPaneStarted.add( label );

        final JPanel panelstarted = new JPanel();
        panelstarted.setBounds( 12, 81, 480, 225 );
        contentPaneStarted.add( panelstarted );
        panelstarted.setLayout( null );

        JLabel label_1 = new JLabel( ml.getProperty( "messic-service-explanation" ) );
        label_1.setBounds( 12, 85, 456, 139 );
        label_1.setHorizontalAlignment( SwingConstants.CENTER );
        panelstarted.add( label_1 );

        JButton button_1 = new JButton( ml.getProperty( "messic-service-opennavigator" ) );
        button_1.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                try
                {
                    Desktop.getDesktop().browse( new URI( textFieldURL.getText() ) );
                }
                catch ( IOException e1 )
                {
                    e1.printStackTrace();
                }
                catch ( URISyntaxException e1 )
                {
                    e1.printStackTrace();
                }
            }
        } );
        button_1.setBounds( 44, 60, 329, 25 );
        panelstarted.add( button_1 );

        textFieldURL = new JTextField();
        textFieldURL.setBounds( 44, 29, 329, 27 );
        textFieldURL.setText( Util.getMessicInternalURL() );
        textFieldURL.setInheritsPopupMenu( true );
        textFieldURL.setEditable( false );
        textFieldURL.setColumns( 10 );
        textFieldURL.setBackground( Color.WHITE );
        panelstarted.add( textFieldURL );

        JButton button_2 = new JButton( ml.getProperty( "messic-url-copy" ) );
        button_2.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
                clpbrd.setContents( new StringSelection( textFieldURL.getText() ), null );
            }
        } );
        button_2.setBounds( 385, 26, 80, 25 );
        panelstarted.add( button_2 );

        JLabel label_2 = new JLabel( ml.getProperty( "messic-service-url-title" ) );
        label_2.setBounds( 12, 0, 408, 38 );
        label_2.setHorizontalAlignment( SwingConstants.CENTER );
        panelstarted.add( label_2 );

        if ( running )
        {
            panelstarted.setVisible( true );
        }
        else
        {
            panelstarted.setVisible( false );
        }

        btnStop.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                if ( btnStop.getText().equals( ml.getProperty( "messic-service-stop" ) ) )
                {
                    try
                    {
                        Util.stopMessicService();
                        btnStop.setText( ml.getProperty( "messic-service-start" ) );
                        try
                        {
                            textFieldURL.setText( Util.getMessicInternalURL() );
                        }
                        catch ( Exception e1 )
                        {
                            e1.printStackTrace();
                        }

                        lblRunning.setText( ml.getProperty( "messic-service-status-stopped" ) );
                        lblRunning.setForeground( Color.RED );

                        panelstarted.setVisible( false );

                    }
                    catch ( IOException e1 )
                    {
                        e1.printStackTrace();
                    }
                }
                else
                {
                    try
                    {
                        ProcessMonitorSwing.this.setCursor( Cursor.getPredefinedCursor( Cursor.WAIT_CURSOR ) );

                        Util.launchMessicService( new MessicLaunchedObserver()
                        {
                            @Override
                            public void messicLaunched()
                            {
                                btnStop.setText( ml.getProperty( "messic-service-stop" ) );
                                lblRunning.setText( ml.getProperty( "messic-service-status-started" ) );
                                lblRunning.setForeground( Color.GREEN );
                                try
                                {
                                    textFieldURL.setText( Util.getMessicInternalURL() );
                                }
                                catch ( Exception e1 )
                                {
                                    e1.printStackTrace();
                                }
                                panelstarted.setVisible( true );
                                ProcessMonitorSwing.this.setCursor( Cursor.getDefaultCursor() );
                            }
                        } );

                    }
                    catch ( IOException e1 )
                    {
                        e1.printStackTrace();
                    }
                    catch ( InterruptedException e1 )
                    {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
            }
        } );
    }
}
