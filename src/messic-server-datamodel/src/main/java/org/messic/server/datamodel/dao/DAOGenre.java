package org.messic.server.datamodel.dao;

import java.util.List;

import org.messic.server.datamodel.MDOGenre;



/**
 * DAO for Author table
 */
public interface DAOGenre extends DAO<MDOGenre>
{
	/**
	 * Find similar genres to the genreName
	 * @param genreName {@link String} genrename to compare
	 * @param username {@link String} user scope (TODO)
	 * @return {@link MDOGenre} similar genres
	 */
	List<MDOGenre> findSimilarGenre(String genreName, String username);

	/**
	 * Find a genre by name
	 * @param genreName {@link String} genreName to search
	 * @return {@link MDOGenre} founded (null if none)
	 */
	MDOGenre getGenre(String genreName);

	/**
	 * Find a genre with name equals to the param
	 * @param genreName {@link String} genre name
	 * @param username {@link String} user scope
	 * @return {@link MDOGenre} found, null if none
	 */
	MDOGenre getByName(String genreName);

}
