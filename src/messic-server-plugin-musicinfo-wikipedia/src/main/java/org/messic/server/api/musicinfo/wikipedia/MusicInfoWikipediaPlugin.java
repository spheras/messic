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
package org.messic.server.api.musicinfo.wikipedia;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.messic.server.api.musicinfo.service.MusicInfoPlugin;
import org.messic.server.api.plugin.MessicPlugin;
import org.xml.sax.SAXException;

public class MusicInfoWikipediaPlugin
    implements MusicInfoPlugin
{

    private Logger log = Logger.getLogger( MusicInfoWikipediaPlugin.class );

    public static final String NAME = "WIKIPEDIA";

    public static final String PROVIDER_NAME = "Wikipedia";

    public static final String EN_DESCRIPTION =
        "Plugin to obtain information of albums, songs and authors from wikipedia.";

    public static final float VERSION = 1.0f;

    public static final float MINIMUM_MESSIC_VERSION = 1.0f;

    /** configuration for the plugin */
    private Properties configuration;

    private Proxy getProxy()
    {
        if ( this.configuration != null )
        {
            String url = (String) this.configuration.get( MessicPlugin.CONFIG_PROXY_URL );
            String port = (String) this.configuration.get( MessicPlugin.CONFIG_PROXY_PORT );
            if ( url != null && port != null && url.length() > 0 && port.length() > 0 )
            {
                SocketAddress addr = new InetSocketAddress( url, Integer.valueOf( port ) );
                Proxy proxy = new Proxy( Proxy.Type.HTTP, addr );
                return proxy;
            }
        }
        return null;
    }

    /**
     * Normalize the query text, just replacing the spaces with underscores
     * 
     * @param query {@link String} query text
     * @return {@link String} query text normalized
     */
    private String normalizeQuery( String query )
    {
        String[] tokens = query.split( " " );
        StringBuffer buf = new StringBuffer();
        for ( int i = 0; i < tokens.length; i++ )
        {
            buf.append( tokens[i] + "_" );
        }
        if ( tokens.length > 1 )
        {
            buf.deleteCharAt( buf.length() - 1 );
        }
        return buf.toString();
    }

    public String search( String query )
        throws IOException, SAXException
    {
        return search( new Locale( "en", "en" ), query );
    }

    /**
     * Do a search over wikipedia api for a certain query string
     * 
     * @param locale {@link Locale} locale for wikipedia search
     * @param query {@link String} string text to query
     * @return {@link String} string returned by wikipedia (html text)
     * @throws IOException
     * @throws SAXException
     */
    private String search( Locale locale, String query )
        throws IOException, SAXException
    {
        String country = locale.getLanguage();
        String nquery = normalizeQuery( query );
        String surl =
            "http://" + country.toLowerCase() + ".wikipedia.org/w/api.php?format=xml&action=query&titles=" + nquery
                + "&prop=revisions&rvprop=content&rvparse";
        URL url = new URL( surl );
        Proxy proxy = getProxy();
        URLConnection connection = ( proxy != null ? url.openConnection( proxy ) : url.openConnection() );
        InputStream is = connection.getInputStream();
        byte[] readed = readInputStream( is );
        String result = new String( readed, "UTF8" );
        WikipediaXMLReader wxr = new WikipediaXMLReader();
        String html = wxr.read( result );

        html =
            html.replaceAll( "href=\"/", "target=\"_blank\" href=\"http://" + country.toLowerCase() + ".wikipedia.org/" );
        return html;
    }

    /**
     * Read an inputstream and return a byte[] with the whole content of the readed at the inputstream
     * 
     * @param is {@link InputStream}
     * @return byte[] content
     * @throws IOException
     */
    public static byte[] readInputStream( InputStream is )
        throws IOException
    {
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int cant = is.read( buffer );
        while ( cant > 0 )
        {
            baos.write( buffer, 0, cant );
            cant = is.read( buffer );
        }

        return baos.toByteArray();
    }

    @Override
    public String getName()
    {
        return NAME;
    }

    @Override
    public Properties getConfiguration()
    {
        return null;
    }

    @Override
    public void setConfiguration( Properties properties )
    {
        this.configuration = properties;
    }

    @Override
    public String getDescription( Locale locale )
    {
        // TODO any other language
        return EN_DESCRIPTION;
    }

    @Override
    public String getAuthorInfo( Locale locale, String authorName )
    {
        try
        {
            return search( locale, authorName );
        }
        catch ( IOException e )
        {
            log.error( "failed!", e );
            return "ERROR: " + e.getMessage();
        }
        catch ( SAXException e )
        {
            log.error( "failed!", e );
            return "ERROR: " + e.getMessage();
        }
    }

    @Override
    public String getAlbumInfo( Locale locale, String authorName, String albumName )
    {
        try
        {
            return search( locale, albumName + "|" + authorName );
        }
        catch ( IOException e )
        {
            log.error( "failed!", e );
            return "ERROR: " + e.getMessage();
        }
        catch ( SAXException e )
        {
            log.error( "failed!", e );
            return "ERROR: " + e.getMessage();
        }
    }

    @Override
    public String getSongInfo( Locale locale, String authorName, String albumName, String songName )
    {
        try
        {
            return search( locale, songName + "|" + albumName + "|" + authorName );
        }
        catch ( IOException e )
        {
            log.error( "failed!", e );
            return "ERROR: " + e.getMessage();
        }
        catch ( SAXException e )
        {
            log.error( "failed!", e );
            return "ERROR: " + e.getMessage();
        }
    }

    @Override
    public float getVersion()
    {
        return VERSION;
    }

    @Override
    public float getMinimumMessicVersion()
    {
        return MINIMUM_MESSIC_VERSION;
    }

    @Override
    public byte[] getProviderIcon()
    {
        InputStream is =
            MusicInfoWikipediaPlugin.class.getResourceAsStream( "/org/messic/server/api/musicinfo/wikipedia/Wikipedia-logo-v2.svg.png" );
        try
        {
            return readInputStream( is );
        }
        catch ( IOException e )
        {
            // TODO
            return null;
        }
    }

    @Override
    public String getProviderName()
    {
        return PROVIDER_NAME;
    }

}
