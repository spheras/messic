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

import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.messic.server.api.datamodel.RandomList;
import org.messic.server.api.datamodel.Song;
import org.messic.server.api.datamodel.User;
import org.messic.server.datamodel.MDOAlbum;
import org.messic.server.datamodel.MDOSong;
import org.messic.server.datamodel.dao.DAOAlbum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DateRandomListPlugin
    implements RandomListPlugin
{
    @Autowired
    private DAOAlbum daoAlbum;

    @Override
    public RandomList getRandomList( User user )
    {
        //int year = Calendar.getInstance().get( Calendar.YEAR );
        int fromYear = daoAlbum.findOldestAlbum( user.getLogin() ); // Util.randInt( 1920, year );
        int toYear = fromYear + 10;// Util.randInt( fromYear, year );
        List<MDOAlbum> albums = daoAlbum.findAlbumsBasedOnDate( user.getLogin(), fromYear, toYear );

        if ( albums != null && albums.size() > 0 )
        {
            RandomList rl = new RandomList( "RandomListName-Date", "RandomListTitle-Date" );
            rl.addDetail( fromYear + " - " + toYear );

            for ( int i = 0; i < albums.size() && rl.getSongs().size() < MAX_ELEMENTS; i++ )
            {
                List<MDOSong> songs = albums.get( i ).getSongs();
                for ( int j = 0; j < songs.size() && rl.getSongs().size() < MAX_ELEMENTS; j++ )
                {
                    MDOSong mdoSong = songs.get( j );
                    Song song = new Song( mdoSong, true, true );
                    rl.addSong( song );
                }

            }

            long seed = System.nanoTime();
            if ( rl.getSongs() != null )
            {
                Collections.shuffle( rl.getSongs(), new Random( seed ) );
            }
            return rl;
        }
        return null;
    }

}
