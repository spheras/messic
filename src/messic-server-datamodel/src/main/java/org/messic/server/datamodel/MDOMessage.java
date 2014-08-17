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
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table( name = "MESSAGES" )
public class MDOMessage
    implements MDO, Serializable
{

    /**
     * 
     */
    private static final long serialVersionUID = 7404763198420787980L;

    @Id
    @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "SEQ_MESSAGES" )
    @SequenceGenerator( name = "SEQ_MESSAGES", sequenceName = "SEQ_MESSAGES" )
    @Column( name = "SID", nullable = false, unique = true )
    private Long sid;

    @Column( name = "MESSSAGE", columnDefinition = "TEXT", nullable = false )
    private String message;

    @Column( name = "FROM", nullable = false )
    private String from;

    @Column( name = "TIMESTAMP", nullable = false )
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
    
    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn( name = "OWNER", nullable = false )
    private MDOUser owner;
    
    /**
     * @constructor
     */
    public MDOMessage()
    {
        super();
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
     * @return the message
     */
    public String getMessage()
    {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage( String message )
    {
        this.message = message;
    }

    /**
     * @return the from
     */
    public String getFrom()
    {
        return from;
    }

    /**
     * @param from the from to set
     */
    public void setFrom( String from )
    {
        this.from = from;
    }

    /**
     * @return the timestamp
     */
    public Date getTimestamp()
    {
        return timestamp;
    }

    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp( Date timestamp )
    {
        this.timestamp = timestamp;
    }

}
