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
