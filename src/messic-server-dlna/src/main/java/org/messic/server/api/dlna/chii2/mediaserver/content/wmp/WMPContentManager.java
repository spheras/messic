package org.messic.server.api.dlna.chii2.mediaserver.content.wmp;

import java.util.List;

import org.fourthline.cling.model.message.UpnpHeaders;
import org.messic.server.api.dlna.MusicService;
import org.messic.server.api.dlna.chii2.medialibrary.api.core.MediaLibraryService;
import org.messic.server.api.dlna.chii2.mediaserver.api.http.HttpServerService;
import org.messic.server.api.dlna.chii2.mediaserver.content.common.CommonContentManager;
import org.messic.server.api.dlna.chii2.transcoder.api.core.TranscoderService;

/**
 * Content Manger for Windows Media Player
 */
public class WMPContentManager extends CommonContentManager {
    /**
     * Constructor
     *
     * @param mediaLibrary Media Library
     * @param httpServer   Http Server
     * @param transcoder   Transcoder
     */
    public WMPContentManager(MediaLibraryService mediaLibrary, HttpServerService httpServer, TranscoderService transcoder, MusicService musicService) {
        super(mediaLibrary, httpServer, transcoder, musicService);
    }

    @Override
    public String getClientProfile() {
        return TranscoderService.PROFILE_WMP;
    }

    @Override
    public boolean isMatch(UpnpHeaders headers) {
        if (headers != null) {
            // If user agent contains XBox, is should be a XBox client
            List<String> userAgent = headers.get("User-agent");
            if (getClientProfile().equalsIgnoreCase(transcoder.getClientProfile(userAgent))) {
                return true;
            }
        }
        return false;
    }
}
