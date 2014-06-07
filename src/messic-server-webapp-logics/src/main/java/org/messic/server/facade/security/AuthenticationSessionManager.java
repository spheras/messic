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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

/**
 * Handle authentication requests through spring security and provides a way to handle sessions
 * accross multiple requests 
 *
 */
public class AuthenticationSessionManager {
    /**
     * Session timeout in milliseconds: 1h 
     */
    public static final long TIMEOUT = 3600*1000; 
   
    private static TokenManagementFilter tokenManagementFilter = null;
    
    private static HashMap<String, AuthenticationHolder> authMap = new HashMap<String, AuthenticationHolder>();

    /**
     * 
     * @param authentication
     * @return
     */
    public static String successfulAuthentication(Authentication authentication) {
        try {
            String token = UUID.randomUUID().toString();
            AuthenticationHolder authHolder = new AuthenticationHolder();
            for(GrantedAuthority gauth : authentication.getAuthorities()){
                authHolder.authorities.add(gauth.getAuthority());
            }
            authHolder.auth = authentication;
            authMap.put(token, authHolder);
            return token;
          } catch(AuthenticationException e) {
            return null;
          }
    }
    
    /**
     * 
     * @param token
     */
    public static void successfulLogout(String token) {
    	authMap.remove(token);         
    }
    
    /**
     * 
     * @param token
     * @return
     */
    public static Authentication authenticate(String token) {
        if(isLoggedIn(token))
    	{
    		Authentication auth = getAuthenticationByToken(token);
    		return auth;
    	}
        else
        {
        	return null;
        }        	
    }

    /**
     * @param token the session token
     * @param role the role to search for
     * @return true if the auth connected to the token has the given role
     */
    public static boolean hasRole(String token, String role) {
        Set<String> auth = getRoles(token);
        return (auth != null && auth.contains(role));
    }

    /**
     * @param token the session token
     * @param roles the user must have ALL roles
     * @return true if the auth connected to the token has all the given role
     */
    public static boolean hasRoles(String token, String[] roles) {
        Set<String> auth = getRoles(token);
        if(auth == null) {
            return false;
        }
        for(String role : roles) {
            if(!auth.contains(role)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param token the session token
     * @param roles the user must have ANY roles
     * @return true if the auth connected to the token has any of the given role
     */
    public static boolean hasAnyRoles(String token, String[] roles) {
        Set<String> auth = getRoles(token);
        if(auth == null) {
            return false;
        }
        for(String role : roles) {
            if(auth.contains(role)) {
                return true;
            }
        }
        return false;
    }


    /**
     * helper function to get all roles
     * @param token the session token
     * @return the set of roles associated to the token
     */
    public static Set<String> getRoles(String token) {
        Set<String> auth = getByToken(token);
        if(auth == null) {
            return null;
        }
        return auth;
    }

    /**
     * @param token the session token
     * @return true if the session token is still valid
     */
    public static boolean isLoggedIn(String token) {
        return getByToken(token) != null;
    }
    
    /**
     * 
     * @return
     */
    public static long getTimeout()
    {
    	
    	if(tokenManagementFilter!=null && tokenManagementFilter.getTokenValiditySeconds()!=0)
    	{
    		return tokenManagementFilter.getTokenValiditySeconds();
    	}
    	else
    	{
    		return TIMEOUT;
    	}
    	
    }


    /**
     * get an authentication by its token.
     * Synchronized to prevent concurrency issues when working with the session hashmap
     * @param token the session token
     * @return null if no valid token is found or if it is timed out
     */
    private static synchronized Set<String> getByToken (String token) {
        AuthenticationHolder cur = authMap.get(token);
        if(cur == null) {
            return null;
        }

        // timeout reached
        if(System.currentTimeMillis() > cur.created + getTimeout()) {
            authMap.remove(token);
            return null;
        }

        // update the created date: 1h inactivity
        cur.created = System.currentTimeMillis();

        return cur.authorities;
    }
    
    /**
     * get an authentication by its token.
     * Synchronized to prevent concurrency issues when working with the session hashmap
     * @param token the session token
     * @return null if no valid token is found or if it is timed out
     */
    private static synchronized Authentication getAuthenticationByToken (String token) {
        AuthenticationHolder cur = authMap.get(token);
        if(cur == null) {
            return null;
        }

        // timeout reached
        if(System.currentTimeMillis() > cur.created + TIMEOUT) {
            authMap.remove(token);
            return null;
        }

        // update the created date: 1h inactivity
        cur.created = System.currentTimeMillis();

        return cur.auth;
    }

    /**
     * 
     * @author semhu
     *
     */
    public static class AuthenticationHolder {
        long created = System.currentTimeMillis();
        Authentication auth;
        Set<String> authorities = new HashSet<String>();
    }
    
}