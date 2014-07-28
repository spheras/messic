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
                FileInputStream fis = new FileInputStream( f );
                String port = new String( Util.readInputStream( fis ) );
                return port;
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
