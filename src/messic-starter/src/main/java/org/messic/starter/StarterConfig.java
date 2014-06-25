package org.messic.starter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class StarterConfig
{
    private Properties configuration;

    public StarterConfig()
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

}
