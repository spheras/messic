package org.messic.server.api.dlna.chii2.transcoder.api.core;

import java.io.File;
import java.util.List;

import org.fourthline.cling.support.model.dlna.DLNAProfiles;
import org.fourthline.cling.support.model.item.Movie;

/**
 * Transcoder Server act as a main entrance for all kinds of transcoding engines.
 */
public interface TranscoderService {

    // Common Profile
    public final static String PROFILE_COMMON = "common";
    // XBox Profile
    public final static String PROFILE_XBOX = "xbox";
    // WMP Profile
    public final static String PROFILE_WMP = "wmp";

    /**
     * Get Client Profile based on user agent information
     *
     * @param userAgent User Agent
     * @return Client Profile
     */
    public String getClientProfile(String userAgent);

    /**
     * Get Client Profile based on user agent information
     *
     * @param userAgent List of User Agent
     * @return Client Profile
     */
    public String getClientProfile(List<String> userAgent);

    /**
     * Whether the media is valid for the client
     *
     * @param client Client
     * @param movie  Movie
     * @return True if valid
     */
    public boolean isValidMedia(String client, Movie movie);

    /**
     * Get Video MIME
     *
     * @param container          Container
     * @param videoFormat        Video Format
     * @param videoFormatProfile Video Format Profile
     * @param videoFormatVersion Video Format Version
     * @param videoCodec         Video Codec
     * @return Video MIME
     */
    public String getVideoMIME(String container, String videoFormat, String videoFormatProfile, int videoFormatVersion, String videoCodec);

    /**
     * Get Audio DLNA Profile
     *
     * @param container          Container
     * @param audioFormat        Audio Format
     * @param audioFormatProfile Audio Format Profile
     * @param audioFormatVersion Audio Format Version
     * @param audioCodec         Audio Codec
     * @param audioBitRate       Audio BitRate
     * @param audioSampleBitRate Audio SampleBitRate
     * @param audioChannels      Audio Channels
     * @return Audio Profile
     */
    public DLNAProfiles getAudioProfile(String container, String audioFormat, String audioFormatProfile, int audioFormatVersion, String audioCodec, long audioBitRate, long audioSampleBitRate, int audioChannels);

    /**
     * Get Video DLNA Profile
     *
     * @param container          Container
     * @param videoFormat        Video Format
     * @param videoFormatProfile Video Format Profile
     * @param videoFormatVersion Video Format Version
     * @param videoCodec         Video Codec
     * @param videoBitRate       BitRate
     * @param videoWidth         Video Width
     * @param videoHeight        Video Height
     * @param fps                Video FPS
     * @param audioFormat        Audio Format
     * @param audioFormatProfile Audio Format Profile
     * @param audioFormatVersion Audio Format Version
     * @param audioCodec         Audio Codec
     * @param audioBitRate       Audio BitRate
     * @param audioSampleBitRate Audio SampleBitRate
     * @param audioChannels      Audio Channels
     * @return Video DLNA Profile
     */
    public DLNAProfiles getVideoProfile(String container, String videoFormat, String videoFormatProfile, int videoFormatVersion, String videoCodec, long videoBitRate, int videoWidth, int videoHeight, float fps,
                                        String audioFormat, String audioFormatProfile, int audioFormatVersion, String audioCodec, long audioBitRate, long audioSampleBitRate, int audioChannels);

    /**
     * Get Image DLNA Profile
     *
     * @param imageType   Image Type
     * @param imageWidth  Image Width
     * @param imageHeight Image Height
     * @return Image Profilen
     */
    public DLNAProfiles getImageProfile(String imageType, int imageWidth, int imageHeight);

    /**
     * Get Video Transcoded DLNA Profile
     *
     * @param client Client
     * @param movie  Movie
     * @return Video Transcoded DLNA Profile
     */
    public DLNAProfiles getVideoTranscodedProfile(String client, Movie movie);

    /**
     * Get Transcoded MIME
     *
     * @param client Client
     * @param movie  Movie
     * @return MIME
     */
    public String getTranscodedMIME(String client, Movie movie);

