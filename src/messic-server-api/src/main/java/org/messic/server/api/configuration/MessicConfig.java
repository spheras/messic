package org.messic.server.api.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.messic.server.Util;
import org.springframework.stereotype.Component;

@Component
public class MessicConfig
{
    private Properties configuration;

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
     * Return the current port in which messic is launched
     * 
     * @return
     */
    public static String getCurrentPort()
    {
        File f = new File( "./currentport" );
        if ( f.exists() )
        {
            try
            {
                Properties pcurrentport = new Properties();
                pcurrentport.load( new FileInputStream( f ) );
                String currentPort = pcurrentport.getProperty( "currentPort" );
                return currentPort;
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }
            return "";
        }
        else
        {
            return "";
        }
    }

    public static boolean isSecured()
    {
        File f = new File( "./currentport" );
        if ( f.exists() )
        {
            Properties pcurrentport = new Properties();
            try
            {
                pcurrentport.load( new FileInputStream( f ) );
                String ssecured = pcurrentport.getProperty( "secured" );
                try
                {
                    Boolean secured = Boolean.valueOf( ssecured );
                    return secured;
                }
                catch ( Exception e )
                {
                    return false;
                }
            }
            catch ( FileNotFoundException e1 )
            {
                e1.printStackTrace();
                return false;
            }
            catch ( IOException e1 )
            {
                e1.printStackTrace();
                return false;
            }
        }
        else
        {
            return false;
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

}
