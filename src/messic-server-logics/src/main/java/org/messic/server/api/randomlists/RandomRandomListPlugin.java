package org.messic.server.api.randomlists;

import java.util.Collections;
import java.util.List;
import java.util.Random;

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
        List<MDOSong> songs = daoSong.getAll( user.getLogin() );
        if ( songs.size() > 0 )
        {
            RandomList rl = new RandomList( "RandomListName-Random", "RandomListTitle-Random" );
            long seed = System.nanoTime();
            Collections.shuffle( songs, new Random( seed ) );

            int current = 0;
            int max = 255;
            for ( MDOSong mdoSong : songs )
            {
                if ( current < max )
                {
                    Song song = new Song( mdoSong );
                    rl.addSong( song );
                    current++;
                }
                else
                {
                    break;
                }
            }
            return rl;
        }
        return null;
    }
}
