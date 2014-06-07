package org.messic.server.api.dlna.chii2.transcoder.api.core;

import java.io.File;
import java.io.InputStream;

/**
 * Transcoder Process
 */
public interface TranscoderProcess {
    /**
     * Init Transcoding Process
     */
    public void init();

    /**
     * Destroy Transcoding Process
     */
    public void destroy();

    /**
     * Move Transcoding Process to Cache
     */
    public void cache();

    /**
     * Get Request ID
     * This should NOT be null
     *
     * @return Request ID
     */
    public String getRequestId();

    /**
     * Get Output File
     * This should NOT be null
     *
     * @return Output File
     */
    public File getOutputFile();

    /**
     * Get Output File Stream
     * This should NOT be null
     *
     * @return Output File Stream
     */
    public InputStream getOutputFileStream();

    /**
     * Get current transcoded file size
     * Size will changed if the process is running
     *
     * @return File Size
     */
    public long getCurrentSize();

    /**
     * Transcoded Process Started or Not
     *
     * @return True if Started
     */
    public boolean isStarted();

    /**
     * Transcoded Process Finished or Not
     *
     * @return True if Finished
     */
    public boolean isFinished();

    /**
     * Transcoded Process Stopped or Not
     *
     * @return True if Stopped
     */
    public boolean isStopped();

    /**
     * Set Transcoded Process Stopped
     *
     * @param stopped Stopped
     */
    public void setStopped(boolean stopped);

    /**
     * Set Transcoded Process Finished
     *
     * @param finished Finished
     */
    public void setFinished(boolean finished);

    /**
     * Set Transcoded Process Started
     *
     * @param started Started
     */
    public void setStarted(boolean started);
}
