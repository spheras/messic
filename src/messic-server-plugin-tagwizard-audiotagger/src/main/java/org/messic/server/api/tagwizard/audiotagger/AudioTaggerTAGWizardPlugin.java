package org.messic.server.api.tagwizard.audiotagger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
import org.messic.server.api.tagwizard.service.Album;
import org.messic.server.api.tagwizard.service.SongTags;
import org.messic.server.api.tagwizard.service.TAGInfo;

public class AudioTaggerTAGWizardPlugin
{

	public static final String NAME="MESSIC TAGWIZARD";
	public static final String DESCRIPTION="TAGWIZARD plugin, based on audiotagger library to obtain mp3 tags (and other formarts) from music files";
	public static final float VERSION=1.0f;
	public static final float MINIMUM_MESSIC_VERSION=1.0f;
	
    
    public List<SongTags> getTags(Album album, File[] f )
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


}
