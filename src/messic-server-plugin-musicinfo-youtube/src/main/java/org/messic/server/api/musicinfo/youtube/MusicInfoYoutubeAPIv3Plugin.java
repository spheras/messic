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
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.messic.server.api.musicinfo.service.MusicInfoPlugin;
import org.messic.server.api.plugin.MessicPlugin;

public class MusicInfoYoutubeAPIv3Plugin
    implements MusicInfoPlugin
{

    private Logger log = Logger.getLogger( MusicInfoYoutubeAPIv3Plugin.class );

    public static final String NAME = "YOUTUBE";

    public static final String PROVIDER_NAME = "Youtube";

    public static final String EN_DESCRIPTION = "Plugin to obtain videos from youtube.";

    public static final float VERSION = 1.0f;

    public static final float MINIMUM_MESSIC_VERSION = 1.0f;

    /** configuration for the plugin */
    private Properties configuration;

    /**
     * Return if messic is configured to use a secure protocol
     * 
     * @return
     */
    private boolean isMessicSecureProtocol()
    {
        if ( this.configuration != null )
        {
            String messicSecure = this.configuration.getProperty( MessicPlugin.CONFIG_SECUREPROTOCOL );
            try
            {
                boolean secure = Boolean.valueOf( messicSecure );
                return secure;
            }
            catch ( Exception e )
            {
                return false;
            }
        }
        else
        {
            return false;
        }

    }

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
        Proxy proxy = getProxy();
        YoutubeSearch ys = new YoutubeSearch();
        List<YoutubeInfo> results = ys.search( search, proxy );
        String htmlCode = "";
        if ( results.size() > 0 )
        {
            htmlCode = htmlCode + "<script type=\"text/javascript\">";
            htmlCode = htmlCode + "  function musicInfoYoutubeDestroy(){";
            htmlCode = htmlCode + "       $('.messic-musicinfo-youtube-overlay').remove();";
            htmlCode = htmlCode + "       $('.messic-musicinfo-youtube-iframe').remove();";
            htmlCode = htmlCode + "  }";
            htmlCode = htmlCode + "  function musicInfoYoutubePlay(id){";
            htmlCode =
                htmlCode
                    + "      var code='<div class=\"messic-musicinfo-youtube-overlay\" onclick=\"musicInfoYoutubeDestroy()\"></div>';";
            htmlCode =
                htmlCode + "      code=code+'<iframe class=\"messic-musicinfo-youtube-iframe\" src=\""
                    + ( isMessicSecureProtocol() ? "https" : "http" )
                    + "://www.youtube.com/embed/'+id+'\" frameborder=\"0\" allowfullscreen></iframe>';";
            htmlCode = htmlCode + "      $(code).hide().appendTo('body').fadeIn();";
            htmlCode = htmlCode + "  }";
            htmlCode = htmlCode + "</script>";
        }
        for ( YoutubeInfo youtubeInfo : results )
        {
            htmlCode =
                htmlCode + "<div class=\"messic-musicinfo-youtube-item\"><img src=\"" + youtubeInfo.thumbnail
                    + "\"/><div class=\"messic-musicinfo-youtube-item-play\" onclick=\"musicInfoYoutubePlay('"
                    + youtubeInfo.id + "')\"></div>" + "<div class=\"messic-musicinfo-youtube-description\">"
                    + "  <div class=\"messic-musicinfo-youtube-item-title\">" + youtubeInfo.title + "</div>"
                    + "  <div class=\"messic-musicinfo-youtube-item-description\">" + youtubeInfo.description
                    + "</div>" + "</div>" + "</div>";
        }
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
            log.error( "failed!", e );
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
            log.error( "failed!", e );
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
            MusicInfoYoutubeAPIv3Plugin.class.getResourceAsStream( "/org/messic/server/api/musicinfo/youtube/YouTube-logo-full_color.png" );
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
