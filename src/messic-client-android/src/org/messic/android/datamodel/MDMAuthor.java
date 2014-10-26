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
package org.messic.android.datamodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MDMAuthor implements Serializable {
	/**
     * 
     */
    private static final long serialVersionUID = -6655094894229295492L;
    private long sid;
	private String name;
	private List<MDMAlbum> albums;
	
	/**
	 * Default constructor
	 */
	public MDMAuthor(){
		//default constructor
	}
	

	public final long getSid() {
		return sid;
	}

	public final void setSid(long sid) {
		this.sid = sid;
	}

	public final String getName() {
		return name;
	}

	public final void setName(String name) {
		this.name = name;
	}

	public final List<MDMAlbum> getAlbums() {
		return albums;
	}

	public final void setAlbums(List<MDMAlbum> albums) {
		this.albums = albums;
	}
	
	public final void addAlbum(MDMAlbum album){
		if(this.albums==null){
			this.albums=new ArrayList<MDMAlbum>();
		}
		this.albums.add(album);
	}
}
