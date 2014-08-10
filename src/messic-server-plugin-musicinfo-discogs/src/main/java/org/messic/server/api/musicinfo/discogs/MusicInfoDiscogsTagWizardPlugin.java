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
package org.messic.server.api.musicinfo.discogs;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.messic.server.api.musicinfo.service.MusicInfoPlugin;
import org.messic.server.api.plugin.MessicPlugin;
import org.messic.server.api.tagwizard.service.Album;
import org.messic.server.api.tagwizard.service.Song;
import org.messic.server.api.tagwizard.service.TAGWizardPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

public class MusicInfoDiscogsTagWizardPlugin
    implements MusicInfoPlugin
{

    private Logger log = Logger.getLogger( MusicInfoDiscogsTagWizardPlugin.class );

    public static final String NAME = "Discogs";

    public static final String PROVIDER_NAME = "Discogs";

    public static final String EN_DESCRIPTION = "Plugin to obtain info from Discogs services.";

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

    /**
     * Obtain a tagwizard plugin with the name
     * 
     * @return TAGWizardPlugin plugin with that name
     */
    private List<TAGWizardPlugin> getTAGWizardPlugins()
    {

        BundleContext context = FrameworkUtil.getBundle( TAGWizardPlugin.class ).getBundleContext();
        ArrayList<TAGWizardPlugin> result = new ArrayList<TAGWizardPlugin>();

        try
        {
            // Query for all service references matching any TAGWizard plugin
            ServiceReference<?>[] refs = null;
            refs = context.getServiceReferences( TAGWizardPlugin.class.getName(), "(TAGWizard=Discogs)" );
            if ( refs != null )
            {
                for ( int i = 0; i < refs.length; i++ )
                {
                    TAGWizardPlugin twp = (TAGWizardPlugin) context.getService( refs[i] );
                    result.add( twp );
                }
            }
        }
        catch ( Exception e )
        {
            // TODO log
        }
        if ( result.size() > 0 )
        {
            return result;
        }
        else
        {
            return null;
        }
    }

    private String UtilEscapeHTML( String html )
    {
        // TODO
        return html;
    }

    private String search( Album album )
        throws IOException
    {
        String htmlCode = "";

        List<TAGWizardPlugin> plugins = getTAGWizardPlugins();
        for ( int i = 0; i < plugins.size(); i++ )
        {
            TAGWizardPlugin plugin = plugins.get( i );
            plugin.setConfiguration( this.getConfiguration() );
            List<Album> albums = plugin.getAlbumInfo( album, null );

            for ( int j = 0; j < albums.size(); j++ )
            {
                Album albumj = albums.get( j );
                htmlCode = htmlCode + "<div class=\"messic-musicinfo-tagwizard-albumcontainer\">";
                htmlCode = htmlCode + "  <div class=\"messic-musicinfo-tagwizard-albuminfo-head\">";
                htmlCode = htmlCode + "    <div class=\"messic-musicinfo-tagwizard-albumtitle\">Author</div>";
                htmlCode =
                    htmlCode
                        + "    <div class=\"messic-musicinfo-tagwizard-albumfield messic-musicinfo-tagwizard-albumfield-author\" title=\""
                        + UtilEscapeHTML( albumj.author ) + "\">" + UtilEscapeHTML( albumj.author ) + "</div>";
                htmlCode = htmlCode + "    <div class=\"messic-musicinfo-tagwizard-albumtitle\">Title</div>";
                htmlCode =
                    htmlCode
                        + "    <div class=\"messic-musicinfo-tagwizard-albumfield messic-musicinfo-tagwizard-albumfield-title\" title=\""
                        + UtilEscapeHTML( albumj.name ) + "\">" + UtilEscapeHTML( albumj.name ) + "</div>";
                htmlCode = htmlCode + "    <div class=\"messic-musicinfo-tagwizard-albumtitle\">Year</div>";
                htmlCode =
                    htmlCode
                        + "    <div class=\"messic-musicinfo-tagwizard-albumfield messic-musicinfo-tagwizard-albumfield-year\" title=\""
                        + UtilEscapeHTML( "" + albumj.year ) + "\">" + UtilEscapeHTML( "" + albumj.year ) + "</div>";
                htmlCode = htmlCode + "    <div class=\"messic-musicinfo-tagwizard-albumtitle\">Genre</div>";
                htmlCode =
                    htmlCode
                        + "    <div class=\"messic-musicinfo-tagwizard-albumfield messic-musicinfo-tagwizard-albumfield-genre\" title=\""
                        + UtilEscapeHTML( albumj.genre ) + "\">" + UtilEscapeHTML( albumj.genre ) + "</div>";
                htmlCode = htmlCode + "    <div class=\"messic-musicinfo-tagwizard-albumtitle\">Comments</div>";
                htmlCode =
                    htmlCode
                        + "    <div class=\"messic-musicinfo-tagwizard-albumfield messic-musicinfo-tagwizard-albumfield-comments\" title=\""
                        + UtilEscapeHTML( albumj.comments ) + "\">" + UtilEscapeHTML( albumj.comments ) + "</div>";
                htmlCode = htmlCode + "  </div>";
                htmlCode = htmlCode + "  <div class=\"messic-musicinfo-tagwizard-albuminfo-body\">";

                for ( int k = 0; k < albumj.songs.size(); k++ )
                {
                    Song song = albumj.songs.get( k );
                    htmlCode =
                        htmlCode + "<div class=\"messic-musicinfo-tagwizard-albumsong-track\">" + song.track + "</div>";
                    htmlCode =
                        htmlCode + "<div class=\"messic-musicinfo-tagwizard-albumsong-name\" title=\""
                            + UtilEscapeHTML( song.name ) + "\">" + UtilEscapeHTML( song.name ) + "</div>";
                }

                htmlCode = htmlCode + "  </div>";
                htmlCode = htmlCode + "  <div class=\"divclearer\"></div>";
                htmlCode = htmlCode + "</div>";
            }
        }

        return htmlCode;
    }

    @Override
    public String getAuthorInfo( Locale locale, String authorName )
    {
        try
        {
            Album album = new Album();
            album.author = authorName;
            return search( album );
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
            Album album = new Album();
            album.author = authorName;
            album.name = albumName;
            return search( album );

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
            Album album = new Album();
            album.author = authorName;
            album.name = albumName;
            album.songs = new ArrayList<Song>();
            Song song = new Song();
            song.name = songName;
            album.songs.add( song );
            return search( album );
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
            MusicInfoDiscogsTagWizardPlugin.class.getResourceAsStream( "/org/messic/server/api/musicinfo/discogs/discogs.png" );
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
