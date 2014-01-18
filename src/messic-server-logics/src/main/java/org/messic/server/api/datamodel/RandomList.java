package org.messic.server.api.datamodel;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

@XmlRootElement
@ApiObject(name="RandomList", description="List with songs")
public class RandomList{
	@ApiObjectField(description="name of the list")
	private String name;
	@ApiObjectField(description="title of the list")
	private String title;
	@ApiObjectField(description="List of details tags that are interesting to consider in the construction of the list")
	private List<String> details;
	@ApiObjectField(description="List of songs of the list")
	private List<Song> songs;

	/**
	 * default constructor
	 */
	public RandomList(String name, String title){
		super();
		setName(name);
		setTitle(title);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addDetail(String detail){
		if(details==null){
			details=new ArrayList<String>();
		}
		details.add(detail);
	}
	public List<String> getDetails() {
		return details;
	}

	public void setDetails(List<String> details) {
		this.details = details;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Song> getSongs() {
		return songs;
	}

	public void setSongs(List<Song> songs) {
		this.songs = songs;
	}
	
	public void addSong(Song song){
		if(this.songs==null){
			this.songs=new ArrayList<Song>();
		}
		this.songs.add(song);
	}

}
