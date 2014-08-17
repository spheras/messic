package org.messic.server.api.dlna.chii2.transcoder.core.dlna.restriction;

import org.apache.commons.lang.ArrayUtils;

/**
 * Array Restriction
 */
public abstract class AudioArrayRestriction<T> extends AudioRestriction<T> {
    // Array of restrictions
    protected T[] restrictions;

    public AudioArrayRestriction(T... restrictions) {
        this.restrictions = restrictions;
    }

    @Override
    public boolean pass(T value) {
        return ArrayUtils.contains(restrictions, value);
    }
}
