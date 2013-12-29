package org.messic.server.datamodel.dao;

import java.util.List;

import org.messic.server.datamodel.MDOAuthor;



/**
 * DAO for Author table
 */
public interface DAOAuthor extends DAO<MDOAuthor>
{

	/**
	 * Find similar authors with the name passed
	 * @param authorName {@link String} author name to compare with
	 * @param username {@link String} username scope
	 * @return {@link List}<MDOAuthor/> list of similar authors
	 */
	List<MDOAuthor> findSimilarAuthors(String authorName, String username);

	/**
	 * return all the authors in the user scope
	 * @param username {@link String} user scope
	 * @return
	 */
	List<MDOAuthor> getAll(String username);
}
