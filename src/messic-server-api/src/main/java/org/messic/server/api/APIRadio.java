package org.messic.server.api;

import java.io.IOException;

import org.messic.configuration.MessicConfig;
import org.messic.server.api.plugin.radio.MessicRadioPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class APIRadio
{
    @Autowired
    private MessicConfig messicConfig;

    private static MessicRadioPlugin plugin = null;

    private static MessicRadioPlugin getPlugin()
    {

        if ( plugin == null )
        {
            BundleContext context = FrameworkUtil.getBundle( MessicRadioPlugin.class ).getBundleContext();
            // Query for all service references matching any TAGWizard plugin
            ServiceReference<?>[] refs;
            try
            {
                refs =
                    context.getServiceReferences( MessicRadioPlugin.class.getName(), "("
                        + MessicRadioPlugin.MESSIC_RADIO_PLUGIN_NAME + "=*)" );
            }
            catch ( InvalidSyntaxException e )
            {
                e.printStackTrace();
                return null;
            }

            MessicRadioPlugin mrp = (MessicRadioPlugin) context.getService( refs[0] );
            plugin = mrp;
        }

        return plugin;

    }

    public void getStatus()
    {
        MessicRadioPlugin mrp = getPlugin();

    }

    public void startRadio()
        throws InvalidSyntaxException, IOException
    {
        MessicRadioPlugin mrp = getPlugin();
        mrp.setConfiguration( this.messicConfig.getConfiguration() );
        mrp.startCast();
    }
}
