package org.messic.server.api.dlna.chii2.transcoder.api.core;

import java.io.File;

/**
 * Image Transcoder Process
 */
public interface ImageTranscoderProcess {
    /**
     * Init Transcoding Process
     */
    public void init();

    /**
     * Destroy Transcoding Process
     */
    public void destroy();

    /**
     * Get output file
     *
     * @return Output Image
     */
    public File getOutputFile();
}
