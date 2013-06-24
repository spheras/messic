package org.messic.server.facade.logics;

import org.messic.server.datamodel.MDOUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping( "/api/login.json" )
public class LoginService
{

    @Autowired
    @Qualifier( "authenticationManager" )
    protected AuthenticationManager authenticationManager;

    @RequestMapping( method = RequestMethod.GET )
    @ResponseBody
    public LoginStatus getStatus()
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ( auth != null && !auth.getName().equals( "anonymousUser" ) && auth.isAuthenticated() )
        {
            return new LoginStatus( true, auth.getName() );
        }
        else
        {
            return new LoginStatus( false, null );
        }
    }

    @RequestMapping( method = RequestMethod.POST )
    @ResponseBody
    public LoginStatus login( @RequestParam( "j_username" )
    String username, @RequestParam( "j_password" )
    String password )
    {

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken( username, password );
//        MDOUser details = new MDOUser( );
//        details.setLogin( username );
//        details.setPassword( password );
//        token.setDetails( details );

        try
        {
            Authentication auth = authenticationManager.authenticate( token );
            SecurityContextHolder.getContext().setAuthentication( auth );
            return new LoginStatus( auth.isAuthenticated(), auth.getName() );
        }
        catch ( BadCredentialsException e )
        {
            return new LoginStatus( false, null );
        }
    }

    public class LoginStatus
    {

        private final boolean loggedIn;

        private final String username;

        public LoginStatus( boolean loggedIn, String username )
        {
            this.loggedIn = loggedIn;
            this.username = username;
        }

        public boolean isLoggedIn()
        {
            return loggedIn;
        }

        public String getUsername()
        {
            return username;
        }
    }
}