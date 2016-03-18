package org.messic.server.api.radio.icecast2.libshout;

import com.sun.jna.IntegerType;
import com.sun.jna.Library;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

public interface LibShoutJNA
    extends Library
{
    /** No error **/
    public static final int SHOUTERR_SUCCESS = 0;

    /** Nonsensical arguments e.g. self being NULL **/
    public static final int SHOUTERR_INSANE = -1;

    /** Couldn't connect **/
    public static final int SHOUTERR_NOCONNECT = -2;

    /** Login failed **/
    public static final int SHOUTERR_NOLOGIN = -3;

    /** Socket error **/
    public static final int SHOUTERR_SOCKET = -4;

    /** Out of memory **/
    public static final int SHOUTERR_MALLOC = -5;

    public static final int SHOUTERR_METADATA = -6;

    /** Cannot set parameter while connected **/
    public static final int SHOUTERR_CONNECTED = -7;

    /** Not connected **/
    public static final int SHOUTERR_UNCONNECTED = -8;

    /** This libshout doesn't support the requested option **/
    public static final int SHOUTERR_UNSUPPORTED = -9;

    /** Socket is busy **/
    public static final int SHOUTERR_BUSY = -10;

    /** TLS requested but not supported by peer **/
    public static final int SHOUTERR_NOTLS = -11;

    /**
     * TLS connection can not be established because of bad certificate
     **/
    public static final int SHOUTERR_TLSBADCERT = -12;

    /** Retry last operation. **/
    public static final int SHOUTERR_RETRY = -13;

    /**
     * The HTTP protocol. This is the native protocol of the Icecast 2 server, and is the default.
     */
    public static final int SHOUT_PROTOCOL_HTTP = 0;

    /**
     * The RoarAudio protocol. This is the native protocol for <application>RoarAudio</application> servers.
     */
    public static final int SHOUT_PROTOCOL_ROARAUDIO = 3;

    /**
     * The Audiocast format. This is the native protocol of <application>Icecast 1
     */
    public static final int SHOUT_PROTOCOL_XAUDIOCAST = 1;

    /**
     * The ShoutCast format. This is the native protocol of <application>ShoutCast</application>.
     */
    public static final int SHOUT_PROTOCOL_ICY = 2;

    /** The Ogg Format. Vorbis or any codec may be used. This is the default format. */
    public static final int SHOUT_FORMAT_OGG = 0; /* application/ogg */

    /** The MP3 format. */
    public static final int SHOUT_FORMAT_MP3 = 1; /* audio/mpeg */

    /** The WebM format. */
    public static final int SHOUT_FORMAT_WEBM = 2; /* video/webm */

    /** The WebM format, audio only streams */
    public static final int SHOUT_FORMAT_WEBMAUDIO = 3; /* audio/webm audio only */

    /**
     * This is deprecated. It is an alias to <constant>SHOUT_FORMAT_OGG</constant>. Please migrate your code.
     */
    public static final int SHOUT_FORMAT_VORBIS = SHOUT_FORMAT_OGG;

    /**
     * TLS (Transport Layer Security) is disabled. Passwords and data will be sent unencrypted.
     */
    public static final int SHOUT_TLS_DISABLED = 0; /* Do not use TLS at all */

    /**
     * TLS (Transport Layer Security) support by the server will be autodetected. This is the default. In this mode TLS
     * is used if supported by the server. <warning>Please note that this is not a secure mode as it will not prevent
     * any downgrade attacks. <constant>SHOUT_TLS_AUTO_NO_PLAIN</constant> is a more secure version of this mode.
     */
    public static final int SHOUT_TLS_AUTO = 1; /* Autodetect which TLS mode to use if any */

    /**
     * TLS (Transport Layer Security) is used. Autodetection is used to find out about which modes are supported by the
     * server. This mode should be used for secure connections.
     */
    public static final int SHOUT_TLS_AUTO_NO_PLAIN = 2; /*
                                                          * Like SHOUT_TLS_AUTO_NO_PLAIN but does not allow plain
                                                          * connections
                                                          */

    /**
     * TLS (Transport Layer Security) is used as defined by RFC2818. In this mode libshout expects a TLS socket on the
     * server side and will begin with a TLS handshake prior to any other communication.
     */
    public static final int SHOUT_TLS_RFC2818 = 11; /* Use TLS for transport layer like HTTPS [RFC2818] does. */

    /**
     * TLS (Transport Layer Security) is used as defined by RFC2817. In this mode libshout will use HTTP/1.1's
     * Upgrade:-process to switch to TLS. This allows to use TLS on a non-TLS socket of the server.
     */
    public static final int SHOUT_TLS_RFC2817 = 12; /* Use TLS via HTTP Upgrade:-header [RFC2817]. */

    /** Used to specify the samplerate of the stream. */
    public static final String SHOUT_AI_SAMPLERATE = "samplerate";

    /** Used to specify the number of channels (usually one or two). */
    public static final String SHOUT_AI_CHANNELS = "channels";

    /** Used to specify the Ogg Vorbis encoding quality of the stream. */
    public static final String SHOUT_AI_QUALITY = "quality";

    /** Sets stream name. */
    public static final String SHOUT_META_NAME = "name";

    /** Sets stream URL. */
    public static final String SHOUT_META_URL = "url";

    /** Sets stream genre */
    public static final String SHOUT_META_GENRE = "genre";

    /** Sets stream description */
    public static final String SHOUT_META_DESCRIPTION = "description";

    /** Sets IRC contact information for stream */
    public static final String SHOUT_META_IRC = "irc";

    /** Sets AIM contact information for stream */
    public static final String SHOUT_META_AIM = "aim";

    /** Sets ICQ contact information for stream */
    public static final String SHOUT_META_ICQ = "icq";

    /** Used to specify the nominal bitrate of the stream. */
    public static final String SHOUT_AI_BITRATE = "bitrate";

    /**
     * Signed SIZE_T.
     */
    public static class ssize_t
        extends long_ptr
    {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        public ssize_t()
        {
            this( 0 );
        }

        public ssize_t( long value )
        {
            super( value );
        }
    }

    /**
     * The maximum number of bytes to which a pointer can point. Use for a count that must span the full range of a
     * pointer.
     */
    public static class size_t
        extends ulong_ptr
    {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        public size_t()
        {
            this( 0 );
        }

        public size_t( long value )
        {
            super( value );
        }
    }

    /**
     * Unsigned LONG_PTR.
     */
    public static class ulong_ptr
        extends IntegerType
    {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        public ulong_ptr()
        {
            this( 0 );
        }

        public ulong_ptr( long value )
        {
            super( Pointer.SIZE, value, true );
        }

        public Pointer toPointer()
        {
            return Pointer.createConstant( longValue() );
        }
    }

    /**
     * Signed long type for pointer precision. Use when casting a pointer to a long to perform pointer arithmetic.
     */
    public static class long_ptr
        extends IntegerType
    {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        public long_ptr()
        {
            this( 0 );
        }

        public long_ptr( long value )
        {
            super( Pointer.SIZE, value );
        }

        public Pointer toPointer()
        {
            return Pointer.createConstant( longValue() );
        }
    }

    /**
     * Initializes the shout library. Currently this initializes the networking mutexes when the library is built with
     * thread safety. This function must always be called before any other libshout function.
     */
    void shout_init();

    /**
     * Releases any resources which may have been allocated by a call to shout_init. An application should call this
     * function after it has finished using libshout.
     */
    void shout_shutdown();

    /**
     * Returns the version of the libshout library, both as a string via the return value, and as a set of integers
     * corresponding to the major, minor and patch levels of the library. The application must allocate the integer
     * parameters. If any parameter is NULL, libshout will not attempt to set it.
     */
    String shout_version( IntByReference major, IntByReference minor, IntByReference patch );

    /**
     * Allocates a new shout_t structure. May return NULL if no memory is available. The result should be disposed of
     * with shout_free when you are finished with it.
     */
    NativeLong shout_new();

    /**
     * Frees a shout_t allocated by shout_new
     * 
     * @param shout_t
     */
    void shout_free( NativeLong shout_t );

    /**
     * Opens a connection to a server. All connection parameters must have been set prior to this call. Return Values:
     * SHOUTERR_SUCCESS:The connection was successfully opened. <br>
     * SHOUTERR_INSANE: self is corrupt or incorrect. Possible reasons include an unset host, port, or password.<br>
     * SHOUTERR_CONNECTED:The connection has already been opened.<br>
     * SHOUTERR_UNSUPPORTED:The protocol/format combination is unsupported. For instance, Ogg Vorbis may only be sent
     * via the HTTP protocol.<br>
     * SHOUTERR_NOCONNECT:A connection to the server could not be established.<br>
     * SHOUTERR_SOCKET: An error occured while talking to the server.<br>
     * SHOUTERR_NOLOGIN: The server refused login, probably because authentication failed.<br>
     * SHOUTERR_MALLOC:There wasn't enough memory to complete the operation.
     * 
     * @param shout_t self
     * @return
     */
    int shout_open( NativeLong shout_t );

    /**
     * Closes a connection to the server. Return Values:<br>
     * SHOUTERR_SUCCESS: The connection was successfully closed.<br>
     * SHOUTERR_INSANE: self is not a valid shout_t object.<br>
     * SHOUTERR_UNCONNECTED: self is not currently connected.<br>
     * 
     * @param shout_t
     * @return
     */
    int shout_close( NativeLong shout_t );

    /**
     * Returns the connection status of the given <type>shout_t</type> object.<br>
     * Return Values<br>
     * SHOUTERR_INSANE: self is not a valid <type>shout_t</type> object.<br>
     * SHOUTERR_UNCONNECTED: self is not currently connected.<br>
     * SHOUTERR_CONNECTED: self is currently connected.
     * 
     * @param shout_t
     * @return
     */
    int shout_get_connected( NativeLong shout_t );

    /**
     * Returns a statically allocated string describing the last shout error that occured in this connection. Only valid
     * until the next call affecting this connection.
     * 
     * @param shout_t
     * @return
     */
    String shout_get_error( NativeLong shout_t );

    /**
     * Returns the shout error code of the last error that occured in this connection.
     * 
     * @param shout_t
     * @return
     */
    int shout_get_errno( NativeLong shout_t );

    /**
     * Sends len bytes of audio data from the buffer pointed to by data to the server. The connection must already have
     * been established by a successful call to shout_open.<br>
     * Return Values<br>
     * SHOUTERR_SUCCESS: The audio data was sent successfully.<br>
     * SHOUTERR_INSANE: self is not a valid shout_t object.<br>
     * SHOUTERR_UNCONNECTED: self is not currently connected.<br>
     * SHOUTERR_MALLOC: There wasn't enough memory to complete the operation.<br>
     * SHOUTERR_SOCKET: An error occured while talking to the server.
     * 
     * @param shout_t
     * @param data
     * @param len
     * @return
     */
    int shout_send( NativeLong shout_t, byte[] data, size_t len );

    /**
     * Causes the caller to sleep for the amount of time necessary to play back audio sent since the last call to
     * shout_sync. Should be called before every call to shout_send to ensure that audio data is sent to the server at
     * the correct speed. Alternatively, the caller may use shout_delay to determine the number of milliseconds to wait
     * and delay itself.
     * 
     * @param shout_t
     */
    void shout_sync( NativeLong shout_t );

    /**
     * Returns the number of milliseconds the caller should wait before calling shout_send again. This function is
     * provided as an alternative to shout_sync for applications that may wish to do other processing in the meantime.
     * 
     * @param shout_t
     * @return
     */
    int shout_delay( NativeLong shout_t );

    /**
     * Returns the number of bytes currently on the write queue. This is only useful in non-blocking mode.
     * 
     * @param shout_t
     */
    ssize_t shout_queuelen( NativeLong shout_t );

    /**
     * Connection parameters<br>
     * *********************<br>
     * The following functions are used to get or set attributes of the shout_t object before calling shout_open They
     * all work the same way: they operate on one attribute of a shout_t*. The shout_get_* functions return the value of
     * their associated parameter, or 0 on error (that's NULL for those functions that return strings). The shout_set_*
     * functions will return either SHOUTERR_SUCCESS on success, or one of:<br>
     * SHOUTERR_INSANE - shout_t is invalid.<br>
     * SHOUTERR_MALLOC - libshout could not allocate enough memory to assign the parameter.<br>
     * SHOUTERR_CONNECTED - you are attempting to change a connection attribute while the connection is open. Since
     * these parameters are only used when first opening the connection, this operation would be useless.
     **/

    /**
     * Sets non-blocking mode. The default is <constant>0</constant>.
     * 
     * @param shout_t
     * @param nonblocking
     * @return
     */
    int shout_set_nonblocking( NativeLong shout_t, IntegerType nonblocking );

    /** Returns non-blocking mode or <constant>0</constant> in case of error. **/
    IntegerType shout_get_nonblocking( NativeLong shout_t );

    /** Sets the server hostname or IP address. The default is <constant>localhost</constant>. */
    int shout_set_host( NativeLong shout_t, String host );

    /** Returns the server hostname or IP address. */
    String shout_get_host( NativeLong shout_t );

    /** Sets the server port. The default is <constant>8000</constant>. */
    int shout_set_port( NativeLong shout_t, short port );

    /** Returns the server port. */
    short shout_get_port( NativeLong shout_t );

    /**
     * Sets the user to authenticate as, for protocols that can use this parameter. The default is
     * <constant>source</constant>.
     */
    int shout_set_user( NativeLong shout_t, String user );

    /** Returns the user name. */
    String shout_get_user( NativeLong shout_t );

    /**
     * Sets the password to authenticate to the server with. This parameter <emphasis>must</emphasis> be set. There is
     * no default.
     */
    int shout_set_password( NativeLong shout_t, String password );

    /**
     * Returns the password.
     * 
     * @param shout_t
     * @return
     */
    String shout_get_password( NativeLong shout_t );

    /**
     * Set the protocol with which to connect to the server. Supported protocols are listed in <link
     * linkend="protocol_constants">Protocol Constants</link>. The default is <constant>SHOUT_PROTOCOL_HTTP</constant>
     * (compatible with Icecast 2).
     */
    int shout_set_protocol( NativeLong shout_t, int protocol );

    /** Returns the protocol used to connect to the server. */
    int shout_get_protocol( NativeLong shout_t );

    /**
     * Sets the audio format of this stream. The currently supported formats are listed in <link
     * linkend="format_constants">Format Constants</link>. The default is <constant>SHOUT_FORMAT_OGG</constant>.
     */
    int shout_set_format( NativeLong shout_t, int format );

    /** Returns the audio format used by this stream. */
    int shout_get_format( NativeLong shout_t );

    /**
     * Sets the mount point for this stream, for protocols that support this option
     * (<constant>SHOUT_PROTOCOL_ICY</constant> doesn't).
     */
    int shout_set_mount( NativeLong shout_t, String mount );

    /** Returns the stream mount point. */
    String shout_get_mount( NativeLong shout_t );

    /**
     * If the server supports it, you can request that your stream be archived on the server under the name
     * <varname>dumpfile</varname>. This can quickly eat a lot of disk space, so think twice before setting it.
     */
    int shout_set_dumpfile( NativeLong shout_t, String dumpfile );

    /** Returns the dump file, if specified. */
    String shout_get_dumpfile( NativeLong shout_t );

    /**
     * Sets the user agent header. This is <constant>libshout/VERSION</constant> by default. If you don't know what this
     * function is for, don't use it.
     */
    int shout_set_agent( NativeLong shout_t, String agent );

    /** Returns the user agent. */
    String shout_get_agent( NativeLong shout_t );

    /**
     * This function sets the TLS (Transport Layer Security) mode to use.<br>
     * mode is a <link linkend="tls_mode_constants">TLS mode. This is <constant>SHOUT_TLS_AUTO by default. To force TLS
     * on you should use SHOUT_TLS_AUTO_NO_PLAIN and SHOUT_TLS_DISABLED to force TLS off. While SHOUT_TLS_AUTO may
     * connect via TLS this is not a secure mode as everybody can do a man in the middle kind of attack and downgrade
     * the connection to plain.
     */
    int shout_set_tls( NativeLong shout_t, int mode );

    /** Returns the currently used TLS mode. */
    int shout_get_tls( NativeLong shout_t );

    /**
     * This sets the CA directory used by libshout to verify server certificates. Defaults to system defaults.
     */
    int shout_set_ca_directory( NativeLong shout_t, String directory );

    /** Returns the currently used CA directory. */
    String shout_get_ca_directory( NativeLong shout_t );

    /**
     * Sets a file with CA certificates used to verify server certificates. Defaults to system defaults. The file must
     * be in PEM format. </para> <tip>You can use this for self signed server certificates. In this case you point this
     * to the server certificate in PEM format. Keep in mind that this will allow self-signed certificates but other
     * checks such as hostname still needs to verify correctly.</tip>
     */
    int shout_set_ca_file( NativeLong shout_t, String file );

    /** Returns the currently used CA file. */
    String shout_get_ca_file( NativeLong shout_t );

    /**
     * This sets the list of currently allowed ciphers in OpenSSL format. Defaults to a list of ciphers considerd secure
     * as of day of release. </para> <caution>Setting this to a insecure list may render encryption and authentication
     * useless.</caution> <warning>Any application using this call must expose this list to the user. If the user can
     * not alter this list your application will harm security badly by preventing the user to get to a save value by
     * setting it manually or upgrading libshout. <emphasis>Do not use this call if you don't know what you are
     * doing.</emphasis></warning>
     */
    int shout_set_allowed_ciphers( NativeLong shout_t, String ciphers );

    /** Returns the currently used list of allowed ciphers. */
    String shout_get_allowed_ciphers( NativeLong shout_t );

    /**
     * This sets the client certificate to be used. Defaults to none. The file must be in PEM format and must contain
     * both the certificate as well as the private key for that certificate.
     */
    int shout_set_client_certificate( NativeLong shout_t, String certificate );

    /** Returns the currently used client certificate. */
    String shout_get_client_certificate( NativeLong shout_t );

    /**
     * Directory parameters<br>
     ******************** <br>
     * The following parameters are optional. They are used to control whether and how your stream will be listed in the
     * server's stream directory (if available).
     */

    /**
     * Setting this to 1 asks the server to list the stream in any directories it knows about. To suppress listing, set
     * this to 0. The default is 0.
     */
    int shout_set_public( NativeLong shout_t, int makepublic );

    /** Returns whether or not this stream is public. */
    int shout_get_public( NativeLong shout_t );

    /** This function sets the meta data for the stream. */
    int shout_set_meta( NativeLong shout_t, String name, String value );

    /** This function gets the meta data for the stream. */
    String shout_get_meta( NativeLong shout_t, String name );

    /**
     * Sets a stream audio parameter (eg bitrate, samplerate, channels or quality). The currently defined parameters are
     * listed in the <link linkend="audio_info_constants">Audio Info Constants</link> section, but you are free to add
     * additional fields if your directory server understands them.
     */
    int shout_set_audio_info( NativeLong shout_t, String name, String value );

    /** Returns the value of the audio info field <varname>name</varname>, if defined. */
    String shout_get_audio_info( NativeLong shout_t, String name );

    /**
     * Metadata<br>
     * These functions currently only make sense for MP3 streams. Vorbis streams are expected to embed metadata as
     * vorbis comments in the audio stream.
     */

    /**
     * Allocates a new metadata structure, or returns NULL if no memory is available. The returned structure should be
     * freed with shout_metadata_free when you are done with it.
     */
    NativeLong shout_metadata_new();

    /** Frees any resources associated with self. */
    void shout_metadata_free( NativeLong shout_metadata_t );

    /**
     * Add metadata value value to self, under the key name. You'll probably want to set name to "song", though "url"
     * may also be useful.<br>
     * Return Values</br> SHOUTERR_SUCCESS:The metadata was copied into self<br>
     * SHOUTERR_INSANE:self is not a valid shout_metadata_t object.<br>
     * SHOUTERR_MALLOC: Couldn't allocate enough memory to copy the metadata.
     */
    int shout_metadata_add( NativeLong shout_metadata_t, String name, String value );

    /**
     * Sets metadata on the connection <varname>self</varname> to <varname>metadata</varname>. Only MP3 streams support
     * this type of metadata update. You may use this function on defined but closed connections (this is useful if you
     * simply want to set the metadata for a stream provided by another process). <br>
     * Return Values<br>
     * SHOUTERR_SUCCESS:Metadata was updated successfully.<br>
     * SHOUTERR_INSANE: self and/or metadata is invalid.<br>
     * SHOUTERR_MALLOC:Couldn't allocate enough memory to complete the operation.<br>
     * SHOUTERR_NOCONNECT:The server refused the connection attempt.<br>
     * SHOUTERR_NOLOGIN: The server did not accept your authorization credentials.<br>
     * SHOUTERR_SOCKET: An error occuredtalking to the server.<br>
     * SHOUTERR_METADATA:The server returned any other error (eg bad mount point).
     */
    int shout_set_metadata( NativeLong shout_t, NativeLong shout_metadata_t );

}
