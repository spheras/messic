package org.messic.server.api.radio.icecast2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.util.Locale;
import java.util.Properties;

import org.messic.server.api.plugin.MessicPlugin;
import org.messic.server.api.plugin.radio.MessicRadioPlugin;
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

    public static final String PARAMETER_MOUNT = "plugin-radio-icecast2-mount";

    private MessicRadioStatus status = MessicRadioStatus.NONE;

    /** configuration for the plugin */
    private Properties configuration;

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

    public void startCast()
        throws IOException
    {
        Libshout icecast = Libshout.getInstance();
        icecast.setHost( (String) getConfiguration().get( PARAMETER_HOST ) );
        icecast.setPort( Integer.valueOf( (String) getConfiguration().get( PARAMETER_PORT ) ) );
        icecast.setProtocol( Libshout.PROTOCOL_HTTP );
        icecast.setPassword( (String) getConfiguration().getProperty( PARAMETER_PASSWORD ) );
        icecast.setMount( (String) getConfiguration().getProperty( PARAMETER_MOUNT ) );
        icecast.setFormat( Libshout.FORMAT_MP3 );
        icecast.open();

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
                this.status = MessicRadioStatus.NOT_ENABLED;
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

    @Override
    public MessicRadioStatus getStatus()
    {
        return this.status;
    }

}
