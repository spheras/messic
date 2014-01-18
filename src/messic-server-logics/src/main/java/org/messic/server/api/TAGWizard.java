package org.messic.server.api;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.images.Artwork;
import org.messic.server.api.datamodel.Album;
import org.messic.server.api.datamodel.Author;
import org.messic.server.api.datamodel.Genre;
import org.messic.server.api.datamodel.Song;
import org.messic.server.datamodel.MDOGenre;
import org.messic.server.datamodel.dao.DAOGenre;
import org.springframework.beans.factory.annotation.Autowired;

public class TAGWizard {
	@Autowired
	public DAOGenre daoGenre;

	public static final String ARTIST = "ARTIST";
	public static final String ALBUM = "ALBUM";
	public static final String YEAR = "YEAR";
	public static final String COMMENT = "COMMENT";
	public static final String GENRE = "GENRE";

	public class ITag {
		public String value;
		public int puntuation;

		public ITag(String value) {
			this.value = value;
		}
	}

	public class TagInfo {
		public HashMap<String, ITag> tags = new HashMap<String, TAGWizard.ITag>();
		public byte[] coverArt;

		public String track;
		public String title;
	}

	public static void main(String[] args) throws Exception {
		File fpath = new File("/home/spheras/Desktop/prueba");
		new TAGWizard().getAlbumWizard(fpath.listFiles());
	}

	public Album getAlbumWizard(File[] f) throws CannotReadException, IOException, ReadOnlyFileException{

		// first, obtain the tags
		ArrayList<TagInfo> tags = new ArrayList<TAGWizard.TagInfo>();
		for (int i = 0; i < f.length; i++) {
			if(f[i].getName().equals(".index")){
				continue;
			}
			AudioFile audioFile=null;
			Tag tag=null;
			try {
				audioFile = AudioFileIO.read(f[i]);
				tag = audioFile.getTag();
			} catch (TagException e) {
			} catch (InvalidAudioFrameException e) {
			}

			if(tag!=null){
				TagInfo ti = new TAGWizard.TagInfo();
				String artist = tag.getFirst(FieldKey.ARTIST);
				String composer = tag.getFirst(FieldKey.COMPOSER);
				if (artist.length() < composer.length()) {
					ti.tags.put(ARTIST, new ITag(composer));
				} else {
					ti.tags.put(ARTIST, new ITag(artist));
				}
				ti.tags.put(ALBUM, new ITag(tag.getFirst(FieldKey.ALBUM)));
				ti.tags.put(YEAR, new ITag(tag.getFirst(FieldKey.YEAR)));
				ti.tags.put(COMMENT, new ITag(tag.getFirst(FieldKey.COMMENT)));
				ti.tags.put(GENRE, new ITag(tag.getFirst(FieldKey.GENRE)));

				Artwork artwork = tag.getFirstArtwork();
				if (artwork != null) {
					ti.coverArt = artwork.getBinaryData();
				}

				ti.track = tag.getFirst(FieldKey.TRACK);
				ti.title = tag.getFirst(FieldKey.TITLE);

				tags.add(ti);
			}
		}

		// second, try to obtain the best results for the album tags
		for (int i = 0; i < tags.size(); i++) {
			TagInfo ti = tags.get(i);
			Iterator<String> keys = ti.tags.keySet().iterator();
			while (keys.hasNext()) {
				String key = keys.next();
				ITag itag = ti.tags.get(key);
				if (itag.value == null || itag.value.length() == 0) {
					itag.puntuation = 0; // no data :(
				} else {
					// 1-10 puntuation
					itag.puntuation = (howManyTimesIsRepeated(key, tags,
							itag.value) * 10 / tags.size());
				}
			}
		}

		// Creating the album...
		Album album = new Album();
		Author author = new Author();
		author.setName(getMostValued(tags, ARTIST));
		album.setAuthor(author);
		album.setComments(getMostValued(tags, COMMENT));
		album.setName(getMostValued(tags, ALBUM));
		try {
			album.setYear(Integer.valueOf(getMostValued(tags, YEAR)));
		} catch (Exception e) {
			//e.printStackTrace();
			album.setYear(1900);
		}
		album.setGenre(getBestGenre(getMostValued(tags, GENRE)));
		
		
		//TODO Cover artwork
		
		//now the songs
		orderByTrack(tags);
		for (TagInfo tagInfo : tags) {
			Song song=new Song();
			int trackNumber=0;
			try{
				trackNumber=Integer.valueOf(tagInfo.track);
			}catch(Exception e){}
			song.setTrack(trackNumber);
			song.setName(tagInfo.title);
			album.addSong(song);
		}

		return album;
	}

