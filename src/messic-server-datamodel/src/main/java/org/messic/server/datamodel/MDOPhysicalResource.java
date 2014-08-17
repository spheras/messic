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

import java.io.IOException;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import org.apache.commons.io.FilenameUtils;
import org.messic.server.Util;

@Entity
@Table( name = "PHYSICAL_RESOURCES" )
@Inheritance( strategy = InheritanceType.JOINED )
@DiscriminatorColumn( name = "DTYPE", discriminatorType = DiscriminatorType.STRING, length = 31 )
@DiscriminatorValue( MDOResource.PHYSICAL_RESOURCE )
public class MDOPhysicalResource
    extends MDOResource
    implements MDO, Serializable
{

    private static final long serialVersionUID = 8792913920714652055L;

    public static final String AUTHOR = "AUTHOR";

    public static final String ALBUM = "ALBUM";

    public static final String ALBUM_RESOURCE = "ALBUM_RESOURCE";

    @Column( name = "LOCATION" )
    private String location;

    public MDOPhysicalResource()
    {
        super();
    }

    public MDOPhysicalResource( MDOUser user, String location )
    {
        super( user );
        this.location = location;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation( String location )
        throws IOException
    {
        if ( Util.haveFilenameIllegalCharacters( location ) )
        {
            throw new IOException( "Illegal characters at location: " + location );
        }
        this.location = location;
    }

    /**
     * Return the extension of the physical resource file
     * 
     * @return
     */
    public String getExtension()
    {
        String extension = FilenameUtils.getExtension( getLocation() );
        if ( extension == null )
        {
            extension = Util.DEFAULT_EXTENSION;
        }
        return extension;
    }
}