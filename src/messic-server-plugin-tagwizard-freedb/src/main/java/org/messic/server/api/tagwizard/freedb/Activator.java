/*
 * Copyright (C) 2013 José Amuedo
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