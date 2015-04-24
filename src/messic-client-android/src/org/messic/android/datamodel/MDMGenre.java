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

import android.database.Cursor;

public class MDMGenre
    implements Serializable
{
    public static final String COLUMN_LOCAL_SID = "lsid";

    public static final String COLUMN_SERVER_SID = "sid";

    public static final String COLUMN_NAME = "name";

    public static final String TABLE_NAME = "genres";

    public static final String TABLE_CREATE = "create table " + TABLE_NAME + "(" + COLUMN_LOCAL_SID
        + " integer primary key autoincrement, " + COLUMN_SERVER_SID + " integer not null, " + COLUMN_NAME
        + " text not null" + ");";

    public static String[] getColumns()
    {
        return new String[] { COLUMN_LOCAL_SID, COLUMN_SERVER_SID, COLUMN_NAME };
    }

    /**
     * 
     */
    private static final long serialVersionUID = 2190757306533134244L;

    private int lsid;

    private int sid;

    private String name;

    public MDMGenre( Cursor cursor )
    {
        this.lsid = cursor.getInt( 0 );
        this.sid = cursor.getInt( 1 );
        this.name = cursor.getString( 2 );
    }

    public MDMGenre()
    {

    }

    public MDMGenre( String name )
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public Integer getSid()
    {
        return sid;
    }

    public void setSid( Integer sid )
    {
        this.sid = sid;
    }

    /**
     * @return the lsid
     */
    public int getLsid()
    {
        return lsid;
    }

    /**
     * @param lsid the lsid to set
     */
    public void setLsid( int lsid )
    {
        this.lsid = lsid;
    }
}
