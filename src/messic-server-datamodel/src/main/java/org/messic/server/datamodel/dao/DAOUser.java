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

import org.messic.server.datamodel.MDOUser;
import org.springframework.transaction.annotation.Transactional;

/**
 * DAO for User table
 */
@Transactional
public interface DAOUser
    extends DAO<MDOUser>
{

    /**
     * Check if exist any admin user at the database
     * 
     * @return boolean true->yes there is at least one admin user
     */
    boolean existAdminUser();

    /**
     * Obtain an user by its login
     * 
     * @param user {@link String} login user
     * @return {@link MDOUser} foudned
     */
    @Transactional
    MDOUser getUserByLogin( String user );

    /**
     * search an user by its sid
     * 
     * @param userSid {@link Long} sid for the user to search
     * @return {@link MDOUser} founded
     */
    @Transactional
    MDOUser getUserById( Long userSid );

    /**
     * Remove an user
     * 
     * @param userSid Long sid to remove
     */
    @Transactional
    void removeUser( Long userSid );

    /**
     * Check if the username already exist at the database
     * 
     * @param user {@link String} username to check
     * @return boolean
     */
    @Transactional
    boolean existUser( String user );

    /**
     * check if exist any user at the database
     * 
     * @return boolean, true->yes, exist at least one user, false->no, there is no user at the database
     */
    @Transactional
    boolean existUsers();

    /**
     * Try to authenticate to an user
     * 
     * @param user {@link String}
     * @param password {@link String}
     * @return {@link MDOUser} user authenticated
     */
    @Transactional
    MDOUser authenticate( String user, String password );

    /**
     * Count how many users allow the DLNA Shared content
     * 
     * @return long
     */
    @Transactional
    long countAllowedDLNAUsers();

    /**
     * return all the DLNA users (users that allow to share their content by DLNA protocol)
     * 
     * @return {@link List}<MDOUser/>
     */
    @Transactional
    List<MDOUser> getDLNAUsers();

    /**
     * Save or update an user
     * 
     * @param user
     * @param updatePassword boolean flag to know if its necessary to update the password
     * @param newPassword {@link String} the new password to be hashed and stored
     * @return
     */
    @Transactional
    MDOUser saveUser( MDOUser user, boolean updatePassword, String newPassword );
    
    /**
     *update all the users to notify the new messic update version 
     */
    @Transactional
    void usersNotifyMessicUpdate();


}
