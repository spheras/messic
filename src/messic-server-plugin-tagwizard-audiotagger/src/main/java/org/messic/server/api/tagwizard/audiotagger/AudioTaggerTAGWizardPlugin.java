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
package org.messic.server.api.tagwizard.audiotagger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.images.Artwork;
import org.messic.server.api.tagwizard.service.Album;
import org.messic.server.api.tagwizard.service.Song;
import org.messic.server.api.tagwizard.service.SongTags;
import org.messic.server.api.tagwizard.service.TAGInfo;

public class AudioTaggerTAGWizardPlugin
{
    private Logger log = Logger.getLogger( AudioTaggerTAGWizardPlugin.class );

    public static final String NAME = "MESSIC TAGWIZARD";

    public static final String DESCRIPTION =
        "TAGWIZARD plugin, based on audiotagger library to obtain mp3 tags (and other formarts) from music files";

    public static final float VERSION = 1.0f;

    public static final float MINIMUM_MESSIC_VERSION = 1.0f;

    public void saveTags( Album album, Song song, File testFile )
        throws CannotReadException, IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException,
        CannotWriteException
    {
        AudioFile f = AudioFileIO.read( testFile );
        Tag tag = f.getTag();
        if ( tag == null )
        {
            tag = f.createDefaultTag();
            f.setTag( tag );
        }

        try
        {
            try
            {
                tag.setField( FieldKey.ARTIST, album.author );
            }
            catch ( Exception e )
            {
                try
                {
                    tag.createField( FieldKey.ARTIST, album.author );
                }
                catch ( Exception e2 )
                {
                    log.error( "failed!", e );
                }
            }
            try
            {
                tag.setField( FieldKey.ALBUM, album.name );
            }
            catch ( Exception e )
            {
                try
                {
                    tag.createField( FieldKey.ALBUM, album.name );
                }
                catch ( Exception e2 )
                {
                    log.error( "failed!", e );
                }
            }
            try
            {
                tag.setField( FieldKey.YEAR, "" + album.year );
            }
            catch ( Exception e )
            {
                try
                {
                    tag.createField( FieldKey.YEAR, "" + album.year );
                }
                catch ( Exception e2 )
                {
                    log.error( "failed!", e );
                }
            }
            try
            {
                tag.setField( FieldKey.COMMENT, album.comments );
            }
            catch ( Exception e )
            {
                try
                {
                    tag.createField( FieldKey.COMMENT, album.comments );
                }
                catch ( Exception e2 )
                {
                    log.error( "failed!", e );
                }
            }
            try
            {
                tag.setField( FieldKey.GENRE, album.genre );
            }
            catch ( Exception e )
            {
                try
                {
                    tag.createField( FieldKey.COMMENT, album.comments );
                }
                catch ( Exception e2 )
                {
                    log.error( "failed!", e );
                }
            }
            try
            {
                tag.setField( FieldKey.TRACK, "" + song.track );
            }
            catch ( Exception e )
            {
                try
                {
                    tag.createField( FieldKey.TRACK, "" + song.track );
                }
                catch ( Exception e2 )
                {
                    log.error( "failed!", e );
                }
            }
            try
            {
                tag.setField( FieldKey.TITLE, song.name );
            }
            catch ( Exception e )
            {
                try
                {
                    tag.createField( FieldKey.TITLE, song.name );
                }
                catch ( Exception e2 )
                {
                    log.error( "failed!", e );
                }
            }
        }
        catch ( Exception e )
        {
            log.error( "failed!", e );
        }

        AudioFileIO.write( f );
    }

    public List<SongTags> getTags( Album album, File[] files, Song[] songsWizard, Properties indexProp )
    {
        // first, obtain the tags
        ArrayList<SongTags> tags = new ArrayList<SongTags>();
        for ( int i = 0; i < files.length; i++ )
        {
            if ( files[i].getName().equals( "index.tmp.properties" ) )
            {
                continue;
            }
            AudioFile audioFile = null;
            Tag tag = null;
            try
            {
                audioFile = AudioFileIO.read( files[i] );
                tag = audioFile.getTag();
            }
            catch ( TagException e )
            {
            }
            catch ( InvalidAudioFrameException e )
            {
            }
            catch ( CannotReadException e )
            {
                log.error( "failed!", e );
            }
            catch ( IOException e )
            {
                log.error( "failed!", e );
            }
            catch ( ReadOnlyFileException e )
            {
                log.error( "failed!", e );
            }
            catch ( Exception e )
            {
                log.error( "failed!", e );
            }

            if ( tag != null )
            {
                SongTags ti = new SongTags();
                //instead of putting the server filename (safe filename), we put the original client filename stored at the index file 
                ti.filename = indexProp.getProperty( files[i].getName() );
                String artist = tag.getFirst( FieldKey.ARTIST );
                String composer = tag.getFirst( FieldKey.COMPOSER );
                if ( artist.length() < composer.length() )
                {
                    ti.tags.put( TAGInfo.ARTIST, new TAGInfo( composer ) );
                }
                else
                {
                    ti.tags.put( TAGInfo.ARTIST, new TAGInfo( artist ) );
                }

                ti.tags.put( TAGInfo.ALBUM, new TAGInfo( tag.getFirst( FieldKey.ALBUM ) ) );
                ti.tags.put( TAGInfo.YEAR, new TAGInfo( tag.getFirst( FieldKey.YEAR ) ) );
                ti.tags.put( TAGInfo.COMMENT, new TAGInfo( tag.getFirst( FieldKey.COMMENT ) ) );
                ti.tags.put( TAGInfo.GENRE, new TAGInfo( tag.getFirst( FieldKey.GENRE ) ) );

                Artwork artwork = tag.getFirstArtwork();
                if ( artwork != null )
                {
                    ti.coverArt = artwork.getBinaryData();
                }

                ti.track = tag.getFirst( FieldKey.TRACK );
                ti.title = tag.getFirst( FieldKey.TITLE );

                // trying to put the best track number (based on filename or based on mp3 tags)
                int trackNumber = -1;
                try
                {
                    trackNumber = Integer.valueOf( ti.track );
                }
                catch ( Exception e )
                {
                }
                if ( songsWizard[i].track != trackNumber )
                {
                    if ( trackNumber <= 0 )
                    {
                        ti.track = "" + songsWizard[i].track;
                    }
                }

                // trying to put the best song name (based on filename or based on mp3 tags)
                if ( !songsWizard[i].name.equals( ti.title ) )
                {
                    if ( ti.title.trim().length() <= 0 )
                    {
                        ti.title = songsWizard[i].name;
                    }
                }

                // finally adding to the tags list
                tags.add( ti );
            }
            else
            {
                SongTags ti = new SongTags();
                ti.tags.put( TAGInfo.ARTIST, new TAGInfo( "" ) );
                ti.tags.put( TAGInfo.ALBUM, new TAGInfo( "" ) );
                ti.tags.put( TAGInfo.YEAR, new TAGInfo( "" ) );
                ti.tags.put( TAGInfo.COMMENT, new TAGInfo( "" ) );
                ti.tags.put( TAGInfo.GENRE, new TAGInfo( "" ) );
                ti.track = "" + songsWizard[i].track;
                ti.title = songsWizard[i].name;
                ti.filename = files[i].getName();
                tags.add( ti );
            }
        }
        return tags;
    }

}
