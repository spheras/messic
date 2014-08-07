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
package org.messic.server.datamodel.dao;

import org.messic.server.datamodel.MDOMessicSettings;
import org.springframework.transaction.annotation.Transactional;



/**
 * DAO for Author table
 */
@Transactional
public interface DAOMessicSettings extends DAO<MDOMessicSettings>
{
	/**
	 * Obtain the current settings for messic
	 * @return {@link MDOMessicSettings}
	 */
    @Transactional
	MDOMessicSettings getSettings();

    /**
	 * Replace the current settings with these new one settings
	 * @param sid
	 * @param genericBaseStorePath
	 * @return 
	 */
    @Transactional
	MDOMessicSettings setSettings(Long sid, String genericBaseStorePath);
	
	/**
	 * Save new settings to the database
	 * @param settings {@link MDOMessicSettings} settings to persist
	 * @return 
	 */
    @Transactional
	void saveSettings(MDOMessicSettings settings);
	
}
