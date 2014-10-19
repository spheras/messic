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
package org.messic.android.datamodel;

import java.util.ArrayList;
import java.util.List;

public class MDMRandomList
{
    private String name;

    private String title;

    private List<String> details;

    private List<MDMSong> songs;

    public MDMRandomList(){
        
    }
    
    /**
     * default constructor
     */
    public MDMRandomList( String name, String title )
    {
        super();
        setName( name );
        setTitle( title );
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public void addDetail( String detail )
    {
        if ( details == null )
        {
            details = new ArrayList<String>();
        }
        details.add( detail );
    }

    public List<String> getDetails()
    {
        return details;
    }

    public void setDetails( List<String> details )
    {
        this.details = details;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle( String title )
    {
        this.title = title;
    }

    public List<MDMSong> getSongs()
    {
        if ( this.songs == null )
        {
            this.songs = new ArrayList<MDMSong>();
        }
        return songs;
    }

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
