package org.messic.server.spring;

import org.springframework.web.context.support.XmlWebApplicationContext;

public class MessicWebApplicationContext
    extends XmlWebApplicationContext
{

    @Override
    protected String[] getDefaultConfigLocations()
    {
        if ( getNamespace() != null )
        {
            return new String[] { "classpath:/springconfig/messic-servlet.xml" };
        }
        else
        {
            return new String[] { DEFAULT_CONFIG_LOCATION };
        }
    }

}
