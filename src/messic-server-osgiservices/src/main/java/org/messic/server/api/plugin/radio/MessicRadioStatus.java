package org.messic.server.api.plugin.radio;

public enum MessicRadioStatus
{

    /**
     * the plugin has not been initialized yet
     */
    NONE,
    /**
     * the service has not been enabled by configuration
     */
    NOT_ENABLED,
    /**
     * the service is enabled but it is can't available on this server
     */
    NOT_AVAILABLE,
    /**
     * The service is enabled and available but has not been started yet
     */
    NOT_STARTED,
    /**
     * The service is started, and waiting content to be casted
     */
    WAITING,
    /**
     * The service is started, and playing music
     */
    PLAYING;

}
