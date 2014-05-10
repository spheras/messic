package org.messic.server.osgi;

import org.apache.log4j.Logger;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class MessicActivator implements BundleActivator {
	
    private static Logger logger = Logger.getLogger(MessicActivator.class);
    
    
	public void start(BundleContext context) {
	    logger.info( "Messic Server Bundle Started!" );
	}

	public void stop(BundleContext context) {
        logger.info( "Messic Server Bundle Stopped!" );
	}
	
}

