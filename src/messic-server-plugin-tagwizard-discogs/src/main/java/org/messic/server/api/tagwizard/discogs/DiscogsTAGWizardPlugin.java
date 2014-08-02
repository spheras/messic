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
package org.messic.server.api.tagwizard.discogs;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.messic.server.api.tagwizard.service.Album;
import org.messic.server.api.tagwizard.service.Song;
import org.messic.server.api.tagwizard.service.TAGWizardPlugin;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class DiscogsTAGWizardPlugin
    implements TAGWizardPlugin
{

    public static final String NAME = "Discogs";

    public static final String DESCRIPTION = "TAGWIZARD plugin, based on discogs service";

    public static final float VERSION = 1.0f;

    public static final float MINIMUM_MESSIC_VERSION = 1.0f;

    /** configuration for the plugin */
    private Properties configuration;

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
    public String getName()
    {
        return NAME;
    }

    @Override
    public String getDescription( Locale locale )
    {
        return DESCRIPTION;
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

    private Proxy getProxy()
    {
        if ( this.configuration != null )
        {
            String url = (String) this.configuration.get( "proxy-url" );
            String port = (String) this.configuration.get( "proxy-port" );
            if ( url != null && port != null && url.length() > 0 && port.length() > 0 )
            {
                SocketAddress addr = new InetSocketAddress( url, Integer.valueOf( port ) );
                Proxy proxy = new Proxy( Proxy.Type.HTTP, addr );
                return proxy;
            }
        }
        return null;
    }

    @Override
    public List<Album> getAlbumInfo( Album albumHelpInfo, File[] files )
    {
        if ( albumHelpInfo == null
            || ( albumHelpInfo.name == null && albumHelpInfo.author == null )
            || ( ( albumHelpInfo.name != null && albumHelpInfo.name.length() <= 0 ) && ( albumHelpInfo.author != null && albumHelpInfo.author.length() <= 0 ) ) )
        {
            return new ArrayList<Album>();
        }

        String baseURL = "http://api.discogs.com/database/search?type=release";

        try
        {
            if ( albumHelpInfo.name != null )
            {
                baseURL = baseURL + "&release_title=" + URLEncoder.encode( albumHelpInfo.name, "UTF-8" ) + "";
            }
            if ( albumHelpInfo.author != null )
            {
                baseURL = baseURL + "&artist=" + URLEncoder.encode( albumHelpInfo.author, "UTF-8" ) + "";
            }

            URL url = new URL( baseURL );
            Proxy proxy = getProxy();
            URLConnection uc = ( proxy != null ? url.openConnection( proxy ) : url.openConnection() );
            uc.setRequestProperty( "User-Agent", "Messic/1.0 +http://spheras.github.io/messic/" );

            ArrayList<Album> result = new ArrayList<Album>();

            JsonFactory jsonFactory = new JsonFactory(); // or, for data binding,
            JsonParser jParser = jsonFactory.createParser( uc.getInputStream() );
            while ( jParser.nextToken() != null )
            {
                String fieldname = jParser.getCurrentName();
                if ( "id".equals( fieldname ) )
                {
                    jParser.nextToken();
                    String id = jParser.getText();
                    // one second per petition allowed by discogs
                    Thread.sleep( 1000 );

                    Album album = getAlbum( id );

                    result.add( album );
                }

            }
            return result;
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

        return null;
    }

    private Album getAlbum( String id )
    {
        String baseURL = "http://api.discogs.com/releases/" + id;
        try
        {
            URL url = new URL( baseURL );
            Proxy proxy = getProxy();
            URLConnection uc = ( proxy != null ? url.openConnection( proxy ) : url.openConnection() );
            uc.setRequestProperty( "User-Agent", "Messic/1.0 +http://spheras.github.io/messic/" );

            Album album = new Album();

            album.name = "";
            album.author = "";
            album.comments = "Info obtained by Discogs provider (http://www.discogs.com/)";
            album.year = 1900;
            album.genre = "";

            JsonFactory jsonFactory = new JsonFactory(); // or, for data binding,
            JsonParser jParser = jsonFactory.createParser( uc.getInputStream() );
            while ( jParser.nextToken() != null )
            {
                String fieldname = jParser.getCurrentName();
                if ( "title".equals( fieldname ) )
                {
                    jParser.nextToken();
                    album.name = jParser.getText();
                }
                if ( "year".equals( fieldname ) )
                {
                    jParser.nextToken();
                    try
                    {
                        album.year = Integer.valueOf( jParser.getText() );
                    }
                    catch ( Exception e )
                    {
                        album.year = 0;
                    }
                }
                if ( "notes".equals( fieldname ) )
                {
                    jParser.nextToken();
                    album.comments = jParser.getText();
                }
                if ( "genres".equals( fieldname ) )
                {
                    jParser.nextToken();
                    jParser.nextToken();
                    album.genre = jParser.getText();
                    do
                    {
                        jParser.nextToken();
                    }
                    while ( !"genres".equals( jParser.getCurrentName() ) );
                }
                if ( "artists".equals( fieldname ) )
                {
                    jParser.nextToken();
                    while ( !"name".equals( jParser.getCurrentName() ) )
                    {
                        jParser.nextToken();
                    }
                    jParser.nextToken();
                    album.author = jParser.getText();
                    do
                    {
                        jParser.nextToken();
                    }
                    while ( !"artists".equals( jParser.getCurrentName() ) );
                }

                if ( "tracklist".equals( fieldname ) )
                {
                    album.songs = new ArrayList<Song>();
                    do
                    {
                        Song newsong = new Song();
                        int tracknumber = 1;
                        while ( jParser.nextToken() != JsonToken.END_OBJECT )
                        {
                            String trackfieldname = jParser.getCurrentName();

                            if ( "extraartists".equals( trackfieldname ) )
                            {
                                do
                                {
                                    while ( jParser.nextToken() != JsonToken.END_OBJECT )
                                    {

                                    }
                                    jParser.nextToken();
                                }
                                while ( !"extraartists".equals( jParser.getCurrentName() ) );
                            }
                            if ( "position".equals( trackfieldname ) )
                            {
                                jParser.nextToken();
                                try
                                {
                                    newsong.track = Integer.valueOf( jParser.getText() );
                                }
                                catch ( Exception e )
                                {
                                    newsong.track = tracknumber;
                                }
                                tracknumber++;
                            }
                            if ( "title".equals( trackfieldname ) )
                            {
                                jParser.nextToken();
                                newsong.name = jParser.getText();
                            }
                        }
                        album.songs.add( newsong );
                        jParser.nextToken();
                    }
                    while ( !"tracklist".equals( jParser.getCurrentName() ) );
                    jParser.nextToken();
                }
            }

            return album;
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

        return null;
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

}
