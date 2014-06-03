package org.messic.server.api.dlna.chii2.transcoder.core.dlna.catalog;

import org.fourthline.cling.support.model.dlna.DLNAProfiles;
import org.messic.server.api.dlna.chii2.transcoder.core.dlna.codec.AudioCodec;
import org.messic.server.api.dlna.chii2.transcoder.core.dlna.restriction.AudioRestriction;

/**
 * AMR
 */
public class AMR extends AudioCatalog {
    public AMR() {
        // AMR
        profileMap.put(DLNAProfiles.AMR, new AudioRestriction[]{
                new AudioCodecRestriction(AudioCodec.AMR)
        });
        // AMR WebPlus
        profileMap.put(DLNAProfiles.AMR_WBplus, new AudioRestriction[]{
                new AudioCodecRestriction(AudioCodec.AMRWB),
                new SampleBitRateRestriction(8000L, 16000L, 24000L, 32000L, 48000L)
        });
    }
}
