/*
 * Copyright (C) 2013
 *
 *  This file is part of Messic.
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.messic.server.datamodel;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table( name = "ALBUMS" )
@DiscriminatorValue( MDOPhysicalResource.ALBUM )
public class MDOAlbum
    extends MDOPhysicalResource
    implements MDO, Serializable
{

    // Default number of volumes for any album
    private static final int DEFAULT_VOLUMES = 1;

    private static final long serialVersionUID = 8792913920714652055L;

    @Column( name = "CREATED", nullable = true )
    // when was this album incorpored to the messic database
    private Date created;

    @Column( name = "NAME", nullable = false )
    private String name;

    @Column( name = "YEAR", nullable = true )
    private Integer year;

    @Column( name = "STATE", nullable = true )
    // this if for future versions, the state of the album will indicate if its active, or else
    private Integer state;

    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn( name = "AUTHOR", nullable = false )
    private MDOAuthor author;

    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn( name = "GENRE", nullable = true )
    private MDOGenre genre;

    @Column( name = "VOLUMES", nullable = true )
    private Integer volumes;

    @OneToMany( mappedBy = "album", cascade = { CascadeType.REMOVE }, fetch = FetchType.LAZY )
    @OrderBy( "TRACK" )
    private List<MDOSong> songs;

    @OneToMany( mappedBy = "album", cascade = { CascadeType.REMOVE } )
    private List<MDOArtwork> artworks;

    @OneToMany( mappedBy = "album", cascade = { CascadeType.REMOVE } )
    private List<MDOOtherResource> others;

    @Column( name = "COMMENTS", nullable = true )
    private String comments;

    @OneToMany( cascade = { CascadeType.REMOVE } )
    private List<MDOGenericTAG> extraTags;

    public MDOAlbum()
    {
        super();
        this.created = new Date( System.currentTimeMillis() );
    }

    public MDOAlbum( MDOUser user, String location, String name, Integer year, MDOAuthor author, MDOGenre genre,
                     String comments )
    {
        super( user, location );
        this.name = name;
        this.year = year;
        this.genre = genre;
        this.author = author;
        this.comments = comments;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public Integer getYear()
    {
        return year;
    }

    public void setYear( Integer year )
    {
        this.year = year;
    }

    public List<MDOSong> getSongs()
    {
        if ( songs == null )
        {
            songs = new ArrayList<MDOSong>();
        }
        return songs;
    }

    public void setSongs( List<MDOSong> songs )
    {
        this.songs = songs;
    }

    public List<MDOArtwork> getArtworks()
    {
        if ( artworks == null )
        {
            artworks = new ArrayList<MDOArtwork>();
        }
        return artworks;
    }

    public void setArtworks( List<MDOArtwork> artworks )
    {
        this.artworks = artworks;
    }

    public List<MDOOtherResource> getOthers()
    {
        if ( others == null )
        {
            others = new ArrayList<MDOOtherResource>();
        }
        return others;
    }

    public void setOthers( List<MDOOtherResource> others )
    {
        this.others = others;
    }

    public MDOAuthor getAuthor()
    {
        return author;
    }

    public void setAuthor( MDOAuthor author )
    {
        this.author = author;
    }

    public String calculateAbsolutePath( MDOMessicSettings settings )
    {
        String basePath = this.getAuthor().calculateAbsolutePath( settings );
        String result = basePath + File.separatorChar + getLocation();
        return result;
    }

    public MDOGenre getGenre()
    {
        return genre;
    }

    public void setGenre( MDOGenre genre )
    {
        this.genre = genre;
    }

    public String getComments()
    {
        return comments;
    }

    public void setComments( String comments )
    {
        this.comments = comments;
    }

    public MDOAlbumResource getCover()
    {
        List<MDOArtwork> artworks = getArtworks();
        if ( artworks != null )
        {
            for ( int i = 0; i < artworks.size(); i++ )
            {
                MDOArtwork artwork = artworks.get( i );
                if ( artwork.isCover() )
                {
                    return artwork;
                }
            }
        }
        return null;
    }

    /**
     * Return all the resources of teh albums: Songs, Artworks and Others.
     * 
     * @return {@link List}<MDOAlbumResources/> list of resources of the album
     */
    public List<MDOAlbumResource> getAllResources()
    {
        List<MDOAlbumResource> resources = new ArrayList<MDOAlbumResource>();
        List<MDOSong> songs = getSongs();
        for ( MDOSong mdoSong : songs )
        {
            resources.add( mdoSong );
        }
        List<MDOArtwork> artworks = getArtworks();
        for ( MDOArtwork mdoArtwork : artworks )
        {
            resources.add( mdoArtwork );
        }
        List<MDOOtherResource> others = getOthers();
        for ( MDOOtherResource mdoOtherResource : others )
        {
            resources.add( mdoOtherResource );
        }
        return resources;
    }

    /**
     * @return the state
     */
    public Integer getState()
    {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState( Integer state )
    {
        this.state = state;
    }

    /**
     * @return the created
     */
    public Date getCreated()
    {
        return created;
    }

    /**
     * @param created the created to set
     */
    public void setCreated( Date created )
    {
        this.created = created;
    }

    /**
     * @return the extraTags
     */
    public List<MDOGenericTAG> getExtraTags()
    {
        return extraTags;
    }

    /**
     * @param extraTags the extraTags to set
     */
    public void setExtraTags( List<MDOGenericTAG> extraTags )
    {
        this.extraTags = extraTags;
    }

    /**
     * @return the volumes
     */
    public Integer getVolumes()
    {
        // as this is a change for version 1.2, it is possible to find albums with a null value
        if ( this.volumes == null )
        {
            this.volumes = DEFAULT_VOLUMES;
        }

        return volumes;
    }

    /**
     * @param volumes the volumes to set
     */
    public void setVolumes( Integer volumes )
    {
        if ( volumes == null )
        {
            this.volumes = DEFAULT_VOLUMES;
        }
        else
        {
            this.volumes = volumes;
        }
    }

}
