package org.messic.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class MessicConfig
{
    private Properties configuration;

    public static final String MESSIC_HTTPPORT = "messic-httpport";

    public static final String MESSIC_SECUREPROTOCOL = "messic-secure";

    public static final String MESSIC_HTTPSPORT = "messic-httpsport";

    public static final String MESSIC_MUSICFOLDER = "messic-musicfolder";

    public static final String MESSIC_PROXYPORT = "proxy-port";

    public static final String MESSIC_PROXYURL = "proxy-url";

    public static final String MESSIC_TIMEOUT = "messic-timeout";

    public void save()
        throws IOException
    {
        File f = new File( "./conf/config.properties" );
        FileOutputStream fos = new FileOutputStream( f );
        Date resultdate = new Date( System.currentTimeMillis() );
        this.configuration.store( fos,
                                  "Messic Configuration - Last Saved:"
                                      + new SimpleDateFormat( "MM/dd/yyyy" ).format( resultdate ) );
        fos.close();
    }

    public MessicConfig()
    {
        File f = new File( "./conf/config.properties" );
        this.configuration = new Properties();
        if ( f.exists() )
        {
            InputStream is;
            try
            {
                is = new FileInputStream( f );
                this.configuration.load( is );
                is.close();
            }
            catch ( FileNotFoundException e )
            {
                e.printStackTrace();
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }

        }
    }

    /**
     * @return the configuration
     */
    public Properties getConfiguration()
    {
        return configuration;
    }

    /**
     * @param configuration the configuration to set
     */
    public void setConfiguration( Properties configuration )
    {
        this.configuration = configuration;
    }

    /**
     * Return if messic is configured to use a secure protocol
     * 
     * @return
     */
    public boolean isMessicSecureProtocol()
    {
        String messicSecure = getConfiguration().getProperty( MESSIC_SECUREPROTOCOL );
        try
        {
            boolean secure = Boolean.valueOf( messicSecure );
            return secure;
        }
        catch ( Exception e )
        {
            return false;
        }

    }

    /**
     * Return the http port configured, null if not configured
     * 
     * @return
     */
    public String getHttpPort()
    {

        String messicHttpPort = getConfiguration().getProperty( MESSIC_HTTPPORT );
        try
        {
            Integer.valueOf( messicHttpPort );
        }
        catch ( Exception e )
        {
            // if the user put something like FREE, then no port configure, the first one which is free
            return null;
        }
        return messicHttpPort;
    }

    /**
     * Return the https port configured, null if not configured
     * 
     * @return
     */
    public String getHttpsPort()
    {

        String messicHttpsPort = getConfiguration().getProperty( MESSIC_HTTPSPORT );
        try
        {
            Integer.valueOf( messicHttpsPort );
        }
        catch ( Exception e )
        {
            // if the user put something like FREE, then no port configure, the first one which is free
            return null;
        }
        return messicHttpsPort;
    }

    /**
     * Return the configured music folder
     * 
     * @return
     */
    public String getMusicFolder()
    {

        String messicMusicFolder = getConfiguration().getProperty( MESSIC_MUSICFOLDER );
        return messicMusicFolder;
    }

    /**
     * Return the proxy port that should be used by messic
     * 
     * @return
     */
    public String getProxyPort()
    {
        return getConfiguration().getProperty( MESSIC_PROXYPORT );
    }

    /**
     * Return the proxy url that should be used by messic
     * 
     * @return
     */
    public String getProxyUrl()
    {

        return getConfiguration().getProperty( MESSIC_PROXYURL );
    }

    /**
     * Return the timeout for the messic web application
     * 
     * @return
     */
    public String getMessicTimeout()
    {
        String timeout = getConfiguration().getProperty( MESSIC_TIMEOUT );

        try
        {
            Long.valueOf( timeout );
        }
        catch ( Exception e )
        {
            timeout = "86400000";
        }

        return timeout;
    }
}
