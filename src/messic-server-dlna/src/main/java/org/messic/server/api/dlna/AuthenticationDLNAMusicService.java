package org.messic.server.api.dlna;

import org.springframework.security.core.Authentication;

public interface AuthenticationDLNAMusicService
{
    /**
     * Perform an authentication, returning the token associated
     * 
     * @param authentication
     * @return
     */
    String successfulAuthenticationDLNA( Authentication authentication);

    /**
     * Read the file to konw which is the current port
     * 
     * @return
     */
    String getCurrentPort();

}
