package org.messic.server.facade.logics;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
            response.getWriter().print( "{success:false, message: 'Username/Password are invalid'}" );
            response.getWriter().flush();
        }
        else
        {
            super.onAuthenticationFailure( request, response, exception );
        }
    }

}