/*
 * Copyright (C) 2013 José Amuedo
 *
 *  This file is part of Messic.
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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

