package org.messic.server.api.dlna.chii2.mediaserver.content.xbox;

/**
 * Helper Class for res@microsoft:codec
 */
public class MicrosoftCodec {
    // Video encoded using the Windows Media Video codec version 7.
    public static final String WMMEDIASUBTYPE_WMV1 = "31564D57-0000-0010-8000-00AA00389B71";
    // Video encoded using the Windows Media Video 8 codec.
    public static final String WMMEDIASUBTYPE_WMV2 = "32564D57-0000-0010-8000-00AA00389B71";
    // Video encoded using the Windows Media Video 9 codec.
    public static final String WMMEDIASUBTYPE_WMV3 = "33564D57-0000-0010-8000-00AA00389B71";
    // Video encoded using the version of the Windows Media Video 9 Advanced Profile codec that was released with the Windows Media Format 9 Series SDK.
    public static final String WMMEDIASUBTYPE_WMVA = "41564D57-0000-0010-8000-00AA00389B71";
    // Video encoded with the Windows Media Video 9 Image codec to transform bitmaps and deformation data into a video stream.
    public static final String WMMEDIASUBTYPE_WMVP = "50564D57-0000-0010-8000-00AA00389B71";
    // Video encoded with the Windows Media Video 9 Image v2 codec.
    public static final String WMMEDIASUBTYPE_WVP2 = "32505657-0000-0010-8000-00AA00389B71";
    // Video encoded using the version of the Windows Media Video 9 Advanced Profile codec that was released with the Windows Media Format 11 SDK. This subtype identifies a bit stream that is compliant with the SMPTE VC-1 standard.
    public static final String WMMEDIASUBTYPE_WVC1 = "31435657-0000-0010-8000-00AA00389B71";
    // Video encoded by the Microsoft MPEG 4 codec version 3. This codec is no longer supported by the Windows Media Format SDK. If this codec is already installed on a computer, installing the Windows Media Format SDK or the redistribution package on a computer will not remove this codec.
    public static final String WMMEDIASUBTYPE_MP43 = "3334504D-0000-0010-8000-00AA00389B71";
    // Video encoded using the ISO MPEG 4 codec version 1.
    public static final String WMMEDIASUBTYPE_MP4S = "5334504D-0000-0010-8000-00AA00389B71";
    // Video encoded with the ISO MPEG4 v1.1 codec.
    public static final String WMMEDIASUBTYPE_M4S2 = "3253344D-0000-0010-8000-00AA00389B71";
    // Video encoded to MPEG 2 specifications.
    public static final String WMMEDIASUBTYPE_MPEG2_VIDEO = "e06d8026-db46-11cf-b4d1-00805f6cbbea";
    // Video encoded with the Windows Media Screen codec version 1.
    public static final String WMMEDIASUBTYPE_MSS1 = "3153534D-0000-0010-8000-00AA00389B71";
    // Video encoded with the Windows Media Video 9 Screen codec.
    public static final String WMMEDIASUBTYPE_MSS2 = "3253534D-0000-0010-8000-00AA00389B71";

    /**
     * Get Video Codec ID (Microsoft Codec ID)
     * TODO: This is not complete yet
     *
     * @param codecName Codec Name
     * @return Codec ID
     */
    public static String getVideoCodecId(String codecName) {
        if ("WMV1".equalsIgnoreCase(codecName)) {
            return "{" + WMMEDIASUBTYPE_WMV1 + "}";
        } else if ("WMV2".equalsIgnoreCase(codecName)) {
            return "{" + WMMEDIASUBTYPE_WMV2 + "}";
        } else if ("WMV3".equalsIgnoreCase(codecName)) {
            return "{" + WMMEDIASUBTYPE_WMV3 + "}";
        } else if ("WMVA".equalsIgnoreCase(codecName)) {
            return "{" + WMMEDIASUBTYPE_WMVA + "}";
        } else if ("WVC1".equalsIgnoreCase(codecName)) {
            return "{" + WMMEDIASUBTYPE_WVC1 + "}";
        } else if ("MP43".equalsIgnoreCase(codecName)) {
            return "{" + WMMEDIASUBTYPE_MP43 + "}";
        } else if ("MP4S".equalsIgnoreCase(codecName)) {
            return "{" + WMMEDIASUBTYPE_MP4S + "}";
        } else if ("M4S2".equalsIgnoreCase(codecName)) {
            return "{" + WMMEDIASUBTYPE_M4S2 + "}";
        } else if ("MSS1".equalsIgnoreCase(codecName)) {
            return "{" + WMMEDIASUBTYPE_MSS1 + "}";
        } else if ("MSS2".equalsIgnoreCase(codecName)) {
            return "{" + WMMEDIASUBTYPE_MSS2 + "}";
        } else if ("MPEG-2V".equalsIgnoreCase(codecName)) {
            return "{" + WMMEDIASUBTYPE_MPEG2_VIDEO + "}";
        } else {
            return null;
        }
    }
}
