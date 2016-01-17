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

import org.messic.server.datamodel.MDOAlbum;
import org.messic.server.datamodel.MDOArtwork;
import org.messic.server.datamodel.MDOGenre;
import org.messic.server.datamodel.dao.DAOAlbum;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class DAOJPAAlbum
    extends DAOJPA<MDOAlbum>
    implements DAOAlbum
{

    public DAOJPAAlbum()
    {
        super( MDOAlbum.class );
    }

    @Override
    @Transactional
    public List<MDOAlbum> getAll( String username, MDOGenre genre )
    {
        Query query =
            entityManager.createQuery( "from MDOAlbum as a where (a.owner.login = :userName) and (a.genre.sid = :genreSid)" );
        query.setParameter( "userName", username );
        query.setParameter( "genreSid", genre.getSid() );

        @SuppressWarnings( "unchecked" )
        List<MDOAlbum> results = query.getResultList();
        return results;
    }

    @Override
    @Transactional
    public List<MDOAlbum> getAllDLNA()
    {
        Query query =
            entityManager.createQuery( "from MDOAlbum as a where (a.owner.allowDLNA = true) ORDER BY UPPER(a.name)" );

        @SuppressWarnings( "unchecked" )
        List<MDOAlbum> results = query.getResultList();
        return results;
    }

    @Override
    @Transactional
    public List<MDOAlbum> getAll( String username, int firstResult, int maxResult, boolean orderDesc,
                                  boolean orderByAuthor )
    {
        String squery = "from MDOAlbum as a where (a.owner.login = :userName)";
        if ( orderByAuthor )
        {
            squery = squery + " ORDER BY UPPER(a.author.name)";
        }
        else
        {
            squery = squery + " ORDER BY UPPER(a.name)";
        }
        if ( orderDesc )
        {
            squery = squery + " DESC";
        }

        Query query = entityManager.createQuery( squery );
        query.setParameter( "userName", username );
        if ( firstResult >= 0 )
        {
            query.setFirstResult( firstResult );
        }
        if ( maxResult >= 0 )
        {
            query.setMaxResults( maxResult );
        }

        @SuppressWarnings( "unchecked" )
        List<MDOAlbum> results = query.getResultList();
        return results;
    }

    @Override
    @Transactional
    public List<MDOAlbum> getRecent( String username, int maxAlbums )
    {
        Query query =
            entityManager.createQuery( "from MDOAlbum as a where (a.owner.login = :userName) AND (a.created is not null)  ORDER BY (a.created) DESC" );
        query.setParameter( "userName", username );
        query.setMaxResults( maxAlbums );

        @SuppressWarnings( "unchecked" )
        List<MDOAlbum> results = query.getResultList();
        return results;
    }

    @Override
    @Transactional
    public List<MDOAlbum> findSimilarAlbums( String albumName, String username )
    {
        Query query =
            entityManager.createQuery( "from MDOAlbum as a where (UPPER(a.name) LIKE :albumName) AND (a.owner.login = :userName)  ORDER BY UPPER(a.name)" );
        query.setParameter( "albumName", "%" + albumName.toUpperCase() + "%" );
        query.setParameter( "userName", username );

        @SuppressWarnings( "unchecked" )
        List<MDOAlbum> results = query.getResultList();
        return results;
    }

    @Override
    @Transactional
    public List<MDOAlbum> findAlbum( String albumName, String username )
    {
        Query query =
            entityManager.createQuery( "from MDOAlbum as a where (UPPER(a.name) = :albumName) AND (a.owner.login = :userName)" );
        query.setParameter( "albumName", albumName.toUpperCase() );
        query.setParameter( "userName", username );

        @SuppressWarnings( "unchecked" )
        List<MDOAlbum> results = query.getResultList();
        return results;
    }

    @Override
    @Transactional
    public List<MDOAlbum> getAll( long authorSid, String username, int firstResult, int maxResult, boolean orderDesc,
                                  boolean orderByAuthor )
    {

        String squery = "from MDOAlbum as a where (a.owner.login = :userName) AND (a.author.sid = :authorSid) ";
        if ( orderByAuthor )
        {
            squery = squery + " ORDER BY UPPER(a.author.name)";
        }
        else
        {
            squery = squery + " ORDER BY UPPER(a.name)";
        }
        if ( orderDesc )
        {
            squery = squery + " DESC";
        }

        Query query = entityManager.createQuery( squery );
        query.setParameter( "userName", username );
        query.setParameter( "authorSid", authorSid );

        if ( firstResult >= 0 )
        {
            query.setFirstResult( firstResult );
        }
        if ( maxResult >= 0 )
        {
            query.setMaxResults( maxResult );
        }

        @SuppressWarnings( "unchecked" )
        List<MDOAlbum> results = query.getResultList();
        return results;

        // String squery =
        // "from MDOAuthor as a where (a.owner.login = :userName) AND (a.sid = :authorSid) ORDER BY UPPER(a.name)";
        // Query query = entityManager.createQuery( squery );
        // query.setParameter( "userName", username );
        // query.setParameter( "authorSid", authorSid );
        //
        // @SuppressWarnings( "unchecked" )
        // List<MDOAuthor> results = query.getResultList();
        // if ( results != null && results.size() > 0 )
        // {
        // ArrayList<MDOAlbum> albums = new ArrayList<MDOAlbum>();
        // Set<MDOAlbum> setAlbums = results.get( 0 ).getAlbums();
        // if ( setAlbums != null && setAlbums.size() > 0 )
        // {
        // Iterator<MDOAlbum> albumsit = setAlbums.iterator();
        // while ( albumsit.hasNext() )
        // {
        // albums.add( albumsit.next() );
        // }
        // }
        // return albums;
        // }
        // return null;
    }

    @Override
    @Transactional
    public void setAlbumCover( long resourceSid, long albumSid, long userSid )
    {
        Query query =
            entityManager.createQuery( "UPDATE MDOArtwork a set a.cover = false WHERE a.owner.sid= :userSid AND a.album.sid= :albumSid" );
        query.setParameter( "userSid", userSid );
        query.setParameter( "albumSid", albumSid );
        query.executeUpdate();

        query =
            entityManager.createQuery( "UPDATE MDOArtwork a set a.cover = true WHERE a.owner.sid= :userSid AND a.album.sid= :albumSid AND a.sid = :resourceSid" );
        query.setParameter( "resourceSid", resourceSid );
        query.setParameter( "albumSid", albumSid );
        query.setParameter( "userSid", userSid );
        query.executeUpdate();
    }

    @Override
    @Transactional
    public MDOArtwork getAlbumCover( long albumSid, String username )
    {
        Query query =
            entityManager.createQuery( "from MDOArtwork a where (a.owner.login = :userName) AND (a.album.sid = :albumSid) AND (a.cover = true)" );
        query.setParameter( "userName", username );
        query.setParameter( "albumSid", albumSid );

        @SuppressWarnings( "unchecked" )
        List<MDOArtwork> results = query.getResultList();
        if ( results != null && results.size() > 0 )
        {
            return results.get( 0 );
        }
        return null;
    }

    @Override
    @Transactional
    public MDOAlbum getAlbum( long albumSid, String username )
    {
        Query query =
            entityManager.createQuery( "from MDOAlbum as a where (a.owner.login = :userName) AND (a.sid = :albumSid)" );
        query.setParameter( "userName", username );
        query.setParameter( "albumSid", albumSid );

        @SuppressWarnings( "unchecked" )
        List<MDOAlbum> results = query.getResultList();
        if ( results != null && results.size() > 0 )
        {
            return results.get( 0 );
        }
        return null;
    }

    @Override
    @Transactional
    public MDOAlbum getByName( String authorName, String albumName, String username )
    {
        Query query =
            entityManager.createQuery( "from MDOAlbum as a where (a.name = :albumName) AND (a.owner.login = :userName) AND (a.author.name = :authorName)" );
        query.setParameter( "albumName", "'" + albumName + "'" );
        query.setParameter( "userName", username );
        query.setParameter( "authorName", authorName.trim() );

        @SuppressWarnings( "unchecked" )
        List<MDOAlbum> results = query.getResultList();
        if ( results != null && results.size() > 0 )
        {
            return results.get( 0 );
        }
        return null;
    }

    @Override
    @Transactional
    public List<MDOAlbum> findAlbumsBasedOnDate( String username, int fromYear, int toYear )
    {
        Query query =
            entityManager.createQuery( "from MDOAlbum as a where (a.owner.login = :userName) AND (a.year >= :fromYear) AND (a.year <= :toYear) ORDER BY a.year" );
        query.setParameter( "userName", username );
        query.setParameter( "fromYear", fromYear );
        query.setParameter( "toYear", toYear );

        @SuppressWarnings( "unchecked" )
        List<MDOAlbum> results = query.getResultList();
        return results;

    }

    @Override
    @Transactional
    public List<MDOAlbum> findSimilarAlbums( long authorSid, String albumName, String username, int firstResult,
                                             int maxResults, boolean orderDesc, boolean orderByAuthor )
    {
        String squery =
            "from MDOAlbum as a where (UPPER(a.name) LIKE :albumName) AND (a.owner.login = :userName)"
                + ( authorSid > 0 ? " AND (a.author.sid = :authorSid)" : "" ) + " ORDER BY";
        if ( orderByAuthor )
        {
            squery = squery + " UPPER(a.author.name)";
        }
        else
        {
            squery = squery + " UPPER(a.name)";
        }
        if ( orderDesc )
        {
            squery = squery + " DESC";
        }

        Query query = entityManager.createQuery( squery );
        query.setParameter( "albumName", "%" + albumName.toUpperCase() + "%" );
        query.setParameter( "userName", username );
        if ( authorSid > 0 )
        {
            query.setParameter( "authorSid", authorSid );
        }
        if ( firstResult >= 0 )
        {
            query.setFirstResult( firstResult );
        }
        if ( maxResults >= 0 )
        {
            query.setMaxResults( maxResults );
        }

        @SuppressWarnings( "unchecked" )
        List<MDOAlbum> results = query.getResultList();
        return results;
    }

    @Override
    @Transactional
    public void setNullGenre( MDOGenre genre )
    {
        // ((a.owner.login = :userName) AND (
        Query query =
            entityManager.createQuery( "UPDATE MDOAlbum a set a.genre = null WHERE a.owner.sid= :userSid AND a.genre.sid = :genreSid" );
        // query.setParameter( "userName", genre.getOwner().getLogin() );
        query.setParameter( "genreSid", genre.getSid() );
        query.setParameter( "userSid", genre.getOwner().getSid() );

        query.executeUpdate();
    }

    @Override
    @Transactional
    public List<MDOAlbum> getAllOfGenre( long genreSid, String username, int firstResult, int maxResults,
                                         boolean orderDesc, boolean orderByAuthor )
    {
        String squery =
            "from MDOAlbum as a where (a.owner.login = :userName) AND (a.genre.sid = :genreSid)  ORDER BY UPPER(a.genre.name),";
        if ( orderByAuthor )
        {
            squery = squery + " UPPER(a.author.name)";
        }
        else
        {
            squery = squery + " UPPER(a.name)";
        }
        if ( orderDesc )
        {
            squery = squery + " DESC";
        }

        Query query = entityManager.createQuery( squery );
        query.setParameter( "userName", username );
        query.setParameter( "genreSid", genreSid );
        if ( firstResult >= 0 )
        {
            query.setFirstResult( firstResult );
        }
        if ( maxResults >= 0 )
        {
            query.setMaxResults( maxResults );
        }

        @SuppressWarnings( "unchecked" )
        List<MDOAlbum> results = query.getResultList();
        return results;
    }

    @Override
    @Transactional
    public int findOldestAlbum( String username )
    {
        Query query =
            entityManager.createQuery( "Select MIN(a.year) from MDOAlbum as a where (a.owner.login = :userName)" );
        query.setParameter( "userName", username );

        @SuppressWarnings( "rawtypes" )
        List result = query.getResultList();
        if ( result != null )
        {
            Object obj = result.get( 0 );
            if ( obj != null )
            {
                int min = (Integer) result.get( 0 );
                return min;
            }
            else
            {
                return 0;
            }
        }
        return 0;
    }

}
