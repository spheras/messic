package org.messic.server.api.tagwizard.audiotagger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.images.Artwork;
import org.messic.server.api.tagwizard.service.SongTags;
import org.messic.server.api.tagwizard.service.TAGInfo;
import org.messic.server.api.tagwizard.service.TAGWizardPlugin;

public class AudioTaggerTAGWizardPlugin implements TAGWizardPlugin
{

	public final String NAME="AUDIOTAGGER TAGWIZARD";
	public final String DESCRIPTION="TAGWIZARD plugin, based on audiotagger library to obtain mp3 tags (and other formarts) from music files";
	public final float VERSION=1.0f;
	public final float MINIMUM_MESSIC_VERSION=1.0f;
	
    @Override
    public List<SongTags> getTags( File[] f )
    {
        // first, obtain the tags
        ArrayList<SongTags> tags = new ArrayList<SongTags>();
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
            catch ( CannotReadException e )
            {
                //TODO
                e.printStackTrace();
            }
            catch ( IOException e )
            {
                //TODO
                e.printStackTrace();
            }
            catch ( ReadOnlyFileException e )
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if(tag!=null){
                SongTags ti = new SongTags();
                String artist = tag.getFirst(FieldKey.ARTIST);
                String composer = tag.getFirst(FieldKey.COMPOSER);
                if (artist.length() < composer.length()) {
                    ti.tags.put(TAGInfo.ARTIST, new TAGInfo(composer));
                } else {
                    ti.tags.put(TAGInfo.ARTIST, new TAGInfo(artist));
                }
                ti.tags.put(TAGInfo.ALBUM, new TAGInfo(tag.getFirst(FieldKey.ALBUM)));
                ti.tags.put(TAGInfo.YEAR, new TAGInfo(tag.getFirst(FieldKey.YEAR)));
                ti.tags.put(TAGInfo.COMMENT, new TAGInfo(tag.getFirst(FieldKey.COMMENT)));
                ti.tags.put(TAGInfo.GENRE, new TAGInfo(tag.getFirst(FieldKey.GENRE)));

                Artwork artwork = tag.getFirstArtwork();
                if (artwork != null) {
                    ti.coverArt = artwork.getBinaryData();
                }

                ti.track = tag.getFirst(FieldKey.TRACK);
                ti.title = tag.getFirst(FieldKey.TITLE);

                tags.add(ti);
            }
        }
        return tags;
    }

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
		return DESCRIPTION;
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
