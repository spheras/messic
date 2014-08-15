package org.messic.server.config;

import org.messic.configuration.MessicConfig;
import org.messic.configuration.MessicVersion;
import org.messic.server.datamodel.update.MessicDBUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class Startup
    implements ApplicationListener<ApplicationContextEvent>
{
    @Autowired
    private MessicDBUpdate dbUpdate;
    
    /** flag to know that we have started already */
    private boolean flagStarted=false;

    @Override
    public void onApplicationEvent( ApplicationContextEvent event )
    {
        synchronized ( this )
        {
            if ( !flagStarted && event instanceof ContextRefreshedEvent )
            {
                MessicVersion mv = MessicConfig.getCurrentVersion();
                dbUpdate.update( mv.sversion, mv.version, mv.revision, mv.patch, mv.semantic );
                this.flagStarted=true;
            }
        }
    }

}
