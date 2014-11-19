package org.messic.android.datamodel;

import java.util.ArrayList;
import java.util.List;

public class MDMPlaylist
    extends MDMFile
{
    /**
     * 
     */
    private static final long serialVersionUID = -8100782035065825588L;

    private long sid;

    private String name;

    private List<MDMSong> songs;

    /**
     * @return the sid
     */
    public long getSid()
    {
        return sid;
    }

    /**
     * @param sid the sid to set
     */
    public void setSid( long sid )
    {
        this.sid = sid;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName( String name )
    {
        this.name = name;
    }

    /**
     * @return the songs
     */
    public List<MDMSong> getSongs()
    {
        return songs;
    }

    /**
     * @param songs the songs to set
     */
    public void setSongs( List<MDMSong> songs )
    {
        this.songs = songs;
    }

    public void addSong( MDMSong song )
    {
        if ( this.songs == null )
        {
            this.songs = new ArrayList<MDMSong>();
        }
        this.songs.add( song );
    }
}
