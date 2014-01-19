package org.messic.server.api.datamodel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;
import org.messic.server.datamodel.MDOAlbum;
import org.messic.server.datamodel.MDOSong;

@XmlRootElement
@ApiObject(name = "Album", description="Album of music")
public class Album {
	@ApiObjectField(description="identificator for the entity")
	private long sid;
	@ApiObjectField(description="temporal code that have the album")
	private String code;
	@ApiObjectField(description="name of the album")
	private String name;
	@ApiObjectField(description="year of publication")
	private Integer year;
	@ApiObjectField(description="Author of the Album")
	private Author author;
	@ApiObjectField(description="Cover image for the album")
	private File cover;
	@ApiObjectField(description="List of songs of the album")
	private List<Song> songs;
	@ApiObjectField(description="List of artworks for the album")
	private List<File> artworks;
	@ApiObjectField(description="Other resources of the album")
	private List<File> others;
	@ApiObjectField(description="Genre of the album")
	private Genre genre;
	@ApiObjectField(description="Comments of the album")
	private String comments;

	/**
	 * Transform a {@link List} of {@link MDOAlbum} to a {@link List} of {@link Album}
	 * @param mdoalbums {@link List}<MDOAlbum/> to convert
	 * @param author Author the Author of these albums, to avoid cross references
	 * @param copyAuthor boolean indicates if it's necessary to copy the author of the album reference
	 * @param copySongs boolean indicates if it's necessary to copy the songs of the album
	 * @return {@link List}<Album/> converted
	 */
	public static List<Album> transform(List<MDOAlbum> mdoalbums, boolean copyAuthor, boolean copySongs){
		ArrayList<Album> albums=new ArrayList<Album>();
		if(mdoalbums!=null){
			for(int i=0;i<mdoalbums.size();i++){
				albums.add(transform(mdoalbums.get(i), copyAuthor, copySongs));
			}
		}
		return albums;
	}


	/**
	 * Transform a {@link MDOAlbum} to an {@link Album}
	 * @param album {@link MDOAlbum} to conver
	 * @param author Author the Author of these albums, to avoid cross references
	 * @param copyAuthor boolean indicates if it's necessary to copy the author of the album reference
	 * @param copySongs boolean indicates if it's necessary to copy the songs of the album
	 * @return {@link Album} converted
	 */
	public static Album transform(MDOAlbum album, boolean copyAuthor, boolean copySongs){
		return new Album(album, copyAuthor, copySongs);
	}

	/**
	 * Default constructor
	 */
	public Album(){
		
	}

	
	/**
	 * Copy constructor
	 * @param mdoalbum {@link MDOAlbum}
	 * @param copyAuthor boolean indicates if it's necessary to create a copy of the author of the album
	 * @param copySongs boolean indicates if is necessary to copy songs also
	 */
	public Album(MDOAlbum mdoalbum, boolean copyAuthor, boolean copySongs){
		setSid(mdoalbum.getSid());
		setName(mdoalbum.getName());
		setYear(mdoalbum.getYear());
		if(copyAuthor){
			setAuthor(new Author(mdoalbum.getAuthor(), false,false));
		}
		setGenre(new Genre(mdoalbum.getGenre()));
		if(copySongs){
			Iterator<MDOSong> mdosongs=mdoalbum.getSongs().iterator();
			while(mdosongs.hasNext()){
				Song song=new Song(mdosongs.next(), null);
				addSong(song);
			}
		}
	}

	/**
	 * Copy constructor
	 * @param mdoalbum {@link MDOAlbum}
	 * @param author {@link Author} this is not an mdoauthor to avoid cross references.
	 * @param copySongs boolean indicates if is necessary to copy songs also
	 */
	public Album(MDOAlbum mdoalbum, Author author, boolean copySongs){
		setSid(mdoalbum.getSid());
		setName(mdoalbum.getName());
		setYear(mdoalbum.getYear());
		setAuthor(author);
		setGenre(new Genre(mdoalbum.getGenre()));
		setComments(mdoalbum.getComments());
		if(copySongs){
			Iterator<MDOSong> mdosongs=mdoalbum.getSongs().iterator();
			while(mdosongs.hasNext()){
				Song song=new Song(mdosongs.next(), null);
				addSong(song);
			}
		}
	}

	public final long getSid() {
		return sid;
	}

	public final void setSid(long sid) {
		this.sid = sid;
	}

	public final String getName() {
		return name;
	}

	public final void setName(String name) {
		this.name = name;
	}

	public final Integer getYear() {
		return year;
	}

	public final void setYear(Integer year) {
		this.year = year;
	}

	public final Author getAuthor() {
		return author;
	}

	public final void setAuthor(Author author) {
		this.author = author;
	}

	public final List<Song> getSongs() {
		return songs;
	}

	public final void setSongs(List<Song> songs) {
		this.songs = songs;
	}
	
	public final void addSong(Song song){
		if(this.songs==null){
			this.songs=new ArrayList<Song>();
		}
		this.songs.add(song);
	}
	public final List<File> getArtworks() {
		return artworks;
	}
	public final void setArtworks(List<File> artworks) {
		this.artworks = artworks;
	}
	
	public final void addArtwork(File artwork){
		if(this.artworks==null){
			this.artworks=new ArrayList<File>();
		}
		this.artworks.add(artwork);
	}
	public final List<File> getOthers() {
		return others;
	}
	public final void setOthers(List<File> others) {
		this.others = others;
	}
	
	public final void addOther(File other){
		if(this.others==null){
			this.others=new ArrayList<File>();
		}
		this.others.add(other);
	}

	public Genre getGenre() {
		return genre;
	}

	public void setGenre(Genre genre) {
		this.genre = genre;
	}


	public String getComments() {
		return comments;
	}


	public void setComments(String comments) {
		this.comments = comments;
	}


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public File getCover() {
		return cover;
	}


	public void setCover(File cover) {
		this.cover = cover;
	}
}
