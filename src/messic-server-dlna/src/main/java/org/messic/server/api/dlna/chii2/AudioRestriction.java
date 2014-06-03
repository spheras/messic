package org.messic.server.api.dlna.chii2;

/**
 * Restriction
 */
public abstract class AudioRestriction<T> {
    /**
     * Value compares to Restrictions
     *
     * @param container          Container
     * @param audioFormat        Audio Format
     * @param audioFormatProfile Audio Format Profile
     * @param audioFormatVersion Audio Format Version
     * @param audioCodec         Audio Codec
     * @param audioBitRate       Audio BitRate
     * @param audioSampleBitRate Audio SampleBitRate
     * @param audioChannels      Audio Channels
     * @return Value
     */
    public abstract T value(String container, String audioFormat, String audioFormatProfile, int audioFormatVersion, String audioCodec, long audioBitRate, long audioSampleBitRate, int audioChannels);

    /**
     * AudioRestriction is passed or not
     *
     * @param value Value to be compared
     * @return True if restriction is passed
     */
    public abstract boolean pass(T value);
}
