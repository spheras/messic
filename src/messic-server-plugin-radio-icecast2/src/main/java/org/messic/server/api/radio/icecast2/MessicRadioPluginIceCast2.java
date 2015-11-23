package org.messic.server.api.radio.icecast2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.util.Locale;
import java.util.Properties;

import org.messic.server.api.plugin.MessicPlugin;
import org.messic.server.api.plugin.radio.MessicRadioInfo;
import org.messic.server.api.plugin.radio.MessicRadioPlugin;
import org.messic.server.api.plugin.radio.MessicRadioSong;
import org.messic.server.api.plugin.radio.MessicRadioStatus;

import com.gmail.kunicins.olegs.libshout.Libshout;

public class MessicRadioPluginIceCast2
    implements MessicRadioPlugin
{
    public static final String NAME = "ICECAST2";

    public static final String EN_DESCRIPTION = "Plugin to stream music through an icecast2 server.";

    public static final float VERSION = 1.0f;

    public static final float MINIMUM_MESSIC_VERSION = 1.0f;

    public static final String PARAMETER_ENABLE = "plugin-radio-icecast2";

    public static final String PARAMETER_HOST = "plugin-radio-icecast2-host";

    public static final String PARAMETER_PORT = "plugin-radio-icecast2-port";

    public static final String PARAMETER_PASSWORD = "plugin-radio-icecast2-password";

    public static final String PARAMETER_USER = "plugin-radio-icecast2-user";

    public static final String PARAMETER_MOUNT = "plugin-radio-icecast2-mount";

    private MessicRadioInfo info = new MessicRadioInfo();

    private String host;

    private int port;

    private String mount;

    /** configuration for the plugin */
    private Properties configuration;

    private MRPCastThread thread = null;

    protected Proxy getProxy()
    {
        if ( this.configuration != null )
        {
            String url = (String) this.configuration.get( MessicPlugin.CONFIG_PROXY_URL );
            String port = (String) this.configuration.get( MessicPlugin.CONFIG_PROXY_PORT );
            if ( url != null && port != null && url.length() > 0 && port.length() > 0 )
            {
                SocketAddress addr = new InetSocketAddress( url, Integer.valueOf( port ) );
                Proxy proxy = new Proxy( Proxy.Type.HTTP, addr );
                return proxy;
            }
        }
        return null;
    }

    private void checkConnection()
    {
        if ( isEnabled() )
        {
            if ( isStarted() )
            {
                Libshout icecast;
                try
                {
                    icecast = Libshout.getInstance();
                    if ( !icecast.isConnected() )
                    {
                        this.info.status = MessicRadioStatus.NOT_STARTED;
                    }
                }
                catch ( IOException e )
                {
                    e.printStackTrace();
                    this.info.status = MessicRadioStatus.NOT_STARTED;
                }

            }
        }
    }

    /**
     * Flag to know if it is the first time we try to connect with the server
     */
    private static boolean flagFirstTime = true;

    @Override
    public synchronized void startCast()
    {
        checkConnection();

        if ( isEnabled() && !isStarted() )
        {
            try
            {
                if ( !flagFirstTime )
                {
                    Libshout icecast = Libshout.getInstance();
                    icecast.close();
                    if ( thread != null )
                    {
                        thread.stopCast();
                    }
                    icecast = Libshout.getInstance( true );
                }
                else
                {
                    flagFirstTime = false;
                }

                Libshout icecast = Libshout.getInstance();
                host = (String) getConfiguration().get( PARAMETER_HOST );
                port = Integer.valueOf( (String) getConfiguration().get( PARAMETER_PORT ) );
                mount = (String) getConfiguration().getProperty( PARAMETER_MOUNT );

                icecast.setHost( host );
                icecast.setPort( port );
                icecast.setProtocol( Libshout.PROTOCOL_HTTP );
                icecast.setUser( (String) getConfiguration().getProperty( PARAMETER_USER ) );
                icecast.setPassword( (String) getConfiguration().getProperty( PARAMETER_PASSWORD ) );
                icecast.setMount( mount );
                icecast.setFormat( Libshout.FORMAT_MP3 );

                // icecast.setDescription( currentFile.albumComments );
                // icecast.setGenre( currentFile.albumGenre );
                // icecast.setInfo( "testkey", "testvalue" );
                // icecast.setInfo( "testkey2", "testvalue2" );
                // icecast.setMeta( "meta1", "value1" );
                // icecast.setMeta( "meta2", "value2" );
                // icecast.setName( currentFile.songName );
                // icecast.setUrl( "http://wwww.messic.com/test" );

                icecast.open();

                // we start the thread to send music to the cast server
                thread = new MRPCastThread();
                thread.start();

                // the status is started
                this.info.status = MessicRadioStatus.STARTED;
                this.info.radioURL = "http://" + host + ":" + port + mount;

            }
            catch ( Exception e )
            {
                e.printStackTrace();
                this.info.status = MessicRadioStatus.NOT_AVAILABLE;
            }

        }
    }

    public synchronized void castSong( MessicRadioSong mp3song )
        throws IOException
    {
        if ( isEnabled() && isStarted() )
        {
            thread.setNextFile( mp3song );
        }
    }

    public synchronized void stopCast()
    {
        if ( isEnabled() && isStarted() )
        {
            try
            {
                thread.stopCast();
                Thread.sleep( 1000 );
            }
            catch ( InterruptedException e )
            {
                e.printStackTrace();
            }
            this.info.status = MessicRadioStatus.STARTED;

            try
            {
                Libshout.getInstance().close();
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }
            this.info.status = MessicRadioStatus.NOT_STARTED;

        }
    }

    @Override
    public float getVersion()
    {
        return VERSION;
    }

    @Override
    public float getMinimumMessicVersion()
    {
        return MINIMUM_MESSIC_VERSION;
    }

    @Override
    public String getName()
    {
        return NAME;
    }

    @Override
    public String getDescription( Locale locale )
    {
        return EN_DESCRIPTION;
    }

    @Override
    public Properties getConfiguration()
    {
        return this.configuration;
    }

    @Override
    public void setConfiguration( Properties properties )
    {
        this.configuration = properties;

        if ( this.configuration != null )
        {
            if ( !isEnabled() )
            {
                this.info.status = MessicRadioStatus.NOT_ENABLED;
            }
        }
    }

    private boolean isEnabled()
    {
        String senable = (String) getConfiguration().get( PARAMETER_ENABLE );
        if ( senable != null )
        {
            return Boolean.valueOf( senable );
        }
        else
        {
            return false;
        }

    }

    public boolean isStarted()
    {
        return this.info.status == MessicRadioStatus.STARTED;
    }

    @Override
    public MessicRadioStatus getStatus()
    {
        if ( this.info.status == MessicRadioStatus.NONE )
        {
            if ( isEnabled() )
            {
                this.info.status = MessicRadioStatus.ENABLED;
            }
            else
            {
                this.info.status = MessicRadioStatus.NOT_ENABLED;
            }
        }
        return this.info.status;
    }

    @Override
    public MessicRadioInfo getInfo()
    {
        return this.info;
    }
}
