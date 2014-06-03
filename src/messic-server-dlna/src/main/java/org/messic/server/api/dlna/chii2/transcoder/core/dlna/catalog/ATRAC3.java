package org.messic.server.api.dlna.chii2.transcoder.core.dlna.catalog;

import org.fourthline.cling.support.model.dlna.DLNAProfiles;
import org.messic.server.api.dlna.chii2.transcoder.core.dlna.codec.AudioCodec;
import org.messic.server.api.dlna.chii2.transcoder.core.dlna.restriction.AudioRestriction;

/**
 * ATRAC3
 */
public class ATRAC3 extends AudioCatalog {
    public ATRAC3() {
        // ATRAC3
        profileMap.put(DLNAProfiles.ATRAC3, new AudioRestriction[]{
                new AudioCodecRestriction(AudioCodec.ATRAC3)
        });
    }
}
