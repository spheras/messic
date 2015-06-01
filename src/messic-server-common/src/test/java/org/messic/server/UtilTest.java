/*
 * 
 * Copyright (C) 2013
 * 
 * This file is part of Messic.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.messic.server;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class UtilTest
{
    @Test
    public void testReplaceIllegalFilenameCharacters()
    {
        // [\"%*\\/:<>?\\^`{|}]+
        String fileName = "04-From?::/let's see.mp3";
        String result = Util.replaceIllegalFilenameCharactersNew( fileName, '_' );
        Assert.assertTrue( result.equals( "04-From_-let's see.mp3" ) );

        fileName = "04-Fr^om /et's see\\.m*3";
        result = Util.replaceIllegalFilenameCharactersNew( fileName, '_' );
        Assert.assertTrue( result.equals( "04-Fr_om -et's see-.m_3" ) );

        fileName = "07 - (You Are) This {Body} of <M>il|k.mp3";
        result = Util.replaceIllegalFilenameCharactersNew( fileName, '_' );
        Assert.assertTrue( result.equals( "07 - (You Are) This [Body] of [M]il-k.mp3" ) );

    }

    @Rule
    public TemporaryFolder tfolder = new TemporaryFolder();

    @Test
    public void testGetRollFileNumber()
        throws IOException
    {
        File f = new File( "./testFile0" );
        File fresult = Util.getRollFileNumber( f );
        Assert.assertTrue( fresult.getName().equals( "testFile0" ) );

        f = tfolder.newFile( "testFile1.txt" );
        fresult = Util.getRollFileNumber( f );
        Assert.assertTrue( fresult.getName().equals( "testFile1_1.txt" ) );

        f = tfolder.newFile( "testFile_2.txt" );
        fresult = Util.getRollFileNumber( f );
        Assert.assertTrue( fresult.getName().equals( "testFile_3.txt" ) );

        f = tfolder.newFile( "testFile_test.txt" );
        fresult = Util.getRollFileNumber( f );
        Assert.assertTrue( fresult.getName().equals( "testFile_test_1.txt" ) );
    }
}
