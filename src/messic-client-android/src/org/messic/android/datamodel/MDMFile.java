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

public class MDMFile
    implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = -1343793219016926339L;

    protected long sid;

    protected String code;

    /** the server filename */
    protected String fileName;

    protected String lfileName;

    protected long size;

    protected MDMAlbum album;

    /** flag to know if the entity has been recovered from database or from cloud json entity */
    private boolean flagFromLocalDatabase;

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
     * @return the lfileName
     */
    public String getLfileName()
    {
        return lfileName;
    }

    /**
     * @return the flagFromLocalDatabase
     */
    public boolean isFlagFromLocalDatabase()
    {
        return flagFromLocalDatabase;
    }

    /**
     * @param flagFromLocalDatabase the flagFromLocalDatabase to set
     */
    public void setFlagFromLocalDatabase( boolean flagFromLocalDatabase )
    {
        this.flagFromLocalDatabase = flagFromLocalDatabase;
    }

    /**
     * @param lfileName the lfileName to set
     */
    public void setLfileName( String lfileName )
    {
        this.lfileName = lfileName;
    }

    /**
     * @return the fileName
     */
    public String getFileName()
    {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName( String fileName )
    {
        this.fileName = fileName;
    }

}
