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

public class TAGInfo {
    
    public static final String ARTIST = "ARTIST";
    public static final String ALBUM = "ALBUM";
    public static final String YEAR = "YEAR";
    public static final String COMMENT = "COMMENT";
    public static final String GENRE = "GENRE";

    
	public String value;
	public int puntuation;

	public TAGInfo(String value) {
		this.value = value;
	}
}
