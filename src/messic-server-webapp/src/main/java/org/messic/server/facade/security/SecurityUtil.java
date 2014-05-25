package org.messic.server.facade.security;

import org.messic.server.api.datamodel.User;
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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ( auth != null )
        {
            User user = new User();
            user.setLogin( auth.getName() );
            return user;
        }
        else
        {
            throw new NotAuthorizedMessicRESTException( new Exception( "Not authorized" ) );
        }
    }

}
