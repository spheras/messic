package org.messic.server.api.datamodel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.messic.server.datamodel.MDOAlbum;
import org.messic.server.datamodel.MDOSong;

@XmlRootElement
public class Album {
	private long sid;
	private String code;
	private String name;
	private Integer year;
	private Author author;
	private File cover;
	private List<Song> songs;
	private List<File> artworks;
	private List<File> others;
	private Genre genre;
	private String comments;

	/**
	 * Transform a {@link List} of {@link MDOAlbum} to a {@link List} of {@link Album}
	 * @param mdoalbums {@link List}<MDOAlbum/> to convert
	 * @param author Author the Author of these albums, to avoid cross references
	 * @param copySongs boolean indicates if it's necessary to copy the songs of the album
	 * @return {@link List}<Album/> converted
	 */
	public static List<Album> transform(List<MDOAlbum> mdoalbums, boolean copySongs){
		ArrayList<Album> albums=new ArrayList<Album>();
		if(mdoalbums!=null){
			for(int i=0;i<mdoalbums.size();i++){
				albums.add(new Album(mdoalbums.get(i), true, copySongs));
			}
		}
		return albums;
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
				Song song=new Song(mdosongs.next(), this);
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
				Song song=new Song(mdosongs.next(), this);
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
