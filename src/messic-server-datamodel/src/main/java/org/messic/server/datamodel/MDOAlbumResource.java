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

import java.io.File;
import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Table( name = "ALBUM_RESOURCES" )
@Inheritance( strategy = InheritanceType.SINGLE_TABLE )
@DiscriminatorValue( MDOPhysicalResource.ALBUM_RESOURCE )
public class MDOAlbumResource
    extends MDOPhysicalResource
    implements MDO, Serializable
{

    public static final String SONG = "SONG";

    public static final String ARTWORK = "ARTWORK";

    public static final String OTHER = "OTHER";

    /**
	 * 
	 */
    private static final long serialVersionUID = 5659995299036490152L;

    public MDOAlbumResource()
    {
        super();
    }

    public MDOAlbumResource( MDOUser user, String location )
    {
        super( user, location );
    }

    /**
     * This strange method is because of problems with inheritance in hibernate and mappedby Yeah, its strange, but ...
     * http://chriswongdevblog.blogspot.com.es/2009/10/polymorphic-one-to-many-relationships.html that's the reason why
     * it's repeated on each {@link MDOAlbumResource} subclass
     * 
     * @return
     */
    public MDOAlbum getAlbum()
    {
        return null;
    }

    /**
     * Return the absolute (relative from author folder) location to this resource
     * 
     * @param settings {@link MDOMessicSettings} settings from messic
     * @return String path
     */
    public String calculateAbsolutePath( MDOMessicSettings settings )
    {
        String albumPath = getAlbum().calculateAbsolutePath( settings );
        return albumPath + File.separatorChar + getLocation();
    }

}