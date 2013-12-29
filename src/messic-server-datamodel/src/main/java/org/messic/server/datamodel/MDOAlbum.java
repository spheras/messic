package org.messic.server.datamodel;


import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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

    @OneToMany(mappedBy = "album")
    private Set<MDOSong> songs;

    @Column(name = "COMMENTS", nullable = true)
    private String comments;

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

    public Set<MDOSong> getSongs() {
        return songs;
    }

    public void setSongs(Set<MDOSong> songs) {
        this.songs = songs;
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

        
}
