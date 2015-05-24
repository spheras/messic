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
package org.messic.server.api.datamodel;

import javax.xml.bind.annotation.XmlRootElement;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

@XmlRootElement
@ApiObject( name = "AlbumConsistency", description = "Consistency Status of an Album" )
public class AlbumConsistency
{
    @ApiObjectField( description = "SID of the album" )
    private Long sid = 0l;

    @ApiObjectField( description = "name of the Album" )
    private String name = "";

    @ApiObjectField( description = "Status of the Album. 0 is OK, 1 is OK but have been repared, 2 is bad->not repared" )
    private Integer status = 0;

    @ApiObjectField( description = "Message of the status" )
    private String message = "";

    /**
     * Default constructor
     */
    public AlbumConsistency()
    {

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
     * @return the status
     */
    public Integer getStatus()
    {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus( Integer status )
    {
        this.status = status;
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


}
