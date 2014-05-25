package org.messic.server.api.datamodel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;
import org.messic.server.datamodel.MDOAlbum;
import org.messic.server.datamodel.MDOArtwork;
import org.messic.server.datamodel.MDOOtherResource;
import org.messic.server.datamodel.MDOSong;

@XmlRootElement
@ApiObject( name = "Album", description = "Album of music" )
public class Album
{
    @ApiObjectField( description = "identificator for the entity" )
    private long sid;

    @ApiObjectField( description = "temporal code that have the album" )
    private String code;

    @ApiObjectField( description = "name of the album" )
    private String name;

    @ApiObjectField( description = "year of publication" )
    private Integer year;

    @ApiObjectField( description = "Author of the Album" )
    private Author author;

    @ApiObjectField( description = "Cover image for the album" )
    private File cover;

    @ApiObjectField( description = "List of songs of the album" )
    private List<Song> songs=new ArrayList<Song>();

    @ApiObjectField( description = "List of artworks for the album" )
    private List<File> artworks=new ArrayList<File>();

    @ApiObjectField( description = "Other resources of the album" )
    private List<File> others=new ArrayList<File>();

    @ApiObjectField( description = "Genre of the album" )
    private Genre genre;

    @ApiObjectField( description = "Comments of the album" )
    private String comments;

    public static Album transform( org.messic.server.api.tagwizard.service.Album salbum )
    {
        Album album = new Album();

        album.author = new Author();
        album.author.setName( salbum.author );
        album.comments = salbum.comments;
        album.genre = new Genre();
        album.genre.setName( salbum.genre );
        album.name = salbum.name;
        album.year = salbum.year;
        album.songs = new ArrayList<Song>();
        for ( int i = 0; i < salbum.songs.size(); i++ )
        {
            org.messic.server.api.tagwizard.service.Song ssong = salbum.songs.get( i );
            Song song = new Song();
            song.setName( ssong.name );
            song.setTrack( ssong.track );
            album.songs.add( song );
        }

        return album;
    }

    public static org.messic.server.api.tagwizard.service.Album transform( Album album )
    {
        org.messic.server.api.tagwizard.service.Album salbum = new org.messic.server.api.tagwizard.service.Album();
        if ( album != null )
        {
            if ( album.getAuthor() != null )
            {
                salbum.author = album.getAuthor().getName();
            }
            salbum.comments = album.getComments();
            if ( album.getGenre() != null )
            {
                salbum.genre = album.getGenre().getName();
            }
            salbum.name = album.getName();
            salbum.year = album.getYear();
            salbum.songs = new ArrayList<org.messic.server.api.tagwizard.service.Song>();
            if ( album.getSongs() != null )
            {
                List<Song> songs = album.getSongs();
                for ( Song song : songs )
                {
                    org.messic.server.api.tagwizard.service.Song ssong =
                        new org.messic.server.api.tagwizard.service.Song();
                    ssong.track = song.getTrack();
                    ssong.name = song.getName();
                    salbum.songs.add( ssong );
                }
            }
        }
        return salbum;
    }

    /**
     * Transform a {@link List} of {@link MDOAlbum} to a {@link List} of {@link Album}
     * 
     * @param mdoalbums {@link List}<MDOAlbum/> to convert
     * @param author Author the Author of these albums, to avoid cross references
     * @param copyAuthor boolean indicates if it's necessary to copy the author of the album reference
     * @param copySongs boolean indicates if it's necessary to copy the songs of the album
     * @param copyResources boolean indicates if it's necessary to copy the resources of the album (artworks and others)
     * @return {@link List}<Album/> converted
     */
    public static List<Album> transform( List<MDOAlbum> mdoalbums, boolean copyAuthor, boolean copySongs,
                                         boolean copyResources )
    {
        ArrayList<Album> albums = new ArrayList<Album>();
        if ( mdoalbums != null )
        {
            for ( int i = 0; i < mdoalbums.size(); i++ )
            {
                albums.add( transform( mdoalbums.get( i ), copyAuthor, copySongs, copyResources ) );
            }
        }
        return albums;
    }

    /**
     * Transform a {@link MDOAlbum} to an {@link Album}
     * 
     * @param album {@link MDOAlbum} to conver
     * @param author Author the Author of these albums, to avoid cross references
     * @param copyAuthor boolean indicates if it's necessary to copy the author of the album reference
     * @param copySongs boolean indicates if it's necessary to copy the songs of the album
     * @param copyResources boolean indicates if it's necessary to copy the resources of the album (artworks and others)
     * @return {@link Album} converted
     */
    public static Album transform( MDOAlbum album, boolean copyAuthor, boolean copySongs, boolean copyResources )
    {
        return new Album( album, copyAuthor, copySongs, copyResources );
    }

