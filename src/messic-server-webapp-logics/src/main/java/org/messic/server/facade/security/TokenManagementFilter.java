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

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

/**
 * Filter for token management replacing session management filter
 * 
 * @author gucluakkaya
 */
public class TokenManagementFilter
    extends AbstractAuthenticationProcessingFilter
{

    private String tokenParameter = "messic_token";

    /**
	 * 
	 */
    public TokenManagementFilter()
    {
        super( "/" );
    }

    public void doFilter( ServletRequest req, ServletResponse res, FilterChain chain )
        throws IOException, ServletException
    {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        if ( !requiresAuthentication( request, response ) )
        {
            chain.doFilter( request, response );
            return;
        }

        try
        {
            Authentication authResult = attemptAuthentication( request, response );
            SecurityContextHolder.getContext().setAuthentication( authResult );
            chain.doFilter( request, response );
        }
        catch ( InternalAuthenticationServiceException failed )
        {
            logger.error( "An internal error occurred while trying to authenticate the user.", failed );
            unsuccessfulAuthentication( request, response, failed );
            return;
        }
        catch ( AuthenticationException failed )
        {
            // Authentication failed
            unsuccessfulAuthentication( request, response, failed );
            return;
        }

    }

    /**
	 * 
	 */
    public Authentication attemptAuthentication( HttpServletRequest request, HttpServletResponse response )
        throws AuthenticationException
    {

        String token = obtainToken( request, tokenParameter );

        Authentication auth = AuthenticationSessionManager.authenticate( token );
        if ( auth != null )
        {
            setDetails( request, (UsernamePasswordAuthenticationToken) auth );

            return this.getAuthenticationManager().authenticate( auth );
        }
        else
        {
            // lets try at the dlna tokens
            auth = AuthenticationSessionManager.authenticateDLNA( token );
            if ( auth != null )
            {
                setDetails( request, (UsernamePasswordAuthenticationToken) auth );
                return auth;
            }
        }

        throw new SessionAuthenticationException( "not valid token" );

    }

    /**
     * Provided so that subclasses may configure what is put into the authentication request's details property.
     * 
     * @param request that an authentication request is being created for
     * @param authRequest the authentication request object that should have its details set
     */
    protected void setDetails( HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest )
    {
        authRequest.setDetails( authenticationDetailsSource.buildDetails( request ) );
    }

    /**
     * @param request
     * @return
     */
    public String obtainToken( HttpServletRequest request, String tokenParameter )
    {
        String tokenHeader = request.getHeader( tokenParameter );
        if ( tokenHeader != null )
        {
            return tokenHeader;
        }
        else
        {
            String tokenParam = request.getParameter( tokenParameter );
            return tokenParam;
        }
    }

    /**
     * @param authenticationTokenId
     */
    public void setAuthenticationTokenId( String authenticationTokenId )
    {
        Assert.notNull( authenticationTokenId, "authenticationTokenId cannot be null" );
        this.tokenParameter = authenticationTokenId;
        this.setRequiresAuthenticationRequestMatcher( new FilterTokenRequestMatcher( tokenParameter ) );
    }

    /**
     * @author semhu
     */
    private static final class FilterTokenRequestMatcher
        implements RequestMatcher
    {

        String authenticationTokenId;

        private FilterTokenRequestMatcher( String authenticationTokenId )
        {
            Assert.hasLength( authenticationTokenId, "authenticationTokenId must be specified" );
            this.authenticationTokenId = authenticationTokenId;
        }

        public boolean matches( HttpServletRequest request )
        {
            if ( request.getHeader( authenticationTokenId ) != null
                || request.getParameter( authenticationTokenId ) != null )
            {
                return true;
            }
            else
            {
                return false;
            }
        }

    }

    public String getTokenParameter()
    {
        return tokenParameter;
    }

    public void setTokenParameter( String tokenParameter )
    {
        this.tokenParameter = tokenParameter;
    }

}
