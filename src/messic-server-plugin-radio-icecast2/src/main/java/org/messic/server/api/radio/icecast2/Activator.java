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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Hashtable;

import org.apache.log4j.Logger;
import org.messic.server.api.plugin.radio.MessicRadioPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.BundleListener;
import org.osgi.framework.Filter;
import org.osgi.framework.FrameworkListener;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

import com.gmail.kunicins.olegs.libshout.Libshout;

public class Activator
    implements BundleActivator
{
    private static Logger logger = Logger.getLogger( Activator.class );

    public static void main( String[] args )
    {
        Activator a = new Activator();
        a.start( new BundleContext()
        {

            @Override
            public boolean ungetService( ServiceReference<?> reference )
            {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public void removeServiceListener( ServiceListener listener )
            {
                // TODO Auto-generated method stub

            }

            @Override
            public void removeFrameworkListener( FrameworkListener listener )
            {
                // TODO Auto-generated method stub

            }

            @Override
            public void removeBundleListener( BundleListener listener )
            {
                // TODO Auto-generated method stub

            }

            @Override
            public <S> ServiceRegistration<S> registerService( Class<S> clazz, S service,
                                                               Dictionary<String, ?> properties )
            {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public ServiceRegistration<?> registerService( String clazz, Object service,
                                                           Dictionary<String, ?> properties )
            {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public ServiceRegistration<?> registerService( String[] clazzes, Object service,
                                                           Dictionary<String, ?> properties )
            {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Bundle installBundle( String location, InputStream input )
                throws BundleException
            {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Bundle installBundle( String location )
                throws BundleException
            {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public <S> Collection<ServiceReference<S>> getServiceReferences( Class<S> clazz, String filter )
                throws InvalidSyntaxException
            {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public ServiceReference<?>[] getServiceReferences( String clazz, String filter )
                throws InvalidSyntaxException
            {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public <S> ServiceReference<S> getServiceReference( Class<S> clazz )
            {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public ServiceReference<?> getServiceReference( String clazz )
            {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public <S> S getService( ServiceReference<S> reference )
            {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public String getProperty( String key )
            {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public File getDataFile( String filename )
            {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Bundle[] getBundles()
            {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Bundle getBundle( String location )
            {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Bundle getBundle( long id )
            {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Bundle getBundle()
            {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public ServiceReference<?>[] getAllServiceReferences( String clazz, String filter )
                throws InvalidSyntaxException
            {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Filter createFilter( String filter )
                throws InvalidSyntaxException
            {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void addServiceListener( ServiceListener listener, String filter )
                throws InvalidSyntaxException
            {
                // TODO Auto-generated method stub

            }

            @Override
            public void addServiceListener( ServiceListener listener )
            {
                // TODO Auto-generated method stub

            }

            @Override
            public void addFrameworkListener( FrameworkListener listener )
            {
                // TODO Auto-generated method stub

            }

            @Override
            public void addBundleListener( BundleListener listener )
            {
                // TODO Auto-generated method stub

            }
        } );
    }

    /**
     * @param bundleContext - the framework context for the bundle.
     **/
    public void start( BundleContext bundleContext )
    {

        if ( OSValidator.isUnix() )
        {

            try
            {
                // let's install the .so libraries
                String soPath = "/org/messic/server/api/radio/icecast2/libshout-java.64.so";
                if ( !System.getProperty( "sun.arch.data.model" ).equalsIgnoreCase( "64" ) )
                {
                    soPath = "/org/messic/server/api/radio/icecast2/libshout-java.so";
                }
                InputStream is = Activator.class.getResourceAsStream( soPath );
                File flibfolder = new File( Libshout.LOCAL_LIB_FOLDER );
                flibfolder.mkdirs();
                FileOutputStream fos = new FileOutputStream( flibfolder.getAbsolutePath() + "/libshout-java.so" );
                byte[] buff = new byte[1024];
                int read = is.read( buff );
                while ( read > 0 )
                {
                    fos.write( buff, 0, read );
                    read = is.read( buff );
                }
                fos.close();
                is.close();

                Libshout libshout = Libshout.getInstance();
                String sversion = libshout.getVersion();
                if ( !sversion.equals( "2.3.1" ) )
                {
                    throw new IOException( "Error loading library!?" );
                }

                logger.info( "Radio - Icecast2 Started" );
                Hashtable<String, String> props = new Hashtable<String, String>();
                props.put( MessicRadioPlugin.MESSIC_RADIO_PLUGIN_NAME, MessicRadioPluginIceCast2.NAME );
                bundleContext.registerService( MessicRadioPlugin.class.getName(), new MessicRadioPluginIceCast2(),
                                               props );

            }
            catch ( IOException ioe )
            {
                ioe.printStackTrace();
                // we can work only on linux
                logger.info( "Radio - Icecast2 plugin ERROR while loading" );

            }

        }
        else
        {
            // we can work only on linux
            logger.info( "Radio - Icecast2 plugin CAN'T start on this OS" );
        }

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