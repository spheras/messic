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

import org.messic.android.controllers.Configuration;
import org.messic.android.datamodel.dao.DAOAlbum;
import org.messic.android.datamodel.dao.DAOSong;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class MDMSong
    extends MDMFile
    implements Serializable
{

    public static final String COLUMN_LOCAL_SID = "lsid";

    public static final String COLUMN_SERVER_SID = "sid";

    public static final String COLUMN_TRACK = "track";

    public static final String COLUMN_NAME = "name";

    public static final String COLUMN_RATE = "rate";

    public static final String COLUMN_SERVER_FILENAME = "sfilename";

    public static final String COLUMN_LOCAL_FILENAME = "lfilename";

    public static final String COLUMN_FK_ALBUM = "fk_album";

    public static final String TABLE_NAME = "songs";

    public static final int COLUMN_LFILENAME_INDEX = 6;

    public static final String TABLE_CREATE = "create table " + TABLE_NAME + "(" + COLUMN_LOCAL_SID
        + " integer primary key autoincrement, " + COLUMN_SERVER_SID + " integer not null, " + COLUMN_TRACK
        + " integer not null," + COLUMN_NAME + " text not null," + COLUMN_RATE + " integer," + COLUMN_SERVER_FILENAME
        + " text not null," + COLUMN_LOCAL_FILENAME + " text," + COLUMN_FK_ALBUM + " integer not null" + ");";

    public static String[] getColumns()
    {
        return new String[] { COLUMN_LOCAL_SID, COLUMN_SERVER_SID, COLUMN_TRACK, COLUMN_NAME, COLUMN_RATE,
            COLUMN_SERVER_FILENAME, COLUMN_LOCAL_FILENAME, COLUMN_FK_ALBUM };
    }

    public MDMSong( Cursor cursor, Context context, boolean loadAlbum )
    {
        this.setFlagFromLocalDatabase( true );
        this.lsid = cursor.getInt( 0 );
        this.sid = cursor.getInt( 1 );
        this.track = cursor.getInt( 2 );
        this.name = cursor.getString( 3 );
        this.rate = cursor.getInt( 4 );
        this.fileName = cursor.getString( 5 );
        this.lfileName = cursor.getString( COLUMN_LFILENAME_INDEX );
        DAOAlbum daoalbum = new DAOAlbum( context );
        daoalbum.open();
        int sidAlbum = cursor.getInt( 7 );
        if ( loadAlbum )
        {
            Cursor cAlbum = daoalbum._get( sidAlbum );
            if ( cAlbum.moveToFirst() )
            {
                this.album = new MDMAlbum( cAlbum, context, false, true );
            }
            else
            {
                Log.w( "MDMSong", "song without album!??:" + this.name );
            }
        }
        daoalbum.close();
    }

    /**
     * 
     */
    private static final long serialVersionUID = -4949164622586035247L;

    private long sid;

    private int lsid;

    private int track;

    private String name;

    private int rate;

    /**
     * default constructor
     */
    public MDMSong()
    {
        super();
    }

    public final long getSid()
    {
        return sid;
    }

    public final void setSid( long sid )
    {
        this.sid = sid;
    }

    public final int getTrack()
    {
        return track;
    }

    public final void setTrack( int track )
    {
        this.track = track;
    }

    public final String getName()
    {
        return name;
    }

    public final void setName( String name )
    {
        this.name = name;
    }

    /**
     * @return the rate
     */
    public int getRate()
    {
        return rate;
    }

    /**
     * @param rate the rate to set
     */
    public void setRate( int rate )
    {
        this.rate = rate;
    }

    /**
     * Obtain the path to store this song at the external SD
     * 
     * @return
     */
    public String calculateExternalStorageFolder()
    {
        return getAlbum().calculateExternalStorageFolder();
    }

    /**
     * Obtain the filename to be stored at the external SD
     * 
     * @return
     */
    public String calculateExternalFilename()
    {
        return "/s" + getSid() + ".mp3";
    }

    /**
     * Obtain the URL to download this song
     * 
     * @return
     */
    public String getURL()
    {
        return Configuration.getBaseUrl() + "/services/songs/" + this.sid + "/audio?messic_token="
            + Configuration.getLastToken();
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

    /**
     * Check if the song is downloaded or not
     * 
     * @return
     */
    public boolean isDownloaded( Context context )
    {
        if ( getLfileName() == null && !isFlagFromLocalDatabase() )
        {
            // we should contrast against the database if the file is or not
            DAOSong daosong = new DAOSong( context );
            daosong.open();
            Cursor c = daosong._getByServerSid( getSid() );
            if ( c.moveToFirst() )
            {
                this.lfileName = c.getString( COLUMN_LFILENAME_INDEX );
                daosong.close();
                return ( getLfileName() != null && getLfileName().length() > 0 );
            }
            else
            {
                return false;
            }
        }
        else
        {
            return ( getLfileName() != null && getLfileName().length() > 0 );
        }
    }
}
