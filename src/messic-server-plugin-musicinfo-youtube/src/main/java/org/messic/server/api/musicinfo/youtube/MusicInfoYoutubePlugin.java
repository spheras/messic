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
package org.messic.server.api.musicinfo.youtube;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;
import java.util.Properties;

import org.messic.server.api.musicinfo.service.MusicInfoPlugin;
import org.messic.server.api.plugin.MessicPlugin;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class MusicInfoYoutubePlugin
    implements MusicInfoPlugin
{

    public static final String NAME = "YOUTUBE";

    public static final String PROVIDER_NAME = "Youtube";

    public static final String EN_DESCRIPTION = "Plugin to obtain videos from youtube.";

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

    private String search( Locale locale, String search )
        throws IOException
    {

        // http://ctrlq.org/code/19608-youtube-search-api
        // Based con code writted by Amit Agarwal

        // YouTube Data API base URL (JSON response)
        String surl = "v=2&alt=jsonc";
        // set paid-content as false to hide movie rentals
        surl = surl + "&paid-content=false";
        // set duration as long to filter partial uploads
        // url = url + "&duration=long";
        // order search results by view count
        surl = surl + "&orderby=viewCount";
        // we can request a maximum of 50 search results in a batch
        surl = surl + "&max-results=50";
        surl = surl + "&q=" + search;

        URI uri = null;
        try
        {
            uri = new URI( "http", "gdata.youtube.com", "/feeds/api/videos", surl, null );
        }
        catch ( URISyntaxException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        URL url = new URL( uri.toASCIIString() );
        System.out.println( surl );
        Proxy proxy = getProxy();
        URLConnection connection = ( proxy != null ? url.openConnection( proxy ) : url.openConnection() );
        InputStream is = connection.getInputStream();

        JsonFactory jsonFactory = new JsonFactory(); // or, for data binding,
                                                     // org.codehaus.jackson.mapper.MappingJsonFactory
        JsonParser jParser = jsonFactory.createParser( is );

        String htmlCode = "<script type='text/javascript'>";
        htmlCode = htmlCode + "  function musicInfoYoutubeDestroy(){";
        htmlCode = htmlCode + "       $('.messic-musicinfo-youtube-overlay').remove();";
        htmlCode = htmlCode + "       $('.messic-musicinfo-youtube-iframe').remove();";
        htmlCode = htmlCode + "  }";
        htmlCode = htmlCode + "  function musicInfoYoutubePlay(id){";
        htmlCode =
            htmlCode
                + "      var code='<div class=\"messic-musicinfo-youtube-overlay\" onclick=\"musicInfoYoutubeDestroy()\"></div>';";
        htmlCode =
            htmlCode
                + "      code=code+'<iframe class=\"messic-musicinfo-youtube-iframe\" src=\"http://www.youtube.com/embed/'+id+'\" frameborder=\"0\" allowfullscreen></iframe>';";
        htmlCode = htmlCode + "      $(code).hide().appendTo('body').fadeIn();";
        htmlCode = htmlCode + "  }";
        htmlCode = htmlCode + "</script>";

        // loop until token equal to "}"
        while ( jParser.nextToken() != null )
        {
            String fieldname = jParser.getCurrentName();
            if ( "items".equals( fieldname ) )
            {
                jParser.nextToken();
                while ( jParser.nextToken() != JsonToken.END_OBJECT )
                {
                    YoutubeItem yi = new YoutubeItem();
                    while ( jParser.nextToken() != JsonToken.END_OBJECT )
                    {
                        if ( jParser.getCurrentToken() == JsonToken.START_OBJECT )
                        {
                            jParser.skipChildren();
                        }
                        fieldname = jParser.getCurrentName();

                        if ( "id".equals( fieldname ) )
                        {
                            jParser.nextToken();
                            yi.id = jParser.getText();
                        }
                        if ( "category".equals( fieldname ) )
                        {
                            jParser.nextToken();
                            yi.category = jParser.getText();
                        }
                        if ( "title".equals( fieldname ) )
                        {
                            jParser.nextToken();
                            yi.title = jParser.getText();
                        }
                        if ( "description".equals( fieldname ) )
                        {
                            jParser.nextToken();
                            yi.description = jParser.getText();
                        }
                        if ( "thumbnail".equals( fieldname ) )
                        {
                            jParser.nextToken();
                            jParser.nextToken();
                            jParser.nextToken();
                            jParser.nextToken();
                            fieldname = jParser.getCurrentName();
                            if ( "hqDefault".equals( fieldname ) )
                            {
                                jParser.nextToken();
                                yi.thumbnail = jParser.getText();
                            }
                            jParser.nextToken();
                        }
                    }

                    if ( yi.category != null && "MUSIC".equals( yi.category.toUpperCase() ) || ( yi.category == null ) )
                    {
                        if ( yi.title != null )
                        {
                            htmlCode =
                                htmlCode
                                    + "<div class='messic-musicinfo-youtube-item'><img src='"
                                    + yi.thumbnail
                                    + "'/><div class='messic-musicinfo-youtube-item-play' onclick='musicInfoYoutubePlay(\""
                                    + yi.id + "\")'></div><div class='messic-musicinfo-youtube-item-title'>" + yi.title
                                    + "</div><div class='messic-musicinfo-youtube-item-description'>" + yi.description
                                    + "</div></div>";
                        }
                    }
                }
            }

        }
        // ObjectMapper om=new ObjectMapper();
        // GAPIResult result=om.readValue(is, GAPIResult.class);
        // System.out.println(result.apiVersion);
        return htmlCode;
    }

    @Override
    public String getAuthorInfo( Locale locale, String authorName )
    {
        try
        {
            return search( locale, "\"" + authorName + "\"" );
        }
        catch ( IOException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "ERROR: " + e.getMessage();
        }
    }

    @Override
    public String getAlbumInfo( Locale locale, String authorName, String albumName )
    {
        try
        {
            return search( locale, "\"" + albumName + "\" " + "\"" + authorName + "\"" );
        }
        catch ( IOException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "ERROR: " + e.getMessage();
        }
    }

    @Override
    public String getSongInfo( Locale locale, String authorName, String albumName, String songName )
    {
        try
        {
            return search( locale, "\"" + songName + "\"" + " \"" + albumName + "\" " + "\"" + authorName );
        }
        catch ( IOException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
            MusicInfoYoutubePlugin.class.getResourceAsStream( "/org/messic/server/api/musicinfo/youtube/YouTube-logo-full_color.png" );
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
    public String getProviderName()
    {
        return PROVIDER_NAME;
    }

}
