package org.messic.server.api.dlna;

import org.fourthline.cling.UpnpService;
import org.fourthline.cling.model.message.IncomingDatagramMessage;
import org.fourthline.cling.model.message.StreamRequestMessage;
import org.fourthline.cling.protocol.ProtocolCreationException;
import org.fourthline.cling.protocol.ProtocolFactoryImpl;
import org.fourthline.cling.protocol.ReceivingAsync;
import org.fourthline.cling.protocol.ReceivingSync;

public class MessicProtocolFactoryImpl
    extends ProtocolFactoryImpl
{

    private StreamRequestMessage message;
    
    public StreamRequestMessage getMessage()
    {
        return message;
    }

    public void setMessage( StreamRequestMessage message )
    {
        this.message = message;
    }

    public MessicProtocolFactoryImpl( UpnpService upnpService )
    {
        super( upnpService );
    }

    public ReceivingSync createReceivingSync( StreamRequestMessage message )
        throws ProtocolCreationException
    {
        setMessage( message );
        return super.createReceivingSync( message );
    }


}
