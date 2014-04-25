package org.messic.server.api.musicinfo.plugin;

import java.util.Locale;
import java.util.Properties;

import org.messic.server.api.musicinfo.service.MusicInfoPlugin;

public class MusicInfoWikipediaPlugin implements MusicInfoPlugin
{

	public final String NAME="WIKIPEDIA MUSICINFO";
	public final String EN_DESCRIPTION="Plugin to obtain information of albums, songs and authors from wikipedia.";
	public final float VERSION=1.0f;
	public final float MINIMUM_MESSIC_VERSION=1.0f;

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public Properties getConfiguration() {
		return null;
	}

	@Override
	public void setConfiguration(Properties properties) {
		//no configuration
	}


	@Override
	public String getDescription(Locale locale) {
		//TODO any other language
		return EN_DESCRIPTION;
	}

	@Override
	public String getAuthorInfo(Locale locale, String authorName) {
		return null;
	}

	@Override
	public String getAlbumInfo(Locale locale, String authorName,
			String albumName) {
		return null;
	}

	@Override
	public String getSongInfo(Locale locale, String authorName,
			String albumName, String songName) {
		return null;
	}

	@Override
	public float getVersion() {
		return VERSION;
	}

	@Override
	public float getMinimumMessicVersion() {
		return MINIMUM_MESSIC_VERSION;
	}

}
