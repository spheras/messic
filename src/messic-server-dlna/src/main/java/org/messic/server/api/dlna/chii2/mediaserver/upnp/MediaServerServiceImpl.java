package org.messic.server.api.dlna.chii2.mediaserver.upnp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.fourthline.cling.DefaultUpnpServiceConfiguration;
import org.fourthline.cling.binding.LocalServiceBindingException;
import org.fourthline.cling.binding.annotations.AnnotationLocalServiceBinder;
import org.fourthline.cling.model.DefaultServiceManager;
import org.fourthline.cling.model.ValidationException;
import org.fourthline.cling.model.meta.DeviceDetails;
import org.fourthline.cling.model.meta.DeviceIdentity;
import org.fourthline.cling.model.meta.Icon;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.LocalService;
import org.fourthline.cling.model.meta.ManufacturerDetails;
import org.fourthline.cling.model.meta.ModelDetails;
import org.fourthline.cling.model.profile.HeaderDeviceDetailsProvider;
import org.fourthline.cling.model.types.DLNACaps;
import org.fourthline.cling.model.types.DLNADoc;
import org.fourthline.cling.model.types.DeviceType;
import org.fourthline.cling.model.types.UDADeviceType;
import org.fourthline.cling.model.types.UDN;
import org.fourthline.cling.registry.RegistryListener;
import org.fourthline.cling.support.connectionmanager.ConnectionManagerService;
import org.fourthline.cling.transport.impl.apache.StreamClientConfigurationImpl;
import org.fourthline.cling.transport.impl.apache.StreamClientImpl;
import org.fourthline.cling.transport.impl.apache.StreamServerConfigurationImpl;
import org.fourthline.cling.transport.impl.apache.StreamServerImpl;
import org.fourthline.cling.transport.spi.NetworkAddressFactory;
import org.fourthline.cling.transport.spi.StreamClient;
import org.fourthline.cling.transport.spi.StreamServer;
import org.messic.server.api.dlna.MessicMediaLibraryService;
import org.messic.server.api.dlna.MessicUpnpServiceImpl;
import org.messic.server.api.dlna.MusicService;
import org.messic.server.api.dlna.chii2.medialibrary.api.core.MediaLibraryService;
import org.messic.server.api.dlna.chii2.mediaserver.api.http.HttpServerService;
import org.messic.server.api.dlna.chii2.transcoder.api.core.TranscoderService;
import org.messic.server.api.dlna.chii2.transcoder.core.TranscoderServiceImpl;

/**
 * UPnP/DLNA Media Server
 */
public class MediaServerServiceImpl
{
    // UDN
    private final UDN udn = UDN.uniqueSystemIdentifier( "Messic MediaServer v1" );

    // Server Prefix
    private String serverPrefix = "Home Server";

    // UPnP Server
    private MessicUpnpServiceImpl upnpService = null;

    // Logger
    private Logger logger = Logger.getLogger( "org.chii2.mediaserver.upnp" );

    // Library
    private MediaLibraryService mediaLibrary;

    // HTTP Server
    private HttpServerService httpService;

    // Transcoder
    private TranscoderService transcoder;

    /**
     * Life Cycle Init
     */
    @SuppressWarnings( "unused" )
    public void init( RegistryListener listener, MusicService musicService )
    {
        logger.info( "Messic Media Server MediaServerService (Core) init." );

        // Set Server Prefix to Host Name
        try
        {
            String hostName = InetAddress.getLocalHost().getHostName().trim();
            if ( hostName != null && hostName.length() > 0 )
            {
                serverPrefix = hostName;
            }
        }
        catch ( UnknownHostException ignore )
        {
        }

        try
        {
            DefaultUpnpServiceConfiguration configuration = new DefaultUpnpServiceConfiguration( 8895 )
            {
                // Override using Apache Http instead of sun http
                @Override
                public StreamClient<StreamClientConfigurationImpl> createStreamClient()
                {
                    int corePoolSize = 5;
                    int maxPoolSize = 10;
                    long keepAliveTime = 5000;

                    ExecutorService threadPoolExecutor =
                        new ThreadPoolExecutor( corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.MILLISECONDS,
                                                new LinkedBlockingQueue<Runnable>() );
                    return new StreamClientImpl( new StreamClientConfigurationImpl( threadPoolExecutor ) );
                }

                @Override
                public StreamServer createStreamServer( NetworkAddressFactory networkAddressFactory )
                {
                    return new StreamServerImpl(
                                                 new StreamServerConfigurationImpl(
                                                                                    networkAddressFactory.getStreamListenPort() ) );
                }
            };
            // Init UPnP stack
            upnpService = new MessicUpnpServiceImpl( configuration, listener );
            // Attach service and device
            upnpService.getRegistry().addDevice( createUPnPDevice( musicService ) );
        }
        catch ( Exception e )
        {
            logger.error( "failed!", e );

            logger.info( "Messic MediaServerService init with exception: {}." + e.getMessage() );
        }
    }

