package org.messic.server.api.dlna.chii2.mediaserver.api.http;

import java.net.InetAddress;
import java.net.URI;

/**
 * Http Server Service for UPnP/DLNA Media Server
 */
public interface HttpServerService {

    /**
     * Get HTTP Server Host
     *
     * @return Host Name
     */
    public InetAddress getHost();

    /**
     * Get HTTP Server Post
     *
     * @return Port
     */
    public int getPort();

    /**
     * Forge Media Item URL
     *
     * @param clientProfile Client Profile
     * @param mediaType     Media Type
     * @param transcoded    Transcoded or not
     * @param mediaId       Movie ID
     * @return Movie URL
     */
    public URI forgeUrl(String mediaType, String clientProfile, boolean transcoded, String mediaId);
}
