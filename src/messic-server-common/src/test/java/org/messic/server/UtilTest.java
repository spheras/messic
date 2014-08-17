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

import org.junit.Test;

import junit.framework.Assert;

public class UtilTest
{
    @Test
    public void testReplaceIllegalFilenameCharacters()
    {
        
        String fileName = "04-From?::/let's see.mp3";
        String result = Util.replaceIllegalFilenameCharacters( fileName, '_' );
        Assert.assertTrue( result.equals( "04-From_let's see.mp3" ) );
        
        fileName = "04-From /et's see.m*3";
        result = Util.replaceIllegalFilenameCharacters( fileName, '_' );
        Assert.assertTrue( result.equals( "04-From _et's see.m_3" ) );
        
        fileName ="07 - (You Are) This Body of Milk.mp3";
        result = Util.replaceIllegalFilenameCharacters( fileName, '_' );
        Assert.assertTrue( result.equals( "07 - _You Are_ This Body of Milk.mp3" ) );
        
    }

}
