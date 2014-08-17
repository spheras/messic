package org.messic.starter;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.messic.configuration.MessicConfig;

public class SelectMusicFolderWindow
    extends JDialog
{

    private JTextField tfMusicFolder;

    private JDialog frame = this;

    /**
     * Create the application.
     */
    public SelectMusicFolderWindow()
    {
        setTitle( "Music Folder" );
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize()
    {
        frame.setResizable( false );
        frame.setModalExclusionType( ModalExclusionType.APPLICATION_EXCLUDE );
        frame.setBounds( 100, 100, 450, 265 );
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation( dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2 );

        frame.setModal( true );
        frame.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
        frame.getContentPane().setLayout( null );

        tfMusicFolder = new JTextField();
        tfMusicFolder.setBounds( 12, 160, 392, 19 );
        frame.getContentPane().add( tfMusicFolder );
        tfMusicFolder.setColumns( 10 );
        tfMusicFolder.setText( System.getProperty( "user.home" ) + File.separatorChar + "messic-data" );

        JButton btnBrowse = new JButton( "..." );
        btnBrowse.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                // Create a file chooser
                final JFileChooser fc = new JFileChooser();
                fc.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
                int returnVal = fc.showOpenDialog( SelectMusicFolderWindow.this );

                if ( returnVal == JFileChooser.APPROVE_OPTION )
                {
                    File file = fc.getSelectedFile();
                    tfMusicFolder.setText( file.getAbsolutePath() );
                }

            }
        } );
        btnBrowse.setBounds( 406, 157, 30, 25 );
        frame.getContentPane().add( btnBrowse );

        final Properties ml = Util.getMultilanguage();

        JLabel lblNewLabel = new JLabel( ml.getProperty( "messic-selectmusic-title" ) );
        lblNewLabel.setBounds( 12, 35, 424, 124 );
        frame.getContentPane().add( lblNewLabel );

        JButton btnAceptar = new JButton( "Aceptar" );
        btnAceptar.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                if ( tfMusicFolder.getText().length() > 0 )
                {
                    try
                    {
                        String folder = tfMusicFolder.getText();
                        File ffolder = new File( folder );
                        ffolder.mkdirs();

                        MessicConfig mc = new MessicConfig();
                        mc.getConfiguration().setProperty( MessicConfig.MESSIC_MUSICFOLDER, folder );
                        mc.save();
                        frame.dispose();
                    }
                    catch ( Exception ex )
                    {
                        JOptionPane.showMessageDialog( SelectMusicFolderWindow.this,
                                                       ml.getProperty( "messic-config-saveerror" ) + "\n "
                                                           + ex.getMessage() );
                    }
                }
            }
        } );
        btnAceptar.setBounds( 12, 202, 117, 25 );
        frame.getContentPane().add( btnAceptar );

        JButton btnCancelar = new JButton( "Cancelar" );
        btnCancelar.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                System.exit( 0 );
            }
        } );
        btnCancelar.setBounds( 319, 202, 117, 25 );
        frame.getContentPane().add( btnCancelar );

        JLabel lIcon = new JLabel( "" );
        lIcon.setBounds( 370, 10, 64, 64 );
        ImageIcon ii = new ImageIcon( getClass().getResource( "/logotipo-64x64.png" ) );
        lIcon.setIcon( ii );
        getContentPane().add( lIcon );
    }
}
