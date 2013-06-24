package org.messic.server.datamodel;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "PLAYLISTS")
@DiscriminatorValue(MDOResource.PLAYLIST)
public class MDOPlaylist extends MDOResource implements
        MDO, Serializable {
    
    private static final long serialVersionUID = 8792913920714652055L;

    @Column(name = "NAME", nullable = false)
    private String name;    

    @ManyToMany
    @JoinTable(name="PLAYLIST_CONTENT", joinColumns=@JoinColumn(name="SONG"), inverseJoinColumns=@JoinColumn(name="PLAYLIST"))
    private Set<MDOSong> songs;
    
    public MDOPlaylist ()
    {
        super();
    }
    
    public MDOPlaylist (MDOUser user, String name)
    {
        super(user);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<MDOSong> getSongs() {
        return songs;
    }

    public void setSongs(Set<MDOSong> songs) {
        this.songs = songs;
    }

}