    /**
     * Get Transcoded Video Codec
     *
     * @param client Client
     * @param movie  Movie
     * @return Video Codec
     */
    public String getVideoTranscodedCodec(String client, Movie movie);

    /**
     * Get List of Transcoder Process
     *
     * @param client Client
     * @param movie  Movie
     * @return Transcoder Process
     */
    public List<TranscoderProcess> getTranscodedProcesses(String client, Movie movie);

    /**
     * Whether the image is valid for the client
     *
     * @param client      Client
     * @param imageType   Image Type
     * @param imageWidth  Image Width
     * @param imageHeight Image Height
     * @return True if valid
     */
    public boolean isValidImage(String client, String imageType, int imageWidth, int imageHeight);

    /**
     * Get Image Transcoded DLNA Profile
     *
     * @param client      Client
     * @param imageType   Image Type
     * @param imageWidth  Image Width
     * @param imageHeight Image Height
     * @return Image Profile
     */
    public DLNAProfiles getImageTranscodedProfile(String client, String imageType, int imageWidth, int imageHeight);

    /**
     * Get Image Transcoded MIME
     *
     * @param client      Client Profile
     * @param imageType   Original Image Type
     * @param imageWidth  Image Width
     * @param imageHeight Image Height
     * @return MIME Type
     */
    public String getImageTranscodedMime(String client, String imageType, int imageWidth, int imageHeight);

    /**
     * Get Image Transcoded Process
     *
     * @param client      Client Profile
     * @param imageFile   Image File
     * @param imageType   Original Image Type
     * @param imageWidth  Image Width
     * @param imageHeight Image Height
     * @return Image Transcoded Process
     */
    public ImageTranscoderProcess getImageTranscodedProcess(String client, File imageFile, String imageType, int imageWidth, int imageHeight);

    /**
     * Get Transcoded Video DLNA Profile
     *
     * @param client             Client
     * @param container          Container
     * @param videoFormat        Video Format
     * @param videoFormatProfile Video Format Profile
     * @param videoFormatVersion Video Format Version
     * @param videoCodec         Video Codec
     * @param videoBitRate       BitRate
     * @param videoWidth         Video Width
     * @param videoHeight        Video Height
     * @param fps                Video FPS
     * @param audioFormat        Audio Format
     * @param audioFormatProfile Audio Format Profile
     * @param audioFormatVersion Audio Format Version
     * @param audioCodec         Audio Codec
     * @param audioBitRate       Audio BitRate
     * @param audioSampleBitRate Audio SampleBitRate
     * @param audioChannels      Audio Channels
     * @return Video DLNA Profile
     */
    public DLNAProfiles getVideoTranscodedProfile(String client, String container, String videoFormat, String videoFormatProfile, int videoFormatVersion, String videoCodec, long videoBitRate, int videoWidth, int videoHeight, float fps, String audioFormat, String audioFormatProfile, int audioFormatVersion, String audioCodec, long audioBitRate, long audioSampleBitRate, int audioChannels);

    /**
     * Get Transcoded MIME
     *
     * @param client             Client
     * @param container          Container
     * @param videoFormat        Video Format
     * @param videoFormatProfile Video Format Profile
     * @param videoFormatVersion Video Format Version
     * @param videoCodec         Video Codec
     * @param videoBitRate       BitRate
     * @param videoWidth         Video Width
     * @param videoHeight        Video Height
     * @param fps                Video FPS
     * @param audioFormat        Audio Format
     * @param audioFormatProfile Audio Format Profile
     * @param audioFormatVersion Audio Format Version
     * @param audioCodec         Audio Codec
     * @param audioBitRate       Audio BitRate
     * @param audioSampleBitRate Audio SampleBitRate
     * @param audioChannels      Audio Channels
     * @return Video MIME
     */
    public String getVideoTranscodedMime(String client, String container, String videoFormat, String videoFormatProfile, int videoFormatVersion, String videoCodec, long videoBitRate, int videoWidth, int videoHeight, float fps, String audioFormat, String audioFormatProfile, int audioFormatVersion, String audioCodec, long audioBitRate, long audioSampleBitRate, int audioChannels);
}