    /**
     * Life Cycle Destroy
     */
    @SuppressWarnings( "unused" )
    public void destroy()
    {
        logger.info( "Chii2 Media Server MediaServerService (Core) destroy." );

        try
        {
            // Shutdown UPnP stack
            if ( upnpService != null )
            {
                upnpService.shutdown();
            }
        }
        catch ( Exception e )
        {
            logger.error( "failed!", e );
            logger.info( "Messic MediaServerService destory with exception: {}." + e.getMessage() );
        }
    }

    /**
     * Create UPnP Device
     * 
     * @return UPnP Device
     * @throws ValidationException thrown when the device graph being instantiated is invalid
     * @throws LocalServiceBindingException thrown when something is wrong with annotation metadata on service
     *             implementation class
     * @throws IOException
     */
    public LocalDevice createUPnPDevice( final MusicService musicService )
        throws ValidationException, LocalServiceBindingException, IOException
    {
        // Device Type
        DeviceType type = new UDADeviceType( "MediaServer", 1 );

        // Windows Media Player Device Details
        DeviceDetails wmpDetails =
            new DeviceDetails(
                               serverPrefix + ": Messic",
                               new ManufacturerDetails( "Chii2", "http://www.messic.org/" ),
                               new ModelDetails( "Windows Media Player Sharing", "Windows Media Player Sharing", "12.0" ),
                               "000da201238c", "100000000001", "http://www.chii2.org/some_user_interface/",
                               new DLNADoc[] { new DLNADoc( "DMS", DLNADoc.Version.V1_5 ), },
                               new DLNACaps( new String[] { "audio-upload" } ) );

        // Common Details
        DeviceDetails chii2Details =
            new DeviceDetails( serverPrefix + ": Messic",
                               new ManufacturerDetails( "Messic", "http://www.messic.org/" ),
                               new ModelDetails( "Messic Home Server", "Messic Home Server", "1" ), "000da201238c",
                               "100000000001", "http://www.messic.org/some_user_interface/",
                               new DLNADoc[] { new DLNADoc( "DMS", DLNADoc.Version.V1_5 ), },
                               new DLNACaps( new String[] { "audio-upload" } ) );

        // Device Details Provider
        Map<HeaderDeviceDetailsProvider.Key, DeviceDetails> headerDetails =
            new HashMap<HeaderDeviceDetailsProvider.Key, DeviceDetails>();
        headerDetails.put( new HeaderDeviceDetailsProvider.Key( "User-Agent", "FDSSDP" ), wmpDetails );
        headerDetails.put( new HeaderDeviceDetailsProvider.Key( "User-Agent", "Xbox.*" ), wmpDetails );
        headerDetails.put( new HeaderDeviceDetailsProvider.Key( "X-AV-Client-Info", ".*PLAYSTATION 3.*" ), chii2Details );
        HeaderDeviceDetailsProvider provider = new HeaderDeviceDetailsProvider( chii2Details, headerDetails );

        // Content Directory Service
        @SuppressWarnings( "unchecked" )
        LocalService<ContentDirectory> contentDirectory =
            new AnnotationLocalServiceBinder().read( ContentDirectory.class );
        contentDirectory.setManager( new DefaultServiceManager<ContentDirectory>( contentDirectory, null )
        {
            @Override
            protected ContentDirectory createServiceInstance()
                throws Exception
            {
                return new ContentDirectory( upnpService, new MessicMediaLibraryService(), null,
                                             new TranscoderServiceImpl(), musicService );
            }
        } );

        // Connection Manager Service
        @SuppressWarnings( "unchecked" )
        LocalService<ConnectionManagerService> connectionManager =
            new AnnotationLocalServiceBinder().read( ConnectionManagerService.class );
        connectionManager.setManager( new DefaultServiceManager<ConnectionManagerService>(
                                                                                           connectionManager,
                                                                                           ConnectionManagerService.class ) );

        // Media Receiver Registrar Service
        @SuppressWarnings( "unchecked" )
        LocalService<MediaReceiverRegistrar> mediaReceiverRegistrar =
            new AnnotationLocalServiceBinder().read( MediaReceiverRegistrar.class );
        mediaReceiverRegistrar.setManager( new DefaultServiceManager<MediaReceiverRegistrar>(
                                                                                              mediaReceiverRegistrar,
                                                                                              MediaReceiverRegistrar.class ) );

        // Creeate/Start UPnP/DLNA Device
        return new LocalDevice( new DeviceIdentity( udn ), type, provider, createDefaultDeviceIcon(),
                                new LocalService[] { connectionManager, contentDirectory, mediaReceiverRegistrar } );
    }

    protected Icon createDefaultDeviceIcon()
        throws IOException
    {
        return new Icon( "image/png", 48, 48, 24, "", new byte[] { 0, 0, 0 } );
    }

}