package org.messic.server.api.dlna.chii2.transcoder.core.dlna.catalog;

import org.fourthline.cling.support.model.dlna.DLNAProfiles;
import org.messic.server.api.dlna.chii2.transcoder.core.dlna.codec.AudioCodec;
import org.messic.server.api.dlna.chii2.transcoder.core.dlna.restriction.AudioRestriction;

/**
 * LPCM
 */
public class LPCM extends AudioCatalog {
    public LPCM() {
        // LPCM Low
        profileMap.put(DLNAProfiles.LPCM_LOW, new AudioRestriction[]{
                new AudioCodecRestriction(AudioCodec.PCM),
                new SampleBitRateRangeRestriction(8000, 32000),
                new ChannelRestriction(1, 2)
        });

        // LPCM
        profileMap.put(DLNAProfiles.LPCM, new AudioRestriction[]{
                new AudioCodecRestriction(AudioCodec.PCM),
                new SampleBitRateRangeRestriction(8000, 48000),
                new ChannelRestriction(1, 2)
        });
    }
}