	/**
	 * Try to find a genre from the database, if not, put the genre founded in
	 * tags
	 * 
	 * @param genre
	 *            {@link String} genreName to compare
	 * @return {@link Genre} genre to link with the album
	 */
	private Genre getBestGenre(String genre) {
		if (this.daoGenre != null) {

			MDOGenre founded = this.daoGenre.getGenre(genre);
			if (founded == null) {
				List<MDOGenre> listf = this.daoGenre
						.findSimilarGenre(genre, "");
				if (listf != null) {
					founded = listf.get(0);
				}
			}

			if (genre != null) {
				return new Genre(founded);
			} else {
				return new Genre(genre);
			}
		}

		return new Genre(genre);
	}

	/**
	 * sort the list of tags by track number
	 * @param tags {@link ArrayList}<TagInfo/> list of tags
	 */
	private static void orderByTrack(ArrayList<TagInfo> tags){
			Collections.sort(tags, new Comparator<TagInfo>() {
				@Override
				public int compare(TagInfo o1, TagInfo o2) {
					int o1p = 0;
					try{
						o1p=Integer.valueOf(o1.track);
					}catch(Exception e){
					}
					int o2p = 0;
					try{
						o2p=Integer.valueOf(o2.track);
					}catch(Exception e){
					}
					
					if (o1p == o2p) {
						return 0;
					} else if (o1p < o2p) {
						return -1;
					} else {
						return 1;
					}
				}
			});

			System.out.println("aver?");
		}
	
	/**
	 * Return the most vauled tag, the one which have the best punctuation
	 * 
	 * @param tags
	 *            {@link ArrayList}<TagInfo/> list of tag infos recovered
	 *            previously
	 * @param key
	 *            {@link String} key to evaluate
	 * @return String the value of the most valued value
	 */
	private static String getMostValued(ArrayList<TagInfo> tags,
			final String key) {
		if(tags!=null && tags.size()>0){
			Collections.sort(tags, new Comparator<TagInfo>() {
				@Override
				public int compare(TagInfo o1, TagInfo o2) {
					int o1p = o1.tags.get(key).puntuation;
					int o2p = o2.tags.get(key).puntuation;
					if (o1p == o2p) {
						return 0;
					} else if (o1p < o2p) {
						return -1;
					} else {
						return 1;
					}
				}
			});
			return tags.get(0).tags.get(key).value;
		}else{
			return "";
		}
	}

	/**
	 * Discover how many times is repeated the value for this tag.. the logic is
	 * that is repeated too many times, then it appears to be more valid...
	 * (just a suggestion :) )
	 * 
	 * @param tag
	 *            {@link String} tag that we are inspecting
	 * @param tags
	 *            {@link ArrayList}<TagInfo/> tagInfo list to compare
	 * @param value
	 *            String value to compare
	 * @return int how many times is repeated the value
	 */
	private static int howManyTimesIsRepeated(String tag,
			ArrayList<TagInfo> tags, String value) {
		int result = 0;
		for (int i = 0; i < tags.size(); i++) {
			if (tags.get(i).tags.get(tag).value.equalsIgnoreCase(value)) {
				result++;
			}
		}

		return result - 1;
	}
}
