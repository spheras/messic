package org.messic.server.api.tagwizard.freedb;

import java.util.Hashtable;

import org.apache.log4j.Logger;
import org.messic.server.api.tagwizard.service.TAGWizardPlugin;
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
        logger.info( "TAGWizard - FreeDB Started" );
        Hashtable<String, String> props = new Hashtable<String, String>();
        props.put( "TAGWizard", FreeDBTAGWizardPlugin.NAME );
        bundleContext.registerService( TAGWizardPlugin.class.getName(), new FreeDBTAGWizardPlugin(), props );
    }

    /**
     * Implements BundleActivator.stop()
     * 
     * @param bundleContext - the framework context for the bundle.
     **/
    public void stop( BundleContext bundleContext )
    {
        // NOTE: The service is automatically unregistered.
        logger.info( "TAGWizard - FreeDB Stopped" );
    }
}