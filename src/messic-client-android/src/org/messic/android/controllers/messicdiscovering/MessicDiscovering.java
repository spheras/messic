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
package org.messic.android.controllers.messicdiscovering;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Locale;

import org.messic.android.controllers.SearchMessicServiceController.SearchListener;

import android.util.Log;

public class MessicDiscovering
{
    private static final String MESSIC_DISCOVER_MESSAGE = "<messic>discover</messic>";

    private static int PORT=8713;
    
    private boolean cancel = false;

    /**
     * Cancel the search
     */
    public void cancel()
    {
        this.cancel = true;
    }

    /**
     * Perform a broadcast message to the net in order to find messic services availables.
     * 
     * @param sl {@link SearchListener} to search events
     */
    public void searchMessicServices( SearchListener sl )
    {
        DatagramSocket c;
        // Find the server using UDP broadcast
        try
        {
            // Open a random port to send the package
            c = new DatagramSocket();
            c.setBroadcast( true );
            c.setSoTimeout( 5000 );

            byte[] sendData = MESSIC_DISCOVER_MESSAGE.getBytes();

            // Try the 255.255.255.255 first
            try
            {
                DatagramPacket sendPacket =
                    new DatagramPacket( sendData, sendData.length, InetAddress.getByName( "255.255.255.255" ), PORT );
                c.send( sendPacket );
                Log.d( "messic-discovering", "Request packet sent to: 255.255.255.255 (DEFAULT)" );
            }
            catch ( Exception e )
            {
            }

            // Broadcast the message over all the network interfaces
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while ( interfaces.hasMoreElements() )
            {
                NetworkInterface networkInterface = interfaces.nextElement();

                if ( networkInterface.isLoopback() || !networkInterface.isUp() )
                {
                    continue; // Don't want to broadcast to the loopback interface
                }

                for ( InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses() )
                {
                    InetAddress broadcast = interfaceAddress.getBroadcast();
                    if ( broadcast == null )
                    {
                        continue;
                    }

                    // Send the broadcast package!
                    try
                    {
                        DatagramPacket sendPacket = new DatagramPacket( sendData, sendData.length, broadcast, PORT );
                        c.send( sendPacket );
                    }
                    catch ( Exception e )
                    {
                    }

                    Log.d( "messic-discovering", "Request packet sent to: " + broadcast.getHostAddress()
                        + "; Interface: " + networkInterface.getDisplayName() );
                }
            }

            Log.d( "messic-discovering", "Done looping over all network interfaces. Now waiting for a reply!" );

            while ( !this.cancel )
            {
                // Wait for a response
                byte[] recvBuf = new byte[15000];
                DatagramPacket receivePacket = new DatagramPacket( recvBuf, recvBuf.length );
                try
                {
                    c.receive( receivePacket );
                    // We have a response
                    Log.d( "messic-discovering", "Broadcast response from server: "
                        + receivePacket.getAddress().getHostAddress() );

                    // Check if the message is correct
                    byte[] bmessage = receivePacket.getData();
                    String message = new String( bmessage, "UTF8" ).trim();
                    if ( message.toLowerCase( Locale.US ).contains( "messic" ) )
                    {
                        try
                        {
                            MessicDiscoveringXmlParser mdxp = new MessicDiscoveringXmlParser();
                            MessicServerInstance md = mdxp.parse( new ByteArrayInputStream( bmessage ) );
                            md.ip = receivePacket.getAddress().getCanonicalHostName();
                            // DO SOMETHING WITH THE SERVER'S IP (for example, store it in your controller)
                            // Controller_Base.setServerIp( receivePacket.getAddress() );
                            sl.messicServiceFound( md );
                        }
                        catch ( Exception e )
                        {
                            Log.e( "messic-discovering", e.getMessage(), e );
                        }
                    }
                }
                catch ( InterruptedIOException ii )
                {
                    // timeout?
                }
            }

            // Close the port!
            c.close();
        }
        catch ( IOException ex )
        {
            Log.e( "messic-discovering", ex.getMessage(), ex );
        }

    }
}
