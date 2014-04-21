package org.messic.osgi;

import org.apache.log4j.Logger;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class MessicActivator implements BundleActivator {
	
    private static Logger logger = Logger.getLogger(MessicActivator.class);
    
    
	public void start(BundleContext context) {
	    logger.info( "Messic Bundle Started!" );
	}

	public void stop(BundleContext context) {
        logger.info( "Messic Bundle Stopped!" );
	}
	
}

