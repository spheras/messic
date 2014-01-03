package org.messic.server.api.datamodel;

import javax.xml.bind.annotation.XmlRootElement;

import org.messic.server.datamodel.MDOAlbum;
import org.messic.server.datamodel.MDOPhysicalResource;
import org.messic.server.datamodel.MDOSong;

@XmlRootElement
public class File {
	private String code;
	private String fileName;
	private Album album;
	
	/**
	 * default constructor
	 */
	public File(){
		
	}
	
	/**
	 * copy constructor
	 * @param mdosong {@link MDOSong}
	 * @param album {@link Album} this is not an {@link MDOAlbum} to avoid cross references
	 */
	public File(MDOPhysicalResource mdopr, Album album){
		setFileName(mdopr.getLocation());
		setAlbum(album);
	}

	public final Album getAlbum() {
		return album;
	}

	public final void setAlbum(Album album) {
		this.album = album;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
