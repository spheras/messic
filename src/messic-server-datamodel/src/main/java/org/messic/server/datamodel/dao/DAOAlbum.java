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
	 * return all the albums of an author, null if author not found
	 * @param authorSid int sid of the author scope
	 * @param username {@link String} user scope
	 * @return {@link List}<MDOAlbum/> list of albums of the author
	 */
	List<MDOAlbum> getAll(long authorSid, String username);

	/**
	 * Return an album with id equals to albumSid param
	 * @param albumSid long sid of the album to get
	 * @param username {@link String} username scope
	 * @return {@link MDOAlbum} album with sid equal to albumSid param
	 */
	MDOAlbum getAlbum(long albumSid, String username);

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
	List<MDOAlbum> findSimilarAlbums(long authorSid, String albumName, String username);
	
	/**
	 * Find an album with name and author name equals to the param
	 * @param authorName {@link String} author name
	 * @param albumName {@link String} album Name
	 * @param username {@link String} user scope
	 * @return {@link MDOAlbum} found, null if none
	 */
	MDOAlbum getByName(String authorName, String albumName, String username);
}
