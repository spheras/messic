package org.messic.server.api.radio.icecast2.libshout;

import java.io.IOException;

import org.messic.server.api.plugin.radio.MessicRadioSong;
import org.messic.server.api.radio.icecast2.libshout.LibShoutJNA.size_t;

import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.ptr.IntByReference;

/**
 * LibshoutJNA wrapper to make it easy.<br>
 * Please see LibShoutJNA comments for an easy comprehension of the library
 */
public class LibShout
{
    public static LibShoutJNA libshout;

    private static LibShout instance;

    private NativeLong shout_t = null;

    public static LibShoutJNA getLib()
    {
        if ( libshout == null )
        {
            libshout = (LibShoutJNA) Native.loadLibrary( "libshout", LibShoutJNA.class );
        }
        return libshout;
    }

    private LibShout()
    {
        getLib().shout_init();
        this.shout_t = getLib().shout_new();
    }

    public void shutdown()
        throws IOException
    {
        getLib().shout_free( shout_t );
        getLib().shout_shutdown();

    }

    public static LibShout get()
    {
        if ( instance == null )
        {
            instance = new LibShout();
        }
        return instance;
    }

    /**
     * Major, minor, and patch levels
     * 
     * @return
     */
    public String getVersion()
    {
        IntByReference major = new IntByReference();
        IntByReference minor = new IntByReference();
        IntByReference patch = new IntByReference();
        return getLib().shout_version( major, minor, patch );
    }

    /**
     * Connection to Icecast established
     * 
     * @return
     */
    public boolean isConnected()
    {
        return getLib().shout_get_connected( shout_t ) == LibShoutJNA.SHOUTERR_CONNECTED ? true : false;
    }

    /**
     * Open connection to Icecast. All parameters must already be set
     * 
     * @throws IOException
     */
    public void open()
        throws IOException
    {
        if ( getLib().shout_open( shout_t ) != LibShoutJNA.SHOUTERR_SUCCESS )
        {
            throw new IOException( getLib().shout_get_error( shout_t ) );
        }
    }

    /**
     * Close connection to Icecast
     */
    public void close()
        throws IOException
    {
        if ( LibShout.get().isConnected() )
        {
            if ( getLib().shout_close( shout_t ) != LibShoutJNA.SHOUTERR_SUCCESS )
            {
                throw new IOException( getLib().shout_get_error( shout_t ) );
            }
        }
    }

    /**
     * Reinit the library. It is init at the construction of this object, but you can force to reinit again.
     */
    public void reinit()
    {
        getLib().shout_init();
    }

    /**
     * Set Icecast host
     * 
     * @param host
     * @throws IOException
     */
    public void setHost( String host )
        throws IOException
    {
        if ( getLib().shout_set_host( shout_t, host ) != LibShoutJNA.SHOUTERR_SUCCESS )
        {
            throw new IOException( getLib().shout_get_error( shout_t ) );
        }
    }

    /**
     * Get Icecast host
     * 
     * @return
     */
    public String getHost()
    {
        return getLib().shout_get_host( shout_t );
    }

    /**
     * Set Icecast protocol
     * 
     * @param protocol
     * @throws IOException
     */
    public void setProtocol( LibShoutProtocol protocol )
        throws IOException
    {
        if ( getLib().shout_set_protocol( shout_t, protocol.getValue() ) != LibShoutJNA.SHOUTERR_SUCCESS )
        {
            throw new IOException( getLib().shout_get_error( shout_t ) );
        }
    }

    /**
     * Get Icecast protocol
     * 
     * @return
     */
    public LibShoutProtocol getProtocol()
    {
        return LibShoutProtocol.from( getLib().shout_get_protocol( shout_t ) );
    }

    /**
     * Set Icecast port
     * 
     * @param port
     * @throws IOException
     */
    public void setPort( int port )
        throws IOException
    {
        if ( getLib().shout_set_port( shout_t, (short) port ) != LibShoutJNA.SHOUTERR_SUCCESS )
        {
            throw new IOException( getLib().shout_get_error( shout_t ) );
        }
    }

    /**
     * Get Icecast port
     * 
     * @return
     */
    public int getPort()
    {
        return getLib().shout_get_port( shout_t );
    }

    /**
     * Set Icecast password
     * 
     * @param password
     * @throws IOException
     */
    public void setPassword( String password )
        throws IOException
    {
        if ( getLib().shout_set_password( shout_t, password ) != LibShoutJNA.SHOUTERR_SUCCESS )
        {
            throw new IOException( getLib().shout_get_error( shout_t ) );
        }
    }

    /**
     * Get Icecast password
     * 
     * @return
     */
    public String getPassword()
    {
        return getLib().shout_get_password( shout_t );
    }

    /**
     * Set Icecast mount
     * 
     * @param mount
     * @throws IOException
     */
    public void setMount( String mount )
        throws IOException
    {
        if ( getLib().shout_set_mount( shout_t, mount ) != LibShoutJNA.SHOUTERR_SUCCESS )
        {
            throw new IOException( getLib().shout_get_error( shout_t ) );
        }
    }

