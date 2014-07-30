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
package org.messic.server.api.tagwizard;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.messic.server.api.APISong;
import org.messic.server.api.configuration.MessicConfig;
import org.messic.server.api.datamodel.Album;
import org.messic.server.api.datamodel.Author;
import org.messic.server.api.datamodel.Genre;
import org.messic.server.api.datamodel.Song;
import org.messic.server.api.datamodel.User;
import org.messic.server.api.tagwizard.audiotagger.AudioTaggerTAGWizardPlugin;
import org.messic.server.api.tagwizard.service.SongTags;
import org.messic.server.api.tagwizard.service.TAGInfo;
import org.messic.server.api.tagwizard.service.TAGWizardPlugin;
import org.messic.server.datamodel.MDOGenre;
import org.messic.server.datamodel.dao.DAOGenre;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TAGWizard
{
    @Autowired
    public DAOGenre daoGenre;

    @Autowired
    public MessicConfig messicConfig;

    @Autowired
    public APISong apiSong;

    /**
     * Obtain a tagwizard plugin with the name
     * 
     * @return TAGWizardPlugin plugin with that name
     */
    private TAGWizardPlugin getTAGWizardPlugin( String name )
    {

        BundleContext context = FrameworkUtil.getBundle( TAGWizardPlugin.class ).getBundleContext();
        ArrayList<TAGWizardPlugin> result = new ArrayList<TAGWizardPlugin>();

        try
        {
            // Query for all service references matching any TAGWizard plugin
            ServiceReference<?>[] refs = null;
            if ( name != null && name.length() > 0 )
            {
                refs = context.getServiceReferences( TAGWizardPlugin.class.getName(), "(TAGWizard=" + name + ")" );
            }
            else
            {
                refs = context.getServiceReferences( TAGWizardPlugin.class.getName(), "(TAGWizard=*)" );
            }
            if ( refs != null )
            {
                for ( int i = 0; i < refs.length; i++ )
                {
                    result.add( (TAGWizardPlugin) context.getService( refs[i] ) );
                }
            }
        }
        catch ( Exception e )
        {
            // TODO log
        }
        if ( result.size() > 0 )
        {
            return result.get( 0 );
        }
        else
        {
            return null;
        }
    }

    /**
     * Function to transform an album from datamodel to an album from service
     * 
     * @param album
     * @return
     */
    public org.messic.server.api.tagwizard.service.Album getServiceAlbum( Album album )
    {
        if ( album == null )
        {
            return new org.messic.server.api.tagwizard.service.Album();
        }

        org.messic.server.api.tagwizard.service.Album result = new org.messic.server.api.tagwizard.service.Album();
        if ( album.getAuthor() != null )
        {
            result.author = album.getAuthor().getName();
        }
        result.comments = album.getComments();
        if ( album.getGenre() != null )
        {
            result.genre = album.getGenre().getName();
        }
        result.name = album.getName();
        result.year = album.getYear();
        result.songs = new ArrayList<org.messic.server.api.tagwizard.service.Song>();
        List<Song> songs = album.getSongs();
        if ( songs != null )
        {
            for ( int i = 0; i < songs.size(); i++ )
            {
                org.messic.server.api.tagwizard.service.Song songr = new org.messic.server.api.tagwizard.service.Song();
                songr.track = songs.get( i ).getTrack();
                songr.name = songs.get( i ).getName();
                result.songs.add( songr );
            }
        }
        return result;
    }

    public org.messic.server.api.datamodel.TAGWizardPlugin getAlbumWizard( Album albumHelpInfo, File[] f,
                                                                           String pluginName )
        throws IOException
    {

        TAGWizardPlugin plugin = getTAGWizardPlugin( pluginName );
        plugin.setConfiguration( this.messicConfig.getConfiguration() );

        org.messic.server.api.tagwizard.service.Album salbum = Album.transform( albumHelpInfo );

        List<org.messic.server.api.tagwizard.service.Album> albums = plugin.getAlbumInfo( salbum, f );
        if ( albums != null )
        {
            List<Album> salbums = new ArrayList<Album>();
            for ( int i = 0; i < albums.size(); i++ )
            {
                org.messic.server.api.tagwizard.service.Album album = albums.get( i );
                if ( album != null )
                {
                    salbums.add( Album.transform( album ) );
                }
            }
            org.messic.server.api.datamodel.TAGWizardPlugin twp =
                new org.messic.server.api.datamodel.TAGWizardPlugin( pluginName, salbums );
            return twp;
        }
        else
        {
            org.messic.server.api.datamodel.TAGWizardPlugin twp =
                new org.messic.server.api.datamodel.TAGWizardPlugin( pluginName, (Album) null );
            return twp;
        }

    }

    public org.messic.server.api.datamodel.TAGWizardPlugin getAlbumWizard( User user, Album albumHelpInfo, File[] f )
        throws IOException
    {
        AudioTaggerTAGWizardPlugin plugin = new AudioTaggerTAGWizardPlugin();

        org.messic.server.api.tagwizard.service.Song[] ssongs =
            new org.messic.server.api.tagwizard.service.Song[f.length];
        for ( int i = 0; i < f.length; i++ )
        {
            Song sdiscovered = apiSong.getSongInfoFromFileName( f[i].getName() );
            org.messic.server.api.tagwizard.service.Song sservice = new org.messic.server.api.tagwizard.service.Song();
            sservice.track = sdiscovered.getTrack();
            sservice.name = sdiscovered.getName();
            ssongs[i] = sservice;
        }

        List<SongTags> tags = plugin.getTags( getServiceAlbum( albumHelpInfo ), f, ssongs );
        if ( tags == null )
        {
            tags = new ArrayList<SongTags>();
        }
        // second, try to obtain the best results for the album tags
        for ( int i = 0; i < tags.size(); i++ )
        {
            SongTags ti = tags.get( i );
            Iterator<String> keys = ti.tags.keySet().iterator();
            while ( keys.hasNext() )
            {
                String key = keys.next();
                TAGInfo itag = ti.tags.get( key );
                if ( itag.value == null || itag.value.length() == 0 )
                {
                    itag.puntuation = 0; // no data :(
                }
                else
                {
                    // 1-10 puntuation
                    itag.puntuation = ( howManyTimesIsRepeated( key, tags, itag.value ) * 10 / tags.size() );
                }
            }
        }

        // Creating the album...
        Album album = new Album();
        Author author = new Author();
        author.setName( getMostValued( tags, TAGInfo.ARTIST ) );
        album.setAuthor( author );
        album.setComments( getMostValued( tags, TAGInfo.COMMENT ) );
        album.setName( getMostValued( tags, TAGInfo.ALBUM ) );
        try
        {
            album.setYear( Integer.valueOf( getMostValued( tags, TAGInfo.YEAR ) ) );
        }
        catch ( Exception e )
        {
            // e.printStackTrace();
            album.setYear( 1900 );
        }
        album.setGenre( getBestGenre( user, getMostValued( tags, TAGInfo.GENRE ) ) );

        // TODO Cover artwork

        // now the songs
        orderByTrack( tags );
        for ( SongTags tagInfo : tags )
        {
            Song song = new Song();
            int trackNumber = 0;
            try
            {
                trackNumber = Integer.valueOf( tagInfo.track );
            }
            catch ( Exception e )
            {
            }
            song.setTrack( trackNumber );
            song.setName( tagInfo.title );
            song.setFileName( tagInfo.filename );
            album.addSong( song );
        }

        org.messic.server.api.datamodel.TAGWizardPlugin twp =
            new org.messic.server.api.datamodel.TAGWizardPlugin( AudioTaggerTAGWizardPlugin.NAME, album );
        return twp;
    }

    /**
     * Try to find a genre from the database, if not, put the genre founded in tags
     * 
     * @param user {@link User} user scope
     * @param genre {@link String} genreName to compare
     * @return {@link Genre} genre to link with the album
     */
    private Genre getBestGenre( User user, String genre )
    {
        if ( this.daoGenre != null )
        {

            MDOGenre founded = this.daoGenre.getGenre( user.getLogin(), genre );
            if ( founded == null )
            {
                List<MDOGenre> listf = this.daoGenre.findSimilarGenre( genre, "" );
                if ( listf != null && listf.size() > 0 )
                {
                    founded = listf.get( 0 );
                }
            }

            if ( genre != null && founded != null )
            {
                return new Genre( founded );
            }
            else
            {
                return new Genre( genre );
            }
        }

        return new Genre( genre );
    }

    /**
     * sort the list of tags by track number
     * 
     * @param tags {@link ArrayList}<TagInfo/> list of tags
     */
    private static void orderByTrack( List<SongTags> tags )
    {
        Collections.sort( tags, new Comparator<SongTags>()
        {
            @Override
            public int compare( SongTags o1, SongTags o2 )
            {
                int o1p = 0;
                try
                {
                    o1p = Integer.valueOf( o1.track );
                }
                catch ( Exception e )
                {
                }
                int o2p = 0;
                try
                {
                    o2p = Integer.valueOf( o2.track );
                }
                catch ( Exception e )
                {
                }

                if ( o1p == o2p )
                {
                    return 0;
                }
                else if ( o1p < o2p )
                {
                    return -1;
                }
                else
                {
                    return 1;
                }
            }
        } );
    }

    /**
     * Return the most vauled tag, the one which have the best punctuation
     * 
     * @param tags {@link ArrayList}<TagInfo/> list of tag infos recovered previously
     * @param key {@link String} key to evaluate
     * @return String the value of the most valued value
     */
    private static String getMostValued( List<SongTags> tags, final String key )
    {
        if ( tags != null && tags.size() > 0 )
        {
            Collections.sort( tags, new Comparator<SongTags>()
            {
                @Override
                public int compare( SongTags o1, SongTags o2 )
                {
                    int o1p = o1.tags.get( key ).puntuation;
                    int o2p = o2.tags.get( key ).puntuation;
                    if ( o1p == o2p )
                    {
                        return 0;
                    }
                    else if ( o1p < o2p )
                    {
                        return -1;
                    }
                    else
                    {
                        return 1;
                    }
                }
            } );
            return tags.get( 0 ).tags.get( key ).value;
        }
        else
        {
            return "";
        }
    }

    /**
     * Discover how many times is repeated the value for this tag.. the logic is that is repeated too many times, then
     * it appears to be more valid... (just a suggestion :) )
     * 
     * @param tag {@link String} tag that we are inspecting
     * @param tags {@link ArrayList}<TagInfo/> tagInfo list to compare
     * @param value String value to compare
     * @return int how many times is repeated the value
     */
    private static int howManyTimesIsRepeated( String tag, List<SongTags> tags, String value )
    {
        int result = 0;
        for ( int i = 0; i < tags.size(); i++ )
        {
            if ( tags.get( i ).tags.get( tag ).value.equalsIgnoreCase( value ) )
            {
                result++;
            }
        }

        return result - 1;
    }
}
