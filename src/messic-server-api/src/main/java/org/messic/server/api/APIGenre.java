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
package org.messic.server.api;

import java.util.List;

import org.messic.server.api.datamodel.Genre;
import org.messic.server.api.datamodel.User;
import org.messic.server.api.exceptions.SidNotFoundMessicException;
import org.messic.server.datamodel.MDOAlbum;
import org.messic.server.datamodel.MDOGenre;
import org.messic.server.datamodel.MDOUser;
import org.messic.server.datamodel.dao.DAOAlbum;
import org.messic.server.datamodel.dao.DAOGenre;
import org.messic.server.datamodel.dao.DAOUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class APIGenre
{
    @Autowired
    private DAOGenre daoGenre;

    @Autowired
    private DAOAlbum daoAlbum;

    @Autowired
    private DAOUser daoUser;

    @Transactional
    public List<Genre> getAll( User user )
    {
        List<MDOGenre> genres = daoGenre.getAll( user.getLogin() );
        return Genre.transform( genres );
    }

    @Transactional
    public void removeGenre( User user, Long genreSid )
    {
        MDOGenre genre = daoGenre.getGenre( user.getLogin(), genreSid );
        // First, putting all ablums to null genre
        daoAlbum.setNullGenre( genre );

        daoGenre.remove( genre );
    }

    @Transactional
    public List<Genre> findSimilar( User user, String genreName )
    {
        List<MDOGenre> genres = daoGenre.findSimilarGenre( genreName, user.getLogin() );
        return Genre.transform( genres );
    }

    public void renameGenre( User user, Long genreSid, String genreName )
        throws SidNotFoundMessicException
    {
        MDOGenre genre = daoGenre.getGenre( user.getLogin(), genreSid );
        if ( genre != null )
        {
            genre.setName( genreName );
            daoGenre.save( genre );
        }
        else
        {
            throw new SidNotFoundMessicException();
        }
    }

    public long fuseGenres( User user, List<Long> genreSids, String newGenreName )
    {
        MDOUser mdouser = daoUser.getUserByLogin( user.getLogin() );

        // 1st, creating the new genre
        MDOGenre newGenre = new MDOGenre( newGenreName, mdouser );
        daoGenre.save( newGenre );

        for ( int i = 0; i < genreSids.size(); i++ )
        {

            // second, replacing current genre by the new one
            MDOGenre genre = this.daoGenre.getGenre( user.getLogin(), genreSids.get( i ) );
            List<MDOAlbum> albums = this.daoAlbum.getAll( user.getLogin(), genre );
            for ( MDOAlbum mdoAlbum : albums )
            {
                mdoAlbum.setGenre( newGenre );
                daoAlbum.save( mdoAlbum );
            }

            // third, removing all olds genres
            daoGenre.remove( genre );
        }

        return daoGenre.getByName( user.getLogin(), newGenreName ).getSid();
    }
}
