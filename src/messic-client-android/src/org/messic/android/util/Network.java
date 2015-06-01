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
package org.messic.android.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.messic.android.controllers.Configuration;
import org.messic.android.datamodel.MDMMessicServerInstance;

import android.os.AsyncTask;

public class Network
{

    public interface MessicServerStatusListener
    {
        void setResponse( boolean reachable, boolean running );
    }

    public static void checkMessicServerUpAndRunning( final MDMMessicServerInstance instance,
                                                      final MessicServerStatusListener msl )
    {
        AsyncTask<Void, Void, Void> at = new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected Void doInBackground( Void... params )
            {
                boolean reachable = isServerReachable( instance.ip );
                if ( reachable )
                {
                    boolean running = isMessicServerInstanceRunning( instance );
                    msl.setResponse( true, running );
                }
                else
                {
                    msl.setResponse( false, false );
                }
                return null;
            }

        };
        at.execute();
    }

    /**
     * Check if messic is running in the current configuration connection WARNING! this must be done in another thread
     * 
     * @param ip
     * @param port
     * @return
     */
    public static boolean isMessicServerInstanceRunning( MDMMessicServerInstance instance )
    {
        try
        {
            nukeNetwork();
            String surl = Configuration.getBaseUrl( instance ) + "/services/check";
            URL url = new URL( surl );
            URLConnection urlConnection = url.openConnection();
            InputStream in = new BufferedInputStream( urlConnection.getInputStream() );
            in.close();
            return true;
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * Function to check if an ip is reachable from the device WARNING! this must be done in another thread
     * 
     * @param ip String ip in the way (xxx.xxx.xxx.xxx)
     * @return boolean true->if it is reachable
     */
    public static boolean isServerReachable( String ip )
    {
        try
        {
            boolean result = InetAddress.getByName( ip ).isReachable( 1000 );
            return result;
        }
        catch ( UnknownHostException e )
        {
        }
        catch ( IOException e )
        {
        }
        return false;
    }

    /**
     * Function to trust all certificates for https connections
     */
    public static void nukeNetwork()
    {
        try
        {
            TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager()
            {
                public X509Certificate[] getAcceptedIssuers()
                {
                    X509Certificate[] myTrustedAnchors = new X509Certificate[0];
                    return myTrustedAnchors;
                }

                public void checkClientTrusted( X509Certificate[] certs, String authType )
                {
                }

                public void checkServerTrusted( X509Certificate[] certs, String authType )
                {
                }
            } };

            SSLContext sc = SSLContext.getInstance( "SSL" );
            sc.init( null, trustAllCerts, new SecureRandom() );
            HttpsURLConnection.setDefaultSSLSocketFactory( sc.getSocketFactory() );
            HttpsURLConnection.setDefaultHostnameVerifier( new HostnameVerifier()
            {
                public boolean verify( String arg0, SSLSession arg1 )
                {
                    return true;
                }
            } );
        }
        catch ( Exception e )
        {
        }
    }

}