    /**
     * Default constructor
     */
    public Album()
    {

    }

    /**
     * Copy constructor
     * 
     * @param mdoalbum {@link MDOAlbum}
     * @param copyAuthor boolean indicates if it's necessary to create a copy of the author of the album
     * @param copySongs boolean indicates if is necessary to copy songs also
     * @param copyResources boolean indicates if it's necessary to copy the resources of the album (artworks and others)
     */
    public Album( MDOAlbum mdoalbum, boolean copyAuthor, boolean copySongs, boolean copyResources )
    {
        setSid( mdoalbum.getSid() );
        setName( mdoalbum.getName() );
        setYear( mdoalbum.getYear() );
        setComments( mdoalbum.getComments() );
        if ( copyAuthor )
        {
            setAuthor( new Author( mdoalbum.getAuthor(), false, false ) );
        }
        setGenre( new Genre( mdoalbum.getGenre() ) );
        if ( copySongs )
        {
            Iterator<MDOSong> mdosongs = mdoalbum.getSongs().iterator();
            while ( mdosongs.hasNext() )
            {
                Song song = new Song( mdosongs.next(), null );
                addSong( song );
            }
        }
        if ( copyResources )
        {
            Iterator<MDOArtwork> mdoartworks = mdoalbum.getArtworks().iterator();
            while ( mdoartworks.hasNext() )
            {
                File file = new File( mdoartworks.next(), null );
                addArtwork( file );
            }
            Iterator<MDOOtherResource> mdoothers = mdoalbum.getOthers().iterator();
            while ( mdoothers.hasNext() )
            {
                File file = new File( mdoothers.next(), null );
                addOther( file );
            }
        }
    }

    /**
     * Copy constructor
     * 
     * @param mdoalbum {@link MDOAlbum}
     * @param author {@link Author} this is not an mdoauthor to avoid cross references.
     * @param copySongs boolean indicates if is necessary to copy songs also
     */
    public Album( MDOAlbum mdoalbum, Author author, boolean copySongs )
    {
        setSid( mdoalbum.getSid() );
        setName( mdoalbum.getName() );
        setYear( mdoalbum.getYear() );
        setAuthor( author );
        setGenre( new Genre( mdoalbum.getGenre() ) );
        setComments( mdoalbum.getComments() );
        if ( copySongs )
        {
            Iterator<MDOSong> mdosongs = mdoalbum.getSongs().iterator();
            while ( mdosongs.hasNext() )
            {
                Song song = new Song( mdosongs.next(), null );
                addSong( song );
            }
        }
    }

    public final long getSid()
    {
        return sid;
    }

    public final void setSid( long sid )
    {
        this.sid = sid;
    }

    public final String getName()
    {
        return name;
    }

    public final void setName( String name )
    {
        this.name = name;
    }

    public final Integer getYear()
    {
        return year;
    }

    public final void setYear( Integer year )
    {
        this.year = year;
    }

    public final Author getAuthor()
    {
        return author;
    }

    public final void setAuthor( Author author )
    {
        this.author = author;
    }

    public final List<Song> getSongs()
    {
        return songs;
    }

    public final void setSongs( List<Song> songs )
    {
        this.songs = songs;
    }

    public final void addSong( Song song )
    {
        if ( this.songs == null )
        {
            this.songs = new ArrayList<Song>();
        }
        this.songs.add( song );
    }

    public final List<File> getArtworks()
    {
        return artworks;
    }

    public final void setArtworks( List<File> artworks )
    {
        this.artworks = artworks;
    }

    public final void addArtwork( File artwork )
    {
        if ( this.artworks == null )
        {
            this.artworks = new ArrayList<File>();
        }
        this.artworks.add( artwork );
    }

    public final List<File> getOthers()
    {
        return others;
    }

    public final void setOthers( List<File> others )
    {
        this.others = others;
    }

    public final void addOther( File other )
    {
        if ( this.others == null )
        {
            this.others = new ArrayList<File>();
        }
        this.others.add( other );
    }

    public Genre getGenre()
    {
        return genre;
    }

    public void setGenre( Genre genre )
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

    public String getCode()
    {
        return code;
    }

    public void setCode( String code )
    {
        this.code = code;
    }

    public File getCover()
    {
        return cover;
    }

    public void setCover( File cover )
    {
        this.cover = cover;
    }
}
