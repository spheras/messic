package com.gmail.kunicins.olegs.libshout;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Main
{

    public static void main( String[] args )
        throws IOException
    {

        Libshout icecast = Libshout.getInstance();
        System.out.println( icecast.getVersion() );

        byte[] buffer = new byte[1024];
        String sfile =
            "/home/spheras/jasdata/music/messic-test/spheras/Duke Ellington/1956-Ellington At Newport/vol01/01-Star Spangled Banner444.mp3";
        InputStream mp3 = new BufferedInputStream( new FileInputStream( new File( sfile ) ) );
        icecast.setHost( "localhost" );
        icecast.setPort( 8000 );
        icecast.setProtocol( Libshout.PROTOCOL_HTTP );
        icecast.setPassword( "hackme" );
        icecast.setMount( "/mymount" );
        icecast.setFormat( Libshout.FORMAT_MP3 );
        icecast.open();
        int read = mp3.read( buffer );
        while ( read > 0 )
        {
            icecast.send( buffer, read );
            read = mp3.read( buffer );
        }
        icecast.close();
        mp3.close();
    }
}
