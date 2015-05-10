package org.messic.server.api.tagwizard.discogs;

import java.util.Dictionary;
import java.util.Hashtable;

import org.junit.Test;
import org.messic.server.api.tagwizard.service.TAGWizardPlugin;
import org.mockito.Mockito;
import org.osgi.framework.BundleContext;

public class ActivatorTest
{

    @Test
    public void testStart()
    {

        BundleContext bc = Mockito.mock( BundleContext.class );

        Activator a = new Activator();
        a.start( bc );

        Dictionary<String, ?> props = new Hashtable<String, String>();

        Mockito.verify( bc, Mockito.times( 1 ) ).registerService( Mockito.eq( TAGWizardPlugin.class.getName() ),
                                                                  Mockito.any( DiscogsTAGWizardPlugin.class ),
                                                                  Mockito.any( props.getClass() ) );
    }

}
