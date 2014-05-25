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
                Login.sucessLogin( this.getTargetUrlParameter(), (String) request.getAttribute( "messic_token" ) );
            response.getWriter().print( login.getJSON() );
            response.getWriter().flush();
        }
        else
        {
            super.onAuthenticationSuccess( request, response, auth );
        }

    }
}