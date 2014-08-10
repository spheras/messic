package org.messic.configuration;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class MessicConfig
{
    private static Logger log = Logger.getLogger( MessicConfig.class );

    private Properties configuration;

    public static final String MESSIC_HTTPPORT = "messic-httpport";

    public static final String MESSIC_SECUREPROTOCOL = "messic-secure";

    public static final String MESSIC_HTTPSPORT = "messic-httpsport";

    public static final String MESSIC_MUSICFOLDER = "messic-musicfolder";

    public static final String MESSIC_PROXYPORT = "proxy-port";

    public static final String MESSIC_PROXYURL = "proxy-url";

    public static final String MESSIC_TIMEOUT = "messic-timeout";

    public static final String configurationFilePath = "./conf/config.properties";

    public MessicConfig()
    {
        File f = new File( configurationFilePath );
        this.configuration = new Properties();
        if ( f.exists() )
        {
            // TODO what to do if in new versions the config.properties have new properties, and this file is from an
            // old version

            InputStream is;
            try
            {
                is = new FileInputStream( f );
                this.configuration.load( is );
                is.close();
            }
            catch ( FileNotFoundException e )
            {
                log.error( "failed!", e );
            }
            catch ( IOException e )
            {
                log.error( "failed!", e );
            }
        }
        else
        {
            try
            {
                this.configuration.load( MessicConfig.class.getResourceAsStream( "/org/messic/configuration/config-default.properties" ) );
                File fnew = new File( configurationFilePath );
                fnew.mkdirs();
                FileOutputStream fos = new FileOutputStream( fnew );
                this.configuration.store( fos, "messic configuration" );
                fos.close();
            }
            catch ( IOException e )
            {
                log.error( "failed!", e );
            }
        }
    }

    public static class MessicVersion
    {
        public MessicVersion( String version )
        {
            this.sversion = version;
            String[] parts = version.split( "\\." );
            this.version = Integer.valueOf( parts[0] );
            this.revision = Integer.valueOf( parts[1] );
            if ( parts[2].indexOf( "-" ) >= 0 )
            {
                String[] subparts = parts[2].split( "-" );
                this.compilation = Integer.valueOf( subparts[0] );
                this.semantic = subparts[1];
            }
            else
            {
                this.compilation = Integer.valueOf( parts[2] );
            }
        }

        public String sversion;

        public int version;

        public int revision;

        public int compilation;

        public String semantic;
    }

    public static MessicVersion getCurrentVersion()
    {
        InputStream is = MessicConfig.class.getResourceAsStream( "/messic.version" );
        try
        {
            byte[] data = readInputStream( is );
            String sdata = new String( data );
            MessicVersion mv = new MessicVersion( sdata );
            return mv;
        }
        catch ( IOException e )
        {
            log.error( "failed!", e );
        }
        return null;
    }

    /**
     * Save the properties file to the file system
     * 
     * @throws IOException
     */
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

    public static class CurrentPort
    {
        /** the current port where messic is launched */
        public int currentPort;

        /** if messic have been launched in secure mode */
        public boolean secure;

    }

    public static CurrentPort checkCurrentport()
    {
        CurrentPort cp = new CurrentPort();
        File f = new File( "./currentport" );
        if ( f.exists() )
        {
            Properties pcurrentport = new Properties();
            try
            {
                pcurrentport.load( new FileInputStream( f ) );
                String ssecured = pcurrentport.getProperty( "secured" );
                String scurrentPort = pcurrentport.getProperty( "currentPort" );
                try
                {
                    cp.secure = Boolean.valueOf( ssecured );
                }
                catch ( Exception e )
                {
                }
                try
                {
                    cp.currentPort = Integer.valueOf( scurrentPort );
                }
                catch ( Exception e )
                {
                    cp.currentPort = 80;
                }

                return cp;
            }
            catch ( FileNotFoundException e1 )
            {
                log.error( "failed!", e1 );
            }
            catch ( IOException e1 )
            {
                log.error( "failed!", e1 );
            }
            return cp;
        }
        else
        {
            return cp;
        }
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

}
