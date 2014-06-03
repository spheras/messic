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
        
        fileName ="07 - (You Take) This Heart of Mine.mp3";
        result = Util.replaceIllegalFilenameCharacters( fileName, '_' );
        Assert.assertTrue( result.equals( "07 - _You Take_ This Heart of Mine.mp3" ) );
    }

}
