package org.messic.server.api.radio.icecast2;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.messic.server.api.radio.icecast2.libshout.LibShoutJNA;
import org.messic.server.api.radio.icecast2.libshout.LibShoutJNA.size_t;

import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.ptr.IntByReference;

public class Main
{

    public static void main( String[] args )
        throws IOException
    {
        // ----------OPENING ------------------
        // 1-init libshout
        LibShoutJNA libshout = (LibShoutJNA) Native.loadLibrary( "libshout", LibShoutJNA.class );
        libshout.shout_init();

        // 2-creating the instance
        NativeLong shout_t = libshout.shout_new();

        // ----------OPENING ------------------

        // 4- getting version
        IntByReference major = new IntByReference();
        IntByReference minor = new IntByReference();
        IntByReference patch = new IntByReference();
        String version = libshout.shout_version( major, minor, patch );
        System.out.println( "version:" + version );
        System.out.println( "version:" + major.getValue() + "." + minor.getValue() + "." + patch.getValue() );

        // 5-working with libshout
        working_libshout( libshout, shout_t );

        // ---------- CLOSING ------------------
        // 6-closing instance
        int closeResult = libshout.shout_close( shout_t );

        // 7-releasing instance
        libshout.shout_free( shout_t );

        // 8-shutdown libshout
        libshout.shout_shutdown();
        // ---------- CLOSING ------------------
    }

    private static void working_libshout( LibShoutJNA libshout, NativeLong shout_t )
        throws IOException
    {

        byte[] buffer = new byte[1024];
        String sfile =
            "/home/spheras/jasdata/music/messic-test/spheras/Duke Ellington/1956-Ellington At Newport/vol01/01-Star Spangled Banner444.mp3";
        InputStream mp3 = new BufferedInputStream( new FileInputStream( new File( sfile ) ) );
        libshout.shout_set_host( shout_t, "localhost" );
        short port = 8000;
        int portResult = libshout.shout_set_port( shout_t, port );
        int protocolResult = libshout.shout_set_protocol( shout_t, LibShoutJNA.SHOUT_PROTOCOL_HTTP );
        libshout.shout_set_password( shout_t, "hackme" );
        libshout.shout_set_mount( shout_t, "/mymount" );
        libshout.shout_set_format( shout_t, LibShoutJNA.SHOUT_FORMAT_MP3 );
        libshout.shout_open( shout_t );
        // getting status
        int connectedResult = libshout.shout_get_connected( shout_t );
        String error = libshout.shout_get_error( shout_t );
        int errNum = libshout.shout_get_errno( shout_t );

        int read = mp3.read( buffer );
        size_t size = new size_t( read );
        while ( read > 0 )
        {
            int resultSend = libshout.shout_send( shout_t, buffer, size );
            libshout.shout_sync( shout_t );
            read = mp3.read( buffer );
            size.setValue( read );
        }
        libshout.shout_close( shout_t );
        mp3.close();
    }
}
