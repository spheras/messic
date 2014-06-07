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
package org.messic.server.datamodel.jpaimpl;

import java.security.MessageDigest;
import java.util.List;

import javax.persistence.Query;

import org.messic.server.datamodel.MDOUser;
import org.messic.server.datamodel.dao.DAOUser;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DAOJPAUser
    extends DAOJPA<MDOUser>
    implements DAOUser
{

    public DAOJPAUser()
    {
        super( MDOUser.class );
    }

    /**
     * Get User by username, null if not found
     * 
     * @param username {@link String} username to find
     * @return {@link MDOUser} user found, null if not found
     */
    @Override
    public MDOUser getUserByLogin(String username)
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
    public MDOUser authenticate(String username, String hashpass)
    {
        Query query =
            entityManager.createQuery( "from User as p where p.login = :login and p.password = :password" );
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

	@Override
	@Transactional
	public MDOUser createUser(String login, String password, String name, String email, byte[] avatar,
			boolean administrator, String storePath) {
		String pass;
		MessageDigest md;
		try {
			
			byte[] hashPassword = password.getBytes("UTF-8");
			
			md = MessageDigest.getInstance("MD5");
			byte[] encpass = md.digest(hashPassword);
			
            //converting byte array to Hexadecimal String
	        StringBuilder sb = new StringBuilder(2*encpass.length);
	        for(byte b : encpass){
	        	sb.append(String.format("%02x", b&0xff));
	        }          
	        pass = sb.toString();
	        
		} catch (Exception e) {
			e.printStackTrace();
			pass = password;
		}
		
		MDOUser newUser = new MDOUser(name, email, avatar, login, pass, administrator, storePath);
		save(newUser);
		return newUser;
	}

	@Override
	public MDOUser updatePassword(Long userSid, String newPassword) {
		MDOUser user = get(userSid);
		
		String pass;
		MessageDigest md;
		try {
			
			byte[] hashPassword = newPassword.getBytes("UTF-8");
			
			md = MessageDigest.getInstance("MD5");
			byte[] encpass = md.digest(hashPassword);
			
            //converting byte array to Hexadecimal String
	        StringBuilder sb = new StringBuilder(2*encpass.length);
	        for(byte b : encpass){
	        	sb.append(String.format("%02x", b&0xff));
	        }          
	        pass = sb.toString();
	        
		} catch (Exception e) {
			e.printStackTrace();
			pass = newPassword;
		}
		
		user.setPassword(pass);
		save(user);
		return user;
	}

	@Override
	@Transactional
	public MDOUser updateUserData(Long userSid, String name, String email, byte[] avatar, String storePath) {
		MDOUser user = get(userSid);
		user.setName(name);
		user.setEmail(email);
		if(avatar!=null)
		{
			user.setAvatar(avatar);
		}
		user.setStorePath(storePath);
		save(user);
		return user;
	}

	@Override
	@Transactional
	public void removeUser(Long userSid) {
		MDOUser user = get(userSid);
		remove(user);
	}

	@Override
	public boolean existUsers() {
		Query query = entityManager.createQuery("SELECT COUNT(sid) FROM MDOUser");
		Long usersCount = (Long)query.getSingleResult();
	    if(usersCount!=null && usersCount.equals(0L))
	    {
	    	return false;
	    }
	    else
	    {
	    	return true;
	    }
	}

	@Override
	public MDOUser getUserById(Long userSid) {
		MDOUser user = get(userSid);
		return user;
	}

}
