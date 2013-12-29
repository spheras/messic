package org.messic.server.datamodel.dao;

import java.util.List;

import org.messic.server.datamodel.MDOAlbum;



/**
 * DAO for Album table
 */
public interface DAOAlbum extends DAO<MDOAlbum>
{
	
	/**
	 * return all the albums in the user scope
	 * @param username {@link String} user scope
	 * @return
	 */
	List<MDOAlbum> getAll(String username);

	/**
	 * return all the albums of an author
	 * @param authorSid int sid of the author scope
	 * @param username {@link String} user scope
	 * @return {@link List}<MDOAlbum/> list of albums of the author
	 */
	List<MDOAlbum> getAll(int authorSid, String username);

	/**
	 * Find similar albums with the name passed.
	 * @param albumName
	 * @param username
	 * @return {@link List}<MDOAlbum/> list of albums similars
	 */
	List<MDOAlbum> findSimilarAlbums(String albumName, String username);
	
	/**
	 * Find similar albums with the name passed and only from the author passed
	 * @param authorSid int sid of the author scope
	 * @param albumName {@link String} name or partial name to search similar
	 * @param username {@link String} username scope
	 * @return {@link List}<MDOAlbum/> list of similar albums
	 */
	List<MDOAlbum> findSimilarAlbums(int authorSid, String albumName, String username);
}
