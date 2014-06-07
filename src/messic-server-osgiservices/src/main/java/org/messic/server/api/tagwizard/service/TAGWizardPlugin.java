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
package org.messic.server.api.tagwizard.service;

import java.io.File;
import java.util.List;

import org.messic.server.api.plugin.MessicPlugin;


/**
 * Plugin to obtain tag information from songs files
 */
public interface TAGWizardPlugin extends MessicPlugin
{
    
    /**
     * Return a set of posibilities to the album info based on the information passed.
     * @param album {@link Album} album info
     * @param files File[] list of files
     * @return List<Album/> list of album posibilities
     */
    List<Album> getAlbumInfo(Album album, File[] files);
}
