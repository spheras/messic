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

import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Network
{
    
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
