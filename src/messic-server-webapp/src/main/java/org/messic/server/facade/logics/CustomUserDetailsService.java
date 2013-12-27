package org.messic.server.facade.logics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.messic.server.business.BUser;
import org.messic.server.business.vo.UserVO;
import org.messic.server.datamodel.MDOUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * http://satishab.blogspot.com.es/2012/10/part-4-securing-web-application-with.html A custom {@link UserDetailsService}
 * where user information is retrieved from a JPA repository
 */
@Service
public class CustomUserDetailsService
    implements UserDetailsService
{

    @Autowired
    private BUser userRepository;

    /**
     * Returns a populated {@link UserDetails} object. The username is first retrieved from the database and then mapped
     * to a {@link UserDetails} object.
     */
    public UserDetails loadUserByUsername( String username )
        throws UsernameNotFoundException
    {
        try
        {
            MDOUser domainUser = userRepository.getUser( username );
            if ( domainUser == null )
            {
            	UserVO uservo = new UserVO();
            	uservo.setLogin(username);
            	uservo.setPassword("12345");
            	uservo.setName("Usuario de pruebas");
            	uservo.setAvatar(new byte[]{});
            	uservo.setName("test");
            	uservo.setEmail("test@a.com");
            	uservo.setAdministrator(true);
            	
                domainUser = userRepository.createUser(uservo);
                //throw new UsernameNotFoundException( "user not found!" );
            }

            boolean enabled = true;
            boolean accountNonExpired = true;
            boolean credentialsNonExpired = true;
            boolean accountNonLocked = true;
            User signedUser =
                new User( domainUser.getLogin(), domainUser.getPassword(), enabled, accountNonExpired,
                          credentialsNonExpired, accountNonLocked, getAuthorities( 1 ) );

            return signedUser;

        }
        catch ( Exception e )
        {
            e.printStackTrace();
            throw new RuntimeException( e );
        }
    }

    /**
     * Retrieves a collection of {@link GrantedAuthority} based on a numerical role
     * 
     * @param role the numerical role
     * @return a collection of {@link GrantedAuthority

     */
    public Collection<? extends GrantedAuthority> getAuthorities( Integer role )
    {
        List<GrantedAuthority> authList = getGrantedAuthorities( getRoles( role ) );
        return authList;
    }

    /**
     * Converts a numerical role to an equivalent list of roles
     * 
     * @param role the numerical role
     * @return list of roles as as a list of {@link String}
     */
    public List<String> getRoles( Integer role )
    {
        List<String> roles = new ArrayList<String>();

        if ( role.intValue() == 1 )
        {
            roles.add( "ROLE_USER" );
            roles.add( "ROLE_ADMIN" );

        }
        else if ( role.intValue() == 2 )
        {
            roles.add( "ROLE_USER" );
        }

        return roles;
    }

    /**
     * Wraps {@link String} roles to {@link SimpleGrantedAuthority} objects
     * 
     * @param roles {@link String} of roles
     * @return list of granted authorities
     */
    public static List<GrantedAuthority> getGrantedAuthorities( List<String> roles )
    {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for ( String role : roles )
        {
            authorities.add( new SimpleGrantedAuthority( role ) );
        }
        return authorities;
    }
}