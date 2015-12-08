package org.messic.server.api.radio.icecast2;

import java.util.Dictionary;
import java.util.Hashtable;

import org.junit.Test;
import org.messic.server.api.plugin.radio.MessicRadioPlugin;
import org.messic.server.api.radio.icecast2.Activator;
import org.messic.server.api.radio.icecast2.MessicRadioPluginIceCast2;
import org.mockito.Mockito;
import org.osgi.framework.BundleContext;

public class ActivatorTest
{

    @Test
    public void testStart()
    {

        //it seems that we cannot test the load of external libraries in the continuum environment
        //@TODO we need to investigate more about this
//        BundleContext bc = Mockito.mock( BundleContext.class );
//
//        Activator a = new Activator();
//        a.start( bc );
//
//        Dictionary<String, ?> props = new Hashtable<String, String>();
//
//        Mockito.verify( bc, Mockito.times( 1 ) ).registerService( Mockito.eq( MessicRadioPlugin.class.getName() ),
//                                                                  Mockito.any( MessicRadioPluginIceCast2.class ),
//                                                                  Mockito.any( props.getClass() ) );
    }

}
