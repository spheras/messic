package org.messic.server.api.datamodel;

import javax.xml.bind.annotation.XmlRootElement;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;
import org.messic.server.datamodel.MDOAlbum;
import org.messic.server.datamodel.MDOSong;

@XmlRootElement
@ApiObject(name="Song", description="A song of an album")
public class Song extends File{
	@ApiObjectField(description="identificator of the song")
	private long sid;
	@ApiObjectField(description="track of the song")
	private int track;
	@ApiObjectField(description="name of the song")
	private String name;
	
	/**
	 * default constructor
	 */
	public Song(){
		super();
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
		setFileName(mdosong.getLocation());
	}

	/**
	 * copy constructor
	 * @param mdosong {@link MDOSong}
	 */
	public Song(MDOSong mdosong){
		setSid(mdosong.getSid());
		setTrack(mdosong.getTrack());
		setName(mdosong.getName());
		Album album=new Album(mdosong.getAlbum(),true,false);
		setAlbum(album);
		setFileName(mdosong.getLocation());
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

}
