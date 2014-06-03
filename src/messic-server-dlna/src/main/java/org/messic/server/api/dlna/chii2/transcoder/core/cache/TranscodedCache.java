package org.messic.server.api.dlna.chii2.transcoder.core.cache;

import java.io.File;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.messic.server.api.dlna.chii2.transcoder.api.core.TranscoderProcess;

/**
 * Global Cache for Transcoded Process and Transcoded File
 */
public class TranscodedCache {
    // Singleton
    private static final TranscodedCache INSTANCE = new TranscodedCache();
    // Max File Count
    private final static int MAX_FILE_COUNT = 100;
    // Max Process Count
    private final static int MAX_PROCESS_COUNT = 1;
    // Transcoded Process Timeout
    private final static int PROCESS_TIMEOUT = 10;
    // Transcoded Process Cache
    private Map<String, TranscoderProcess> processCache = Collections.synchronizedMap(new LinkedHashMap<String, TranscoderProcess>());
    // Transcoded Process Timer Cache
    private Map<String, ScheduledFuture> timerCache = Collections.synchronizedMap(new LinkedHashMap<String, ScheduledFuture>());
    // Transcoded File Cache
    private Map<String, File> fileCache = Collections.synchronizedMap(new LinkedHashMap<String, File>());
    // Timers
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(MAX_PROCESS_COUNT);
    // Logger
//    private Logger logger = LoggerFactory.getLogger("org.chii2.transcoder.core");

    /**
     * Try to cache Transcoded Process
     *
     * @param process Transcoded Process
     */
    public void cache(TranscoderProcess process) {
        if (process != null) {
            // Running
            if (process.isStarted() && !process.isFinished() && !process.isStopped()) {
                String id = process.getRequestId();
                if (!processCache.containsKey(id)) {
                    processCache.put(id, process);
//                    logger.debug("Add transcoded process from request {} to cache.", id);
                    timerCache.put(id, this.createProcessTimer(id));
//                    logger.info("Add transcoded process timer from request {} to cache.", id);
                    // Remove old one
                    if (processCache.size() > MAX_PROCESS_COUNT) {
                        for (Map.Entry<String, TranscoderProcess> entry : processCache.entrySet()) {
                            // Remove from cache
                            TranscoderProcess transcoderProcess = processCache.remove(entry.getKey());
//                            logger.info("Remove transcoded process from request {} from cache because cache limited.", entry.getKey());
                            // Cancel Timer
                            ScheduledFuture timer = timerCache.remove(entry.getKey());
                            if (timer != null) {
                                timer.cancel(false);
                            }
//                            logger.info("Timer from request {} has been canceled.", entry.getKey());
                            // Terminate process
//                            logger.info("Terminate process from request {} because cache limited.", entry.getKey());
                            this.terminateProcess(transcoderProcess);
                            if (processCache.size() <= MAX_PROCESS_COUNT) {
                                break;
                            }
                        }
                    }
                }
            } else {
                // Destroy Process First
                process.destroy();
                // Start and Finished
                if (process.isStarted() && process.isFinished()) {
                    String id = process.getRequestId();
                    if (!fileCache.containsKey(id)) {
                        fileCache.put(id, process.getOutputFile());
//                        logger.debug("Add transcoded file {} from request {} to cache.", process.getOutputFile().getAbsolutePath(), id);
                        // Remove old one
                        if (fileCache.size() > MAX_FILE_COUNT) {
                            for (Map.Entry<String, File> entry : fileCache.entrySet()) {
                                // Remove from cache
                                File file = fileCache.remove(entry.getKey());
//                                logger.info("Remove transcoded file {} from request {} from cache because cache limited.", file.getAbsolutePath(), entry.getKey());
                                // Delete file
//                                logger.info("Delete transcoded file {} from request {}.", file.getAbsolutePath(), entry.getKey());
                                this.deleteFile(entry.getValue());
                                if (fileCache.size() <= MAX_FILE_COUNT) {
                                    break;
                                }
                            }
                        }
                    }
                }
            }

        }
    }

    /**
     * Get Transcoded Cache Instance
     *
     * @return Transcoded Cache Instance
     */
    public static TranscodedCache getInstance() {
        return INSTANCE;
    }

    /**
     * Cache contains file for request id
     *
     * @param requestId Request ID
     * @return True if in the file cache
     */
    public boolean containFile(String requestId) {
        return fileCache.containsKey(requestId);
    }

