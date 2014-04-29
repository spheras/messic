package org.messic.server.datamodel;

import java.io.File;
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
@DiscriminatorValue(MDOAlbumResource.SONG)
public class MDOSong extends MDOPhysicalResource implements
        MDO, Serializable {
    
    private static final long serialVersionUID = 8792913920714652055L;

    @Column(name = "NAME", nullable = false)
    private String name;    
    
    @Column(name = "TRACK", nullable = false)
    private Integer track;
    
    @ManyToMany(mappedBy="songs")
    private Set<MDOPlaylist> playlists;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ALBUM", nullable = false)
    private MDOAlbum album;   


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

    public Integer getTrack() {
        return track;
    }


    public Set<MDOPlaylist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(Set<MDOPlaylist> playlists) {
        this.playlists = playlists;
    }

	public String getAbsolutePath() {
		return getAlbum().getAuthor().getLocation().concat(""+File.separatorChar).concat(getAlbum().getLocation()).concat(""+File.separatorChar).concat(getLocation()) ;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTrack(Integer track) {
		this.track = track;
	}

    public MDOAlbum getAlbum() {
        return album;
    }

    public void setAlbum(MDOAlbum album) {
        this.album = album;
    }


}