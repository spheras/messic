package org.messic.server.api.plugin.radio;

public enum MessicRadioStatus
{
    /**
     * the plugin has not been initialized yet
     */
    NONE,
    /**
     * the plugin has not been enabled by configuration
     */
    NOT_ENABLED,
    /**
     * the service is not available
     */
    NOT_AVAILABLE,
    /**
     * The service has not been started
     */
    NOT_STARTED,
    /**
     * The service is started, and waiting content to be casted
     */
    STARTED
}
