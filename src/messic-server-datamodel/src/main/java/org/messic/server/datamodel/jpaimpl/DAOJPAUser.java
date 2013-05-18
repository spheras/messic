package org.messic.server.datamodel.jpaimpl;

import java.util.List;

import javax.persistence.Query;

import org.messic.server.datamodel.MDOUser;
import org.messic.server.datamodel.dao.DAOUser;
import org.springframework.stereotype.Component;

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
        Query query = entityManager.createQuery( "from MDOUser as p where p.username = :username" );
        query.setParameter( "username", username );
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
            entityManager.createQuery( "from User as p where p.username = :username and p.hashpass = :hashpass" );
        query.setParameter( "username", username );
        query.setParameter( "hashpass", hashpass );
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

}
