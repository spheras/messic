package org.messic.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

public class MessicCheckUpdate
{
    public static void main( String[] args )
        throws IOException
    {
        checkUpdate();
    }

    private static Proxy getProxy( MessicConfig mc )
    {

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

    /**
     * Check the update server to know whats the last version of messic available.
     * 
     * @return
     * @throws IOException
     */
    public static MessicVersion checkUpdate()
        throws IOException
    {
        Proxy proxy = getProxy( new MessicConfig() );

        String sUrl = "https://raw.githubusercontent.com/spheras/messic/gh-pages/messic-version.properties";
        URL url = new URL( sUrl );
        URLConnection connection = ( proxy != null ? url.openConnection( proxy ) : url.openConnection() );
        InputStream is = connection.getInputStream();
        Properties p = new Properties();
        p.load( is );
        is.close();
        String sversion = p.getProperty( "messic.last.stable.version" );

        MessicVersion mv = new MessicVersion( sversion );
        return mv;
    }
}
