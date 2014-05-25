package org.messic.server.api.randomlists;

import org.messic.server.api.datamodel.RandomList;
import org.messic.server.api.datamodel.User;

public interface RandomListPlugin
{
    /**
     * Obtain the randomlist
     * 
     * @param user {@link User} user scope
     * @return {@link RandomList} generated, null if none
     */
    RandomList getRandomList( User user );

}
