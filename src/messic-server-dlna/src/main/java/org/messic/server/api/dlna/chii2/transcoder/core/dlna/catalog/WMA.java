package org.messic.server.api.dlna.chii2.transcoder.core.dlna.catalog;

import org.fourthline.cling.support.model.dlna.DLNAProfiles;
import org.messic.server.api.dlna.chii2.transcoder.core.dlna.codec.AudioCodec;
import org.messic.server.api.dlna.chii2.transcoder.core.dlna.restriction.AudioRestriction;

/**
 * WMA
 */
public class WMA extends AudioCatalog {
    public WMA() {
        // WMA Base
        profileMap.put(DLNAProfiles.WMABASE, new AudioRestriction[]{
                new AudioCodecRestriction(AudioCodec.WMA),
                new SampleBitRateRangeRestriction(0, 48000),
                new AudioBitRateRangeRestriction(0, 193000),
                new ChannelRestriction(1, 2)
        });

        // WMA Full
        profileMap.put(DLNAProfiles.WMAFULL, new AudioRestriction[]{
                new AudioCodecRestriction(AudioCodec.WMA),
                new SampleBitRateRangeRestriction(0, 48000),
                new AudioBitRateRangeRestriction(0, 385000),
                new ChannelRestriction(1, 2)
        });

        // WMA Pro
        profileMap.put(DLNAProfiles.WMAPRO, new AudioRestriction[]{
                new AudioCodecRestriction(AudioCodec.WMA),
                new SampleBitRateRangeRestriction(0, 96000),
                new AudioBitRateRangeRestriction(0, 1500000),
                new ChannelRestriction(1, 8)
        });
    }
}
