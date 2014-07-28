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

import org.messic.server.datamodel.MDOPlaylist;
import org.messic.server.datamodel.dao.DAOPlaylist;
import org.springframework.stereotype.Component;

@Component
public class DAOJPAPlaylist
    extends DAOJPA<MDOPlaylist>
    implements DAOPlaylist
{

    public DAOJPAPlaylist()
    {
        super( MDOPlaylist.class );
    }

    @Override
    public MDOPlaylist get( String username, long sid )
    {
        Query query =
            entityManager.createQuery( "from MDOPlaylist as a where (a.owner.login = :userName AND a.sid = :playlistSid)" );
        query.setParameter( "userName", username );
        query.setParameter( "playlistSid", sid );

        @SuppressWarnings( "unchecked" )
        List<MDOPlaylist> results = query.getResultList();
        if ( results != null && results.size() > 0 )
        {
            return results.get( 0 );
        }
        return null;
    }

    @Override
    public List<MDOPlaylist> getAll( String username )
    {
        Query query = entityManager.createQuery( "from MDOPlaylist as a where (a.owner.login = :userName)" );
        query.setParameter( "userName", username );

        @SuppressWarnings( "unchecked" )
        List<MDOPlaylist> results = query.getResultList();
        return results;
    }

    @Override
    public MDOPlaylist getByName( String username, String name )
    {
        Query query =
            entityManager.createQuery( "from MDOPlaylist as a where (a.owner.login = :userName) and (a.name = :playlistName)" );
        query.setParameter( "userName", username );
        query.setParameter( "playlistName", name );

        @SuppressWarnings( "unchecked" )
        List<MDOPlaylist> results = query.getResultList();
        if ( results != null && results.size()>0 )
        {
            return results.get( 0 );
        }
        else
        {
            return null;
        }
    }
}
