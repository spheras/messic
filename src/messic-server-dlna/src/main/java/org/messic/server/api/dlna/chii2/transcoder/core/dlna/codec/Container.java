package org.messic.server.api.dlna.chii2.transcoder.core.dlna.codec;

/**
 * Container
 */
public enum Container {
    AVI(new String[] {"AVI"}),
    MKV(new String[] {"Matroska"}),
    WM(new String[] {"Windows Media"}),
    ADTS(new String[] {"ADTS"}),
    MPEG_PS(new String[] {"MPEG-PS"}),
    MPEG_ES(new String[] {"MPEG-ES"}),
    MPEG_TS(new String[] {"MPEG-TS"}),
    MPEG4(new String[] {"MPEG-4"}),
    QUICK_TIME(new String[] {"QuickTime"});

    // Formats
    private String[] formats;

    Container(String[] formats) {
        this.formats = formats;
    }

    /**
     * Given fields match container condition
     *
     * @param container  Container
     * @param target  Target Container Type
     * @return True if match
     */
    public static boolean match(String container, Container target) {
        if (target.formats != null && target.formats.length > 0) {
            for (String acceptableFormat : target.formats) {
                if (acceptableFormat.equalsIgnoreCase(container)) return true;
            }
            return false;
        } else {
            return true;
        }
    }
}
