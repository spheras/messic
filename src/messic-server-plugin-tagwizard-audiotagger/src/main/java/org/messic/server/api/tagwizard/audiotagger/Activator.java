package org.messic.server.api.tagwizard.audiotagger;

import java.util.Hashtable;

import org.messic.server.api.tagwizard.service.TAGWizardPlugin;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator
    implements BundleActivator
{

    /**
     * Bundle-Classpath: WEB-INF/lib/messic-server-osgiservices-1.0-SNAPSHOT.jar
     * Implements BundleActivator.start().
     * 
     * @param bundleContext - the framework context for the bundle.
     **/
    public void start( BundleContext bundleContext )
    {
        System.out.println( "TAGWizard - AudioTagger Started" );
        Hashtable<String, String> props = new Hashtable<String, String>();
        props.put("Language", "English");
        bundleContext.registerService(TAGWizardPlugin.class.getName(), new AudioTaggerTAGWizardPlugin(), props);
    }

    /**
     * Implements BundleActivator.stop()
     * 
     * @param bundleContext - the framework context for the bundle.
     **/
    public void stop( BundleContext bundleContext )
    {
        // NOTE: The service is automatically unregistered.
        System.out.println( "TAGWizard - AudioTagger Stopped" );
    }
}