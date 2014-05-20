package org.messic.server.facade.logics;

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
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

/**
 * Filter for token management replacing session management filter
 * @author gucluakkaya
 *
 */
public class TokenManagementFilter extends AbstractAuthenticationProcessingFilter {
	
	public static final long TWO_WEEKS_S = 1209600;

    private long tokenValiditySeconds = TWO_WEEKS_S;
   
	private String tokenParameter = "messic_token";
	
	/**
	 * 
	 */
	public TokenManagementFilter() {
		super("/");
    }
	
	
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        
        if (!requiresAuthentication(request, response)) {
            chain.doFilter(request, response);
            return;
        }
        
        try {
        	Authentication authResult = attemptAuthentication(request, response);
            SecurityContextHolder.getContext().setAuthentication(authResult);
            chain.doFilter(request, response);
        } catch(InternalAuthenticationServiceException failed) {
            logger.error("An internal error occurred while trying to authenticate the user.", failed);
            unsuccessfulAuthentication(request, response, failed);
            return;
        }
        catch (AuthenticationException failed) {
            // Authentication failed
            unsuccessfulAuthentication(request, response, failed);
            return;
        }

        
    }
	
	/**
	 * 
	 */
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        
        String token = obtainToken(request);
        
        Authentication auth = AuthenticationSessionManager.authenticate(token);
        
        setDetails(request, (UsernamePasswordAuthenticationToken)auth);
        
        return this.getAuthenticationManager().authenticate(auth);
        
    }
	 	
		
	/**
     * Provided so that subclasses may configure what is put into the authentication request's details
     * property.
     *
     * @param request that an authentication request is being created for
     * @param authRequest the authentication request object that should have its details set
     */
    protected void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }    
    
    /**
     * 
     * @param request
     * @return
     */
    protected String obtainToken(HttpServletRequest request) {
    	return request.getHeader(tokenParameter);
    }
    
    /**
     * 
     * @param authenticationTokenId
     */
    public void setAuthenticationTokenId(String authenticationTokenId) {
        Assert.notNull(authenticationTokenId, "authenticationTokenId cannot be null");
        this.tokenParameter = authenticationTokenId;
        this.setRequiresAuthenticationRequestMatcher(new FilterTokenRequestMatcher(tokenParameter));
    }
    
    /**
     * 
     * @param tokenValiditySeconds
     */
    public void setTokenValiditySeconds(long tokenValiditySeconds) {
        this.tokenValiditySeconds=tokenValiditySeconds;
    }
    
    /**
     * 
     * @return
     */
    public long getTokenValiditySeconds() {
        return tokenValiditySeconds;
    }
    
    /**
     * 
     * @author semhu
     *
     */
    private static final class FilterTokenRequestMatcher implements RequestMatcher {
        
    	String authenticationTokenId;
    	
        private FilterTokenRequestMatcher(String authenticationTokenId) {
            Assert.hasLength(authenticationTokenId, "authenticationTokenId must be specified");
            this.authenticationTokenId = authenticationTokenId;
        }

        public boolean matches(HttpServletRequest request) {
             if(request.getHeader(authenticationTokenId)!=null)
             {
            	 return true;
             }
             else
             {
            	 return false;
             }
        }
        
    }

}
