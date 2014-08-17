package org.messic.server.api.dlna.chii2.transcoder.core.dlna.catalog;

import org.fourthline.cling.support.model.dlna.DLNAProfiles;
import org.messic.server.api.dlna.chii2.transcoder.core.dlna.codec.AudioCodec;
import org.messic.server.api.dlna.chii2.transcoder.core.dlna.restriction.AudioRestriction;

/**
 * MP3
 */
public class MP3 extends AudioCatalog {
    public MP3() {
        // MP3
        profileMap.put(DLNAProfiles.MP3, new AudioRestriction[]{
                new AudioCodecRestriction(AudioCodec.MP3),
                new SampleBitRateRestriction(32000L, 44100L, 48000L),
                new AudioBitRateRangeRestriction(32000, 320000),
                new ChannelRestriction(1, 2)
        });

        //MP3X
        profileMap.put(DLNAProfiles.MP3X, new AudioRestriction[]{
                new AudioCodecRestriction(AudioCodec.MP3X),
                new SampleBitRateRestriction(16000L, 22050L, 24000L, 32000L, 44100L, 48000L),
                new AudioBitRateRangeRestriction(8000, 320000),
                new ChannelRestriction(1, 2)
        });
    }
}
