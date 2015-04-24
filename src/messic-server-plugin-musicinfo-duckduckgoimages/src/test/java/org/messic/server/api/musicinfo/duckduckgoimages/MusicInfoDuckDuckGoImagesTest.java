package org.messic.server.api.musicinfo.duckduckgoimages;

import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

import junit.framework.Assert;

import org.junit.Test;
import org.messic.server.api.plugin.MessicPlugin;

public class MusicInfoDuckDuckGoImagesTest
{

    @Test
    public void testGetName()
    {
        MusicInfoDuckDuckGoImages d = new MusicInfoDuckDuckGoImages();
        Assert.assertTrue( d.getName().equals( MusicInfoDuckDuckGoImages.NAME ) );
    }

    @Test
    public void testConfiguration()
    {
        Properties p = new Properties();
        p.put( MessicPlugin.CONFIG_PROXY_URL, "127.0.0.1" );
        p.put( MessicPlugin.CONFIG_PROXY_PORT, "3128" );

        MusicInfoDuckDuckGoImages d = new MusicInfoDuckDuckGoImages();
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

        MusicInfoDuckDuckGoImages d = new MusicInfoDuckDuckGoImages();
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
        MusicInfoDuckDuckGoImages i = new MusicInfoDuckDuckGoImages();
        String desc = i.getDescription( Locale.ENGLISH );
        Assert.assertTrue( desc.equals( MusicInfoDuckDuckGoImages.EN_DESCRIPTION ) );
    }

    @Test
    public void testConstructURL()
    {
        MusicInfoDuckDuckGoImages i = new MusicInfoDuckDuckGoImages();

        String surl = i.constructURL( new String[] { "test", "another test" } );

        System.out.println( surl );
        Assert.assertTrue( surl.trim().equals( "https://duckduckgo.com/i.js?o=json&q=\"test\" \"another test\"" ) );
    }

    @Test
    public void testGetProviderIcon()
    {
        MusicInfoDuckDuckGoImages i = new MusicInfoDuckDuckGoImages();
        Assert.assertTrue( i.getProviderIcon() != null );
        Assert.assertTrue( i.getProviderIcon().length > 0 );
    }

    @Test
    public void testGetProviderName()
    {
        MusicInfoDuckDuckGoImages i = new MusicInfoDuckDuckGoImages();
        Assert.assertTrue( i.getProviderName().equals( MusicInfoDuckDuckGoImages.PROVIDER_NAME ) );
    }

    @Test
    public void testSearch()
        throws IOException
    {
        MusicInfoDuckDuckGoImages i = new MusicInfoDuckDuckGoImages();
        String sbase = i.constructURL( new String[] { "test", "icon" } );
        String content = i.search( sbase );
        Assert.assertTrue( content.indexOf( ".jpg" ) > 0 );
    }

}
