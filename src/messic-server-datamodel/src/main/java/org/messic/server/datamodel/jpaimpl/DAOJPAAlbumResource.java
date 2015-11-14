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

import org.messic.server.datamodel.MDOAlbumResource;
import org.messic.server.datamodel.dao.DAOAlbumResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class DAOJPAAlbumResource
    extends DAOJPA<MDOAlbumResource>
    implements DAOAlbumResource
{

    public DAOJPAAlbumResource()
    {
        super( MDOAlbumResource.class );
    }

    @Override
    @Transactional
    public MDOAlbumResource get( String username, long sid )
    {
        String squery = "from MDOAlbumResource as a where (a.owner.login = :userName AND a.sid = :sid)";
        Query query = entityManager.createQuery( squery );
        query.setParameter( "userName", username );
        query.setParameter( "sid", sid );

        @SuppressWarnings( "unchecked" )
        List<MDOAlbumResource> results = query.getResultList();
        if ( results != null && results.size() > 0 )
        {
            return results.get( 0 );
        }
        return null;
    }

    public void removeVolumeAlbumResources( String username, long albumSid, int volume )
    {

        // sorry, but h2 doesn't support JOINS AND CROSS under delete statemente, and the followng sentence will be
        // converted in
        // DELETE FROM GENRE CROSS[*] JOIN USERS MDOUSER1_ WHERE LOGIN=?
        // Query query = entityManager.createQuery( "delete from MDOGenre as g where (g.owner.login = :userName)" );
        // https://code.google.com/p/h2database/issues/detail?id=504

        // Query query =
        // entityManager.createQuery(
        // "delete MDOAlbumResource as a where (a.owner.login= :username AND a.album.sid = :albumsid AND a.volume = :volume)"
        // );
        // query.setParameter( "userName", username );
        // query.setParameter( "albumsid", albumSid );
        // query.setParameter( "volume", volume );
        //
        // query.executeUpdate();

        String squery =
            "from MDOSong as a where (a.owner.login= :username AND a.volume = :volume AND a.album.sid = :albumsid)";

        Query query = entityManager.createQuery( squery );
        query.setParameter( "username", username );
        query.setParameter( "albumsid", albumSid );
        query.setParameter( "volume", volume );
        @SuppressWarnings( "unchecked" )
        List<MDOAlbumResource> results1 = query.getResultList();
        if ( results1 != null && results1.size() > 0 )
        {
            for ( MDOAlbumResource mdoAlbumResource : results1 )
            {
                remove( mdoAlbumResource );
            }
        }

        squery =
            "from MDOArtwork as a where (a.owner.login= :username AND a.volume = :volume AND a.album.sid = :albumsid)";
        query = entityManager.createQuery( squery );
        query.setParameter( "username", username );
        query.setParameter( "albumsid", albumSid );
        query.setParameter( "volume", volume );
        @SuppressWarnings( "unchecked" )
        List<MDOAlbumResource> results2 = query.getResultList();
        if ( results2 != null && results2.size() > 0 )
        {
            for ( MDOAlbumResource mdoAlbumResource : results2 )
            {
                remove( mdoAlbumResource );
            }
        }

        squery =
            "from MDOOtherResource as a where (a.owner.login= :username AND a.volume = :volume AND a.album.sid = :albumsid)";
        query = entityManager.createQuery( squery );
        query.setParameter( "username", username );
        query.setParameter( "albumsid", albumSid );
        query.setParameter( "volume", volume );
        @SuppressWarnings( "unchecked" )
        List<MDOAlbumResource> results3 = query.getResultList();
        if ( results3 != null && results3.size() > 0 )
        {
            for ( MDOAlbumResource mdoAlbumResource : results3 )
            {
                remove( mdoAlbumResource );
            }
        }
    }

}
