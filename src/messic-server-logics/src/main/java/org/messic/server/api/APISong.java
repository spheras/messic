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
package org.messic.server.api;

import java.io.File;
import java.io.IOException;

import org.messic.server.Util;
import org.messic.server.api.datamodel.Song;
import org.messic.server.api.datamodel.User;
import org.messic.server.datamodel.MDOSong;
import org.messic.server.datamodel.dao.DAOAuthor;
import org.messic.server.datamodel.dao.DAOMessicSettings;
import org.messic.server.datamodel.dao.DAOSong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class APISong
{
    @Autowired
    private DAOMessicSettings daoSettings;

    @Autowired
    private DAOSong daoSong;

    @Autowired
    private DAOAuthor daoAuthor;

    @Transactional
    public void remove( User user, Long songSid )
        throws IOException
    {
        MDOSong song = this.daoSong.get( user.getLogin(), songSid );
        if ( song != null )
        {
            // first, removing the song file
            String path = song.calculateAbsolutePath( daoSettings.getSettings() );
            File fpath = new File( path );
            fpath.delete();
            // after, removing the album data from database
            this.daoSong.remove( song );
        }
    }

    public byte[] getAudioSong( User mdouser, long sid )
        throws IOException
    {
        MDOSong song = daoSong.get( mdouser.getLogin(), sid );
        if ( song != null )
        {
            String filePath = song.calculateAbsolutePath( daoSettings.getSettings() );
            File fsong = new File( filePath );
            if ( fsong.exists() )
            {
                return Util.readFile( filePath );
            }
        }

        return null;
    }

    /**
     * Return any possible separator between the track number and the filename
     * 
     * @param fileName
     * @return
     */
    private char getPossibleSeparator( String fileName )
    {
        for ( int i = 0; i < fileName.length(); i++ )
        {
            char c = fileName.substring( i, i + 1 ).toCharArray()[0];
            boolean isDigit = ( c >= '0' && c <= '9' );
            if ( !isDigit )
            {
                if ( c == ' ' )
                {
                    // then possible spacess until the real separator
                    char c1 = fileName.substring( i + 1, i + 2 ).toCharArray()[0];
                    char c2 = fileName.substring( i + 2, i + 3 ).toCharArray()[0];

                    if ( c1 != ' ' && ( c2 == ' ' ) )
                    {
                        return c1;
                    }
                    if(c1=='-'){
                        return c1;
                    }
                }

                return c;
            }
        }
        return ' ';
    }

    /**
     * Try to get the song name and track from a file name
     * 
     * @param filePath {@link String} the filename to investigate
     * @return {@link Song} song info, only the track number and track name
     */
    public Song getSongInfoFromFileName( String filePath )
    {
        File tmpf = new File( filePath );
        String fileName = tmpf.getName().trim();

        fileName = fileName.replaceAll( "_", " " );

        int where = Util.areThereNumbers( fileName );

        if ( where >= 0 )
        {
            char c = fileName.substring( 0, 1 ).toCharArray()[0];
            boolean isDigit = ( c >= '0' && c <= '9' );
            if ( isDigit )
            {
                // the track number start inmediatelly
                char separator = getPossibleSeparator( fileName );
                String[] parts = fileName.split( "\\" + separator );
                if ( parts.length <= 1 )
                {
                    separator = ' ';
                    parts = fileName.split( "" + separator );
                }
                for ( int i = 0; i < parts.length - 1; i++ )
                {
                    if ( Util.areThereNumbers( parts[i] ) >= 0 )
                    {
                        try
                        {
                            int trackn = Integer.valueOf( parts[i].trim() );
                            String trackName = Util.addPartsFromArray( parts, i + 1, parts.length - 1, "" + separator );
                            int wheredot = trackName.lastIndexOf( "." );
                            if ( wheredot >= 0 )
                            {
                                trackName = trackName.substring( 0, wheredot );
                            }
                            return new Song( 0, trackn, trackName.trim() );
                        }
                        catch ( Exception e )
                        {
                            // not a track?
                        }
                    }
                }
            }
            else
            {
                // maybe... something like [track] songname
                char close = ']';
                if ( c == '[' )
                {
                    close = ']';
                }
                if ( c == '(' )
                {
                    close = ')';
                }

                int whereclose = fileName.indexOf( close );
                if ( whereclose >= 0 )
                {
                    String trackn = fileName.substring( 1, whereclose );
                    String trackName = fileName.substring( whereclose + 1 );
                    int wheredot = trackName.lastIndexOf( "." );
                    if ( wheredot >= 0 )
                    {
                        trackName = trackName.substring( 0, wheredot );
                    }
                    return new Song( 0, Integer.valueOf( trackn ), trackName.trim() );
                }
            }
        }

        String trackName = fileName;
        int wheredot = trackName.indexOf( "." );
        if ( wheredot >= 0 )
        {
            trackName = trackName.substring( 0, wheredot );
        }
        return new Song( -1, -1, trackName );
    }

}
