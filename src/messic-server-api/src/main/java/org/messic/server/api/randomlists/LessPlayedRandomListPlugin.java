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
import org.messic.server.datamodel.MDOUser;
import org.messic.server.datamodel.dao.DAOSong;
import org.messic.server.datamodel.dao.DAOUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LessPlayedRandomListPlugin
    implements RandomListPlugin
{
    @Autowired
    private DAOSong daoSong;

    @Autowired
    private DAOUser daoUser;

    @Override
    public RandomList getRandomList( User user )
    {
        MDOUser mdoUser = daoUser.getUserByLogin( user.getLogin() );
        if ( !mdoUser.getAllowStatistics() )
        {
            return null;
        }
        RandomList rl = new RandomList( "RandomListName-LessPlayed", "RandomListTitle-LessPlayed" );

        List<MDOSong> songs = daoSong.getAllOrderByLessPlayed( user.getLogin() );
        for ( int j = 0; j < songs.size() && rl.getSongs().size() < MAX_ELEMENTS; j++ )
        {
            MDOSong mdoSong = songs.get( j );
            Song song = new Song( mdoSong, true, true );
            rl.addSong( song );
        }

        return rl;
    }

}
