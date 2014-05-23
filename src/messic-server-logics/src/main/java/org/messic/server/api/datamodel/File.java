package org.messic.server.api.datamodel;

import javax.xml.bind.annotation.XmlRootElement;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;
import org.messic.server.datamodel.MDOAlbum;
import org.messic.server.datamodel.MDOPhysicalResource;
import org.messic.server.datamodel.MDOSong;

@XmlRootElement
@ApiObject(name="File", description="Resource of an album")
public class File {
    @ApiObjectField(description="identificator of the resource")
    private long sid;
	@ApiObjectField(description="temporal code for the resource")
	private String code;
	@ApiObjectField(description="fileName for the resource")
	private String fileName;
    @ApiObjectField(description="Size of the file")
	private long size;
	@ApiObjectField(description="Owner album of the resource")
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
		setSid( mdopr.getSid() );
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

    public long getSid()
    {
        return sid;
    }

    public void setSid( long sid )
    {
        this.sid = sid;
    }

    public long getSize()
    {
        return size;
    }

    public void setSize( long size )
    {
        this.size = size;
    }
}
