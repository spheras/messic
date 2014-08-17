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
package org.messic.server.api.datamodel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;
import org.messic.server.datamodel.MDOAlbum;
import org.messic.server.datamodel.MDOAuthor;

@XmlRootElement
@ApiObject(name="Author", description="Author of albums")
public class Author {
	@ApiObjectField(description="identification number for the author")
	private long sid;
	@ApiObjectField(description="name of the author")
	private String name;
	@ApiObjectField(description="list of albums of the author")
	private List<Album> albums;
	
	/**
	 * Default constructor
	 */
	public Author(){
		//default constructor
	}
	
	/**
	 * Transform a {@link List} of {@link MDOAuthor} to a {@link List} of {@link Author}
	 * @param mdoauthors {@link List}<MDOAuthor/> to convert
	 * @param copyAlbums boolean indicates if is necessary to copy the albums of each author
	 * @param copySongs boolean indicates if it's necessary to copy each song of the album
	 * @return {@link List}<Author/> converted
	 */
	public static List<Author> transform(List<MDOAuthor> mdoauthors, boolean copyAlbums, boolean copySongs){
		ArrayList<Author> authors=new ArrayList<Author>();
		if(mdoauthors!=null){
			for(int i=0;i<mdoauthors.size();i++){
				authors.add(transform(mdoauthors.get(i), copyAlbums, copySongs));
			}
		}
		return authors;
	}

	/**
	 * Transform a {@link MDOAuthor} to an {@link Author}
	 * @param mdoauthors {@link MDOAuthor} to convert
	 * @param copyAlbums boolean indicates if is necessary to copy the albums of the author
	 * @param copySongs boolean indicates if it's necessary to copy each song of the album
	 * @return {@link Author} converted
	 */
	public static Author transform(MDOAuthor mdoauthor, boolean copyAlbums, boolean copySongs){
		return new Author(mdoauthor, copyAlbums, copySongs);
	}

	/**
	 * Construct by an {@link MDOAuthor}
	 * @param mdoauthor {@link MDOAuthor} to copy data
	 * @param copyAlbums boolean indicates if it's necessary to copy each albums from the authors
	 * @param copySongs boolean indicates if it's necessary to copy each song of the album
	 */
	public Author(MDOAuthor mdoauthor, boolean copyAlbums, boolean copySongs){
		setSid(mdoauthor.getSid());
		setName(mdoauthor.getName());
		if(copyAlbums){
			Iterator<MDOAlbum> mdoalbums=mdoauthor.getAlbums().iterator();
			while(mdoalbums.hasNext()){
				MDOAlbum mdoAlbum=mdoalbums.next();
				Album album=new Album(mdoAlbum, null, copySongs);
				addAlbum(album);
			}
		}
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

	public final List<Album> getAlbums() {
		return albums;
	}

	public final void setAlbums(List<Album> albums) {
		this.albums = albums;
	}
	
	public final void addAlbum(Album album){
		if(this.albums==null){
			this.albums=new ArrayList<Album>();
		}
		this.albums.add(album);
	}
}
