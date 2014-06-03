package org.messic.server.api.dlna.chii2.transcoder.core.dlna.restriction;

/**
 * Image Restriction
 */
public abstract class ImageRestriction<T> {
    /**
     * Value to be compared
     *
     * @param imageType   Image Type
     * @param imageWidth  Image Width
     * @param imageHeight Image Height
     * @return Value
     */
    public abstract T value(String imageType, int imageWidth, int imageHeight);

    /**
     * VideoRestriction is passed or not
     *
     * @param value Value to be compared
     * @return True if restriction is passed
     */
    public abstract boolean pass(T value);
}
