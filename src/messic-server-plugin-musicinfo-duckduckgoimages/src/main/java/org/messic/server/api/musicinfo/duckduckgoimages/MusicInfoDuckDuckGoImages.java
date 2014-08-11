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
package org.messic.server.api.musicinfo.duckduckgoimages;

import java.io.ByteArrayOutputStream;
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

import org.apache.log4j.Logger;
import org.messic.server.api.musicinfo.service.MusicInfoPlugin;
import org.messic.server.api.plugin.MessicPlugin;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MusicInfoDuckDuckGoImages
    implements MusicInfoPlugin
{
    private Logger log = Logger.getLogger( MusicInfoDuckDuckGoImages.class );

    public static final String NAME = "DuckDuckGoImages";

    public static final String PROVIDER_NAME = "Duck Duck Go Images";

    public static final String EN_DESCRIPTION = "Plugin to obtain images from Duck Duck Go images search engine.";

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
        return this.configuration;
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

    private String search( String baseUrl )
        throws IOException
    {
        URL url = new URL( baseUrl );
        Proxy proxy = getProxy();
        URLConnection connection = ( proxy != null ? url.openConnection( proxy ) : url.openConnection() );
        connection.setRequestProperty( "User-Agent", "Messic/1.0 +http://spheras.github.io/messic/" );

        InputStream is = connection.getInputStream();
        List<String> urlImages = getUrlImages( is );

        String htmlCode = "<script type='text/javascript'>";
        htmlCode = htmlCode + "  function musicInfoDuckDuckGoImagesDestroy(){";
        htmlCode = htmlCode + "       $('.messic-musicinfo-duckduckgoimages-overlay').remove();";
        htmlCode = htmlCode + "  }";
        htmlCode = htmlCode + "  function musicInfoDuckDuckGoImagesShow(url){";
        htmlCode =
            htmlCode
                + "      var code= '<div class=\"messic-musicinfo-duckduckgoimages-overlay\" onclick=\"musicInfoDuckDuckGoImagesDestroy()\">';";
        htmlCode = htmlCode + "      code=code+'  <image src=\"'+url+'\" />';";
        htmlCode =
            htmlCode
                + "      code=code+'  <a class=\"messic-musicinfo-duckduckgoimages-zoom\" href=\"'+url+'\" target=\"_blank\"></a>';";
        htmlCode = htmlCode + "      code=code+'</div>';";
        htmlCode = htmlCode + "      $(code).hide().appendTo('body').fadeIn();";
        htmlCode = htmlCode + "  }";
        htmlCode = htmlCode + "</script>";

        htmlCode = htmlCode + "<div class=\"messic-musicinfo-duckduckgoimages-imagecontainer\">";
        htmlCode =
            htmlCode + "  <div>we &lt;3 <a href=\"http://www.duckduckgo.com\" target=\"_blank\">DuckDuckGo</a></div>";
        for ( String urlimage : urlImages )
        {
            htmlCode =
                htmlCode + " <img src=\"" + urlimage + "\" onclick=\"musicInfoDuckDuckGoImagesShow('" + urlimage
                    + "')\"/>";
        }
        htmlCode = htmlCode + "</div>";

        return htmlCode;
    }

    public List<String> getUrlImages( InputStream is )
    {
        ArrayList<String> result = new ArrayList<String>();
        ObjectMapper om = new ObjectMapper();
        try
        {
            jsonobject jo = om.readValue( is, jsonobject.class );
            for ( int i = 0; i < jo.results.size(); i++ )
            {
                result.add( jo.results.get( i ).j );
            }
        }
        catch ( JsonParseException e )
        {
            log.error( "failed!", e );
        }
        catch ( JsonMappingException e )
        {
            log.error( "failed!", e );
        }
        catch ( IOException e )
        {
            log.error( "failed!", e );
        }

        return result;
    }

    private String constructURL( String[] phrases )
    {
        String baseUrl = "https://duckduckgo.com/i.js?o=json&q=";
        for ( String phrase : phrases )
        {
            baseUrl = baseUrl + "\"" + phrase + "\" ";
        }
        return baseUrl;

    }

    @Override
    public String getAuthorInfo( Locale locale, String authorName )
    {
        try
        {
            return search( constructURL( new String[] { URLEncoder.encode( authorName, "UTF-8" ) } ) );
        }
        catch ( IOException e )
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
            return search( constructURL( new String[] { URLEncoder.encode( authorName, "UTF-8" ),
                URLEncoder.encode( albumName, "UTF-8" ) } ) );
        }
        catch ( IOException e )
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
            return search( constructURL( new String[] { URLEncoder.encode( authorName, "UTF-8" ),
                URLEncoder.encode( albumName, "UTF-8" ), URLEncoder.encode( songName, "UTF-8" ) } ) );
        }
        catch ( IOException e )
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
            MusicInfoDuckDuckGoImages.class.getResourceAsStream( "/org/messic/server/api/musicinfo/duckduckgoimages/9129e7ed.png" );
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
