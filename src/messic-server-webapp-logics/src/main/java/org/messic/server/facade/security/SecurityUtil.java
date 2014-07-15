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
package org.messic.server.facade.security;

import org.messic.server.api.datamodel.User;
import org.messic.server.datamodel.MDOUser;
import org.messic.server.datamodel.dao.DAOUser;
import org.messic.server.facade.controllers.rest.exceptions.NotAuthorizedMessicRESTException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil
{

    /**
     * Return the current user logged. Throws an exception if nobody logged
     * 
     * @return {@link User} user logged
     * @throws NotAuthorizedMessicRESTException
     */
    public static User getCurrentUser()
        throws NotAuthorizedMessicRESTException
    {
        return getCurrentUser( false, null );
    }

    /**
     * Return the current user logged. Throws an exception if nobody logged depending on the parameter completeUser, the
     * function will return an {@link User} with only the username, or an {@link User} with all the information.
     * 
     * @param completeUser boolean flag to know if the returned user must be complete with all the user information or
     *            just the login information
     * @param daoUser {@link DAOUser} this param is only necessary when param completeUser is true. If not, it can be
     *            null
     * @return {@link User} user logged
     * @throws NotAuthorizedMessicRESTException
     */
    public static User getCurrentUser( boolean completeUser, DAOUser daoUser )
        throws NotAuthorizedMessicRESTException
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ( auth != null && auth.getPrincipal().equals( "anonymousUser" ) )
        {
            return null;
        }
        if ( auth != null )
        {
            if ( !completeUser )
            {
                User user = new User();
                user.setLogin( auth.getName() );
                return user;
            }
            else
            {
                MDOUser mdoUser = daoUser.getUserByLogin( auth.getName() );
                User user = new User( mdoUser );
                return user;
            }
        }
        else
        {
            return null;
        }
    }

}
