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

import org.messic.server.datamodel.MDOGenre;



/**
 * DAO for Author table
 */
public interface DAOGenre extends DAO<MDOGenre>
{
    
    /**
     * Return a set of random genres
     * @param username {@link String} user scope
     * @param number int max number of genres
     * @return List<MDOGenre/> the list of genres
     */
    List<MDOGenre> getRandomGenre(String username, int number);

	/**
	 * Find similar genres to the genreName
	 * @param genreName {@link String} genrename to compare
	 * @param username {@link String} user scope
	 * @return {@link MDOGenre} similar genres
	 */
	List<MDOGenre> findSimilarGenre(String genreName, String username);

	/**
	 * Find a genre by name
	 * @param genreName {@link String} genreName to search
	 * @return {@link MDOGenre} founded (null if none)
	 */
	MDOGenre getGenre(String username,  String genreName);

	/**
	 * Find a genre with name equals to the param
	 * @param genreName {@link String} genre name
	 * @param username {@link String} user scope
	 * @return {@link MDOGenre} found, null if none
	 */
	MDOGenre getByName(String username, String genreName);

}
