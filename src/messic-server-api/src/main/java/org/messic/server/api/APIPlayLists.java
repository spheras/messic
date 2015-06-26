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

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.messic.server.api.datamodel.Playlist;
import org.messic.server.api.datamodel.Song;
import org.messic.server.api.datamodel.User;
import org.messic.server.api.exceptions.ExistingMessicException;
import org.messic.server.api.exceptions.ResourceNotFoundMessicException;
import org.messic.server.api.exceptions.SidNotFoundMessicException;
import org.messic.server.datamodel.MDOPlaylist;
import org.messic.server.datamodel.MDOSong;
import org.messic.server.datamodel.dao.DAOMessicSettings;
import org.messic.server.datamodel.dao.DAOPlaylist;
import org.messic.server.datamodel.dao.DAOSong;
import org.messic.server.datamodel.dao.DAOUser;
import org.messic.server.playlists.m3u.M3U;
import org.messic.server.playlists.m3u.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class APIPlayLists
{
    @Autowired
    private DAOPlaylist daoPlaylist;

    @Autowired
    private DAOMessicSettings daoSettings;

    @Autowired
    private DAOUser daoUser;

    @Autowired
    private DAOSong daoSong;

    @Transactional
    public void getPlaylistZip( User user, Long playlistSid, OutputStream os )
        throws IOException, SidNotFoundMessicException
    {
        MDOPlaylist mdoplaylist = daoPlaylist.get( user.getLogin(), playlistSid );
        if ( mdoplaylist == null )
        {
            throw new SidNotFoundMessicException();
        }
        List<MDOSong> desiredSongs = mdoplaylist.getSongs();

        ZipOutputStream zos = new ZipOutputStream( os );
        // level - the compression level (0-9)
        zos.setLevel( 9 );

        HashMap<String, String> songs = new HashMap<String, String>();

        M3U m3u = new M3U();
        m3u.setExtensionM3U( true );
        List<Resource> resources = m3u.getResources();

        for ( MDOSong song : desiredSongs )
        {
            if ( song != null )
            {

                // add file
                // extract the relative name for entry purpose
                String entryName = song.getLocation();
                if ( songs.get( entryName ) == null )
                {
                    Resource r = new Resource();
                    r.setLocation( song.getLocation() );
                    r.setName( song.getName() );
                    resources.add( r );

                    songs.put( entryName, "ok" );
                    // song not repeated
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

        // the last is the playlist m3u
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try
        {
            m3u.writeTo( baos, "UTF8" );
            // song not repeated
            ZipEntry ze = new ZipEntry( mdoplaylist.getName() + ".m3u" );
            zos.putNextEntry( ze );
            byte[] bytes = baos.toByteArray();
            zos.write( bytes, 0, bytes.length );
            zos.closeEntry();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

        zos.close();
    }

    public void createOrUpdate( User user, Playlist playlist )
        throws SidNotFoundMessicException, ExistingMessicException
    {
        MDOPlaylist mdoplaylist = null;
        if ( playlist.getSid() > 0 )
        {
            // UPDATE A PLAYLIST
            mdoplaylist = daoPlaylist.get( user.getLogin(), playlist.getSid() );
            if ( mdoplaylist == null )
            {
                throw new SidNotFoundMessicException();
            }
        }
        else
        {
            MDOPlaylist existent = daoPlaylist.getByName( user.getLogin(), playlist.getName() );
            if ( existent == null )
            {
                mdoplaylist = new MDOPlaylist();
                mdoplaylist.setOwner( daoUser.getUserByLogin( user.getLogin() ) );
            }
            else
            {
                throw new ExistingMessicException();
            }
        }

        mdoplaylist.setName( playlist.getName() );
        ArrayList<MDOSong> songs = new ArrayList<MDOSong>();
        for ( int i = 0; i < playlist.getSongs().size(); i++ )
        {
            Song song = playlist.getSongs().get( i );
            MDOSong mdoSong = daoSong.get( user.getLogin(), song.getSid() );
            if ( mdoSong != null )
            {
                boolean found = false;
                for ( int j = 0; j < songs.size(); j++ )
                {
                    if ( songs.get( j ).getSid() == mdoSong.getSid() )
                    {
                        found = true;
                        break;
                    }
                }
                if ( !found )
                {
                    songs.add( mdoSong );
                }
            }
            else
            {
                throw new SidNotFoundMessicException();
            }
        }

        mdoplaylist.setSongs( songs );

        daoPlaylist.save( mdoplaylist );
    }

    @Transactional
    public void remove( User user, long playlistSid )
    {
        MDOPlaylist mdoPlaylist = this.daoPlaylist.get( user.getLogin(), playlistSid );
        if ( mdoPlaylist != null )
        {
            this.daoPlaylist.remove( mdoPlaylist );
        }
    }

    @Transactional
    public Playlist getPlaylist( User user, long playlistSid, boolean copySongs )
        throws ResourceNotFoundMessicException
    {
        MDOPlaylist mdoPlaylist = daoPlaylist.get( user.getLogin(), playlistSid );
        if ( mdoPlaylist != null )
        {
            Playlist pl = new Playlist( mdoPlaylist, copySongs );
            return pl;
        }
        else
        {
            throw new ResourceNotFoundMessicException( "Playlist not found!" );
        }
    }

    @Transactional
    public List<Playlist> getAllLists( User user, boolean copySongs )
    {
        List<MDOPlaylist> playlists = daoPlaylist.getAll( user.getLogin() );
        List<Playlist> result = new ArrayList<Playlist>();

        for ( MDOPlaylist mdoPlaylist : playlists )
        {
            Playlist pl = new Playlist( mdoPlaylist, copySongs );
            result.add( pl );
        }

        return result;
    }

}
