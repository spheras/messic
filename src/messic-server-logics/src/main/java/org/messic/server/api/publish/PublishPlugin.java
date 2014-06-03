package org.messic.server.api.publish;

import java.util.Locale;

import org.messic.server.api.plugin.MessicPlugin;

public interface PublishPlugin extends MessicPlugin{

    static final String PUBLISH_PLUGIN_NAME="Publish";
    
    /**
     * Return an icon image (jpeg, or png), which can be representative of the provider 
     * @return byte[] the image
     */
    byte[] getProviderIcon();
    
}
