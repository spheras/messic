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

import org.apache.commons.io.FilenameUtils;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;
import org.messic.server.Util;
import org.messic.server.datamodel.MDOAlbum;
import org.messic.server.datamodel.MDOPhysicalResource;
import org.messic.server.datamodel.MDOSong;

@XmlRootElement
@ApiObject( name = "File", description = "Resource of an album" )
public class File
{
    @ApiObjectField( description = "identificator of the resource" )
    private long sid;

    @ApiObjectField( description = "temporal code for the resource" )
    private String code;

    @ApiObjectField( description = "fileName for the resource at the server" )
    private String fileName;

    @ApiObjectField( description = "Size of the file" )
    private long size;

    @ApiObjectField( description = "volume of the album in which this resource is linked" )
    private int volume;

    @ApiObjectField( description = "Owner album of the resource" )
    private Album album;

    /**
     * default constructor
     */
    public File()
    {

    }

    /**
     * copy constructor
     * 
     * @param mdosong {@link MDOSong}
     * @param volume int volume where is stored the resource
     * @param album {@link Album} this is not an {@link MDOAlbum} to avoid cross references
     */
    public File( MDOPhysicalResource mdopr, int volume, Album album )
    {
        setFileName( mdopr.getLocation() );
        setVolume( volume );
        setAlbum( album );
        setSid( mdopr.getSid() );
    }

    public final Album getAlbum()
    {
        return album;
    }

    public final void setAlbum( Album album )
    {
        this.album = album;
    }

    public String getFileName()
    {
        return fileName;
    }

    /**
     * Return the fileName but without any special characters
     * 
     * @return String
     */
    public String calculateSecureFileName( char replacementChar )
    {
        String name = FilenameUtils.removeExtension( this.fileName );
        String secureName = Util.replaceIllegalFilenameCharactersNew( name, replacementChar );
        String secureExtension = calculateSecureExtension( replacementChar );
        return secureName + "." + secureExtension;
    }

    /**
     * Return the fileName but without any special characters
     * 
     * @return String
     */
    public String calculateSecureExtension( char replacementChar )
    {
        String extension = FilenameUtils.getExtension( this.fileName );
        if ( extension == null || extension.length() <= 0 )
        {
            extension = Util.DEFAULT_EXTENSION;
        }

        String secureExtension = Util.replaceIllegalFilenameCharactersNew( extension, replacementChar );
        return secureExtension;
    }

    public void setFileName( String fileName )
    {
        this.fileName = fileName;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode( String code )
    {
        this.code = code;
    }

    public long getSid()
    {
        return sid;
    }

    public void setSid( long sid )
    {
        this.sid = sid;
    }

    public long getSize()
    {
        return size;
    }

    public void setSize( long size )
    {
        this.size = size;
    }

    /**
     * @return the volume
     */
    public int getVolume()
    {
        return volume;
    }

    /**
     * @param volume the volume to set
     */
    public void setVolume( int volume )
    {
        this.volume = volume;
    }
}
