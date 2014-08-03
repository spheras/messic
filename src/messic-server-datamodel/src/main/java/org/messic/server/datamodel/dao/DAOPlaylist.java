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
package org.messic.server.datamodel.dao;

import java.util.List;

import org.messic.server.datamodel.MDOPlaylist;

/**
 * DAO for Playlist table
 */
public interface DAOPlaylist
    extends DAO<MDOPlaylist>
{

    /***
     * Obtain a certain playlist
     * 
     * @param username {@link String} user scope
     * @param sid long sid of the playlist to obtain
     * @return {@link MDOPlaylist}
     */
    MDOPlaylist get( String username, long sid );

    /**
     * return all the playlist of the user
     * 
     * @param username {@link String} user scope
     * @return {@link List}<MdoPlaylist/> the list of playlists
     */
    List<MDOPlaylist> getAll( String username );

    /**
     * return all the playlist of users that allow dlna
     * 
     * @return {@link List}<MdoPlaylist/> the list of playlists
     */
    List<MDOPlaylist> getAllDLNA();

    /**
     * return a playlist searching by name
     * 
     * @param username {@link String} user scope
     * @param name {@link String} name of the playlist to search
     * @return {@link MDOPlaylist} playlist founded
     */
    MDOPlaylist getByName( String username, String name );
}
