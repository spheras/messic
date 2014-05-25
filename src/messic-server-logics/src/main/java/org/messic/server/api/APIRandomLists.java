package org.messic.server.api;

import java.util.ArrayList;
import java.util.List;

import org.messic.server.api.datamodel.RandomList;
import org.messic.server.api.datamodel.User;
import org.messic.server.api.randomlists.RandomListPlugin;
import org.messic.server.datamodel.dao.DAOAuthor;
import org.messic.server.datamodel.dao.DAOSong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class APIRandomLists
{
    @Autowired
    private DAOSong daoSong;

    @Autowired
    private DAOAuthor daoAuthor;

    @Autowired
    private RandomListPlugin[] plugins;

    @Transactional
    public List<RandomList> getAllLists( User user )
    {
        ArrayList<RandomList> result = new ArrayList<RandomList>();

        for ( int i = 0; i < plugins.length; i++ )
        {
            RandomListPlugin rlp = plugins[i];
            RandomList rl = rlp.getRandomList( user );
            if ( rl != null )
            {
                result.add( rl );
            }
        }

        return result;
    }

}