    /**
     * Get Icecast mount
     * 
     * @return
     */
    public String getMount()
    {
        return getLib().shout_get_mount( shout_t );
    }

    /**
     * Set format parameter
     * 
     * @param format
     * @throws IOException
     */
    public void setFormat( LibShoutFormat format )
        throws IOException
    {
        if ( getLib().shout_set_format( shout_t, format.getValue() ) != LibShoutJNA.SHOUTERR_SUCCESS )
        {
            throw new IOException( getLib().shout_get_error( shout_t ) );
        }
    }

    /**
     * Get format parameter
     * 
     * @return
     */
    public LibShoutFormat getFormat()
    {
        return LibShoutFormat.from( getLib().shout_get_format( shout_t ) );
    }

    /**
     * Set Icecast username
     * 
     * @param username
     * @throws IOException
     */
    public void setUser( String username )
        throws IOException
    {
        if ( getLib().shout_set_user( shout_t, username ) != LibShoutJNA.SHOUTERR_SUCCESS )
        {
            throw new IOException( getLib().shout_get_error( shout_t ) );
        }
    }

    /**
     * Get Icecast username
     * 
     * @return
     */
    public String getUser()
    {
        return getLib().shout_get_user( shout_t );
    }

    /**
     * Set info parameter
     * 
     * @param key
     * @param value
     * @throws IOException
     */
    public void setInfo( String key, String value )
        throws IOException
    {
        if ( getLib().shout_set_audio_info( shout_t, key, value ) != LibShoutJNA.SHOUTERR_SUCCESS )
        {
            throw new IOException( getLib().shout_get_error( shout_t ) );
        }
    }

    /**
     * Get info parameter
     * 
     * @param key
     * @return
     */
    public String getInfo( String key )
    {
        return getLib().shout_get_audio_info( shout_t, key );
    }

    /**
     * Send data to Icecast, parsing it for format specific timing info
     * 
     * @param data
     * @param length
     * @throws IOException
     */
    public void send( byte[] data, int length )
        throws IOException
    {
        send( data, length, true );
    }

    /**
     * Send data to Icecast, parsing it for format specific timing info
     * 
     * @param data
     * @param length
     * @throws IOException
     */
    public void send( byte[] data, int length, boolean sync )
        throws IOException
    {
        size_t size_t = new size_t( length );
        if ( getLib().shout_send( shout_t, data, size_t ) != LibShoutJNA.SHOUTERR_SUCCESS )
        {
            throw new IOException( getLib().shout_get_error( shout_t ) );
        }
        if ( sync )
            getLib().shout_sync( shout_t );
    }

    /**
     * Sync the stream
     */
    public void sync()
    {
        getLib().shout_sync( shout_t );
    }

    /**
     * Set MP3 meta parameter
     * 
     * @param key
     * @param value
     * @throws IOException
     */
    public void setMeta( MessicRadioSong song )
        throws IOException
    {

        NativeLong instanceMeta = getLib().shout_metadata_new();

        /*
         * We can do this, because we know how libshout works. This adds "charset=UTF-8" to the HTTP metadata update
         * request and has the desired effect of letting newer-than-2.3.1 versions of Icecast know which encoding we're
         * using.
         */
        if ( getLib().shout_metadata_add( instanceMeta, "charset", "UTF-8" ) != LibShoutJNA.SHOUTERR_SUCCESS )
        {
            throw new IOException( getLib().shout_get_error( shout_t ) );
        }

        if ( getLib().shout_metadata_add( instanceMeta, "artist",
                                          ( song != null && song.authorName != null ? song.authorName : "messic" ) ) != LibShoutJNA.SHOUTERR_SUCCESS )
        {
            throw new IOException( getLib().shout_get_error( shout_t ) );
        }
        if ( getLib().shout_metadata_add( instanceMeta, "title",
                                          ( song != null && song.songName != null ? song.songName : "waiting cast..." ) ) != LibShoutJNA.SHOUTERR_SUCCESS )
        {
            throw new IOException( getLib().shout_get_error( shout_t ) );
        }
        if ( getLib().shout_metadata_add( instanceMeta, "song",
                                          ( song != null && song.songName != null ? song.songName : "waiting cast..." ) ) != LibShoutJNA.SHOUTERR_SUCCESS )
        {
            throw new IOException( getLib().shout_get_error( shout_t ) );
        }
        if ( getLib().shout_metadata_add( instanceMeta, "genre",
                                          ( song != null && song.albumGenre != null ? song.albumGenre : "messic genre" ) ) != LibShoutJNA.SHOUTERR_SUCCESS )
        {
            throw new IOException( getLib().shout_get_error( shout_t ) );
        }
        if ( getLib().shout_metadata_add( instanceMeta, "description", "messic radio" ) != LibShoutJNA.SHOUTERR_SUCCESS )
        {
            throw new IOException( getLib().shout_get_error( shout_t ) );
        }

        if ( getLib().shout_set_metadata( shout_t, instanceMeta ) != LibShoutJNA.SHOUTERR_SUCCESS )
        {
            throw new IOException( getLib().shout_get_error( shout_t ) );
        }

        getLib().shout_metadata_free( instanceMeta );

    }
}
