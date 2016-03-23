package org.messic.starter;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.messic.configuration.MessicConfig;
import org.messic.starter.Util.MusicFolderState;

public class ProcessMonitorWindow
    extends JFrame
{

    /**
     * 
     */
    private static final long serialVersionUID = -3867858734439306220L;

    private JPanel contentPanelBackground;

    private JTextField textFieldURL;

    private JLabel closeLabel;

    private JCheckBox checkProxy;

    private JCheckBox chckbxSecureCommunications;

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
                    ProcessMonitorWindow frame = new ProcessMonitorWindow();
                    frame.setVisible( true );
                }
                catch ( Exception e )
                {
                    e.printStackTrace();
                }
            }
        } );
    }

    private ImageIcon closeNormal;

    private ImageIcon closeHover;

    /**
     * method to flag all the fields and let know to the user that something was modified and must be saved or discard
     */
    private void configurationModified()
    {
        flagModified = true;
        checkProxy.setBackground( modifiedColor );
        tfProxyPort.setBackground( modifiedColor );
        tfProxyURL.setBackground( modifiedColor );
        chckbxSecureCommunications.setBackground( modifiedColor );
        tfHTTPPort.setBackground( modifiedColor );
        tfHTTPSPort.setBackground( modifiedColor );
        tfSessionTimeout.setBackground( modifiedColor );
        tfConfigMusicFolder.setBackground( modifiedColor );
    }

    /**
     * Create the frame.
     * 
     * @throws Exception
     */
    public ProcessMonitorWindow()
        throws Exception
    {
        setBackground( Color.RED );

        // listeners to flag the modification done
        ActionListener actionModifyListener = new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                configurationModified();
            }
        };
        KeyListener keyModifyListener = new KeyListener()
        {

            @Override
            public void keyTyped( KeyEvent e )
            {
                configurationModified();
            }

            @Override
            public void keyReleased( KeyEvent e )
            {
            }

            @Override
            public void keyPressed( KeyEvent e )
            {
            }
        };

        setTitle( "Messic Monitor" );
        ArrayList<Image> icons = new ArrayList<Image>();
        icons.add( Toolkit.getDefaultToolkit().getImage( ProcessMonitorWindow.class.getResource( "/logotipo-32x32.png" ) ) );
        icons.add( Toolkit.getDefaultToolkit().getImage( ProcessMonitorWindow.class.getResource( "/logotipo-64x64.png" ) ) );
        icons.add( Toolkit.getDefaultToolkit().getImage( ProcessMonitorWindow.class.getResource( "/logotipo-128x128.png" ) ) );
        icons.add( Toolkit.getDefaultToolkit().getImage( ProcessMonitorWindow.class.getResource( "/logotipo-256x256.png" ) ) );
        setIconImages( icons );
        setResizable( false );
        final Properties ml = Util.getMultilanguage();

        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        setUndecorated( true );
        setBounds( 100, 100, 800, 582 );
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation( dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2 );

        contentPanelBackground = new JPanelWithBackground( ProcessMonitorWindow.class.getResource( "/window.png" ) );
        contentPanelBackground.setBorder( new EmptyBorder( 5, 5, 5, 5 ) );
        setContentPane( contentPanelBackground );
        contentPanelBackground.setOpaque( false );
        contentPanelBackground.setLayout( null );

        // close "button" - show this image by default
        closeNormal = new ImageIcon( getClass().getResource( "/close.png" ) );
        // close "button" - show this when the mouse enter is detected
        closeHover = new ImageIcon( getClass().getResource( "/close-hover.png" ) );

        JLabel lblMessicMonitor = new JLabel( ml.getProperty( "messic-title" ) );
        lblMessicMonitor.setForeground( Color.WHITE );
        lblMessicMonitor.setBounds( 0, 0, 800, 36 );
        lblMessicMonitor.setFont( new Font( "Dialog", Font.BOLD, 30 ) );
        lblMessicMonitor.setHorizontalAlignment( SwingConstants.CENTER );
        contentPanelBackground.add( lblMessicMonitor );

        JLabel lblMessicVersion = new JLabel( "messic v" + MessicConfig.getCurrentVersion().sversion );
        lblMessicVersion.setForeground( Color.WHITE );
        lblMessicVersion.setBounds( 300, 540, 480, 36 );
        lblMessicVersion.setFont( new Font( "Dialog", Font.BOLD, 12 ) );
        lblMessicVersion.setHorizontalAlignment( SwingConstants.RIGHT );
        contentPanelBackground.add( lblMessicVersion );

        final JPanel panel_config = new JPanel();
        panel_config.setOpaque( false );
        panel_config.setBounds( 192, 42, 584, 443 );
        panel_config.setVisible( false );

        closeLabel = new JLabel( closeNormal );
        closeLabel.setBounds( 750, 0, 43, 39 );
        closeLabel.setCursor( new Cursor( Cursor.HAND_CURSOR ) );
        contentPanelBackground.add( closeLabel );
        contentPanelBackground.add( panel_config );
        panel_config.setLayout( null );

        tfProxyURL = new JTextField();
        tfProxyURL.setBounds( 57, 53, 196, 19 );
        tfProxyURL.addKeyListener( keyModifyListener );
        panel_config.add( tfProxyURL );
        tfProxyURL.setColumns( 10 );

        checkProxy = new JCheckBox( ml.getProperty( "messic-config-proxy" ) );
        checkProxy.setOpaque( false );
        checkProxy.setBounds( 26, 8, 550, 23 );
        checkProxy.addActionListener( actionModifyListener );
        panel_config.add( checkProxy );

        JLabel lblUrlProxy = new JLabel( ml.getProperty( "messic-config-urlproxy" ) );
        lblUrlProxy.setBounds( 57, 39, 196, 15 );
        panel_config.add( lblUrlProxy );

        JLabel lblPortProxy = new JLabel( ml.getProperty( "messic-config-portproxy" ) );
        lblPortProxy.setBounds( 57, 74, 196, 15 );
        panel_config.add( lblPortProxy );

        tfProxyPort = new JTextField();
        tfProxyPort.setColumns( 10 );
        tfProxyPort.setBounds( 57, 88, 196, 19 );
        tfProxyPort.addKeyListener( keyModifyListener );
        panel_config.add( tfProxyPort );

        chckbxSecureCommunications = new JCheckBox( ml.getProperty( "messic-config-secure" ) );
        chckbxSecureCommunications.setOpaque( false );
        chckbxSecureCommunications.setBounds( 26, 138, 534, 23 );
        chckbxSecureCommunications.addActionListener( actionModifyListener );
        panel_config.add( chckbxSecureCommunications );

        JLabel lblHttpPort = new JLabel( ml.getProperty( "messic-config-httpport" ) );
        lblHttpPort.setBounds( 26, 169, 546, 15 );
        panel_config.add( lblHttpPort );

        tfHTTPPort = new JTextField();
        tfHTTPPort.setColumns( 10 );
        tfHTTPPort.setBounds( 26, 183, 196, 19 );
        tfHTTPPort.addKeyListener( keyModifyListener );
        panel_config.add( tfHTTPPort );

        JLabel lblHttpsPort = new JLabel( ml.getProperty( "messic-config-httpsport" ) );
        lblHttpsPort.setBounds( 26, 214, 196, 15 );
        panel_config.add( lblHttpsPort );

        tfHTTPSPort = new JTextField();
        tfHTTPSPort.setColumns( 10 );
        tfHTTPSPort.setBounds( 26, 228, 196, 19 );
        tfHTTPSPort.addKeyListener( keyModifyListener );
        panel_config.add( tfHTTPSPort );

        JLabel lblMessicTimeout = new JLabel( ml.getProperty( "messic-config-timeout" ) );
        lblMessicTimeout.setVerticalAlignment( SwingConstants.BOTTOM );
        lblMessicTimeout.setBounds( 26, 259, 534, 15 );
        panel_config.add( lblMessicTimeout );

        tfSessionTimeout = new JTextField();
        tfSessionTimeout.setColumns( 10 );
        tfSessionTimeout.setBounds( 26, 273, 196, 19 );
        tfSessionTimeout.addKeyListener( keyModifyListener );
        panel_config.add( tfSessionTimeout );

        JLabel lblMusicFolder = new JLabel( ml.getProperty( "messic-config-musicfolder" ) );
        lblMusicFolder.setVerticalAlignment( SwingConstants.BOTTOM );
        lblMusicFolder.setBounds( 26, 304, 510, 15 );
        panel_config.add( lblMusicFolder );

        tfConfigMusicFolder = new JTextField();
        tfConfigMusicFolder.setColumns( 10 );
        tfConfigMusicFolder.setBounds( 26, 318, 510, 19 );
        tfConfigMusicFolder.addKeyListener( keyModifyListener );
        panel_config.add( tfConfigMusicFolder );

        JButton btnBrowse = new JButton( "..." );
        btnBrowse.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                // Create a file chooser
                final JFileChooser fc = new JFileChooser();
                fc.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
                int returnVal = fc.showOpenDialog( ProcessMonitorWindow.this );

                if ( returnVal == JFileChooser.APPROVE_OPTION )
                {
                    File file = fc.getSelectedFile();

                    MusicFolderState mfs = Util.testIfMusicFolderIsEmpty( file.getAbsolutePath() );
                    if ( mfs.equals( MusicFolderState.EXIST_WITH_DATABASE ) )
                    {
                        int result =
                            JOptionPane.showConfirmDialog( ProcessMonitorWindow.this,
                                                           ml.getProperty( "messic-selectfolder-existingdatabase" ),
                                                           "", JOptionPane.YES_NO_OPTION );
                        if ( result == JOptionPane.NO_OPTION )
                        {
                            return;
                        }
                    }
                    else if ( mfs.equals( MusicFolderState.EXIST_WITHOUT_DATABASE ) )
                    {
                        int result =
                            JOptionPane.showConfirmDialog( ProcessMonitorWindow.this,
                                                           ml.getProperty( "messic-selectfolder-notempty" ), "",
                                                           JOptionPane.YES_NO_OPTION );
                        if ( result == JOptionPane.NO_OPTION )
                        {
                            return;
                        }
                    }

                    tfConfigMusicFolder.setText( file.getAbsolutePath() );
                    configurationModified();
                }
            }
        } );
        btnBrowse.setBounds( 541, 315, 35, 25 );
        panel_config.add( btnBrowse );

        JButton btnSaveChanges = new JButton( ml.getProperty( "messic-config-save" ) );
        btnSaveChanges.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                String folder = tfConfigMusicFolder.getText().trim();
                MessicConfig mc = new MessicConfig();
                Properties p = mc.getConfiguration();
                String storedMusicFolder = p.getProperty( MessicConfig.MESSIC_MUSICFOLDER );
                if ( !storedMusicFolder.equals( folder ) )
                {
                    MusicFolderState mfs = Util.testIfMusicFolderIsEmpty( folder );
                    if ( mfs.equals( MusicFolderState.EXIST_WITH_DATABASE ) )
                    {
                        int result =
                            JOptionPane.showConfirmDialog( ProcessMonitorWindow.this,
                                                           ml.getProperty( "messic-selectfolder-existingdatabase" ),
                                                           "", JOptionPane.YES_NO_OPTION );
                        if ( result == JOptionPane.NO_OPTION )
                        {
                            return;
                        }
                    }
                    else if ( mfs.equals( MusicFolderState.EXIST_WITHOUT_DATABASE ) )
                    {
                        int result =
                            JOptionPane.showConfirmDialog( ProcessMonitorWindow.this,
                                                           ml.getProperty( "messic-selectfolder-notempty" ), "",
                                                           JOptionPane.YES_NO_OPTION );
                        if ( result == JOptionPane.NO_OPTION )
                        {
                            return;
                        }
                    }

                    File ffolder = new File( folder );
                    ffolder.mkdirs();
                    if ( !ffolder.exists() )
                    {
                        JOptionPane.showMessageDialog( ProcessMonitorWindow.this,
                                                       ml.getProperty( "messic-selectfolder-error" ) );
                        return;
                    }
                }

                p.setProperty( MessicConfig.MESSIC_PROXYURL, ( checkProxy.isSelected() ? tfProxyURL.getText() : "" ) );
                p.setProperty( MessicConfig.MESSIC_PROXYPORT, ( checkProxy.isSelected() ? tfProxyPort.getText() : "" ) );
                p.setProperty( MessicConfig.MESSIC_TIMEOUT, tfSessionTimeout.getText() );
                p.setProperty( MessicConfig.MESSIC_SECUREPROTOCOL, "" + chckbxSecureCommunications.isSelected() );
                p.setProperty( MessicConfig.MESSIC_HTTPPORT, tfHTTPPort.getText() );
                p.setProperty( MessicConfig.MESSIC_HTTPSPORT, tfHTTPSPort.getText() );
                p.setProperty( MessicConfig.MESSIC_MUSICFOLDER, tfConfigMusicFolder.getText().trim() );
                try
                {
                    mc.save();
                    fillConfiguration();
                    JOptionPane.showMessageDialog( ProcessMonitorWindow.this, ml.getProperty( "messic-config-saveok" ) );
                }
                catch ( IOException e1 )
                {
                    JOptionPane.showMessageDialog( ProcessMonitorWindow.this,
                                                   ml.getProperty( "messic-config-saveerror" ) + "\n "
                                                       + e1.getMessage() );
                    e1.printStackTrace();
                }
            }
        } );
        btnSaveChanges.setBounds( 12, 406, 200, 25 );
        panel_config.add( btnSaveChanges );

        JButton btnCancelChanges = new JButton( ml.getProperty( "messic-config-cancel" ) );
        btnCancelChanges.setBounds( 372, 406, 200, 25 );
        panel_config.add( btnCancelChanges );
        btnCancelChanges.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                fillConfiguration();
            }

        } );

        final JPanel panel_main = new JPanel();
        panel_main.setBorder( null );
        panel_main.setBounds( 192, 42, 584, 443 );
        contentPanelBackground.add( panel_main );
        panel_main.setOpaque( false );
        panel_main.setLayout( null );

        JLabel lblMessicServiceIs = new JLabel( ml.getProperty( "messic-service-status-title" ) );
        lblMessicServiceIs.setBounds( 12, 12, 345, 17 );
        lblMessicServiceIs.setHorizontalAlignment( SwingConstants.RIGHT );
        lblMessicServiceIs.setFont( new Font( "Dialog", Font.BOLD, 14 ) );
        panel_main.add( lblMessicServiceIs );

        final boolean running = Util.isMessicRunning();
        final JLabel lblRunning =
            new JLabel( ( running ? ml.getProperty( "messic-service-status-started" )
                            : ml.getProperty( "messic-service-status-stopped" ) ) );
        lblRunning.setBounds( 369, 11, 95, 17 );
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
        panel_main.add( lblRunning );

        final JButton btnStop =
            new JButton(
                         ( running ? ml.getProperty( "messic-service-stop" ) : ml.getProperty( "messic-service-start" ) ) );
        btnStop.setBounds( 482, 8, 95, 25 );
        panel_main.add( btnStop );
        // }

        JButton button = new JButton( ml.getProperty( "messic-password-reset" ) );
        button.setBounds( 126, 319, 355, 25 );
        button.setForeground( Color.black );
        button.setBackground( Color.RED );
        button.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                ProcessMonitorWindow.this.setCursor( Cursor.getPredefinedCursor( Cursor.WAIT_CURSOR ) );

                Util.resetAdminPassword( ml );
                ProcessMonitorWindow.this.setCursor( Cursor.getDefaultCursor() );
            }
        } );
        panel_main.add( button );

        JLabel label = new JLabel( ml.getProperty( "messic-password-reset-explanation" ) );
        label.setBounds( 12, 344, 566, 87 );
        label.setForeground( Color.DARK_GRAY );
        label.setHorizontalAlignment( SwingConstants.CENTER );
        panel_main.add( label );

        final JPanel panelstopped = new JPanel();
        panelstopped.setBounds( 11, 41, 566, 263 );
        panel_main.add( panelstopped );
        panelstopped.setLayout( null );

        JLabel label_3 = new JLabel( ml.getProperty( "messic-service-stopped-explanation" ) );
        label_3.setBounds( 12, 12, 542, 200 );
        label_3.setHorizontalAlignment( SwingConstants.CENTER );
        panelstopped.add( label_3 );

        final JPanel panelstarted = new JPanel();
        panelstarted.setBounds( 11, 41, 566, 263 );
        panel_main.add( panelstarted );
        panelstarted.setLayout( null );

        JLabel label_1 = new JLabel( ml.getProperty( "messic-service-explanation" ) );
        label_1.setBounds( 12, 86, 542, 165 );
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
        button_1.setBounds( 12, 60, 450, 25 );
        panelstarted.add( button_1 );

        textFieldURL = new JTextField();
        textFieldURL.setBounds( 12, 29, 450, 27 );
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
        button_2.setBounds( 474, 30, 80, 25 );
        panelstarted.add( button_2 );

        JLabel label_2 = new JLabel( ml.getProperty( "messic-service-url-title" ) );
        label_2.setBounds( 12, 0, 408, 38 );
        label_2.setHorizontalAlignment( SwingConstants.LEFT );
        panelstarted.add( label_2 );

        final JButton btnMain = new JButton( ml.getProperty( "messic-menu-main" ) );
        final JButton btnConfiguration = new JButton( ml.getProperty( "messic-menu-config" ) );
        btnMain.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                if ( flagModified )
                {
                    JOptionPane.showMessageDialog( ProcessMonitorWindow.this, ml.getProperty( "messic-config-pending" ) );
                    return;
                }
                panel_main.setVisible( true );
                panel_config.setVisible( false );
                btnMain.setBackground( new Color( 100, 255, 255 ) );
                btnMain.setForeground( Color.BLACK );
                btnConfiguration.setForeground( Color.BLACK );
                btnConfiguration.setBackground( Color.LIGHT_GRAY );
            }
        } );
        btnMain.setBackground( Color.CYAN );
        btnMain.setForeground( Color.BLACK );
        btnMain.setFont( new Font( "Dialog", Font.BOLD, 16 ) );
        btnMain.setBorder( BorderFactory.createLineBorder( new Color( 180, 180, 180 ), 2, true ) );
        btnMain.setCursor( new Cursor( Cursor.HAND_CURSOR ) );
        btnMain.setBounds( 13, 51, 174, 43 );
        contentPanelBackground.add( btnMain );

        btnConfiguration.setForeground( Color.BLACK );
        btnConfiguration.setBackground( Color.LIGHT_GRAY );
        btnConfiguration.setFont( new Font( "Dialog", Font.BOLD, 16 ) );
        btnConfiguration.setBorder( BorderFactory.createLineBorder( new Color( 180, 180, 180 ), 2, true ) );
        btnConfiguration.setCursor( new Cursor( Cursor.HAND_CURSOR ) );
        btnConfiguration.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                panel_main.setVisible( false );
                panel_config.setVisible( true );
                btnConfiguration.setBackground( new Color( 100, 255, 255 ) );
                btnConfiguration.setForeground( Color.black );
                btnMain.setForeground( Color.black );
                btnMain.setBackground( Color.LIGHT_GRAY );
            }
        } );
        btnConfiguration.setBounds( 13, 100, 174, 43 );
        contentPanelBackground.add( btnConfiguration );

        if ( running )
        {
            panelstarted.setVisible( true );
            panelstopped.setVisible( false );
        }
        else
        {
            panelstarted.setVisible( false );
            panelstopped.setVisible( true );
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
                        panelstopped.setVisible( true );

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
                        boolean result = checkBeforeStart();
                        if ( !result )
                            return;

                        ProcessMonitorWindow.this.setCursor( Cursor.getPredefinedCursor( Cursor.WAIT_CURSOR ) );

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
                                panelstopped.setVisible( false );
                                ProcessMonitorWindow.this.setCursor( Cursor.getDefaultCursor() );
                            }
                        }, null, false );

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

        installListeners();

        fillConfiguration();

    }

    /**
     * Do some checks before start. Return true if everything is OK. false is something is wrong and we cannot start.
     * 
     * @return result
     */
    private boolean checkBeforeStart()
    {
        // 1st- check if the destination folder exist
        MessicConfig mc = new MessicConfig();
        String paramMusicFolder = mc.getMusicFolder();
        File fMusicFolder = new File( paramMusicFolder );
        final Properties ml = Util.getMultilanguage();
        if ( !fMusicFolder.exists() )
        {
            int result =
                JOptionPane.showConfirmDialog( ProcessMonitorWindow.this,
                                               ml.getProperty( "messic-check-musicfolder-noexist" ), "",
                                               JOptionPane.YES_NO_OPTION );
            if ( result == JOptionPane.YES_OPTION )
            {
                return true;
            }
            else
            {
                return false;
            }
        }

        return true;
    }

    private Point initialClick;

    private JTextField tfProxyURL;

    private JTextField tfProxyPort;

    private JTextField tfHTTPPort;

    private JTextField tfHTTPSPort;

    private JTextField tfSessionTimeout;

    private JTextField tfConfigMusicFolder;

    private void installListeners()
    {
        // Get point of initial mouse click
        addMouseListener( new MouseAdapter()
        {
            public void mousePressed( MouseEvent e )
            {
                initialClick = e.getPoint();
                getComponentAt( initialClick );
            }
        } );

        // Move window when mouse is dragged
        addMouseMotionListener( new MouseMotionAdapter()
        {
            public void mouseDragged( MouseEvent e )
            {
                // get location of Window
                int thisX = getLocation().x;
                int thisY = getLocation().y;

                // Determine how much the mouse moved since the initial click
                int xMoved = ( thisX + e.getX() ) - ( thisX + initialClick.x );
                int yMoved = ( thisY + e.getY() ) - ( thisY + initialClick.y );

                // Move window to this position
                int X = thisX + xMoved;
                int Y = thisY + yMoved;
                setLocation( X, Y );
            }
        } );

        // Close "button" (image) listeners
        closeLabel.addMouseListener( new MouseAdapter()
        {

            public void mouseReleased( MouseEvent e )
            {
                closeLabel.setIcon( closeNormal );
            }

            public void mouseEntered( MouseEvent e )
            {
                closeLabel.setIcon( closeHover );
            }

            public void mouseExited( MouseEvent e )
            {
                closeLabel.setIcon( closeNormal );
            }

            public void mouseClicked( MouseEvent e )
            {
                close();
            }
        } );
    }

    // close and dispose
    public void close()
    {
        setVisible( false );
        dispose();
        System.exit( 0 );
    }

    private Color modifiedColor = new Color( 0xFFAAAA );

    private boolean flagModified = false;

    /**
     * fill all the config fields with teh current configuration
     */
    public void fillConfiguration()
    {
        Color defaultColor = new Color( 0xFFFFFF );

        MessicConfig sc = new MessicConfig();
        String proxyURL = sc.getProxyUrl();
        String proxyPort = sc.getProxyPort();

        if ( proxyURL != null && proxyURL.trim().length() > 0 )
        {
            this.checkProxy.setSelected( true );
            this.tfProxyURL.setText( proxyURL );
            this.tfProxyPort.setText( proxyPort );
        }
        else
        {
            this.checkProxy.setSelected( false );
            this.tfProxyURL.setText( "" );
            this.tfProxyPort.setText( "" );
        }

        boolean bsecure = sc.isMessicSecureProtocol();
        this.chckbxSecureCommunications.setSelected( bsecure );
        String httpPort = sc.getHttpPort();
        this.tfHTTPPort.setText( ( httpPort == null ? "FREE" : httpPort ) );
        String httpsPort = sc.getHttpsPort();
        this.tfHTTPSPort.setText( ( httpsPort == null ? "FREE" : httpsPort ) );
        this.tfSessionTimeout.setText( sc.getMessicTimeout() );

        if ( sc.getMusicFolder() == null || sc.getMusicFolder().length() <= 0 )
        {
            SelectMusicFolderWindow smfw = new SelectMusicFolderWindow();
            smfw.setVisible( true );
            sc = new MessicConfig();
        }

        this.tfConfigMusicFolder.setText( sc.getMusicFolder() );

        // background default color for all of them (when modified it turns to other color)
        this.checkProxy.setBackground( defaultColor );
        this.tfProxyURL.setBackground( defaultColor );
        this.tfProxyPort.setBackground( defaultColor );
        this.chckbxSecureCommunications.setBackground( defaultColor );
        this.tfHTTPPort.setBackground( defaultColor );
        this.tfHTTPSPort.setBackground( defaultColor );
        this.tfSessionTimeout.setBackground( defaultColor );
        this.tfConfigMusicFolder.setBackground( defaultColor );
        this.flagModified = false;
    }
}
