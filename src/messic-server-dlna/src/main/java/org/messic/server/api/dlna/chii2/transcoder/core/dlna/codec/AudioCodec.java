package org.messic.server.api.dlna.chii2.transcoder.core.dlna.codec;

import org.apache.commons.lang.ArrayUtils;

/**
 * Audio Codec
 */
public enum AudioCodec {
    AAC(new String[]{"AAC", "ER AAC"}, null, null, null),
    AAC_LC(new String[]{"AAC", "ER AAC"}, new String[]{"LC"}, null, null),
    AAC_MAIN(new String[]{"AAC", "ER AAC"}, new String[]{"MAIN"}, null, null),
    AAC_LTP(new String[]{"AAC", "ER AAC"}, new String[]{"LTP"}, null, null),
    HEAAC(new String[]{"AAC", "ER AAC"}, new String[]{"HE-AAC", "HE-AAC / LC"}, new int[]{0, 1}, null),
    HEAAC_V2(new String[]{"AAC", "ER AAC"}, new String[]{"HE-AAC", "HE-AAC / LC"}, new int[]{2}, null),
    BSAC(new String[]{"BSAC", "ER BSAC"}, null, null, null),
    AC3(new String[]{"2000", "AC3"}),
    EAC3(new String[]{"AC3+"}),
    ATRAC3(new String[]{"Atrac3"}, null, null, null),
    G726(new String[]{"G726"}, null, null, null),
    AMR(new String[]{"samr", "7A21", "7A22"}),
    AMRWB(new String[]{"sawp"}),
    PCM(new String[]{"PCM"}, null, null, null),
    ADPCM(new String[]{"ADPCM"}, null, null, null),
    WMA(new String[]{"160", "161", "163", "WMA1", "WMA2", "WMA9"}),
    MP2(new String[]{"50", "MPA1L2"}),
    MP3(new String[]{"55", "6B", "MPA1L3"}),
    MP3X(new String[]{"55", "6B", "MPA1L3", "MPA2L3"});

    // Acceptable Codec
    private String[] codecs;
    // Acceptable Format
    private String[] formats;
    // Acceptable Format Profile
    private String[] profiles;
    // Acceptable Format Version
    private int[] versions;

    AudioCodec(String[] codecs) {
        this.codecs = codecs;
    }

    AudioCodec(String[] formats, String[] profiles, int[] versions, String[] codecs) {
        this.formats = formats;
        this.profiles = profiles;
        this.versions = versions;
        this.codecs = codecs;
    }

    /**
     * Given fields match audio codec condition
     *
     * @param format  Audio Format
     * @param profile Audio Format Profile
     * @param version Audio Format Version
     * @param codec   Audio Codec
     * @param target  Target Codec Type
     * @return True if match
     */
    public static boolean match(String format, String profile, int version, String codec, AudioCodec target) {
        if (target.codecs != null && target.codecs.length > 0) {
            boolean codecMatch = false;
            for (String acceptableCodec : target.codecs) {
                if (acceptableCodec.equalsIgnoreCase(codec)) {
                    codecMatch = true;
                    break;
                }
            }
            if (!codecMatch) return false;
        }
        if (target.formats != null && target.formats.length > 0) {
            boolean formatMatch = false;
            for (String acceptableFormat : target.formats) {
                if (acceptableFormat.equalsIgnoreCase(format)) {
                    formatMatch = true;
                    break;
                }
            }
            if (!formatMatch) return false;
        }
        if (target.profiles != null && target.profiles.length > 0) {
            boolean profileMatch = false;
            for (String acceptableProfile : target.profiles) {
                if (acceptableProfile.equalsIgnoreCase(profile)) {
                    profileMatch = true;
                    break;
                }
            }
            if (!profileMatch) return false;
        }
        if (target.versions != null && target.versions.length > 0 && !ArrayUtils.contains(target.versions, version)) {
            return false;
        }
        return true;
    }
}
