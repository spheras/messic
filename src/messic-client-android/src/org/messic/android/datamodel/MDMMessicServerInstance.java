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

import org.messic.android.datamodel.dao.DAO;

import android.database.Cursor;

public class MDMMessicServerInstance
{
    public static final String COLUMN_SID = "lsid";

    public static final String COLUMN_IP = "ip";

    public static final String COLUMN_NAME = "name";

    public static final String COLUMN_SECURED = "secured";

    public static final String COLUMN_PORT = "port";

    public static final String COLUMN_DESCRIPTION = "description";

    public static final String COLUMN_VERSION = "version";

    public static final String COLUMN_LAST_USER = "lastuser";

    public static final String COLUMN_LAST_PASSWORD = "lastpassword";

    public static final String TABLE_NAME = "messicServerInstances";

    public static final String TABLE_CREATE = "create table " + TABLE_NAME + "(" + COLUMN_SID
        + " integer primary key autoincrement, " + COLUMN_IP + " text not null, " + COLUMN_NAME + " text not null, "
        + COLUMN_SECURED + " integer not null, " + COLUMN_PORT + " integer not null, " + COLUMN_DESCRIPTION + " text, "
        + COLUMN_VERSION + " text not null, " + COLUMN_LAST_USER + " text, " + COLUMN_LAST_PASSWORD + " text" + ");";

    public int lsid;

    public String ip;

    public String name;

    public boolean secured;

    public int port;

    public String description;

    public String version;

    public String lastUser;

    public String lastPassword;

    public static String[] getColumns()
    {
        return new String[] { COLUMN_SID, COLUMN_IP, COLUMN_NAME, COLUMN_SECURED, COLUMN_PORT, COLUMN_DESCRIPTION,
            COLUMN_VERSION, COLUMN_LAST_USER, COLUMN_LAST_PASSWORD };
    }

    public MDMMessicServerInstance()
    {
    }

    public MDMMessicServerInstance( Cursor cursor )
    {
        this.lsid = cursor.getInt( 0 );
        this.ip = cursor.getString( 1 );
        this.name = cursor.getString( 2 );
        this.secured = ( cursor.getInt( 3 ) == DAO.BOOLEAN_TRUE ? true : false );
        this.port = cursor.getInt( 4 );
        this.description = cursor.getString( 5 );
        this.version = cursor.getString( 6 );
        this.lastUser = cursor.getString( 7 );
        this.lastPassword = cursor.getString( 8 );
    }
}
