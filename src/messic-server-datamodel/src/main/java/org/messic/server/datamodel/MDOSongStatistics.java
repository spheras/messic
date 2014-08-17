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
package org.messic.server.datamodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table( name = "SONG_STATISTICS" )
public class MDOSongStatistics
    implements MDO, Serializable
{

    private static final long serialVersionUID = 8792913920714652055L;

    @Id
    @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "SEQ_STATISTICS" )
    @SequenceGenerator( name = "SEQ_STATISTICS", sequenceName = "SEQ_STATISTICS" )
    @Column( name = "SID", nullable = false, unique = true )
    private Long sid;

    @Column( name = "TIMES_PLAYED", nullable = true )
    private Integer timesplayed = 0;

    @ElementCollection
    @CollectionTable( name = "SONG_STATISTICS_PLAYEDTIMES", joinColumns = @JoinColumn( name = "sid" ) )
    @Column( name = "PLAYED_TIMES" )
    @Type( type = "date" )
    private List<Date> playedTimes;

    @Column( name = "TIMES_STOPPED", nullable = true )
    private Integer timesstopped = 0;

    public MDOSongStatistics()
    {
        super();
    }

    /**
     * @return the timesplayed
     */
    public Integer getTimesplayed()
    {
        return timesplayed;
    }

    /**
     * @param timesplayed the timesplayed to set
     */
    public void setTimesplayed( Integer timesplayed )
    {
        this.timesplayed = timesplayed;
    }

    /**
     * @return the timesstopped
     */
    public Integer getTimesstopped()
    {
        return timesstopped;
    }

    /**
     * @param timesstopped the timesstopped to set
     */
    public void setTimesstopped( Integer timesstopped )
    {
        this.timesstopped = timesstopped;
    }

    public void addTimePlayed()
    {
        this.timesplayed++;
        if ( this.playedTimes == null )
        {
            this.playedTimes = new ArrayList<Date>();
        }
        this.playedTimes.add( new Date( System.currentTimeMillis() ) );

    }

    /**
     * @return the sid
     */
    public Long getSid()
    {
        return sid;
    }

    /**
     * @param sid the sid to set
     */
    public void setSid( Long sid )
    {
        this.sid = sid;
    }

    /**
     * @return the playedTimes
     */
    public List<Date> getPlayedTimes()
    {
        return playedTimes;
    }

    /**
     * @param playedTimes the playedTimes to set
     */
    public void setPlayedTimes( List<Date> playedTimes )
    {
        this.playedTimes = playedTimes;
    }
}