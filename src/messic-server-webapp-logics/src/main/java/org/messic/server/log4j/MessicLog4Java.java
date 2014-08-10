package org.messic.server.log4j;

import java.io.File;

import org.apache.log4j.RollingFileAppender;

public class MessicLog4Java
    extends RollingFileAppender
{

    @Override
    public void setFile( String file )
    {
        String newFile = getFileParam( file );
        
        super.setFile( newFile );
    }

    private String getFileParam( String file )
    {
        String messicAppFolder = System.getProperty( "messic.app.folder" );
        if ( messicAppFolder != null && messicAppFolder.length() > 0 )
        {
            return messicAppFolder + File.separatorChar + file;
        }
        else
        {
            return file;
        }
    }
}
