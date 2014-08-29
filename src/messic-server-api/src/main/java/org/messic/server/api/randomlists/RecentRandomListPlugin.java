package org.messic.server.api.randomlists;

import java.util.List;

import org.messic.server.api.datamodel.RandomList;
import org.messic.server.api.datamodel.Song;
import org.messic.server.api.datamodel.User;
import org.messic.server.datamodel.MDOAlbum;
import org.messic.server.datamodel.MDOSong;
import org.messic.server.datamodel.MDOUser;
import org.messic.server.datamodel.dao.DAOAlbum;
import org.messic.server.datamodel.dao.DAOUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RecentRandomListPlugin
    implements RandomListPlugin
{
    @Autowired
    private DAOAlbum daoAlbum;

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
        RandomList rl = new RandomList( "RandomListName-Recent", "RandomListTitle-Recent" );

        List<MDOAlbum> albums = daoAlbum.getRecent( user.getLogin(), 3 );
        for ( int i = 0; i < albums.size(); i++ )
        {
            MDOAlbum album = albums.get( i );
            List<MDOSong> songs = album.getSongs();
            for ( int j = 0; j < songs.size() && rl.getSongs().size() < MAX_ELEMENTS; j++ )
            {
                MDOSong mdoSong = songs.get( j );
                Song song = new Song( mdoSong, true, true );
                rl.addSong( song );
            }
        }

        return rl;
    }

    @Override
    public String getName()
    {
        return getClass().getName();
    }

}
