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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Query;

import org.messic.server.datamodel.MDOSong;
import org.messic.server.datamodel.dao.DAOSong;
import org.springframework.stereotype.Component;

@Component
public class DAOJPASong
    extends DAOJPA<MDOSong>
    implements DAOSong
{

    public DAOJPASong()
    {
        super( MDOSong.class );
    }

    @Override
    public MDOSong get( String username, long sid )
    {
        Query query =
            entityManager.createQuery( "from MDOSong as a where (a.owner.login = :userName AND a.sid = :songSid)" );
        query.setParameter( "userName", username );
        query.setParameter( "songSid", sid );

        @SuppressWarnings( "unchecked" )
        List<MDOSong> results = query.getResultList();
        if ( results != null && results.size() > 0 )
        {
            return results.get( 0 );
        }
        return null;
    }

    @Override
    public List<MDOSong> getAll( String username )
    {
        Query query = entityManager.createQuery( "from MDOSong as a where (a.owner.login = :userName)" );
        query.setParameter( "userName", username );

        @SuppressWarnings( "unchecked" )
        List<MDOSong> results = query.getResultList();
        return results;
    }

    @Override
    public List<MDOSong> getAllDLNA()
    {
        Query query = entityManager.createQuery( "from MDOSong as a where (a.owner.allowDLNA= true)" );

        @SuppressWarnings( "unchecked" )
        List<MDOSong> results = query.getResultList();
        return results;
    }

    @Override
    public List<MDOSong> genericFind( String username, List<String> searches )
    {
        HashMap<Long, MDOSong> finalResult = new HashMap<Long, MDOSong>();
        for ( int i = 0; i < searches.size(); i++ )
        {
            String content = searches.get( i );

            String sql = "from MDOSong as a WHERE (a.owner.login = :userName) AND (";
            sql = sql + "(UPPER(a.name) LIKE :what) OR ";
            sql = sql + "(UPPER(a.album.name) LIKE :what) OR ";
            sql = sql + "(UPPER(a.album.genre.name) LIKE :what) OR ";
            try
            {
                Integer.parseInt( content );
                sql = sql + "(a.album.year LIKE :what) OR ";
            }
            catch ( NumberFormatException nfe )
            {
            }
            sql = sql + "(UPPER(a.album.comments) LIKE :what) OR ";
            sql = sql + "(UPPER(a.album.author.name) LIKE :what)";
            sql = sql + ")";

            Query query = entityManager.createQuery( sql );
            query.setParameter( "userName", username );
            query.setParameter( "what", "%" + content.toUpperCase() + "%" );

            @SuppressWarnings( "unchecked" )
            List<MDOSong> results = query.getResultList();
            for ( MDOSong mdoSong : results )
            {
                finalResult.put( mdoSong.getSid(), mdoSong );
            }
        }

        ArrayList<MDOSong> result = new ArrayList<MDOSong>();
        Iterator<MDOSong> songsit = finalResult.values().iterator();
        while ( songsit.hasNext() )
        {
            result.add( songsit.next() );
        }
        return result;
    }

    @Override
    public List<MDOSong> getLovedSongs( String username )
    {
        Query query =
            entityManager.createQuery( "from MDOSong as a where (a.owner.login = :userName) and (a.rate=2 or a.rate=3) ORDER BY (a.statistics.timesplayed) DESC" );
        query.setParameter( "userName", username );

        @SuppressWarnings( "unchecked" )
        List<MDOSong> results = query.getResultList();
        return results;
    }

    @Override
    public List<MDOSong> getAllOrderByMostPlayed( String username )
    {
        Query query =
            entityManager.createQuery( "from MDOSong as a where (a.owner.login = :userName) and (a.statistics.timesplayed>0) ORDER BY (a.statistics.timesplayed) DESC" );
        query.setParameter( "userName", username );

        @SuppressWarnings( "unchecked" )
        List<MDOSong> results = query.getResultList();
        return results;
    }

    @Override
    public List<MDOSong> getAllOrderByLessPlayed( String username )
    {
        Query query =
            entityManager.createQuery( "from MDOSong as a where (a.owner.login = :userName) ORDER BY (a.statistics.timesplayed) ASC" );
        query.setParameter( "userName", username );

        @SuppressWarnings( "unchecked" )
        List<MDOSong> results = query.getResultList();

        query =
            entityManager.createQuery( "from MDOSong as a where (a.statistics is null) AND (a.owner.login = :userName)" );
        query.setParameter( "userName", username );

        @SuppressWarnings( "unchecked" )
        List<MDOSong> otherResults = query.getResultList();

        otherResults.addAll( results );
        return otherResults;
    }
}
