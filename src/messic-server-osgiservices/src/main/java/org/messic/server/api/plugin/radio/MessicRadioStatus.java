package org.messic.server.api.plugin.radio;

public enum MessicRadioStatus
{

    /**
     * the plugin has not been initialized yet
     */
    NONE,
    /**
     * the service is not available
     */
    NOT_AVAILABLE,
    /**
     * the service is available but the plugin has not been enabled by configuration
     */
    NOT_ENABLED,
    /**
     * The service has not been started
     */
    NOT_STARTED,
    /**
     * The service is available and enabled
     */
    ENABLED,
    /**
     * The service is started, and waiting content to be casted
     */
    STARTED;



}
