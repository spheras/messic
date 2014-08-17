package org.messic.service;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import org.apache.log4j.Logger;

public class AnimatedGifSplashScreen
    extends JWindow
{
    private static Logger log = Logger.getLogger( AnimatedGifSplashScreen.class );

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
     * Read an inputstream and return a byte[] with the whole content of the readed at the inputstream
     * 
     * @param is {@link InputStream}
     * @return byte[] content
     * @throws IOException
     */
    public static byte[] readInputStream( InputStream is )
        throws IOException
    {
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int cant = is.read( buffer );
        while ( cant > 0 )
        {
            baos.write( buffer, 0, cant );
            cant = is.read( buffer );
        }

        return baos.toByteArray();
    }

    public AnimatedGifSplashScreen()
        throws IOException
    {
        JLabel label = new JLabel();

        label.setHorizontalAlignment( SwingConstants.CENTER );

        ImageIcon ii =
            new ImageIcon(
                           readInputStream( this.getClass().getResourceAsStream( "/tumblr_n24lc8Wmhg1qk57pio1_500.gif" ) ) );
        label.setIcon( ii );
        add( label, BorderLayout.CENTER );
        label.setBorder( new LineBorder( new Color( 0x888888 ), 3 ) );

        // Set up the glass pane, which appears over both menu bar
        // and content pane and is an item listener on the change
        // button.
        MyGlassPane myGlassPane = new MyGlassPane();
        setGlassPane( myGlassPane );
        myGlassPane.setVisible( true );

        // frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize( new Dimension( 360, 220 ) );
        setSize( ii.getIconWidth() + 20, ii.getIconHeight() + 20 );
        setBackground( new Color( 0x444444 ) );
        // frame.pack();
        setLocationRelativeTo( null );
        setVisible( true );
        setAlwaysOnTop( true );
    }

    /**
     * We have to provide our own glass pane so that it can paint.
     */
    class MyGlassPane
        extends JComponent
    {

        /**
         * 
         */
        private static final long serialVersionUID = -7727880991589656780L;

        private int flat = 0;

        private long started = System.currentTimeMillis();

        public MyGlassPane()
        {
            super();
            try
            {
                createFont();
            }
            catch ( FontFormatException e )
            {
                log.error( "failed!", e );
            }
            catch ( IOException e )
            {
                log.error( "failed!", e );
            }
        }

        private Font myFont = null;

        private void createFont()
            throws FontFormatException, IOException
        {
            Font font =
                Font.createFont( Font.TRUETYPE_FONT, this.getClass().getResourceAsStream( "/Amaranth-Bold-webfont.ttf" ) );
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont( font );

            myFont = new Font( "Amaranth", Font.BOLD, 30 );
        }

        protected void paintComponent( Graphics g )
        {
            super.paintComponent( g );
            Graphics2D g2 = (Graphics2D) g;
            RenderingHints rh =
                new RenderingHints( RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
            g2.setRenderingHints( rh );

            g.setFont( myFont );
            g.setColor( Color.white );
            int width = getParent().getWidth();
            int height = getParent().getHeight();

            long current = System.currentTimeMillis();
            long time = current - started;
            if ( time > 200 )
            {
                flat++;
                if ( flat > 3 )
                {
                    flat = 0;
                }
                started = current;
            }

            String result = "Loading";
            for ( int i = 0; i < flat; i++ )
            {
                result = result + " .";
            }

            g.drawString( result, ( width ) - 180, height - 20 );
        }

    }

}
