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

/**
 * DAO for Album table
 */
public interface DAOAlbum
    extends DAO<MDOAlbum>
{
    /**
     * Devuelve el año más antiguo que existe de un album
     * 
     * @param username user scope
     * @return int el año más antiguo
     */
    int findOldestAlbum( String username );

    /**
     * Find albums that have been created in the period of the years passed as parameters [fromYear, toYear]
     * 
     * @param username user scope
     * @param fromYear Year from starting to search
     * @param toYear Year to ending to search
     * @return
     */
    List<MDOAlbum> findAlbumsBasedOnDate( String username, int fromYear, int toYear );

    /**
     * Set null to all the albums that have a certain genere
     * 
     * @param genre {@link MDOGenre} genre to search
     */
    void setNullGenre( MDOGenre genre );

    /**
     * Return all the albums, from all the users, but only who allow DLNA share content
     * 
     * @return List of albums shared
     */
    List<MDOAlbum> getAllDLNA();

    /**
     * return all the albums of a certain genre
     * 
     * @param username {@link String} user scope
     * @param genre {@link MDOGenre} genre to search
     * @return
     */
    List<MDOAlbum> getAll( String username, MDOGenre genre );

    /**
     * return all the albums in the user scope
     * 
     * @param username {@link String} user scope
     * @return
     */
    List<MDOAlbum> getAll( String username );

    /**
     * return all the albums of an author, null if author not found
     * 
     * @param authorSid long sid of the author scope
     * @param username {@link String} user scope
     * @return {@link List}<MDOAlbum/> list of albums of the author
     */
    List<MDOAlbum> getAll( long authorSid, String username );

    /**
     * return all the albums which have a genre
     * 
     * @param genreSid long sid of the genre scope
     * @param username {@link String} user scope
     * @return {@link List}<MDOAlbum/> list of albums with the genre
     */
    List<MDOAlbum> getAllOfGenre( long genreSid, String username );

    /**
     * Return an album with id equals to albumSid param
     * 
     * @param albumSid long sid of the album to get
     * @param username {@link String} username scope
     * @return {@link MDOAlbum} album with sid equal to albumSid param
     */
    MDOAlbum getAlbum( long albumSid, String username );

    /**
     * Find similar albums with the name passed.
     * 
     * @param albumName
     * @param username
     * @return {@link List}<MDOAlbum/> list of albums similars
     */
    List<MDOAlbum> findSimilarAlbums( String albumName, String username );

    /**
     * Find similar albums with the name passed and only from the author passed
     * 
     * @param authorSid int sid of the author scope
     * @param albumName {@link String} name or partial name to search similar
     * @param username {@link String} username scope
     * @return {@link List}<MDOAlbum/> list of similar albums
     */
    List<MDOAlbum> findSimilarAlbums( long authorSid, String albumName, String username );

    /**
     * Find an album with name and author name equals to the param
     * 
     * @param authorName {@link String} author name
     * @param albumName {@link String} album Name
     * @param username {@link String} user scope
     * @return {@link MDOAlbum} found, null if none
     */
    MDOAlbum getByName( String authorName, String albumName, String username );

    /**
     * Obtain the {@link MDOArtwork} which is the cover of the album
     * 
     * @param albumSid long sid of the album
     * @param username {@link String} user scope
     * @return {@link MDOArtwork} which is the cover of the album
     */
    MDOArtwork getAlbumCover( long albumSid, String username );

    /**
     * Set an artwork resource from the album as cover of the album
     * 
     * @param resourceSid long sid of the resource to be set as cover
     * @param albumSid long sid of the album which have the resource
     * @param userSid long isd of the user scope
     */
    void setAlbumCover( long resourceSid, long albumSid, long userSid );

}
