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
package org.messic.server.api.musicinfo.service;

import java.util.Locale;

import org.messic.server.api.plugin.MessicPlugin;

public interface MusicInfoPlugin extends MessicPlugin{

    static final String MUSIC_INFO_PLUGIN_NAME="MusicInfo";
    
    /**
     * Return an icon image (jpeg, or png), which can be representative of the provider 
     * @return byte[] the image
     */
    byte[] getProviderIcon();
    
	/**
	 * Obtain html info of the author name param
	 * @param locale {@link Locale} desired locale, at least english
	 * @param authorName {@link String} name of the author to locate
	 * @return {@link String} html info
	 */
	String getAuthorInfo(Locale locale, String authorName);

	/**
	 * Return the name of the provider which is the base to obtain the information
	 * @return {@link String}  the provider name
	 */
	String getProviderName();
	
	/**
	 * Obtain html info of the album passed of the author param
	 * @param locale {@link Locale} in which is desired the info (english as default)
	 * @param authorName {@link String} name of the author
	 * @param albumName {@link String} name of the album
	 * @return {@link String} html response
	 */
	String getAlbumInfo(Locale locale, String authorName, String albumName);
	
	/**
	 * Obtain html info of the song passed which album name of the author param
	 * @param locale {@link Locale} in which is desired the info (english as default)
	 * @param authorName {@link String} name of the author
	 * @param albumName {@link String} name of the album
	 * @param songName {@link String} name of the song
	 * @return {@link String} html response
	 */
	String getSongInfo(Locale locale, String authorName, String albumName, String songName);
}
