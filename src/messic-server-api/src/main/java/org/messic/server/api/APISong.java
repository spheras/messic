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
package org.messic.server.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.messic.server.Util;
import org.messic.server.api.datamodel.Song;
import org.messic.server.api.datamodel.User;
import org.messic.server.api.exceptions.SidNotFoundMessicException;
import org.messic.server.datamodel.MDOAlbum;
import org.messic.server.datamodel.MDOAuthor;
import org.messic.server.datamodel.MDOMessicSettings;
import org.messic.server.datamodel.MDOSong;
import org.messic.server.datamodel.MDOSongStatistics;
import org.messic.server.datamodel.MDOUser;
import org.messic.server.datamodel.dao.DAOAlbum;
import org.messic.server.datamodel.dao.DAOAuthor;
import org.messic.server.datamodel.dao.DAOMessicSettings;
import org.messic.server.datamodel.dao.DAOPlaylist;
import org.messic.server.datamodel.dao.DAOSong;
import org.messic.server.datamodel.dao.DAOSongStatistics;
import org.messic.server.datamodel.dao.DAOUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class APISong
{
    @Autowired
    private DAOMessicSettings daoSettings;

    @Autowired
    private DAOSong daoSong;

    @Autowired
    private DAOSongStatistics daoSongStatistics;

    @Autowired
    private DAOUser daoUser;

    @Autowired
    private DAOAuthor daoAuthor;

    @Autowired
    private DAOAlbum daoAlbum;

    @Autowired
    private DAOPlaylist daoPlaylist;

    public List<Song> getSongsOfAuthor( User user, long authorSid, boolean includeAlbumInfo, boolean includeAuthorInfo )
        throws SidNotFoundMessicException
    {
        MDOAuthor mdoauthor = daoAuthor.get( user.getLogin(), authorSid );
        if ( mdoauthor != null )
        {
            List<Song> result = new ArrayList<Song>();
            Set<MDOAlbum> albums = mdoauthor.getAlbums();
            for ( MDOAlbum mdoalbum : albums )
            {
                List<MDOSong> songs = mdoalbum.getSongs();
                for ( MDOSong mdoSong : songs )
                {
                    Song song = new Song( mdoSong, includeAlbumInfo, includeAuthorInfo );
                    result.add( song );
                }
            }
            return result;
        }
        else
        {
            throw new SidNotFoundMessicException();
        }
    }

    public List<Song> getSongsOfAlbum( User user, long albumSid, boolean includeAlbumInfo, boolean includeAuthorInfo )
        throws SidNotFoundMessicException
    {
        MDOAlbum mdoalbum = daoAlbum.getAlbum( albumSid, user.getLogin() );
        if ( mdoalbum != null )
        {
            List<Song> result = new ArrayList<Song>();
            List<MDOSong> songs = mdoalbum.getSongs();
            for ( MDOSong mdoSong : songs )
            {
                Song song = new Song( mdoSong, includeAlbumInfo, includeAuthorInfo );
                result.add( song );
            }
            return result;
        }
        else
        {
            throw new SidNotFoundMessicException();
        }
    }

    @Transactional
    public Song getSong( User user, long songSid, boolean includeAlbumInfo, boolean includeAuthorInfo )
        throws SidNotFoundMessicException, IOException
    {
        MDOSong mdosong = daoSong.get( user.getLogin(), songSid );
        if ( mdosong != null )
        {
            Song song = new Song( mdosong, includeAlbumInfo, includeAuthorInfo );
            return song;
        }
        else
        {
            throw new SidNotFoundMessicException();
        }
    }

    @Transactional
    public void updateSong( User user, Song song )
        throws SidNotFoundMessicException, IOException
    {
        MDOSong mdosong = daoSong.get( user.getLogin(), song.getSid() );
        if ( mdosong != null )
        {
            String oldLocation = mdosong.calculateAbsolutePath( daoSettings.getSettings() );
            boolean nameChanged = !song.getName().equals( mdosong.getName() );

            try
            {
                if ( nameChanged )
                {
                    mdosong.setName( song.getName() );
                }
                mdosong.setRate( song.getRate() );
                mdosong.setTrack( song.getTrack() );

                daoSong.save( mdosong );
            }
            finally
            {
                if ( nameChanged )
                {
                    String newLocation = mdosong.calculateAbsolutePath( daoSettings.getSettings() );
                    File fOld = new File( oldLocation );
                    if ( fOld.exists() )
                    {
                        FileUtils.moveFile( fOld, new File( newLocation ) );
                    }
                    else
                    {
                        throw new IOException( "The song file doesn't exist!!" );
                    }
                }
            }
        }
        else
        {
            throw new SidNotFoundMessicException();
        }
    }

    @Transactional
    public void getSongsZip( User user, List<Long> desiredSongs, OutputStream os )
        throws IOException
    {
        ZipOutputStream zos = new ZipOutputStream( os );
        // level - the compression level (0-9)
        zos.setLevel( 9 );

        HashMap<String, String> songs = new HashMap<String, String>();
        for ( Long songSid : desiredSongs )
        {
            MDOSong song = daoSong.get( user.getLogin(), songSid );
            if ( song != null )
            {
                // add file
                // extract the relative name for entry purpose
                String entryName = song.getLocation();
                if ( songs.get( entryName ) == null )
                {
                    // not repeated
                    songs.put( entryName, "ok" );
                    ZipEntry ze = new ZipEntry( entryName );
                    zos.putNextEntry( ze );
                    FileInputStream in = new FileInputStream( song.calculateAbsolutePath( daoSettings.getSettings() ) );
                    int len;
                    byte buffer[] = new byte[1024];
                    while ( ( len = in.read( buffer ) ) > 0 )
                    {
                        zos.write( buffer, 0, len );
                    }
                    in.close();
                    zos.closeEntry();
                }
            }
        }

        zos.close();
    }

    @Transactional
    public void remove( User user, Long songSid )
        throws IOException
    {
        MDOSong song = this.daoSong.get( user.getLogin(), songSid );
        if ( song != null )
        {

            // // we should remove manually all the playlist links
            // Set<MDOPlaylist> playlists = song.getPlaylists();
            // for ( MDOPlaylist mdoPlaylist : playlists )
            // {
            // List<MDOSong> psongs = mdoPlaylist.getSongs();
            // for ( int i = 0; i < psongs.size(); i++ )
            // {
            // MDOSong mdoSong2 = psongs.get( i );
            // if ( mdoSong2 != null )
            // {
            // if ( mdoSong2.getSid() == song.getSid() )
            // {
            // psongs.remove( i );
            // i = i - 1;
            // }
            // }
            // }
            // daoPlaylist.save( mdoPlaylist );
            // }

            // first, removing the song file
            String path = song.calculateAbsolutePath( daoSettings.getSettings() );
            File fpath = new File( path );
            fpath.delete();
            // after, removing the album data from database
            this.daoSong.remove( song );
        }
    }

    public class AudioSongStream
    {
        public RandomAccessFile raf;

        public InputStream is;

        public long contentLength;

        public long lastModified;

        public String songFileName;
    }

    @Transactional
    public AudioSongStream getAudioSong( User user, long sid )
        throws IOException
    {
        MDOUser mdouser = daoUser.getUserByLogin( user.getLogin() );

        MDOSong song = daoSong.get( mdouser.getLogin(), sid );
        if ( song != null )
        {
            MDOMessicSettings settings = daoSettings.getSettings();

            if ( mdouser.getAllowStatistics() )
            {
                MDOSongStatistics statistics = song.getStatistics();
                if ( statistics == null )
                {
                    statistics = new MDOSongStatistics();
                    daoSongStatistics.save( statistics );
                    statistics = daoSongStatistics.getStatistics( user.getLogin(), song.getSid() );
                    song.setStatistics( statistics );
                }
                statistics.addTimePlayed();
                daoSong.save( song );
            }

            String filePath = song.calculateAbsolutePath( settings );
            File fsong = new File( filePath );
            if ( fsong.exists() )
            {
                AudioSongStream ass = new AudioSongStream();
                ass.raf = new RandomAccessFile( fsong, "r" );
                ass.is = new FileInputStream( fsong );
                // ass.is = new FileInputStream( fsong );
                ass.contentLength = fsong.length();
                ass.lastModified = fsong.lastModified();
                ass.songFileName = fsong.getName();
                return ass;
            }
        }

        return null;
    }

    /**
     * Return any possible separator between the track number and the filename
     * 
     * @param fileName
     * @return
     */
    private char getPossibleSeparator( String fileName )
    {
        for ( int i = 0; i < fileName.length(); i++ )
        {
            char c = fileName.substring( i, i + 1 ).toCharArray()[0];
            boolean isDigit = ( c >= '0' && c <= '9' );
            if ( !isDigit )
            {
                if ( c == ' ' )
                {
                    // then possible spacess until the real separator
                    char c1 = fileName.substring( i + 1, i + 2 ).toCharArray()[0];
                    char c2 = fileName.substring( i + 2, i + 3 ).toCharArray()[0];

                    if ( c1 != ' ' && ( c2 == ' ' ) && !((c1>='a' && c1<='z') || (c1>='A' && c1<='Z')) )
                    {
                        return c1;
                    }
                    if ( c1 == '-' )
                    {
                        return c1;
                    }
                }

                return c;
            }
        }
        return ' ';
    }

    /**
     * Try to get the song name and track from a file name
     * 
     * @param filePath {@link String} the filename to investigate
     * @return {@link Song} song info, only the track number and track name
     */
    public Song getSongInfoFromFileName( String filePath )
    {
        File tmpf = new File( filePath );
        String fileName = tmpf.getName().trim();

        fileName = fileName.replaceAll( "_", " " );

        int where = Util.areThereNumbers( fileName );

        if ( where >= 0 )
        {
            char c = fileName.substring( 0, 1 ).toCharArray()[0];
            boolean isDigit = ( c >= '0' && c <= '9' );
            if ( isDigit )
            {
                // the track number start inmediatelly
                char separator = getPossibleSeparator( fileName );
                String[] parts = fileName.split( "\\" + separator );
                if ( parts.length <= 1 )
                {
                    separator = ' ';
                    parts = fileName.split( "" + separator );
                }
                for ( int i = 0; i < parts.length - 1; i++ )
                {
                    if ( Util.areThereNumbers( parts[i] ) >= 0 )
                    {
                        try
                        {
                            int trackn = Integer.valueOf( parts[i].trim() );
                            String trackName = Util.addPartsFromArray( parts, i + 1, parts.length - 1, "" + separator );
                            int wheredot = trackName.lastIndexOf( "." );
                            if ( wheredot >= 0 )
                            {
                                trackName = trackName.substring( 0, wheredot );
                            }
                            return new Song( 0, trackn, trackName.trim(), 0 );
                        }
                        catch ( Exception e )
                        {
                            // not a track?
                        }
                    }
                }
            }
            else
            {
                int wheresearch = where - 1;
                do
                {
                    c = fileName.charAt( wheresearch );
                    wheresearch = wheresearch - 1;
                }
                while ( c == ' ' && wheresearch >= 0 );

                // maybe... something like [track] songname
                char close = ']';
                if ( c == '[' )
                {
                    close = ']';
                }
                if ( c == '(' )
                {
                    close = ')';
                }
                if ( c == '-' )
                {
                    close = '-';
                }

                int whereclose = fileName.indexOf( close, where );
                if ( whereclose >= 0 )
                {
                    String trackn = fileName.substring( where, whereclose ).trim();
                    String trackName = fileName.substring( whereclose + 1 ).trim();
                    int wheredot = trackName.lastIndexOf( "." );
                    if ( wheredot >= 0 )
                    {
                        trackName = trackName.substring( 0, wheredot );
                    }
                    return new Song( 0, Integer.valueOf( trackn ), trackName.trim(), 0 );
                }
            }
        }

        String trackName = fileName;
        int wheredot = trackName.indexOf( "." );
        if ( wheredot >= 0 )
        {
            trackName = trackName.substring( 0, wheredot );
        }
        return new Song( -1, -1, trackName, 0 );
    }

}
