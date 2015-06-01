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

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.messic.server.Util;

@Entity
@Table( name = "SONGS" )
@DiscriminatorValue( MDOAlbumResource.SONG )
public class MDOSong
    extends MDOAlbumResource
    implements MDO, Serializable
{

    private static final long serialVersionUID = 8792913920714652055L;

    @Column( name = "NAME", nullable = false )
    private String name;

    @Column( name = "TRACK", nullable = false )
    private Integer track;

    @Column( name = "RATE", nullable = true )
    private Integer rate;

    @OneToMany( cascade = { CascadeType.REMOVE }, fetch = FetchType.LAZY )
    private List<MDOGenericTAG> extraTags;

    @ManyToMany( targetEntity = MDOPlaylist.class, fetch = FetchType.LAZY )
    @JoinTable( name = "PLAYLIST_CONTENT", joinColumns = @JoinColumn( name = "PLAYLIST" ), inverseJoinColumns = @JoinColumn( name = "SONG" ) )
    private Set<MDOPlaylist> playlists;

    @OneToOne( targetEntity = MDOSongStatistics.class, fetch = FetchType.LAZY, cascade = { CascadeType.REMOVE } )
    private MDOSongStatistics statistics;

    /**
     * Problem with inheritance and mappedby
     * http://chriswongdevblog.blogspot.com.es/2009/10/polymorphic-one-to-many-relationships.html that's the reason why
     * it's repeated on each {@link MDOAlbumResource} subclass
     */
    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn( name = "ALBUM", nullable = false )
    private MDOAlbum album;

    public MDOSong()
    {
        super();
    }

    public MDOSong( MDOUser user, String location, String name )
    {
        super( user, location );
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public Integer getTrack()
    {
        return track;
    }

    public Set<MDOPlaylist> getPlaylists()
    {
        return playlists;
    }

    public void setPlaylists( Set<MDOPlaylist> playlists )
    {
        this.playlists = playlists;
    }

    public MDOAlbum getAlbum()
    {
        return album;
    }

    public void setAlbum( MDOAlbum album )
    {
        this.album = album;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public void setTrack( Integer track )
    {
        this.track = track;
    }

    /**
     * Obtain the theorical filename of a song, i mean, the location that sould be, based on the track number and song
     * name (the location is the filename for songs)
     * 
     * @param song {@link MDOSong} song to get the theorical file name
     * @param extension {@link String} extension for the fileName
     * @return {@link String} the theorical location
     */
    public final String calculateSongTheoricalFileName( String extension, char illegalCharacterReplacement )
    {
        String result = Util.leftZeroPadding( getTrack(), 2 ) + "-" + getName();
        result = Util.replaceIllegalFilenameCharactersNew( result, illegalCharacterReplacement );
        String resultExtension = Util.replaceIllegalFilenameCharactersNew( extension, illegalCharacterReplacement );
        result = result + "." + resultExtension;
        return result;
    }

    /**
     * @return the rate
     */
    public Integer getRate()
    {
        if ( this.rate == null )
        {
            this.rate = 0;
        }
        return rate;
    }

    /**
     * @param rate the rate to set
     */
    public void setRate( Integer rate )
    {
        this.rate = rate;
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
     * @return the statistics
     */
    public MDOSongStatistics getStatistics()
    {
        return statistics;
    }

    /**
     * @param statistics the statistics to set
     */
    public void setStatistics( MDOSongStatistics statistics )
    {
        this.statistics = statistics;
    }

}