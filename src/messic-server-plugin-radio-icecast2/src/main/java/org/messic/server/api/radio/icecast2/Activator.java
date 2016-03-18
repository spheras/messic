/*
 * Copyright (C) 2013
 *
 *  This file is part of Messic.
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.messic.server.api.radio.icecast2;

import java.util.Hashtable;

import org.apache.log4j.Logger;
import org.messic.server.api.plugin.radio.MessicRadioPlugin;
import org.messic.server.api.radio.icecast2.libshout.LibShout;
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
        String sversion = LibShout.get().getVersion();
        if ( !sversion.equals( "2.3.1" ) )
        {
            logger.warn( "icecast2 version " + sversion );
        }
        logger.info( "Radio - Icecast2 Started" );

        Hashtable<String, String> props = new Hashtable<String, String>();
        props.put( MessicRadioPlugin.MESSIC_RADIO_PLUGIN_NAME, MessicRadioPluginIceCast2.NAME );
        bundleContext.registerService( MessicRadioPlugin.class.getName(), new MessicRadioPluginIceCast2(), props );

    }

    /**
     * Implements BundleActivator.stop()
     * 
     * @param bundleContext - the framework context for the bundle.
     **/
    public void stop( BundleContext bundleContext )
    {
        // NOTE: The service is automatically unregistered.
        logger.info( "MessicRadio - Icecast2 Stopped" );
    }
}