    /**
     * Cache contains process for request id
     *
     * @param requestId Request ID
     * @return True if in the process cache
     */
    public boolean containProcess(String requestId) {
        return processCache.containsKey(requestId);
    }

    /**
     * Retrieve File from cache
     *
     * @param requestId Request ID
     * @return File
     */
    public File retrieveFile(String requestId) {
        File file = null;
        if (fileCache.containsKey(requestId)) {
            file = fileCache.remove(requestId);
            if (file != null) {
//                logger.debug("File {} for request {} is retrieved from cache.", file.getAbsolutePath(), requestId);
            }
        }
        return file;
    }

    /**
     * Retrieve Process from cache
     *
     * @param requestId Request ID
     * @return Transcoded Process
     */
    public TranscoderProcess retrieveProcess(String requestId) {
        TranscoderProcess process = null;
        if (timerCache.containsKey(requestId)) {
            ScheduledFuture timer = timerCache.remove(requestId);
            if (timer != null) {
                timer.cancel(false);
            }
//            logger.info("Timer cancelled for request {}.", requestId);
        }
        if (processCache.containsKey(requestId)) {
            process = processCache.remove(requestId);
//            logger.debug("Process for request {} is retrieved from cache.", requestId);
        }
        return process;
    }

    /**
     * Shutdown the cache
     * This should be called before application exit
     */
    public void shutdown() {
        for (ScheduledFuture timer : timerCache.values()) {
            timer.cancel(false);
        }
        timerCache.clear();
        scheduler.shutdownNow();
        for (TranscoderProcess process : processCache.values()) {
            process.destroy();
        }
        processCache.clear();
        for (File file : fileCache.values()) {
            this.deleteFile(file);
        }
        fileCache.clear();
    }

    private void deleteFile(File file) {
        if (file != null && file.exists()) {
            try {
                boolean deleted = file.delete();
                if (deleted) {
//                    logger.info("Temp file {} has been deleted.", file.getAbsolutePath());
                } else {
//                    logger.warn("Temp file {} can not be deleted.", file.getAbsolutePath());
                }
            } catch (SecurityException e) {
                e.printStackTrace();
//                logger.error("Try to delete temp file {} with Security Exception: {}", file.getAbsolutePath(), e.getMessage());
            }
        }
    }

    private void terminateProcess(TranscoderProcess process) {
        // Destroy
        process.destroy();
        // Finished ? add to file cache
        if (process.isStarted() && process.isFinished()) {
            String id = process.getRequestId();
            if (!fileCache.containsKey(id)) {
                fileCache.put(id, process.getOutputFile());
//                logger.debug("Add transcoded file {} from request {} to cache.", process.getOutputFile().getAbsolutePath(), id);
                // Remove old one
                if (fileCache.size() > MAX_FILE_COUNT) {
                    for (Map.Entry<String, File> entry : fileCache.entrySet()) {
                        // Remove from cache
                        File file = fileCache.remove(entry.getKey());
//                        logger.info("Remove transcoded file {} from request {} from cache because cache limited.", file.getAbsolutePath(), entry.getKey());
                        // Delete file
//                        logger.info("Delete transcoded file {} from request {}.", file.getAbsolutePath(), entry.getKey());
                        this.deleteFile(entry.getValue());
                        if (fileCache.size() <= MAX_FILE_COUNT) {
                            break;
                        }
                    }
                }
            }
        } else {
            this.deleteFile(process.getOutputFile());
        }
    }

    private ScheduledFuture createProcessTimer(String processId) {
        return scheduler.schedule(new ProcessCallable(processId), PROCESS_TIMEOUT, TimeUnit.SECONDS);
    }

    private class ProcessCallable implements Callable<Boolean> {
        // Transcoded Process Request ID
        private String processId;

        public ProcessCallable(String processId) {
            this.processId = processId;
        }

        @Override
        public Boolean call() throws Exception {
            if (processCache.containsKey(processId)) {
                // Force Stop Process
                TranscoderProcess process = processCache.remove(processId);
                if (process != null) {
//                    logger.debug("Terminate process from request {} by timer.", processId);
                    terminateProcess(process);
                }
                // Remove timer from cache
                timerCache.remove(processId);
                return true;
            } else {
                return false;
            }
        }
    }
}
