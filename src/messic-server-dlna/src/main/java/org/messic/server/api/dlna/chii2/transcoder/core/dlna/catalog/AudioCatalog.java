package org.messic.server.api.dlna.chii2.transcoder.core.dlna.catalog;

import java.util.LinkedHashMap;
import java.util.Map;

import org.fourthline.cling.support.model.dlna.DLNAProfiles;
import org.messic.server.api.dlna.chii2.transcoder.core.dlna.codec.AudioCodec;
import org.messic.server.api.dlna.chii2.transcoder.core.dlna.codec.Container;
import org.messic.server.api.dlna.chii2.transcoder.core.dlna.restriction.AudioArrayRestriction;
import org.messic.server.api.dlna.chii2.transcoder.core.dlna.restriction.AudioRangeRestriction;
import org.messic.server.api.dlna.chii2.transcoder.core.dlna.restriction.AudioRestriction;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * DLNA Audio Catalog
 */
public class AudioCatalog {
    // Profile - Restrictions Map
    protected LinkedHashMap<DLNAProfiles, AudioRestriction[]> profileMap = new LinkedHashMap<DLNAProfiles, AudioRestriction[]>();

    /**
     * Resolve DLNA Profile based on conditions
     *
     * @param container          Container
     * @param audioFormat        Audio Format
     * @param audioFormatProfile Audio Format Profile
     * @param audioFormatVersion Audio Format Version
     * @param audioCodec         Audio Codec
     * @param audioBitRate       Audio BitRate
     * @param audioSampleBitRate Audio SampleBitRate
     * @param audioChannels      Audio Channels
     * @return DLNA Profile, Null if not match
     */
    public DLNAProfiles resolve(String container, String audioFormat, String audioFormatProfile, int audioFormatVersion, String audioCodec, long audioBitRate, long audioSampleBitRate, int audioChannels) {
        loop_profile:
        for (Map.Entry<DLNAProfiles, AudioRestriction[]> entry : profileMap.entrySet()) {
            for (AudioRestriction audioRestriction : entry.getValue()) {
                //noinspection unchecked
                if (!audioRestriction.pass(audioRestriction.value(container, audioFormat, audioFormatProfile, audioFormatVersion, audioCodec, audioBitRate, audioSampleBitRate, audioChannels)))
                    continue loop_profile;
            }
            return entry.getKey();
        }
        return DLNAProfiles.NONE;
    }

    /**
     * Container Restriction
     */
    public class ContainerRestriction extends AudioRestriction<String> {
        // Target Container
        private Container targetContainer;

        public ContainerRestriction(Container targetContainer) {
            this.targetContainer = targetContainer;
        }

        @Override
        public String value(String container, String audioFormat, String audioFormatProfile, int audioFormatVersion, String audioCodec, long audioBitRate, long audioSampleBitRate, int audioChannels) {
            return container;
        }

        @Override
        public boolean pass(String value) {
            return Container.match(value, targetContainer);
        }
    }

    /**
     * Audio Codec Restriction
     */
    public class AudioCodecRestriction extends AudioRestriction<Object[]> {
        // Target Audio Codec
        private AudioCodec targetCodec;

        public AudioCodecRestriction(AudioCodec targetCodec) {
            this.targetCodec = targetCodec;
        }

        @Override
        public Object[] value(String container, String audioFormat, String audioFormatProfile, int audioFormatVersion, String audioCodec, long audioBitRate, long audioSampleBitRate, int audioChannels) {
            return new Object[]{audioFormat, audioFormatProfile, audioFormatVersion, audioCodec};
        }

        @Override
        public boolean pass(Object[] value) {
            String audioFormat = (String) value[0];
            String audioFormatProfile = (String) value[1];
            int audioFormatVersion = (Integer) value[2];
            String audioCodec = (String) value[3];
            return AudioCodec.match(audioFormat, audioFormatProfile, audioFormatVersion, audioCodec, targetCodec);
        }
    }

    /**
     * SampleBitRate Array Restriction
     */
    public class SampleBitRateRestriction extends AudioArrayRestriction<Long> {

        public SampleBitRateRestriction(Long... restrictions) {
            super(restrictions);
        }

        @Override
        public Long value(String container, String audioFormat, String audioFormatProfile, int audioFormatVersion, String audioCodec, long audioBitRate, long audioSampleBitRate, int audioChannels) {
            return audioSampleBitRate;
        }
    }

    /**
     * SampleBitRate Range Restriction
     */
    public class SampleBitRateRangeRestriction extends AudioRangeRestriction<Long> {

        public SampleBitRateRangeRestriction(long minValue, long maxValue) {
            super(minValue, maxValue);
        }

        @Override
        public Long value(String container, String audioFormat, String audioFormatProfile, int audioFormatVersion, String audioCodec, long audioBitRate, long audioSampleBitRate, int audioChannels) {
            return audioSampleBitRate;
        }

        @Override
        public boolean pass(Long value) {
            return value >= minValue && value <= maxValue;
        }
    }

    /**
     * Audio BitRate Range Restriction
     */
    public class AudioBitRateRangeRestriction extends AudioRangeRestriction<Long> {

        public AudioBitRateRangeRestriction(long minValue, long maxValue) {
            super(minValue, maxValue);
        }

        @Override
        public Long value(String container, String audioFormat, String audioFormatProfile, int audioFormatVersion, String audioCodec, long audioBitRate, long audioSampleBitRate, int audioChannels) {
            return audioBitRate;
        }

        @Override
        public boolean pass(Long value) {
            return value >= minValue && value <= maxValue;
        }
    }

    /**
     * Channel Restriction
     */
    public class ChannelRestriction extends AudioRangeRestriction<Integer> {

        public ChannelRestriction(int minValue, int maxValue) {
            super(minValue, maxValue);
        }

        @Override
        public Integer value(String container, String audioFormat, String audioFormatProfile, int audioFormatVersion, String audioCodec, long audioBitRate, long audioSampleBitRate, int audioChannels) {
            return audioChannels;
        }

        @Override
        public boolean pass(Integer value) {
            return value >= minValue && value <= maxValue;
        }
    }
}
