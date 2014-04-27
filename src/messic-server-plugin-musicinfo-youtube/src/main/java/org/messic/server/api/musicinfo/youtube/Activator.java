package org.messic.server.api.musicinfo.youtube;

import java.util.Hashtable;

import org.apache.log4j.Logger;
import org.messic.server.api.musicinfo.service.MusicInfoPlugin;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator
    implements BundleActivator
{
    private static Logger logger = Logger.getLogger( Activator.class );

    /**
     * @param bundleContext - the framework context for the bundle.
     **/
    public void start( BundleContext bundleContext )
    {
        logger.info( "MusicInfo - Youtube Started" );
        Hashtable<String, String> props = new Hashtable<String, String>();
        props.put( MusicInfoPlugin.MUSIC_INFO_PLUGIN_NAME, MusicInfoYoutubePlugin.NAME );
        bundleContext.registerService( MusicInfoPlugin.class.getName(), new MusicInfoYoutubePlugin(), props );
    }

    /**
     * Implements BundleActivator.stop()
     * 
     * @param bundleContext - the framework context for the bundle.
     **/
    public void stop( BundleContext bundleContext )
    {
        // NOTE: The service is automatically unregistered.
        logger.info( "MusicInfo - Youtube Stopped" );
    }
}