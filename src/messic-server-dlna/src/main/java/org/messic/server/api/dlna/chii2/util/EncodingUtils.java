package org.messic.server.api.dlna.chii2.util;

import org.apache.commons.lang.StringUtils;
import org.messic.server.api.dlna.chii2.Encoding;
import org.messic.server.api.dlna.chii2.Zhcode;

/**
 * Encoding Utils
 */
public class EncodingUtils {
    // Chinese Convert Tool
    private final static Zhcode zhcode = new Zhcode();

    /**
     * Convert Unicode Simplified Chinese to Unicode Traditional Chinese
     * @param input Input String
     * @return Output String
     */
    public static String convertS2T(String input) {
        return convertString(input, Encoding.UNICODES, Encoding.UNICODET);
    }

    /**
     * Convert Unicode Traditional Chinese to Unicode Simplified Chinese
     * @param input Input String
     * @return Output String
     */
    public static String convertT2S(String input) {
        return convertString(input, Encoding.UNICODET, Encoding.UNICODES);
    }

    /**
     * Convert Chinese String
     * @param input Input String
     * @param sourceEncoding Source Encoding, List in Encoding class
     * @param targetEncoding Target Encoding, List in Encoding class
     * @return Output String
     */
    public static String convertString(String input, int sourceEncoding, int targetEncoding) {
        if (StringUtils.isEmpty(input)) {
            return input;
        }
        return zhcode.convertString(input, sourceEncoding, targetEncoding);
    }
}
