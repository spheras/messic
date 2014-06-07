/*
 * Copyright (C) 2013 José Amuedo
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
package org.messic.server.api.randomlists;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.messic.server.api.datamodel.RandomList;
import org.messic.server.api.datamodel.Song;
import org.messic.server.api.datamodel.User;
import org.messic.server.datamodel.MDOAlbum;
import org.messic.server.datamodel.MDOGenre;
import org.messic.server.datamodel.MDOSong;
import org.messic.server.datamodel.dao.DAOAlbum;
import org.messic.server.datamodel.dao.DAOGenre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GenreRandomListPlugin
    implements RandomListPlugin
{
    @Autowired
    private DAOGenre daoGenre;

    @Autowired
    private DAOAlbum daoAlbum;

    @Override
    public RandomList getRandomList( User user )
    {
        List<MDOGenre> randomGenreList = daoGenre.getRandomGenre( user.getLogin(), 1 );
        if ( randomGenreList != null && randomGenreList.size() > 0 )
        {
            List<MDOAlbum> albumList = daoAlbum.getAll( user.getLogin(), randomGenreList.get( 0 ) );

            RandomList rl = new RandomList( "RandomListName-Genre", "RandomListTitle-Genre" );
            rl.addDetail( randomGenreList.get( 0 ).getName() );

            for ( int i = 0; i < albumList.size(); i++ )
            {
                for ( MDOSong mdoSong : albumList.get( i ).getSongs() )
                {
                    Song song = new Song( mdoSong );
                    rl.addSong( song );
                }

            }

            long seed = System.nanoTime();
            Collections.shuffle( rl.getSongs(), new Random( seed ) );
            return rl;
        }
        return null;
    }

}
