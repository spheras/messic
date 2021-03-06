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

import org.messic.server.datamodel.MDOSong;
import org.springframework.transaction.annotation.Transactional;

/**
 * DAO for Song table
 */
@Transactional
public interface DAOSong
    extends DAO<MDOSong>
{

    /**
     * Get all the loved songs by the users. It means those songs which have rate 2 or 3
     * 
     * @param username {@link String} user scope
     * @param max int max results
     * @return {@link List}<MDOSong/> list of songs which are loved by the user
     */
    @Transactional
    List<MDOSong> getLovedSongs( String username, int max );

    /**
     * Get a song with a certain sid
     * 
     * @param username {@link String} username scope -avoid attack to get resources from other user-
     * @param sid long sid of the song
     * @return {@link MDOSong} song founded
     */
    @Transactional
    MDOSong get( String username, long sid );

    /**
     * Get all the songs of a certain user
     * 
     * @param username {@link String} username
     * @return {@link List}<MDOSong/> list of songs
     */
    @Transactional
    List<MDOSong> getAll( String username );

    /**
     * Get all the songs of a certain user, but ordered descending by most played
     * 
     * @param username {@link String} username
     * @param max int max elements returned
     * @return {@link List}<MDOSong/> list of songs ordered
     */
    @Transactional
    List<MDOSong> getAllOrderByMostPlayed( String username, int max );

    /**
     * Get all the songs of a certain user, but ordered ascending by the less played
     * 
     * @param username user scope
     * @param max int maximum results
     * @return list of songs ordered by the less played
     */
    @Transactional
    List<MDOSong> getAllOrderByLessPlayed( String username, int max );

    /**
     * Get some random songs.
     * 
     * @param username {@link String} user scope
     * @param max int maximum results returned
     * @return list of random songs
     */
    List<MDOSong> getRandom( String username, int max );

    /**
     * Get all the songs of users that allow DLNA Share content
     * 
     * @return {@link List}<MDOSong/> list of songs
     */
    @Transactional
    List<MDOSong> getAllDLNA();

    /**
     * Find songs by a generic search. It searchs everything by songs, authors, etc..
     * 
     * @param username {@link String} user scope
     * @param List<String/> searches a list of contents related with songs
     * @return {@link List}<MDOSong/> List of songs that match to the search
     */
    @Transactional
    List<MDOSong> genericFind( String username, List<String> searches );

}
