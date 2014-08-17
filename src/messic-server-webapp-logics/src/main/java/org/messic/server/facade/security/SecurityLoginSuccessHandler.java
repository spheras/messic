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

import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiError;
import org.jsondoc.core.annotation.ApiErrors;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiResponseObject;
import org.jsondoc.core.pojo.ApiVerb;
import org.messic.server.api.datamodel.Login;
import org.messic.server.facade.controllers.rest.exceptions.UnknownMessicRESTException;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

@Service
@Api( name = "Login services", description = "Methods for login at messic. Once logged, it's necessary to add the header messic_token (with the token returned by this api) at each call.  It's also valid to add the token as a parameter of the url" )
public class SecurityLoginSuccessHandler
    extends SimpleUrlAuthenticationSuccessHandler
{

    public SecurityLoginSuccessHandler()
    {
        super();
    }

    @ApiMethod( path = "/messiclogin", verb = ApiVerb.POST, description = "process login", produces = { MediaType.APPLICATION_JSON_VALUE } )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ) } )
    @ApiResponseObject
    public void onAuthenticationSuccess( HttpServletRequest request, HttpServletResponse response, Authentication auth )
        throws IOException, ServletException
    {

        if ( "XMLHttpRequest".equals( request.getHeader( "X-Requested-With" ) ) )
        {
            Login login =
                Login.sucessLogin( this.getTargetUrlParameter(), (String) request.getAttribute( "messic_token" ),
                                   ( (User) auth.getPrincipal() ).getUsername() );
            response.getWriter().print( login.getJSON() );
            response.getWriter().flush();
        }
        else
        {
            super.onAuthenticationSuccess( request, response, auth );
        }

    }
}
