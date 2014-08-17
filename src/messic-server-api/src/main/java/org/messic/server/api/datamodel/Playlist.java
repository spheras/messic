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
package org.messic.server.api.datamodel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;
import org.messic.server.datamodel.MDOPlaylist;
import org.messic.server.datamodel.MDOSong;

@XmlRootElement
@ApiObject( name = "Playlist", description = "A playlist of songs" )
public class Playlist
    extends File
{
    @ApiObjectField( description = "identificator of the playlist" )
    private long sid;

    @ApiObjectField( description = "name of the playlist" )
    private String name;

    @ApiObjectField( description = "List of songs contained at the playlist" )
    private List<Song> songs;

    public Playlist()
    {
        super();
    }

    public Playlist( MDOPlaylist mdoplaylist, boolean copySongs )
    {
        this.setSid( mdoplaylist.getSid() );
        this.setName( mdoplaylist.getName() );
        if ( mdoplaylist.getSongs() != null && copySongs )
        {
            Iterator<MDOSong> songs = mdoplaylist.getSongs().iterator();
            while ( songs.hasNext() )
            {
                MDOSong mdosong = songs.next();
                if ( mdosong != null )
                {
                    Song song = new Song( mdosong, true, true );
                    addSong( song );
                }
            }
        }
    }

    /**
     * @return the sid
     */
    public long getSid()
    {
        return sid;
    }

    /**
     * @param sid the sid to set
     */
    public void setSid( long sid )
    {
        this.sid = sid;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName( String name )
    {
        this.name = name;
    }

    /**
     * @return the songs
     */
    public List<Song> getSongs()
    {
        return songs;
    }

    /**
     * @param songs the songs to set
     */
    public void setSongs( List<Song> songs )
    {
        this.songs = songs;
    }

    public void addSong( Song song )
    {
        if ( this.songs == null )
        {
            this.songs = new ArrayList<Song>();
        }
        this.songs.add( song );
    }
}
