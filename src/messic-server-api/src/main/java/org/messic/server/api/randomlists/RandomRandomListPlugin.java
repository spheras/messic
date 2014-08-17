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
package org.messic.server.api.randomlists;

import java.util.List;

import org.messic.server.api.datamodel.RandomList;
import org.messic.server.api.datamodel.Song;
import org.messic.server.api.datamodel.User;
import org.messic.server.datamodel.MDOSong;
import org.messic.server.datamodel.dao.DAOSong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RandomRandomListPlugin
    implements RandomListPlugin
{
    @Autowired
    private DAOSong daoSong;

    @Override
    public RandomList getRandomList( User user )
    {
        // first list, getting all the songs shuffled
        List<MDOSong> songs = daoSong.getRandom( user.getLogin(), MAX_ELEMENTS );
        if ( songs.size() > 0 )
        {
            RandomList rl = new RandomList( "RandomListName-Random", "RandomListTitle-Random" );
            for ( int i = 0; i < songs.size() && i < MAX_ELEMENTS; i++ )
            {
                MDOSong mdoSong = songs.get( i );
                Song song = new Song( mdoSong, true, true );
                rl.addSong( song );
            }
            return rl;
        }
        return null;
    }

    @Override
    public String getName()
    {
        return getClass().getName();
    }
}
