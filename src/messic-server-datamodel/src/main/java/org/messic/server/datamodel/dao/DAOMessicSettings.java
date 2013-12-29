package org.messic.server.datamodel.dao;

import org.messic.server.datamodel.MDOMessicSettings;



/**
 * DAO for Author table
 */
public interface DAOMessicSettings extends DAO<MDOMessicSettings>
{
	/**
	 * Obtain the current settings for messic
	 * @return {@link MDOMessicSettings}
	 */
	MDOMessicSettings getSettings();
	/**
	 * Replace the current settings with these new one settings
	 * @param newSettings {@link MDOMessicSettings}
	 * @return 
	 */
	MDOMessicSettings setSettings(MDOMessicSettings newSettings);
}
