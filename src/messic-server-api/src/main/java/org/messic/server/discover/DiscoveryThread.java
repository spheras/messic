package org.messic.server.discover;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.apache.log4j.Logger;
import org.messic.configuration.MessicConfig;
import org.messic.configuration.MessicConfig.CurrentPort;
import org.messic.server.datamodel.dao.DAOMessicSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.stereotype.Component;

@Component
public class DiscoveryThread
    implements Runnable, ApplicationListener<ApplicationContextEvent>
{
    private Logger log = Logger.getLogger( DiscoveryThread.class );

    private static final String MESSIC_DISCOVER_MESSAGE = "<messic>discover</messic>";

    private DatagramSocket socket;

    private boolean stop = false;

    @Autowired
    private DAOMessicSettings daoSettings;

    private Thread threadCover = null;

    public boolean isStarted()
    {
        return !stop;
    }

    public void stop()
    {
        this.stop = true;
        this.threadCover = null;
        try
        {
            this.socket.close();
            this.socket.disconnect();
        }
        catch ( Exception e )
        {
        }
    }

    @Override
    public void run()
    {
        try
        {
            // Keep a socket open to listen to all the UDP trafic that is destined for this port
            socket = new DatagramSocket( 8713, InetAddress.getByName( "0.0.0.0" ) );
            socket.setBroadcast( true );
            // socket.setSoTimeout( 5000 );

            while ( !stop )
            {
                try
                {
                    log.debug( "Discovery Service - Ready to receive broadcast packets!" );

                    // Receive a packet
                    byte[] recvBuf = new byte[15000];
                    DatagramPacket packet = new DatagramPacket( recvBuf, recvBuf.length );
                    socket.receive( packet );

                    // Packet received
                    log.debug( "Discovery Service - Discovery packet received from: "
                        + packet.getAddress().getHostAddress() );
                    log.debug( "Discovery Service - Packet received; data: " + new String( packet.getData() ) );

                    // See if the packet holds the right command (message)
                    String message = new String( packet.getData() ).trim();
                    if ( message.equals( MESSIC_DISCOVER_MESSAGE ) )
                    {
                        CurrentPort cp = MessicConfig.checkCurrentport();

                        String sendDataMessge = "<messic>";
                        sendDataMessge += "<hostname>" + InetAddress.getLocalHost().getHostName() + "</hostname>";
                        sendDataMessge += "<port>" + cp.currentPort + "</port>";
                        sendDataMessge += "<secure>" + cp.secure + "</secure>";
                        sendDataMessge += "<version>" + MessicConfig.getCurrentVersion().sversion + "</version>";
                        sendDataMessge += "</messic>";
                        byte[] sendData = sendDataMessge.getBytes();

                        // Send a response
                        DatagramPacket sendPacket =
                            new DatagramPacket( sendData, sendData.length, packet.getAddress(), packet.getPort() );
                        socket.send( sendPacket );

                        log.debug( "Discovery Service - Sent packet to: " + sendPacket.getAddress().getHostAddress() );
                    }
                }
                catch ( InterruptedIOException ii )
                {
                    // timeout?
                }

            }
        }
        catch ( IOException ex )
        {
            log.error( ex );
        }
    }

    /** flag to know if messic event have been received already or not */
    private boolean flagMessicInited = false;

    @Override
    public void onApplicationEvent( ApplicationContextEvent event )
    {
        ApplicationContext ac = event.getApplicationContext();
        synchronized ( this )
        {
            if ( ac != null && !this.flagMessicInited )
            {
                this.flagMessicInited = true;
                checkService();
            }
        }
    }

    public void checkService()
    {
        if ( this.daoSettings.getSettings().isAllowMessicDiscovering() )
        {
            if ( this.threadCover != null )
            {
                this.threadCover.interrupt();
            }
            this.stop();
            
            this.stop=false;
            this.threadCover = new Thread( this );
            this.threadCover.start();
        }
        else
        {
            this.stop();
        }
    }

}