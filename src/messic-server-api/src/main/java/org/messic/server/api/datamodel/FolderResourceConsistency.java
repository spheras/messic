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
@ApiObject( name = "FolderResourceConsistency", description = "Consistency Status of a folder at the messic music folder" )
public class FolderResourceConsistency
{
    @ApiObjectField( description = "relative path of the problematic resource" )
    private String relativeLocation = "";

    @ApiObjectField( description = "absolute path of the problematic resource" )
    private String absoluteLocation = "";

    @ApiObjectField( description = "is an author folder" )
    private boolean authorFolder = false;

    @ApiObjectField( description = "is an album folder" )
    private boolean albumFolder = false;

    @ApiObjectField( description = "is an album resource" )
    private boolean albumResource = false;

    @ApiObjectField( description = "Message of the status" )
    private String message = "";

    @ApiObjectField( description = "Status of the folder. 0->everything is OK, 1->something bad" )
    private int status = 0;

    /**
     * Default constructor
     */
    public FolderResourceConsistency()
    {

    }

    /**
     * @return the relativeLocation
     */
    public String getRelativeLocation()
    {
        return relativeLocation;
    }

    /**
     * @param relativeLocation the relativeLocation to set
     */
    public void setRelativeLocation( String relativeLocation )
    {
        this.relativeLocation = relativeLocation;
    }

    /**
     * @return the absoluteLocation
     */
    public String getAbsoluteLocation()
    {
        return absoluteLocation;
    }

    /**
     * @param absoluteLocation the absoluteLocation to set
     */
    public void setAbsoluteLocation( String absoluteLocation )
    {
        this.absoluteLocation = absoluteLocation;
    }

    /**
     * @return the authorFolder
     */
    public boolean isAuthorFolder()
    {
        return authorFolder;
    }

    /**
     * @param authorFolder the authorFolder to set
     */
    public void setAuthorFolder( boolean authorFolder )
    {
        this.authorFolder = authorFolder;
    }

    /**
     * @return the albumFolder
     */
    public boolean isAlbumFolder()
    {
        return albumFolder;
    }

    /**
     * @param albumFolder the albumFolder to set
     */
    public void setAlbumFolder( boolean albumFolder )
    {
        this.albumFolder = albumFolder;
    }

    /**
     * @return the albumResource
     */
    public boolean isAlbumResource()
    {
        return albumResource;
    }

    /**
     * @param albumResource the albumResource to set
     */
    public void setAlbumResource( boolean albumResource )
    {
        this.albumResource = albumResource;
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
     * @return the status
     */
    public int getStatus()
    {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus( int status )
    {
        this.status = status;
    }

}
