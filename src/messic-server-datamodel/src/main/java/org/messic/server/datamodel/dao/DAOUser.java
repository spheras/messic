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

import org.messic.server.datamodel.MDOUser;

/**
 * DAO for User table
 */
public interface DAOUser
    extends DAO<MDOUser>
{
    public MDOUser getUserByLogin( String user );

    public MDOUser getUserById( Long userSid );

    public MDOUser createUser( String login, String password, String name, String email, byte[] avatar,
                               boolean administrator, String storePath );

    public MDOUser updatePassword( Long userSid, String newPassword );

    public MDOUser updateUserData( Long userSid, String name, String email, byte[] avatar, String storePath );

    public void removeUser( Long userSid );

    public boolean existUser( String user );

    /**
     * check if exist any user at the database
     * 
     * @return boolean, true->yes, exist at least one user, false->no, there is no user at the database
     */
    public boolean existUsers();

    public MDOUser authenticate( String user, String password );

}
