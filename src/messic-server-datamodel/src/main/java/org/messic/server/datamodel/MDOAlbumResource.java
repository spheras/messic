package org.messic.server.datamodel;

import java.io.Serializable;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ALBUM_RESOURCES")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@DiscriminatorValue(MDOPhysicalResource.ALBUM_RESOURCE)
public class MDOAlbumResource extends MDOPhysicalResource implements
        MDO, Serializable {

	public static final String SONG = "SONG";
    public static final String ARTWORK = "ARTWORK";
    public static final String OTHER = "OTHER";

    /**
	 * 
	 */
	private static final long serialVersionUID = 5659995299036490152L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ALBUM", nullable = false)
    private MDOAlbum album;   

    public MDOAlbumResource ()
    {
        super();
    }
    
    public MDOAlbumResource (MDOUser user, String location)
    {
        super(user,location);
    }

    public MDOAlbum getAlbum() {
        return album;
    }

    public void setAlbum(MDOAlbum album) {
        this.album = album;
    }

}