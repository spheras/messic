package org.messic.server.datamodel.jpaimpl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
    public MDOUser getUser( String username )
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
        return ( getUser( username ) != null );
    }

    /**
     * TODO
     * 
     * @param username
     * @param hashpass
     * @return
     */
    public MDOUser authenticate( String username, String hashpass )
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
	public MDOUser createUser(String login, String password, String name,
			boolean administrator) {
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
		
		MDOUser newUser = new MDOUser(name, login, pass, administrator);
		save(newUser);
		return newUser;
	}

	@Override
	public MDOUser updatePassword(Long userSid, String newPassword) {
		MDOUser user = get(userSid);
		user.setPassword(newPassword);
		save(user);
		return user;
	}

	@Override
	@Transactional
	public MDOUser updateUserData(Long userSid, String name) {
		MDOUser user = get(userSid);
		user.setName(name);
		save(user);
		return user;
	}

	@Override
	@Transactional
	public void removeUser(Long userSid) {
		MDOUser user = get(userSid);
		remove(user);
	}

}
