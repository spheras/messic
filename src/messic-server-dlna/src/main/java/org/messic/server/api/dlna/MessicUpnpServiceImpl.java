package org.messic.server.api.dlna;

import org.fourthline.cling.UpnpServiceConfiguration;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.protocol.ProtocolFactory;
import org.fourthline.cling.registry.RegistryListener;

public class MessicUpnpServiceImpl
    extends UpnpServiceImpl
{
    public MessicUpnpServiceImpl( UpnpServiceConfiguration configuration, RegistryListener... registryListeners )
    {
        super( configuration, registryListeners );
    }

    @Override
    protected ProtocolFactory createProtocolFactory()
    {
        return new MessicProtocolFactoryImpl( this );
    }

    
}
