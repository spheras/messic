package org.messic.server.api.radio.icecast2.libshout;

public enum LibShoutFormat
{

    /** The Ogg Format. Vorbis or any codec may be used. This is the default format. */
    SHOUT_FORMAT_OGG( 0 ), /* application/ogg */

    /** The MP3 format. */
    SHOUT_FORMAT_MP3( 1 ), /* audio/mpeg */

    /** The WebM format. */
    SHOUT_FORMAT_WEBM( 2 ), /* video/webm */

    /** The WebM format, audio only streams */
    SHOUT_FORMAT_WEBMAUDIO( 3 ), /* audio/webm audio only */

    /**
     * This is deprecated. It is an alias to <constant>SHOUT_FORMAT_OGG</constant>. Please migrate your code.
     */
    SHOUT_FORMAT_VORBIS( 0 );

    private final int value;

    LibShoutFormat( final int newValue )
    {
        value = newValue;
    }

    public int getValue()
    {
        return value;
    }

    public static LibShoutFormat from( int value )
    {
        for ( LibShoutFormat s : values() )
        {
            if ( s.value == value )
                return s;
        }
        return null;
    }
}
