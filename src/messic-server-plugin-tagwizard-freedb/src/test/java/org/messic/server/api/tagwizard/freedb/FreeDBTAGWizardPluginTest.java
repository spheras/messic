package org.messic.server.api.tagwizard.freedb;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import junit.framework.Assert;

import org.junit.Test;
import org.messic.server.api.plugin.MessicPlugin;
import org.messic.server.api.tagwizard.service.Album;

public class FreeDBTAGWizardPluginTest
{

    @Test
    public void testGetName()
    {
        FreeDBTAGWizardPlugin d = new FreeDBTAGWizardPlugin();
        Assert.assertTrue( d.getName().equals( FreeDBTAGWizardPlugin.NAME ) );
    }

    @Test
    public void testConfiguration()
    {
        Properties p = new Properties();
        p.put( MessicPlugin.CONFIG_PROXY_URL, "127.0.0.1" );
        p.put( MessicPlugin.CONFIG_PROXY_PORT, "3128" );

        FreeDBTAGWizardPlugin d = new FreeDBTAGWizardPlugin();
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

        FreeDBTAGWizardPlugin d = new FreeDBTAGWizardPlugin();
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
        FreeDBTAGWizardPlugin i = new FreeDBTAGWizardPlugin();
        String desc = i.getDescription( Locale.ENGLISH );
        Assert.assertTrue( desc.equals( FreeDBTAGWizardPlugin.DESCRIPTION ) );
    }

    @Test
    public void testGetProviderName()
    {
        FreeDBTAGWizardPlugin i = new FreeDBTAGWizardPlugin();
        Assert.assertTrue( i.getName().equals( FreeDBTAGWizardPlugin.NAME ) );
    }

    @Test
    public void testGetAlbumInfo()
        throws IOException
    {
        FreeDBTAGWizardPlugin i = new FreeDBTAGWizardPlugin();
        Album album = new Album();
        album.author = "Boards of Canada";
        album.name = "Boc Maxima";
        List<Album> albums = i.getAlbumInfo( album, new File[] {} );
        Assert.assertTrue( albums.size() > 0 );
        System.out.println( "true" );
    }

}
