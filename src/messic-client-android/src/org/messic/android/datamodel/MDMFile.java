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


public class MDMFile implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = -1343793219016926339L;

    private long sid;

    private String code;

    private String fileName;

    private long size;

    private MDMAlbum album;

    /**
     * default constructor
     */
    public MDMFile()
    {

    }

    public final MDMAlbum getAlbum()
    {
        return album;
    }

    public final void setAlbum( MDMAlbum album )
    {
        this.album = album;
    }

    public String getFileName()
    {
        return fileName;
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
}
