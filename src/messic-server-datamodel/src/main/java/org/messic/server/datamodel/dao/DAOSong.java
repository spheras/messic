package org.messic.server.datamodel.dao;

import java.util.List;

import org.messic.server.datamodel.MDOSong;



/**
 * DAO for Song table
 */
public interface DAOSong extends DAO<MDOSong>
{
	/**
	 * Get a song with a certain sid
	 * @param username {@link String} username scope -avoid attack to get resources from other user-
	 * @param sid long sid of the song
	 * @return {@link MDOSong} song founded
	 */
	MDOSong get(String username, long sid);

	/**
	 * Get all the songs of a certain user
	 * @param username {@link String} username
	 * @return {@link List}<MDOSong/> list of songs
	 */
	List<MDOSong> getAll(String username);

	/**
	 * Find songs by a generic search. It searchs everything by songs, authors, etc..
	 * @param username {@link String} user scope
	 * @param List<String/> searches a list of contents related with songs 
	 * @return {@link List}<MDOSong/> List of songs that match to the search
	 */
    List<MDOSong> genericFind(String username, List<String> searches);
	
}
