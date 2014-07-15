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

import java.util.List;

import javax.persistence.Query;

import org.messic.server.datamodel.MDOGenre;
import org.messic.server.datamodel.MDOUser;
import org.messic.server.datamodel.dao.DAOGenre;
import org.springframework.stereotype.Component;

@Component
public class DAOJPAGenre
    extends DAOJPA<MDOGenre>
    implements DAOGenre
{

    public DAOJPAGenre()
    {
        super( MDOGenre.class );
    }

    @Override
    public List<MDOGenre> getAll( String username )
    {
        Query query = entityManager.createQuery( "from MDOGenre as a where (a.owner.login= :userName)" );
        query.setParameter( "userName", username );
        @SuppressWarnings( "unchecked" )
        List<MDOGenre> results = query.getResultList();
        return results;
    }

    @Override
    public List<MDOGenre> getRandomGenre( String username, int number )
    {
        Query query =
            entityManager.createQuery( "from MDOGenre as a where (1=1 AND a.owner.login= :userName) order by rand()" );
        query.setParameter( "userName", username );
        query.setMaxResults( 5 );
        @SuppressWarnings( "unchecked" )
        List<MDOGenre> results = query.getResultList();
        return results;
    }

    /**
     * @TODO limit to the valid genres for the user?
     */
    @Override
    public List<MDOGenre> findSimilarGenre( String genreName, String username )
    {
        Query query =
            entityManager.createQuery( "from MDOGenre as a where (a.owner.login = :userName) AND (a.name LIKE :genreName)" );
        query.setParameter( "genreName", "%" + genreName + "%" );
        query.setParameter( "userName", username );

        @SuppressWarnings( "unchecked" )
        List<MDOGenre> results = query.getResultList();
        return results;
    }

    @Override
    public MDOGenre getGenre( String username, Long genreSid )
    {
        Query query =
            entityManager.createQuery( "from MDOGenre as a where (a.owner.login = :userName) AND (a.sid= :genreSid)" );
        query.setParameter( "genreSid", genreSid );
        query.setParameter( "userName", username );

        @SuppressWarnings( "unchecked" )
        List<MDOGenre> results = query.getResultList();
        if ( results != null && results.size() > 0 )
        {
            return results.get( 0 );
        }
        else
        {
            return null;
        }
    }

    public MDOGenre getGenre( String username, String genreName )
    {
        Query query =
            entityManager.createQuery( "from MDOGenre as a where (a.owner.login = :userName) AND (a.name = :genreName)" );
        query.setParameter( "genreName", "'" + genreName + "'" );
        query.setParameter( "userName", username );

        @SuppressWarnings( "unchecked" )
        List<MDOGenre> results = query.getResultList();
        if ( results != null && results.size() > 0 )
        {
            return results.get( 0 );
        }
        else
        {
            return null;
        }
    }

    public MDOGenre getByName( String username, String genreName )
    {
        Query query =
            entityManager.createQuery( "from MDOGenre as a where (a.owner.login = :userName) AND (a.name = :genreName)" );
        query.setParameter( "genreName", genreName );
        query.setParameter( "userName", username );

        @SuppressWarnings( "unchecked" )
        List<MDOGenre> results = query.getResultList();
        if ( results != null && results.size() > 0 )
        {
            return results.get( 0 );
        }
        return null;
    }

}
