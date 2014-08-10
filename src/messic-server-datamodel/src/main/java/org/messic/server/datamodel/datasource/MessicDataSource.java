package org.messic.server.datamodel.datasource;

import java.io.File;

import org.apache.commons.dbcp.BasicDataSource;

public class MessicDataSource
    extends BasicDataSource
{
    public static String databasePath;

    @Override
    public synchronized void setUrl( String url )
    {
        String musicFolder = System.getProperty( "messic.musicfolder" );
        if ( musicFolder == null || musicFolder.length() <= 0 )
        {
            musicFolder = System.getProperty( "user.home" ) + File.separatorChar + "messic-data";
        }
        File f = new File( musicFolder );
        if ( !f.exists() )
        {
            f.mkdirs();
        }

        String messic_path = f.getAbsolutePath();
        messic_path = messic_path.replaceAll( "\\\\", "\\\\\\\\" );
        String newURL = url.replaceAll( "MESSIC_PATH", messic_path );

        databasePath = messic_path;

        super.setUrl( newURL );
    }

}
