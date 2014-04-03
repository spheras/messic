package org.messic.server.api.tagwizard;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.messic.server.api.datamodel.Album;
import org.messic.server.api.datamodel.Author;
import org.messic.server.api.datamodel.Genre;
import org.messic.server.api.datamodel.Song;
import org.messic.server.api.tagwizard.service.SongTags;
import org.messic.server.api.tagwizard.service.TAGInfo;
import org.messic.server.api.tagwizard.service.TAGWizardPlugin;
import org.messic.server.datamodel.MDOGenre;
import org.messic.server.datamodel.dao.DAOGenre;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.springframework.beans.factory.annotation.Autowired;

public class TAGWizard
{
    @Autowired
    public DAOGenre daoGenre;

    public static void main( String[] args )
        throws Exception
    {
        File fpath = new File( "/home/spheras/Desktop/prueba" );
        new TAGWizard().getAlbumWizard( fpath.listFiles() );
    }

    /**
     * Obtain the list of tagwizard plugins
     * 
     * @return List<TAGWizardPlugin/> the list of plugins to execute
     */
    private List<TAGWizardPlugin> getTAGWizardPlugins()
    {

        BundleContext context = FrameworkUtil.getBundle( TAGWizardPlugin.class ).getBundleContext();
        ArrayList<TAGWizardPlugin> result = new ArrayList<TAGWizardPlugin>();

        try
        {
            // Query for all service references matching any language.
            ServiceReference<?>[] refs = context.getServiceReferences( TAGWizardPlugin.class.getName(), "(Language=*)" );
            if ( refs != null )
            {
                for ( int i = 0; i < refs.length; i++ )
                {
                    result.add( (TAGWizardPlugin) context.getService( refs[i] ) );
                }
            }
        }
        catch ( Exception e )
        {
            // TODO log
        }
        return result;
    }

    public Album getAlbumWizard( File[] f )
        throws IOException
    {

        List<TAGWizardPlugin> plugins = getTAGWizardPlugins();
        List<SongTags> tags = plugins.get( 0 ).getTags( f );
        // second, try to obtain the best results for the album tags
        for ( int i = 0; i < tags.size(); i++ )
        {
            SongTags ti = tags.get( i );
            Iterator<String> keys = ti.tags.keySet().iterator();
            while ( keys.hasNext() )
            {
                String key = keys.next();
                TAGInfo itag = ti.tags.get( key );
                if ( itag.value == null || itag.value.length() == 0 )
                {
                    itag.puntuation = 0; // no data :(
                }
                else
                {
                    // 1-10 puntuation
                    itag.puntuation = ( howManyTimesIsRepeated( key, tags, itag.value ) * 10 / tags.size() );
                }
            }
        }

        // Creating the album...
        Album album = new Album();
        Author author = new Author();
        author.setName( getMostValued( tags, TAGInfo.ARTIST ) );
        album.setAuthor( author );
        album.setComments( getMostValued( tags, TAGInfo.COMMENT ) );
        album.setName( getMostValued( tags, TAGInfo.ALBUM ) );
        try
        {
            album.setYear( Integer.valueOf( getMostValued( tags, TAGInfo.YEAR ) ) );
        }
        catch ( Exception e )
        {
            // e.printStackTrace();
            album.setYear( 1900 );
        }
        album.setGenre( getBestGenre( getMostValued( tags, TAGInfo.GENRE ) ) );

        // TODO Cover artwork

        // now the songs
        orderByTrack( tags );
        for ( SongTags tagInfo : tags )
        {
            Song song = new Song();
            int trackNumber = 0;
            try
            {
                trackNumber = Integer.valueOf( tagInfo.track );
            }
            catch ( Exception e )
            {
            }
            song.setTrack( trackNumber );
            song.setName( tagInfo.title );
            album.addSong( song );
        }

        return album;
    }

    /**
     * Try to find a genre from the database, if not, put the genre founded in tags
     * 
     * @param genre {@link String} genreName to compare
     * @return {@link Genre} genre to link with the album
     */
    private Genre getBestGenre( String genre )
    {
        if ( this.daoGenre != null )
        {

            MDOGenre founded = this.daoGenre.getGenre( genre );
            if ( founded == null )
            {
                List<MDOGenre> listf = this.daoGenre.findSimilarGenre( genre, "" );
                if ( listf != null )
                {
                    founded = listf.get( 0 );
                }
            }

            if ( genre != null )
            {
                return new Genre( founded );
            }
            else
            {
                return new Genre( genre );
            }
        }

        return new Genre( genre );
    }

    /**
     * sort the list of tags by track number
     * 
     * @param tags {@link ArrayList}<TagInfo/> list of tags
     */
    private static void orderByTrack( List<SongTags> tags )
    {
        Collections.sort( tags, new Comparator<SongTags>()
        {
            @Override
            public int compare( SongTags o1, SongTags o2 )
            {
                int o1p = 0;
                try
                {
                    o1p = Integer.valueOf( o1.track );
                }
                catch ( Exception e )
                {
                }
                int o2p = 0;
                try
                {
                    o2p = Integer.valueOf( o2.track );
                }
                catch ( Exception e )
                {
                }

                if ( o1p == o2p )
                {
                    return 0;
                }
                else if ( o1p < o2p )
                {
                    return -1;
                }
                else
                {
                    return 1;
                }
            }
        } );

        System.out.println( "aver?" );
    }

    /**
     * Return the most vauled tag, the one which have the best punctuation
     * 
     * @param tags {@link ArrayList}<TagInfo/> list of tag infos recovered previously
     * @param key {@link String} key to evaluate
     * @return String the value of the most valued value
     */
    private static String getMostValued( List<SongTags> tags, final String key )
    {
        if ( tags != null && tags.size() > 0 )
        {
            Collections.sort( tags, new Comparator<SongTags>()
            {
                @Override
                public int compare( SongTags o1, SongTags o2 )
                {
                    int o1p = o1.tags.get( key ).puntuation;
                    int o2p = o2.tags.get( key ).puntuation;
                    if ( o1p == o2p )
                    {
                        return 0;
                    }
                    else if ( o1p < o2p )
                    {
                        return -1;
                    }
                    else
                    {
                        return 1;
                    }
                }
            } );
            return tags.get( 0 ).tags.get( key ).value;
        }
        else
        {
            return "";
        }
    }

    /**
     * Discover how many times is repeated the value for this tag.. the logic is that is repeated too many times, then
     * it appears to be more valid... (just a suggestion :) )
     * 
     * @param tag {@link String} tag that we are inspecting
     * @param tags {@link ArrayList}<TagInfo/> tagInfo list to compare
     * @param value String value to compare
     * @return int how many times is repeated the value
     */
    private static int howManyTimesIsRepeated( String tag, List<SongTags> tags, String value )
    {
        int result = 0;
        for ( int i = 0; i < tags.size(); i++ )
        {
            if ( tags.get( i ).tags.get( tag ).value.equalsIgnoreCase( value ) )
            {
                result++;
            }
        }

        return result - 1;
    }
}
