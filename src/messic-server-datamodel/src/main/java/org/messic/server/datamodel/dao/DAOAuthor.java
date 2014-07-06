/*
 * Copyright (C) 2013 José Amuedo
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

import org.messic.server.datamodel.MDOAuthor;



/**
 * DAO for Author table
 */
public interface DAOAuthor extends DAO<MDOAuthor>
{
	
	/**
	 * Return a list of random authors. The result is limited by the number
	 * @param username String user scope
	 * @param number int number of limited results
	 * @return {@link List}<MDOAuthor/>
	 */
    List<MDOAuthor> getRandomAuthors(String username, int number);


    /**
     * Return the list of first letter (Distinct) of all the author at database.
     * @param username {@link String} user scope
     * @return {@link List}<String/>
     */
    List<String> getFirstCharacters(String username);

	/**
	 * Find similar authors with the name passed
	 * @param authorName {@link String} author name to compare with
	 * @param contains boolean flag to indicate if the authorName is a partial search with any authorname wich contains the words of the param, or starting 
	 * @param username {@link String} username scope
	 * @return {@link List}<MDOAuthor/> list of similar authors
	 */
	List<MDOAuthor> findSimilarAuthors(String authorName, boolean contains, String username);

	/**
	 * return all the authors in the user scope
	 * @param username {@link String} user scope
	 * @return
	 */
	List<MDOAuthor> getAll(String username);

   /**
     * return all the authors, but only from users that allow DLNA
     * @return
     */
    List<MDOAuthor> getAllDLNA();

	/**
	 * Find an author with name equals to the param authorName
	 * @param authorName {@link String} author name
	 * @param username {@link String} user scope
	 * @return {@link MDOAuthor} found, null if none
	 */
	MDOAuthor getByName(String authorName, String username);
	
	/**
	 * Find an author by sid.
	 * @param username {@link String} userscope
	 * @param authorSid long sid of the author to get
	 * @return {@link MDOAuthor} author found, null if not
	 */
	MDOAuthor get(String username, long authorSid);
}
