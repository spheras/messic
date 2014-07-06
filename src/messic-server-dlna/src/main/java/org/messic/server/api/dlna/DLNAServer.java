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
package org.messic.server.api.dlna;

import javax.annotation.PostConstruct;

import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.registry.Registry;
import org.fourthline.cling.registry.RegistryListener;
import org.messic.server.api.dlna.chii2.mediaserver.upnp.MediaServerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.stereotype.Component;

@Component
public class DLNAServer
    implements ApplicationListener<ApplicationContextEvent>
{
    @Autowired
    private MusicService musicService;

    /** upnp service */
    private MediaServerServiceImpl upnpService;

    /* flag to know if the server has been started */
    private boolean flagStarted = false;

    /** flag to know if messic event have been received already or not */
    private boolean flagMessicInited = false;

    /**
     * Starts or Stops the server depending on the Messic Preferences and Users preferences. This method should be
     * called each time Messic Preferences are changed, or Users preferences are changed, or New Users become to Messic,
     * or Users are deleted from Messic
     * 
     * @throws InterruptedException
     */
    public void refreshServer()
    {
        if ( this.musicService.isGenericAllowed() )
        {
            if ( this.musicService.isAnyUserAllowed() )
            {
                if ( !this.flagStarted )
                {
                    try
                    {
                        Thread t = new Thread()
                        {
                            public void run()
                            {
                                try
                                {
                                    startServer();
                                }
                                catch ( InterruptedException e )
                                {
                                    e.printStackTrace();
                                }
                            };
                        };
                        t.start();

                    }
                    catch ( Exception e )
                    {
                        e.printStackTrace();
                    }
                    return;
                }
                else
                {
                    return;
                }
            }
        }

        if ( this.flagStarted )
        {
            stopServer();
        }
    }

    /**
     * Stops the UPNP Server
     */
    public void stopServer()
    {
        this.flagStarted = false;
        System.out.println( "Stopping Cling..." );
        upnpService.destroy();
    }

    /**
     * Launched when INIT messic
     * 
     * @throws InterruptedException
     */
    @PostConstruct
    public void InitMessic()
        throws InterruptedException
    {
        for ( int i = 0; i < 100; i++ )
            System.out.println( "SÑFLASLDJFSAÑDFJASDFJKLASDÑFASD JFJASDÑL FJASDÑL FJSDÑKL FJSDÑKL FJASDÑL FJASÑL FJ" );

        refreshServer();
    }

    /**
     * Starts the UPNP Server
     */
    public void startServer()
        throws InterruptedException
    {
        this.flagStarted = true;

        // UPnP discovery is asynchronous, we need a callback
        RegistryListener listener = new RegistryListener()
        {

            public void remoteDeviceDiscoveryStarted( Registry registry, RemoteDevice device )
            {
                System.out.println( "Discovery started: " + device.getDisplayString() );
            }

            public void remoteDeviceDiscoveryFailed( Registry registry, RemoteDevice device, Exception ex )
            {
                System.out.println( "Discovery failed: " + device.getDisplayString() + " => " + ex );
            }

            public void remoteDeviceAdded( Registry registry, RemoteDevice device )
            {
                System.out.println( "Remote device available: " + device.getDisplayString() );
            }

            public void remoteDeviceUpdated( Registry registry, RemoteDevice device )
            {
                System.out.println( "Remote device updated: " + device.getDisplayString() );
            }

            public void remoteDeviceRemoved( Registry registry, RemoteDevice device )
            {
                System.out.println( "Remote device removed: " + device.getDisplayString() );
            }

            public void localDeviceAdded( Registry registry, LocalDevice device )
            {
                System.out.println( "Local device added: " + device.getDisplayString() );
            }

            public void localDeviceRemoved( Registry registry, LocalDevice device )
            {
                System.out.println( "Local device removed: " + device.getDisplayString() );
            }

            public void beforeShutdown( Registry registry )
            {
                System.out.println( "Before shutdown, the registry has devices: " + registry.getDevices().size() );
            }

            public void afterShutdown()
            {
                System.out.println( "Shutdown of registry complete!" );

            }
        };

        // This will create necessary network resources for UPnP right away
        System.out.println( "Starting Cling UPNP Server..." );
        this.upnpService = new MediaServerServiceImpl();
        this.upnpService.init( listener, this.musicService );
    }

    @Override
    public void onApplicationEvent( ApplicationContextEvent arg0 )
    {

        ApplicationContext ac = arg0.getApplicationContext();
        synchronized ( this )
        {
            if ( ac != null && !this.flagMessicInited )
            {
                this.flagMessicInited = true;
                refreshServer();
            }
        }
    }

}
