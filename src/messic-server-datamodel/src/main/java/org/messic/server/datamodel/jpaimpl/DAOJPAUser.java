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
package org.messic.server.datamodel.jpaimpl;

import java.security.MessageDigest;
import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.messic.server.datamodel.MDOUser;
import org.messic.server.datamodel.dao.DAOUser;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class DAOJPAUser
    extends DAOJPA<MDOUser>
    implements DAOUser
{
    private static Logger log = Logger.getLogger( DAOJPAUser.class );

    public DAOJPAUser()
    {
        super( MDOUser.class );
    }

    /**
     * Check if exist any admin user at the database
     * 
     * @return boolean true->yes there is at least one admin user
     */
    @Override
    @Transactional
    public boolean existAdminUser()
    {
        Query query = entityManager.createQuery( "from MDOUser as p where p.administrator = true" );
        @SuppressWarnings( "unchecked" )
        List<MDOUser> results = query.getResultList();
        return ( results.size() > 0 );
    }

    /**
     * Get User by username, null if not found
     * 
     * @param username {@link String} username to find
     * @return {@link MDOUser} user found, null if not found
     */
    @Override
    @Transactional
    public MDOUser getUserByLogin( String username )
    {
        Query query = entityManager.createQuery( "from MDOUser as p where p.login = :login" );
        query.setParameter( "login", username );
        @SuppressWarnings( "unchecked" )
        List<MDOUser> results = query.getResultList();
        if ( results == null || results.size() <= 0 )
        {
            return null;
        }
        else
        {
            return results.get( 0 );
        }
    }

    /**
     * tell if a user exist in the database
     * 
     * @param username {@link String} username
     * @return boolean true->yes, it exists
     */
    @Transactional
    @Override
    public boolean existUser( String username )
    {
        return ( getUserByLogin( username ) != null );
    }

    /**
     * TODO
     * 
     * @param username
     * @param hashpass
     * @return
     */
    @Transactional
    @Override
    public MDOUser authenticate( String username, String hashpass )
    {
        Query query = entityManager.createQuery( "from User as p where p.login = :login and p.password = :password" );
        query.setParameter( "login", username );
        query.setParameter( "password", hashpass );
        @SuppressWarnings( "unchecked" )
        List<MDOUser> results = query.getResultList();
        if ( results == null || results.size() <= 0 )
        {
            return null;
        }
        else
        {
            return results.get( 0 );
        }
    }

    private String getHashPassword( String newPassword )
    {
        String pass;
        MessageDigest md;
        try
        {

            byte[] hashPassword = newPassword.getBytes( "UTF-8" );

            md = MessageDigest.getInstance( "MD5" );
            byte[] encpass = md.digest( hashPassword );

            // converting byte array to Hexadecimal String
            StringBuilder sb = new StringBuilder( 2 * encpass.length );
            for ( byte b : encpass )
            {
                sb.append( String.format( "%02x", b & 0xff ) );
            }
            pass = sb.toString();

        }
        catch ( Exception e )
        {
            log.error( "failed!", e );
            pass = newPassword;
        }

        return pass;
    }

    @Override
    @Transactional
    public MDOUser saveUser( MDOUser user, boolean updatePassword, String newPassword )
    {
        // just to check if the password have changed, because we only save the hash of the password, not the password
        // itself
        MDOUser existingUser = getUserByLogin( user.getLogin() );
        if ( existingUser != null )
        {
            if ( updatePassword )
            {
                // hashing the password
                user.setPassword( getHashPassword( newPassword ) );
            }
        }
        else
        {
            // hashing the password
            user.setPassword( getHashPassword( newPassword ) );
        }

        this.save( user );
        return user;
    }

    @Override
    @Transactional
    public void removeUser( Long userSid )
    {
        MDOUser user = get( userSid );
        remove( user );
    }

    @Override
    @Transactional
    public boolean existUsers()
    {
        Query query = entityManager.createQuery( "SELECT COUNT(sid) FROM MDOUser" );
        Long usersCount = (Long) query.getSingleResult();
        if ( usersCount != null && usersCount.equals( 0L ) )
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    @Override
    @Transactional
    public List<MDOUser> getDLNAUsers()
    {
        Query query = entityManager.createQuery( "from MDOUser as a where (a.allowDLNA = true)" );

        @SuppressWarnings( "unchecked" )
        List<MDOUser> results = query.getResultList();
        return results;
    }

    @Override
    @Transactional
    public long countAllowedDLNAUsers()
    {
        Query query = entityManager.createQuery( "SELECT COUNT(sid) FROM MDOUser as p where p.allowDLNA=true" );
        Long usersCount = (Long) query.getSingleResult();
        if ( usersCount != null )
        {
            return usersCount;
        }
        else
        {
            return 0;
        }

    }

    @Override
    @Transactional
    public MDOUser getUserById( Long userSid )
    {
        MDOUser user = get( userSid );
        return user;
    }

}
