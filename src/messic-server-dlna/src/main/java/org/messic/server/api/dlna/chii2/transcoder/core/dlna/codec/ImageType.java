package org.messic.server.api.dlna.chii2.transcoder.core.dlna.codec;

/**
 * Image Type
 */
public enum ImageType {
    JPEG(new String[]{"JPEG"}),
    PNG(new String[]{"PNG"});

    //Acceptable Types
    private String[] types;

    ImageType(String[] types) {
        this.types = types;
    }

    /**
     * Image Type match or not
     *
     * @param type   Image Type
     * @param target Target ImageType enum
     * @return True if match
     */
    public static boolean match(String type, ImageType target) {
        for (String targetType : target.types) {
            if (targetType.equalsIgnoreCase(type)) return true;
        }
        return false;
    }
}
