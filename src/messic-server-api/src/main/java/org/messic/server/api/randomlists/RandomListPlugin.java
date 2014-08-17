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

import org.messic.server.api.datamodel.RandomList;
import org.messic.server.api.datamodel.User;

public interface RandomListPlugin
{
    /**
     * Max number of elements that should be at the list
     */
    public static int MAX_ELEMENTS = 40;

    /**
     * Obtain the randomlist
     * 
     * @param user {@link User} user scope
     * @return {@link RandomList} generated, null if none
     */
    RandomList getRandomList( User user );

    /**
     * Get the name of the plugin
     * 
     * @return String the name
     */
    String getName();
}
