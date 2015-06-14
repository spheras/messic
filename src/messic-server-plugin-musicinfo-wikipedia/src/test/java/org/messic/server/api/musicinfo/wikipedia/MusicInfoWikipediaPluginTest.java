package org.messic.server.api.musicinfo.wikipedia;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

public class MusicInfoWikipediaPluginTest
{

    @Test
    public void testSearch()
        throws IOException, SAXException
    {
        MusicInfoWikipediaPlugin miwp = new MusicInfoWikipediaPlugin();
        String result = miwp.search( "radiohead" );
        Assert.assertTrue( result.length() > 0 );
    }
}
