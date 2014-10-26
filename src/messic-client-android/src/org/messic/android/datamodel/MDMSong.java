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

import java.io.Serializable;

import org.messic.android.controllers.Configuration;

public class MDMSong
    extends MDMFile implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = -4949164622586035247L;

    private long sid;

    private int track;

    private String name;

    private int rate;

    public String getURL()
    {
        return Configuration.getBaseUrl() + "/services/songs/" + this.sid + "/audio?messic_token=" + Configuration.getLastToken();
    }

    /**
     * Constructor
     * 
     * @param sid long sid of the song
     * @param track int track of the song
     * @param name {@link String} name of the song
     * @param rate int rate of the song
     */
    public MDMSong( long sid, int track, String name, int rate )
    {
        super();
        this.sid = sid;
        this.track = track;
        this.name = name;
        this.rate = rate;
    }

    /**
     * default constructor
     */
    public MDMSong()
    {
        super();
    }

    public final long getSid()
    {
        return sid;
    }

    public final void setSid( long sid )
    {
        this.sid = sid;
    }

    public final int getTrack()
    {
        return track;
    }

    public final void setTrack( int track )
    {
        this.track = track;
    }

    public final String getName()
    {
        return name;
    }

    public final void setName( String name )
    {
        this.name = name;
    }

    /**
     * @return the rate
     */
    public int getRate()
    {
        return rate;
    }

    /**
     * @param rate the rate to set
     */
    public void setRate( int rate )
    {
        this.rate = rate;
    }
}
