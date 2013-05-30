package org.messic.server.datamodel;


import java.io.Serializable;
import java.util.Date;
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
    
    @Column(name = "XDATE", nullable = false)
    private Date date;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AUTHOR", nullable = false)
    private MDOAuthor author;   
    
    @OneToMany(mappedBy = "album")
    private Set<MDOSong> songs;
    
    public MDOAlbum ()
    {
        super();
    }
    
    public MDOAlbum (MDOUser user, String location, String name, Date date)
    {
        super(user,location);
        this.name = name;
        this.date = date;        
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

        
}
