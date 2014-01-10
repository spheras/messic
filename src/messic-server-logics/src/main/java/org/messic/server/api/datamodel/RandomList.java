package org.messic.server.api.datamodel;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RandomList{
	private String name;
	private String title;
	private List<String> details;
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
