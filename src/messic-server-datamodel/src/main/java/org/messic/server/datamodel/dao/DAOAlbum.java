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

import org.messic.server.datamodel.MDOAlbum;
import org.messic.server.datamodel.MDOArtwork;
import org.messic.server.datamodel.MDOGenre;
import org.springframework.transaction.annotation.Transactional;

/**
 * DAO for Album table
 */
@Transactional
public interface DAOAlbum
    extends DAO<MDOAlbum>
{
    /**
     * Find the oldest existing album
     * 
     * @param username user scope
     * @return int el año más antiguo
     */
    @Transactional
    int findOldestAlbum( String username );

    /**
     * Obtain recent albums, incorpored to the database
     * 
     * @param username String user scope
     * @param maxAlbums int max number of albums returned
     * @return {@link List}<MDOAlbum/> List of albums, ordered by created (when incorpored to the database)
     */
    List<MDOAlbum> getRecent( String username, int maxAlbums );

    /**
     * Find an album with the name albumName parameter
     * 
     * @param albumName {@link String} album name to search
     * @param username {@link String} user scope
     * @return {@link List}<MDOAlbum/> list of albums with that name
     */
    @Transactional
    List<MDOAlbum> findAlbum( String albumName, String username );

    /**
     * Find albums that have been created in the period of the years passed as parameters [fromYear, toYear]
     * 
     * @param username user scope
     * @param fromYear Year from starting to search
     * @param toYear Year to ending to search
     * @return
     */
    @Transactional
    List<MDOAlbum> findAlbumsBasedOnDate( String username, int fromYear, int toYear );

    /**
     * Set null to all the albums that have a certain genere
     * 
     * @param genre {@link MDOGenre} genre to search
     */
    @Transactional
    void setNullGenre( MDOGenre genre );

    /**
     * Return all the albums, from all the users, but only who allow DLNA share content
     * 
     * @return List of albums shared
     */
    @Transactional
    List<MDOAlbum> getAllDLNA();

    /**
     * return all the albums of a certain genre
     * 
     * @param username {@link String} user scope
     * @param genre {@link MDOGenre} genre to search
     * @return
     */
    @Transactional
    List<MDOAlbum> getAll( String username, MDOGenre genre );

    /**
     * return all the albums in the user scope
     * 
     * @param username {@link String} user scope
     * @param firstResult int first result for pagination
     * @param maxResult int max result for pagination
     * @param orderDesc boolean if the results should be ordered asc or desc
     * @param orderByAuthor boolean if the reuslts shoulld be ordered by album name (by default) or by author name.
     * @return list of albums
     */
    @Transactional
    List<MDOAlbum> getAll( String username, int firstResult, int maxResult, boolean orderDesc, boolean orderByAuthor );

    /**
     * return all the albums of an author, null if author not found
     * 
     * @param authorSid long sid of the author scope
     * @param username {@link String} user scope
     * @param firstResult int first result for pagination
     * @param maxResult int max result for pagination
     * @param orderDesc boolean if the results should be ordered asc or desc
     * @param orderByAuthor boolean if the reuslts shoulld be ordered by album name (by default) or by author name.
     * @return {@link List}<MDOAlbum/> list of albums of the author
     */
    @Transactional
    List<MDOAlbum> getAll( long authorSid, String username, int firstResult, int maxResult, boolean orderDesc,
                           boolean orderByAuthor );

    /**
     * return all the albums which have a genre
     * 
     * @param genreSid long sid of the genre scope
     * @param username {@link String} user scope
     * @param firstResult int first result for pagination
     * @param maxResult int max result for pagination
     * @param orderDesc boolean if the results should be ordered asc or desc
     * @param orderByAuthor boolean if the reuslts shoulld be ordered by album name (by default) or by author name.
     * @return {@link List}<MDOAlbum/> list of albums with the genre
     */
    @Transactional
    List<MDOAlbum> getAllOfGenre( long genreSid, String username, int firstResult, int maxResults, boolean orderDesc, boolean orderByAuthor );

    /**
     * Return an album with id equals to albumSid param
     * 
     * @param albumSid long sid of the album to get
     * @param username {@link String} username scope
     * @param firstResult int first result for pagination
     * @param maxResult int max result for pagination
     * @return {@link MDOAlbum} album with sid equal to albumSid param
     */
    @Transactional
    MDOAlbum getAlbum( long albumSid, String username );

    /**
     * Find similar albums with the name passed.
     * 
     * @param albumName
     * @param username
     * @return {@link List}<MDOAlbum/> list of albums similars
     */
    @Transactional
    List<MDOAlbum> findSimilarAlbums( String albumName, String username );

    /**
     * Find similar albums with the name passed and only from the author passed
     * 
     * @param authorSid int sid of the author scope
     * @param albumName {@link String} name or partial name to search similar
     * @param username {@link String} username scope
     * @param firstResult int first result for pagination
     * @param maxResult int max result for pagination
     * @param orderDesc boolean if the results should be ordered asc or desc
     * @param orderByAuthor boolean if the reuslts shoulld be ordered by album name (by default) or by author name.
     * @return {@link List}<MDOAlbum/> list of similar albums
     */
    @Transactional
    List<MDOAlbum> findSimilarAlbums( long authorSid, String albumName, String username, int firstResult, int maxResults, boolean orderDesc, boolean orderByAuthor);

    /**
     * Find an album with name and author name equals to the param
     * 
     * @param authorName {@link String} author name
     * @param albumName {@link String} album Name
     * @param username {@link String} user scope
     * @return {@link MDOAlbum} found, null if none
     */
    @Transactional
    MDOAlbum getByName( String authorName, String albumName, String username );

    /**
     * Obtain the {@link MDOArtwork} which is the cover of the album
     * 
     * @param albumSid long sid of the album
     * @param username {@link String} user scope
     * @return {@link MDOArtwork} which is the cover of the album
     */
    @Transactional
    MDOArtwork getAlbumCover( long albumSid, String username );

    /**
     * Set an artwork resource from the album as cover of the album
     * 
     * @param resourceSid long sid of the resource to be set as cover
     * @param albumSid long sid of the album which have the resource
     * @param userSid long isd of the user scope
     */
    @Transactional
    void setAlbumCover( long resourceSid, long albumSid, long userSid );

}
