package org.messic.server.api.radio.icecast2.libshout;

public enum LibShoutProtocol
{
    /**
     * The HTTP protocol. This is the native protocol of the Icecast 2 server, and is the default.
     */
    SHOUT_PROTOCOL_HTTP( 0 ),

    /**
     * The RoarAudio protocol. This is the native protocol for <application>RoarAudio</application> servers.
     */
    SHOUT_PROTOCOL_ROARAUDIO( 3 ),

    /**
     * The Audiocast format. This is the native protocol of <application>Icecast 1
     */
    SHOUT_PROTOCOL_XAUDIOCAST( 1 ),

    /**
     * The ShoutCast format. This is the native protocol of <application>ShoutCast</application>.
     */
    SHOUT_PROTOCOL_ICY( 2 );

    private final int value;

    LibShoutProtocol( final int newValue )
    {
        value = newValue;
    }

    public int getValue()
    {
        return value;
    }

    public static LibShoutProtocol from( int value )
    {
        for ( LibShoutProtocol s : values() )
        {
            if ( s.value == value )
                return s;
        }
        return null;
    }
}
