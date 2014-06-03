package org.messic.server.api.dlna;

import javax.annotation.PostConstruct;

import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.registry.Registry;
import org.fourthline.cling.registry.RegistryListener;
import org.messic.server.api.dlna.chii2.mediaserver.upnp.MediaServerServiceImpl;
import org.messic.server.datamodel.dao.DAOSong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.stereotype.Component;

@Component
public class DLNAMain
    implements ApplicationListener<ApplicationContextEvent>
{
    @Autowired
    private DAOSong daosong;

    @Autowired
    private MusicService musicService;

    private boolean flagStarted = false;

    public DLNAMain()
    {

    }

    @PostConstruct
    public void startServer()
        throws InterruptedException
    {
        System.out.println( "nowww!" );

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
        System.out.println( "Starting Cling..." );
        MediaServerServiceImpl upnpService = new MediaServerServiceImpl();
        upnpService.init( listener, this.musicService );
        // UpnpService upnpService = new UpnpServiceImpl( listener );

        // Send a search message to all devices and services, they should
        // respond soon
        // upnpService.getControlPoint().search( new STAllHeader() );

        // Let's wait 10 seconds for them to respond
        System.out.println( "Waiting 10 seconds before shutting down..." );
        Thread.sleep( 10000000 );

        // Release all resources and advertise BYEBYE to other UPnP devices
        System.out.println( "Stopping Cling..." );
        upnpService.destroy();

    }

    @Override
    public void onApplicationEvent( ApplicationContextEvent arg0 )
    {

        ApplicationContext ac = arg0.getApplicationContext();
        synchronized ( this )
        {
            if ( ac != null && !flagStarted )
            {
                flagStarted = true;

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
        }
    }

}
