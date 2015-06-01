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
import java.awt.Toolkit;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import org.apache.log4j.Logger;
import org.messic.configuration.MessicConfig;

public class AnimatedGifSplashScreen
    extends JWindow
{
    private static Logger log = Logger.getLogger( AnimatedGifSplashScreen.class );

    // List of known url of animated gifs at the web to show as splash screens
    private static String[] knownGifUrls = { "http://33.media.tumblr.com/tumblr_lk23bhrYsh1qchrblo1_500.gif",
        "http://38.media.tumblr.com/c6fded3abd6535f6b4f9cf7a73cea710/tumblr_mhnavyed481rkbqvco1_500.gif",
        "http://33.media.tumblr.com/883e4eeb283c668482a9f57d49ca5202/tumblr_mir5epOXxe1rmgj27o1_500.gif",
        "http://38.media.tumblr.com/6d2d7ed3eb81a5f1971a56fe22a4fafc/tumblr_n4fmibUE3m1qexuy0o1_500.gif",
        "http://31.media.tumblr.com/c5cbbdc568b552b910fe3c19d22bf4ae/tumblr_n6zxafDJC81tnzwcoo1_500.gif",
        "http://38.media.tumblr.com/3e61bf2940872bf9998bec29f923f377/tumblr_n6ni53w3Ou1tbb7cno1_500.gif",
        "http://33.media.tumblr.com/bfbda61e7201c1266de2ce01666ea6bd/tumblr_mtw7g5Bfcu1sg7iqko1_400.gif",
        "http://media.giphy.com/media/14mssh7YCCk61G/giphy.gif",
        "http://www.dj-rooms.com/wp-content/uploads/2011/08/djrooms_anim05.gif",
        "http://www.dj-rooms.com/wp-content/uploads/2011/08/djrooms_anim06.gif",
        "http://www.dj-rooms.com/wp-content/uploads/2011/08/djrooms_anim07.gif",
        "http://www.dj-rooms.com/wp-content/uploads/2011/08/djrooms_anim08.gif",
        "http://www.dj-rooms.com/wp-content/uploads/2011/08/djrooms_anim10.gif",
        "http://www.dj-rooms.com/wp-content/uploads/2011/08/djrooms_anim12.gif",
        "http://www.dj-rooms.com/wp-content/uploads/2011/08/djrooms_anim13.gif",
        "http://www.dj-rooms.com/wp-content/uploads/2011/08/djrooms_anim14.gif",
        "http://www.dj-rooms.com/wp-content/uploads/2011/08/djrooms_anim01.gif",
        "http://25.media.tumblr.com/027d6732a0153e38628445b394f07898/tumblr_n24lc8Wmhg1qk57pio1_500.gif" };

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

    /**
     * http://stackoverflow.com/questions/363681/generating-random-integers-in-a-range-with-java Returns a pseudo-random
     * number between min and max, inclusive. The difference between min and max can be at most
     * <code>Integer.MAX_VALUE - 1</code>.
     * 
     * @param min Minimum value
     * @param max Maximum value. Must be greater than min.
     * @return Integer between min and max, inclusive.
     * @see java.util.Random#nextInt(int)
     */
    public static int randInt( int min, int max )
    {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt( ( max - min ) + 1 ) + min;

        return randomNum;
    }

    private void getRandomImageIcon( final JLabel label )
        throws IOException
    {
        Thread t = new Thread()
        {
            public void run()
            {
                try
                {
                    URL url = new URL( knownGifUrls[randInt( 0, knownGifUrls.length - 1 )] );
                    Proxy proxy = getProxy();
                    URLConnection uc;
                    if ( proxy != null )
                    {
                        uc = url.openConnection( proxy );
                    }
                    else
                    {
                        uc = url.openConnection();
                    }
                    ImageIcon ii = new ImageIcon( readInputStream( uc.getInputStream() ) );
                    label.setIcon( ii );
                    setSize( ii.getIconWidth() + 20, ii.getIconHeight() + 20 );
                    setBackground( new Color( 0x444444 ) );
                    getContentPane().setBackground( Color.LIGHT_GRAY );
                    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                    setLocation( dim.width / 2 - getSize().width / 2, dim.height / 2 - getSize().height / 2 );
                }
                catch ( Exception e )
                {
                    e.printStackTrace();
                }
            };
        };
        t.start();
    }

    private Proxy getProxy()
    {
        MessicConfig mc = new MessicConfig();

        String url = (String) mc.getProxyUrl();
        String port = (String) mc.getProxyPort();
        if ( url != null && port != null && url.length() > 0 && port.length() > 0 )
        {
            SocketAddress addr = new InetSocketAddress( url, Integer.valueOf( port ) );
            Proxy proxy = new Proxy( Proxy.Type.HTTP, addr );
            return proxy;
        }
        return null;
    }

    public AnimatedGifSplashScreen()
        throws IOException
    {
        JLabel label = new JLabel();

        label.setHorizontalAlignment( SwingConstants.CENTER );

        setSize( 400, 100 );

        getRandomImageIcon( label );
        getContentPane().setBackground( new Color( 0x000000 ) );
        add( label, BorderLayout.CENTER );
        label.setBorder( new LineBorder( new Color( 0x888888 ), 3 ) );
        label.setBackground( new Color( 0x000000 ) );

        // Set up the glass pane, which appears over both menu bar
        // and content pane and is an item listener on the change
        // button.
        MyGlassPane myGlassPane = new MyGlassPane();
        setGlassPane( myGlassPane );
        myGlassPane.setVisible( true );

        // frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        // setMinimumSize( new Dimension( 360, 220 ) );

        setBackground( new Color( 0x000000 ) );
        // frame.pack();
        setLocationRelativeTo( null );
        setVisible( true );
        // setAlwaysOnTop( true );
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
