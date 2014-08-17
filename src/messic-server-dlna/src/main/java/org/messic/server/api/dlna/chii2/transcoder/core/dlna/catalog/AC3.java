package org.messic.server.api.dlna.chii2.transcoder.core.dlna.catalog;

import org.fourthline.cling.support.model.dlna.DLNAProfiles;
import org.messic.server.api.dlna.chii2.transcoder.core.dlna.codec.AudioCodec;
import org.messic.server.api.dlna.chii2.transcoder.core.dlna.restriction.AudioRestriction;

/**
 * AC3
 */
public class AC3 extends AudioCatalog {
    public AC3() {
        // AC3
        profileMap.put(DLNAProfiles.AC3, new AudioRestriction[]{
                new AudioCodecRestriction(AudioCodec.AC3),
                new SampleBitRateRestriction(32000L, 44100L, 48000L),
                new AudioBitRateRangeRestriction(32000, 640000),
                new ChannelRestriction(1, 6)
        });
    }
}
