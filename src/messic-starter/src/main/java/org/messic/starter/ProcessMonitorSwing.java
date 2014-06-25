package org.messic.starter;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;

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

    private JPanel contentPane;

    private JTextField txtHttpmessic;

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
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        setBounds( 100, 100, 488, 338 );
        contentPane = new JPanel();
        contentPane.setBorder( new EmptyBorder( 5, 5, 5, 5 ) );
        setContentPane( contentPane );
        contentPane.setLayout( null );

        JLabel lblMessicMonitor = new JLabel( "Messic Monitor" );
        lblMessicMonitor.setBounds( 12, 0, 438, 36 );
        lblMessicMonitor.setFont( new Font( "Dialog", Font.BOLD, 30 ) );
        lblMessicMonitor.setHorizontalAlignment( SwingConstants.CENTER );
        contentPane.add( lblMessicMonitor );

        JLabel lblMessicServiceIs = new JLabel( "Messic Service is currently:" );
        lblMessicServiceIs.setHorizontalAlignment( SwingConstants.RIGHT );
        lblMessicServiceIs.setFont( new Font( "Dialog", Font.BOLD, 14 ) );
        lblMessicServiceIs.setBounds( 12, 48, 252, 17 );
        contentPane.add( lblMessicServiceIs );

        boolean running = Util.isMessicRunning();
        JLabel lblRunning = new JLabel( ( running ? "STARTED" : "STOPPED" ) );
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
        contentPane.add( lblRunning );

        final JButton btnStop = new JButton( ( running ? "STOP" : "START" ) );
        btnStop.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                if ( btnStop.getText().equals( "STOP" ) )
                {
                    try
                    {
                        Util.stopMessicService();
                        btnStop.setText( "START" );
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
                        Util.launchMessicService(null);
                        btnStop.setText( "STOP" );
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
        btnStop.setBounds( 379, 44, 95, 25 );
        contentPane.add( btnStop );

        if ( running )
        {
            JLabel lblThisIsThe = new JLabel( "This is the URL to open the Messic User Interface" );
            lblThisIsThe.setHorizontalAlignment( SwingConstants.CENTER );
            lblThisIsThe.setBounds( 12, 92, 424, 15 );
            contentPane.add( lblThisIsThe );

            txtHttpmessic = new JTextField();
            txtHttpmessic.setInheritsPopupMenu( true );
            txtHttpmessic.setBackground( Color.WHITE );
            txtHttpmessic.setEditable( false );
            txtHttpmessic.setText( Util.getMessicInternalURL() );
            txtHttpmessic.setBounds( 22, 119, 374, 19 );
            contentPane.add( txtHttpmessic );
            txtHttpmessic.setColumns( 10 );

            JButton btnOpenUrlWith = new JButton( "Open URL with Default Navigator" );
            btnOpenUrlWith.addActionListener( new ActionListener()
            {
                public void actionPerformed( ActionEvent e )
                {
                    // open navigator
                    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
                    if ( desktop != null && desktop.isSupported( Desktop.Action.BROWSE ) )
                    {
                        try
                        {
                            desktop.browse( new URI( txtHttpmessic.getText() ) );
                        }
                        catch ( Exception ex )
                        {
                            ex.printStackTrace();
                        }
                    }
                }
            } );
            btnOpenUrlWith.setBounds( 105, 150, 302, 25 );
            contentPane.add( btnOpenUrlWith );

            JLabel lblRememberThatMessic =
                new JLabel(
                            "<html>Remember that messic interface can be showed in any navigator of your network (yeah! included other pcs, tablets, ...) <br><br> The only thing you need is to open a navigator (preferible a modern one like Firefox) and put above URL.</html>" );
            lblRememberThatMessic.setHorizontalAlignment( SwingConstants.CENTER );
            lblRememberThatMessic.setBounds( 12, 182, 462, 120 );
            contentPane.add( lblRememberThatMessic );

            JButton btnCopy = new JButton( "copy" );
            btnCopy.addActionListener( new ActionListener()
            {
                public void actionPerformed( ActionEvent e )
                {
                    txtHttpmessic.selectAll();
                    txtHttpmessic.copy();
                }
            } );
            btnCopy.setBounds( 403, 116, 71, 25 );
            contentPane.add( btnCopy );
        }
    }
}
