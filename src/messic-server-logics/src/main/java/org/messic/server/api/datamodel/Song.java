package org.messic.server.api.datamodel;

import javax.xml.bind.annotation.XmlRootElement;

import org.messic.server.datamodel.MDOAlbum;
import org.messic.server.datamodel.MDOSong;

@XmlRootElement
public class Song {
	private long sid;
	private int track;
	private String name;
	private Album album;
	
	/**
	 * default constructor
	 */
	public Song(){
		
	}
	
	/**
	 * copy constructor
	 * @param mdosong {@link MDOSong}
	 * @param album {@link Album} this is not an {@link MDOAlbum} to avoid cross references
	 */
	public Song(MDOSong mdosong, Album album){
		setSid(mdosong.getSid());
		setTrack(mdosong.getTrack());
		setName(mdosong.getName());
		setAlbum(album);
	}

	public final long getSid() {
		return sid;
	}

	public final void setSid(long sid) {
		this.sid = sid;
	}

	public final int getTrack() {
		return track;
	}

	public final void setTrack(int track) {
		this.track = track;
	}

	public final String getName() {
		return name;
	}

	public final void setName(String name) {
		this.name = name;
	}

	public final Album getAlbum() {
		return album;
	}

	public final void setAlbum(Album album) {
		this.album = album;
	}
}
