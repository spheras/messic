package org.messic.server.api.tagwizard.discogs;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import junit.framework.Assert;

import org.junit.Test;
import org.messic.server.api.plugin.MessicPlugin;
import org.messic.server.api.tagwizard.service.Album;

public class DiscogsTAGWizardPluginTest
{

    @Test
    public void testGetName()
    {
        DiscogsTAGWizardPlugin d = new DiscogsTAGWizardPlugin();
        Assert.assertTrue( d.getName().equals( DiscogsTAGWizardPlugin.NAME ) );
    }

    @Test
    public void testConfiguration()
    {
        Properties p = new Properties();
        p.put( MessicPlugin.CONFIG_PROXY_URL, "127.0.0.1" );
        p.put( MessicPlugin.CONFIG_PROXY_PORT, "3128" );

        DiscogsTAGWizardPlugin d = new DiscogsTAGWizardPlugin();
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

        DiscogsTAGWizardPlugin d = new DiscogsTAGWizardPlugin();
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
        DiscogsTAGWizardPlugin i = new DiscogsTAGWizardPlugin();
        String desc = i.getDescription( Locale.ENGLISH );
        Assert.assertTrue( desc.equals( DiscogsTAGWizardPlugin.DESCRIPTION ) );
    }

    @Test
    public void testGetProviderName()
    {
        DiscogsTAGWizardPlugin i = new DiscogsTAGWizardPlugin();
        Assert.assertTrue( i.getName().equals( DiscogsTAGWizardPlugin.NAME ) );
    }

    @Test
    public void testGetAlbumInfo()
        throws IOException
    {
        DiscogsTAGWizardPlugin i = new DiscogsTAGWizardPlugin();
        Album album = new Album();
        album.author = "Boards of Canada";
        album.name = "Boc Maxima";
        List<Album> albums = i.getAlbumInfo( album, new File[] {} );
        Assert.assertTrue( albums.size() > 0 );
        System.out.println( "true" );
    }

}
