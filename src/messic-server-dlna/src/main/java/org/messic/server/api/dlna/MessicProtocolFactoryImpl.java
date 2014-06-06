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
