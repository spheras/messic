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

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.messic.server.api.datamodel.Login;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Service;

@Service
public class SecurityLoginFailureHandler
    extends SimpleUrlAuthenticationFailureHandler
{

    @Override
    public void onAuthenticationFailure( HttpServletRequest request, HttpServletResponse response,
                                         AuthenticationException exception )
        throws IOException, ServletException
    {
        if ( "XMLHttpRequest".equals( request.getHeader( "X-Requested-With" ) ) )
        {
            Login login = Login.failedLogin( "Username/Password are invalid" );
            response.getWriter().print( login.getJSON() );
            response.getWriter().flush();
        }
        else
        {
            super.onAuthenticationFailure( request, response, exception );
        }
    }

}