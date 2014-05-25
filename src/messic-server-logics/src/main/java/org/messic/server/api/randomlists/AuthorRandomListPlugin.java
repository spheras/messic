package org.messic.server.api.randomlists;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.messic.server.api.datamodel.RandomList;
import org.messic.server.api.datamodel.Song;
import org.messic.server.api.datamodel.User;
import org.messic.server.datamodel.MDOAlbum;
import org.messic.server.datamodel.MDOAuthor;
import org.messic.server.datamodel.MDOSong;
import org.messic.server.datamodel.dao.DAOAuthor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthorRandomListPlugin
    implements RandomListPlugin
{
    @Autowired
    private DAOAuthor daoAuthor;

    @Override
    public RandomList getRandomList( User user )
    {
        // second list, getting all the songs of an author
        List<MDOAuthor> randomAuthorList = daoAuthor.getRandomAuthors( user.getLogin(), 1 );
        if ( randomAuthorList != null && randomAuthorList.size() > 0 )
        {
            RandomList rl = new RandomList( "RandomListName-Author", "RandomListTitle-Author" );
            rl.addDetail( randomAuthorList.get( 0 ).getName() );
            Iterator<MDOAlbum> albumsit = randomAuthorList.get( 0 ).getAlbums().iterator();
            while ( albumsit.hasNext() )
            {
                MDOAlbum album = albumsit.next();
                for ( MDOSong mdoSong : album.getSongs() )
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
