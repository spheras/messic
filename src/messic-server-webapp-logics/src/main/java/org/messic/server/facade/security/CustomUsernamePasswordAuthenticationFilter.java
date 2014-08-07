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

import org.messic.server.api.dlna.AuthenticationDLNAMusicService;
import org.messic.server.config.MessicConfig;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class CustomUsernamePasswordAuthenticationFilter
    extends UsernamePasswordAuthenticationFilter
    implements AuthenticationDLNAMusicService
{

    /**
	 * 
	 */
    protected void successfulAuthentication( HttpServletRequest request, HttpServletResponse response,
                                             Authentication authResult )
        throws IOException, ServletException
    {

        if ( logger.isDebugEnabled() )
        {
            logger.debug( "Authentication success. Updating SecurityContextHolder to contain: " + authResult );
        }

        SecurityContextHolder.getContext().setAuthentication( authResult );

        getRememberMeServices().loginSuccess( request, response, authResult );

        // Fire event
        if ( this.eventPublisher != null )
        {
            eventPublisher.publishEvent( new InteractiveAuthenticationSuccessEvent( authResult, this.getClass() ) );
        }

        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken( obtainUsername( request ), obtainPassword( request ) );

        String token = AuthenticationSessionManager.successfulAuthentication( authentication );

        request.setAttribute( "messic_token", token );

        getSuccessHandler().onAuthenticationSuccess( request, response, authResult );

    }

    /**
     * function to obtain the messic token
     * 
     * @param request {@link HttpServletRequest}
     * @return {@link String} messic token obtained
     */
    protected String obtainToken( HttpServletRequest request )
    {
        return request.getHeader( "messic_token" );
    }

    /**
     * For the DLNA Services
     */
    @Override
    public String successfulAuthenticationDLNA( Authentication authentication )
    {
        return AuthenticationSessionManager.successfulAuthenticationDLNA( authentication );
    }

    @Override
    public String getCurrentPort()
    {
        return MessicConfig.getCurrentPort();
    }

    @Override
    public boolean isSecured()
    {
        return MessicConfig.isSecured();
    }

}