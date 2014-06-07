package org.messic.server.api.dlna.chii2.transcoder.core.dlna.restriction;

/**
 * Range Restriction
 */
public abstract class AudioRangeRestriction<T> extends AudioRestriction<T> {
    // Min
    protected T minValue;
    // Max
    protected T maxValue;

    public AudioRangeRestriction(T minValue, T maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
    }
}
