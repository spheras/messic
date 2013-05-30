package org.messic.server.datamodel;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "SONGS")
@DiscriminatorValue(MDOPhysicalResource.SONG)

public class MDOSong extends MDOPhysicalResource implements
        MDO, Serializable {
    
    private static final long serialVersionUID = 8792913920714652055L;

    @Column(name = "NAME", nullable = false)
    private String name;    

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ALBUM", nullable = false)
    private MDOAlbum album;   
    
    @ManyToMany(mappedBy="songs")
    private Set<MDOPlaylist> playlists;
    
    public MDOSong ()
    {
        super();
    }
    
    public MDOSong (MDOUser user, String location, String name)
    {
        super(user,location);
        this.name = name;        
    }
    
    public String getName() {
        return name;
    }

    public MDOAlbum getAlbum() {
        return album;
    }

    public void setAlbum(MDOAlbum album) {
        this.album = album;
    }

    public Set<MDOPlaylist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(Set<MDOPlaylist> playlists) {
        this.playlists = playlists;
    }

	public String getAbsolutePath() {
		return getAlbum().getAuthor().getLocation().concat("/").concat(getAlbum().getLocation()).concat("/").concat(getLocation()) ;
	}
        
}