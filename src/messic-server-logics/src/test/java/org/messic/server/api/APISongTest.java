/*
 * Copyright (C) 2013 José Amuedo
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

import org.junit.Assert;
import org.junit.Test;
import org.messic.server.api.datamodel.Song;

public class APISongTest
{

    @Test
    public void testGetSongInfoFromFileName()
    {
        APISong ast = new APISong();
        String fileName = "01-This is the song.mp3";
        Song result = ast.getSongInfoFromFileName( fileName );

        Assert.assertTrue( result.getName().equals( "This is the song" ) );
        Assert.assertTrue( result.getTrack() == 1 );
        
        fileName = "02 - This-is-the-song";
        result = ast.getSongInfoFromFileName( fileName );

        Assert.assertTrue( result.getName().equals( "This-is-the-song" ) );
        Assert.assertTrue( result.getTrack() == 2 );

        fileName = "03. This.is.the.song.mp4";
        result = ast.getSongInfoFromFileName( fileName );

        Assert.assertTrue( result.getName().equals( "This.is.the.song" ) );
        Assert.assertTrue( result.getTrack() == 3);

        fileName = "[04] This is the song.mp5";
        result = ast.getSongInfoFromFileName( fileName );

        Assert.assertTrue( result.getName().equals( "This is the song" ) );
        Assert.assertTrue( result.getTrack() == 4);

        fileName = "05 To think.mp3";
        result = ast.getSongInfoFromFileName( fileName );

        Assert.assertTrue( result.getName().equals( "To think" ) );
        Assert.assertTrue( result.getTrack() == 5);
        
        fileName = "06 2 think.mp3";
        result = ast.getSongInfoFromFileName( fileName );

        Assert.assertTrue( result.getName().equals( "2 think" ) );
        Assert.assertTrue( result.getTrack() == 6);
    }
}
