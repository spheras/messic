package org.messic.server.api.musicinfo.service;

import java.util.Locale;

import org.messic.server.api.plugin.MessicPlugin;

public interface MusicInfoPlugin extends MessicPlugin{

	/**
	 * Obtain html info of the author name param
	 * @param locale {@link Locale} desired locale, at least english
	 * @param authorName {@link String} name of the author to locate
	 * @return {@link String} html info
	 */
	String getAuthorInfo(Locale locale, String authorName);

	/**
	 * Obtain html info of the album passed of the author param
	 * @param locale {@link Locale} in which is desired the info (english as default)
	 * @param authorName {@link String} name of the author
	 * @param albumName {@link String} name of the album
	 * @return {@link String} html response
	 */
	String getAlbumInfo(Locale locale, String authorName, String albumName);
	
	/**
	 * Obtain html info of the song passed which album name of the author param
	 * @param locale {@link Locale} in which is desired the info (english as default)
	 * @param authorName {@link String} name of the author
	 * @param albumName {@link String} name of the album
	 * @param songName {@link String} name of the song
	 * @return {@link String} html response
	 */
	String getSongInfo(Locale locale, String authorName, String albumName, String songName);
}
