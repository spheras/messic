/*
 * Copyright (C) 2013 José Amuedo
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.messic.server.api.APIUser;
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
    private APIUser userAPI;

    /**
     * Returns a populated {@link UserDetails} object. The username is first retrieved from the database and then mapped
     * to a {@link UserDetails} object.
     */
    public UserDetails loadUserByUsername( String username )
        throws UsernameNotFoundException
    {
        org.messic.server.api.datamodel.User domainUser = userAPI.getUser( username );
        if ( domainUser == null )
        {
            //throw new UsernameNotFoundException( "user not found!" );
             org.messic.server.api.datamodel.User user = new org.messic.server.api.datamodel.User();
             user.setLogin(username);
             //TODO solucionar esto!!
             user.setPassword("12345");
             user.setName("Usuario de pruebas");
             user.setAvatar(new byte[]{});
             user.setName("test");
             user.setEmail("test@a.com");
             user.setAdministrator(true);
            
             domainUser = userAPI.createUser(user);
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