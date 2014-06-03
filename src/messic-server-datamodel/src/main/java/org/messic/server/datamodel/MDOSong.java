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

    @ManyToMany( mappedBy = "songs" )
    private Set<MDOPlaylist> playlists;

    /**
     * Problem with inheritance and mappedby
     * http://chriswongdevblog.blogspot.com.es/2009/10/polymorphic-one-to-many-relationships.html that's the reason why
     * it's repeated on each {@link MDOAlbumResource} subclass
     */
    @ManyToOne( fetch = FetchType.LAZY )
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
        result = Util.replaceIllegalFilenameCharacters( result, illegalCharacterReplacement );
        String resultExtension = Util.replaceIllegalFilenameCharacters( extension, illegalCharacterReplacement );
        result = result + "." + resultExtension;
        return result;
    }

}