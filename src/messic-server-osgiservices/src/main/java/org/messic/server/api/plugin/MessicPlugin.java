package org.messic.server.api.plugin;

import java.util.Locale;
import java.util.Properties;

/**
 * Messic Plugin interface
 */
public interface MessicPlugin {
	
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
