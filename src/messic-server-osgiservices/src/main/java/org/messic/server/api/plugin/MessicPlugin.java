/*
 * Copyright (C) 2013
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
package org.messic.server.api.plugin;

import java.util.Locale;
import java.util.Properties;

/**
 * Messic Plugin interface
 */
public interface MessicPlugin {
	
    public static final String CONFIG_PROXY_URL="proxy-url";
    public static final String CONFIG_PROXY_PORT="proxy-port";
    public static final String CONFIG_SECUREPROTOCOL = "messic-secure";
    
	/**
	 * Return the version of the plugin, like 2.15
	 * @return float version of the plugin
	 */
	float getVersion();

	/**
	 * Return the minimum required version of messic
	 * @return float minimum required version of messic
	 */
	float getMinimumMessicVersion();
	
	/**
	 * Get the name of the plugin
	 * @return String the name
	 */
	String getName();
	
	/**
	 * Obtain the description of the plugin in the corresponding language
	 * @param locale {@link Locale} locale for the language needed
	 * @return {@link String} the description in the locale desired (or at least in english)
	 */
	String getDescription(Locale locale);

	/**
	 * Get the default configuration of the plugin
	 * @return {@link Properties}  configuration
	 */
	Properties getConfiguration();

	/**
	 * set the configuration of the plugin
	 * @param properties {@link Properties} configuration
	 */
	void setConfiguration(Properties properties);
}
