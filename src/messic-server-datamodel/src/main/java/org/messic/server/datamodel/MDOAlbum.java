package org.messic.server.datamodel;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name = "ALBUMS")
@DiscriminatorValue(MDOPhysicalResource.ALBUM)

public class MDOAlbum extends MDOPhysicalResource implements
        MDO, Serializable {
    
    private static final long serialVersionUID = 8792913920714652055L;

    @Column(name = "NAME", nullable = false)
    private String name;
    
    @Column(name = "YEAR", nullable = true)
    private Integer year;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AUTHOR", nullable = false)
    private MDOAuthor author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GENRE", nullable = true)
    private MDOGenre genre;   

    @OneToMany(mappedBy = "album", cascade={CascadeType.REMOVE})
    @OrderBy("TRACK")
    private List<MDOSong> songs;

    @OneToMany(mappedBy = "album", cascade={CascadeType.REMOVE})
    private List<MDOAlbumResource> artworks;
    
    @OneToMany(mappedBy = "album", cascade={CascadeType.REMOVE})
    private List<MDOAlbumResource> others;
    
    
    @Column(name = "COMMENTS", nullable = true)
    private String comments;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ALBUM", nullable = true)
    private MDOAlbumResource cover;

    public MDOAlbum ()
    {
        super();
    }
    
    public MDOAlbum (MDOUser user, String location, String name, Integer year, MDOAuthor author, MDOGenre genre, String comments)
    {
        super(user,location);
        this.name = name;
        this.year = year;
        this.genre= genre;
        this.author=author;
        this.comments=comments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public List<MDOSong> getSongs() {
    	if(songs==null){
    		songs=new ArrayList<MDOSong>();
    	}
        return songs;
    }

    public void setSongs(List<MDOSong> songs) {
        this.songs = songs;
    }

    public List<MDOAlbumResource> getArtworks() {
    	if(artworks==null){
    		artworks=new ArrayList<MDOAlbumResource>();
    	}
        return artworks;
    }

    public void setArtworks(List<MDOAlbumResource> artworks) {
        this.artworks = artworks;
    }

    public List<MDOAlbumResource> getOthers() {
    	if(others==null){
    		others=new ArrayList<MDOAlbumResource>();
    	}
        return others;
    }

    public void setOthers(List<MDOAlbumResource> others) {
        this.others = others;
    }

    public MDOAuthor getAuthor() {
        return author;
    }

    public void setAuthor(MDOAuthor author) {
        this.author = author;
    }
    
    public String getAbsolutePath() {
		return getAuthor().getLocation().concat("/").concat(getLocation()) ;
	}

	public MDOGenre getGenre() {
		return genre;
	}

	public void setGenre(MDOGenre genre) {
		this.genre = genre;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public MDOAlbumResource getCover() {
		return cover;
	}

	public void setCover(MDOAlbumResource cover) {
		this.cover = cover;
	}

        
}
