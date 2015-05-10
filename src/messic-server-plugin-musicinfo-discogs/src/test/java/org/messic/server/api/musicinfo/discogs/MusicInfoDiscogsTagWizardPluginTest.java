package org.messic.server.api.musicinfo.discogs;

import java.util.Locale;
import java.util.Properties;

import junit.framework.Assert;

import org.junit.Test;
import org.messic.server.api.plugin.MessicPlugin;

public class MusicInfoDiscogsTagWizardPluginTest
{

    @Test
    public void testGetName()
    {
        MusicInfoDiscogsTagWizardPlugin d = new MusicInfoDiscogsTagWizardPlugin();
        Assert.assertTrue( d.getName().equals( MusicInfoDiscogsTagWizardPlugin.NAME ) );
    }

    @Test
    public void testConfiguration()
    {
        Properties p = new Properties();
        p.put( MessicPlugin.CONFIG_PROXY_URL, "127.0.0.1" );
        p.put( MessicPlugin.CONFIG_PROXY_PORT, "3128" );

        MusicInfoDiscogsTagWizardPlugin d = new MusicInfoDiscogsTagWizardPlugin();
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

        MusicInfoDiscogsTagWizardPlugin d = new MusicInfoDiscogsTagWizardPlugin();
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
        MusicInfoDiscogsTagWizardPlugin i = new MusicInfoDiscogsTagWizardPlugin();
        String desc = i.getDescription( Locale.ENGLISH );
        Assert.assertTrue( desc.equals( MusicInfoDiscogsTagWizardPlugin.EN_DESCRIPTION ) );
    }

    @Test
    public void testGetProviderIcon()
    {
        MusicInfoDiscogsTagWizardPlugin i = new MusicInfoDiscogsTagWizardPlugin();
        Assert.assertTrue( i.getProviderIcon() != null );
        Assert.assertTrue( i.getProviderIcon().length > 0 );
    }

    @Test
    public void testGetProviderName()
    {
        MusicInfoDiscogsTagWizardPlugin i = new MusicInfoDiscogsTagWizardPlugin();
        Assert.assertTrue( i.getProviderName().equals( MusicInfoDiscogsTagWizardPlugin.PROVIDER_NAME ) );
    }

}
