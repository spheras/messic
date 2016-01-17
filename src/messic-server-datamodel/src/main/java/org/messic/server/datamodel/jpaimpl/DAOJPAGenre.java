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
import org.messic.server.datamodel.dao.DAOGenre;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class DAOJPAGenre
    extends DAOJPA<MDOGenre>
    implements DAOGenre
{

    public DAOJPAGenre()
    {
        super( MDOGenre.class );
    }

    @Override
    @Transactional
    public List<MDOGenre> getAll( String username )
    {
        Query query =
            entityManager.createQuery( "from MDOGenre as a where (a.owner.login= :userName) ORDER BY UPPER(a.name)" );
        query.setParameter( "userName", username );
        @SuppressWarnings( "unchecked" )
        List<MDOGenre> results = query.getResultList();
        return results;
    }

    @Override
    @Transactional
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
    @Transactional
    public List<MDOGenre> findSimilarGenre( String genreName, String username )
    {
        Query query =
            entityManager.createQuery( "from MDOGenre as a where (a.owner.login = :userName) AND (UPPER(a.name) LIKE :genreName)" );
        query.setParameter( "genreName", "%" + genreName.toUpperCase() + "%" );
        query.setParameter( "userName", username );

        @SuppressWarnings( "unchecked" )
        List<MDOGenre> results = query.getResultList();
        return results;
    }

    @Override
    @Transactional
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

    @Override
    @Transactional
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

    @Override
    @Transactional
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

    @Override
    @Transactional
    public void removeAllGenres( String username )
    {
        // sorry, but h2 doesn't support JOINS AND CROSS under delete statemente, and the followng sentence will be
        // converted in
        // DELETE FROM GENRE CROSS[*] JOIN USERS MDOUSER1_ WHERE LOGIN=?
        // Query query = entityManager.createQuery( "delete from MDOGenre as g where (g.owner.login = :userName)" );
        // https://code.google.com/p/h2database/issues/detail?id=504
        
        // so, we need to first get the genres and after delete them manually :(
        List<MDOGenre> genres = getAll( username );

        for ( MDOGenre mdoGenre : genres )
        {
            remove( mdoGenre );
        }

    }

}
