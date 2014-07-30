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

import org.messic.server.datamodel.MDOSongStatistics;
import org.messic.server.datamodel.dao.DAOSongStatistics;
import org.springframework.stereotype.Component;

@Component
public class DAOJPASongStatistics
    extends DAOJPA<MDOSongStatistics>
    implements DAOSongStatistics
{

    public DAOJPASongStatistics()
    {
        super( MDOSongStatistics.class );
    }

    @Override
    public MDOSongStatistics getStatistics( String username, long songSid )
    {
        Query query =
            entityManager.createQuery( "from MDOSongStatistics as a where (a.song.owner.login = :userName AND a.song.sid = :songSid)" );
        query.setParameter( "userName", username );
        query.setParameter( "songSid", songSid );

        @SuppressWarnings( "unchecked" )
        List<MDOSongStatistics> results = query.getResultList();
        if ( results != null && results.size() > 0 )
        {
            return results.get( 0 );
        }
        return null;
    }

}
