package org.messic.server.api.radio.icecast2;

import java.util.Locale;
import java.util.Properties;

import junit.framework.Assert;

import org.junit.Test;
import org.messic.server.api.plugin.MessicPlugin;
import org.messic.server.api.radio.icecast2.MessicRadioPluginIceCast2;

public class MessicRadioPluginIceCast2Test
{

    @Test
    public void testGetName()
    {
        MessicRadioPluginIceCast2 d = new MessicRadioPluginIceCast2();
        Assert.assertTrue( d.getName().equals( MessicRadioPluginIceCast2.NAME ) );
    }

    @Test
    public void testConfiguration()
    {
        Properties p = new Properties();
        p.put( MessicPlugin.CONFIG_PROXY_URL, "127.0.0.1" );
        p.put( MessicPlugin.CONFIG_PROXY_PORT, "3128" );

        MessicRadioPluginIceCast2 d = new MessicRadioPluginIceCast2();
        d.setConfiguration( p );
        Properties pp = d.getConfiguration();

        Assert.assertTrue( pp.getProperty( MessicPlugin.CONFIG_PROXY_URL ).equals( "127.0.0.1" ) );

        Assert.assertTrue( pp.getProperty( MessicPlugin.CONFIG_PROXY_PORT ).equals( "3128" ) );
    }

    @Test
    public void testProxy()
    {
        Properties p = new Properties();
        p.put( MessicPlugin.CONFIG_PROXY_URL, "127.0.0.1" );
        p.put( MessicPlugin.CONFIG_PROXY_PORT, "3128" );

        MessicRadioPluginIceCast2 d = new MessicRadioPluginIceCast2();
        d.setConfiguration( p );

        Assert.assertNotNull( d.getProxy() );
        d.setConfiguration( null );
        Assert.assertNull( d.getProxy() );
        d.setConfiguration( new Properties() );
        Assert.assertNull( d.getProxy() );
    }

    @Test
    public void testDescription()
    {
        MessicRadioPluginIceCast2 i = new MessicRadioPluginIceCast2();
        String desc = i.getDescription( Locale.ENGLISH );
        Assert.assertTrue( desc.equals( MessicRadioPluginIceCast2.EN_DESCRIPTION ) );
    }

}
