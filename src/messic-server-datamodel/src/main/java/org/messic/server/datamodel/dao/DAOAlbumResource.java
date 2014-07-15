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

import org.messic.server.datamodel.MDOAlbumResource;



/**
 * DAO for PhysicalResource table
 */
public interface DAOAlbumResource extends DAO<MDOAlbumResource>
{
    /**
     * Obtain an albumresource with the sid, only search in the scope of the username
     * @param username {@link String} user scope
     * @param sid long sid of the resource
     * @return {@link MDOAlbumResource}
     */
    MDOAlbumResource get(String username, long sid);	
}
