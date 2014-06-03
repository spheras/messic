package org.messic.server.facade.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class CustomUsernamePasswordAuthenticationFilter
    extends UsernamePasswordAuthenticationFilter
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

}