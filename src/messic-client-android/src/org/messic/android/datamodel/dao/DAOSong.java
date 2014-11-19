package org.messic.android.datamodel.dao;

import org.messic.android.datamodel.MDMSong;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class DAOSong
    extends DAO
{

    public DAOSong( Context context )
    {
        super( context, MDMSong.TABLE_NAME, MDMSong.getColumns() );
    }

    public MDMSong save( MDMSong song )
    {
        open();
        ContentValues cv = new ContentValues();
        cv.put( MDMSong.COLUMN_TRACK, song.getTrack() );
        cv.put( MDMSong.COLUMN_FILENAME, song.getFileName() );
        cv.put( MDMSong.COLUMN_NAME, song.getName() );
        cv.put( MDMSong.COLUMN_RATE, song.getRate() );
        cv.put( MDMSong.COLUMN_SERVER_SID, song.getLsid() );

        Cursor c = null;
        if ( song.getLsid() > 0 )
        {
            cv.put( MDMSong.COLUMN_LOCAL_SID, song.getLsid() );
            c = super._update( cv, song.getLsid() );
        }
        else
        {
            c = super._save( cv );
        }

        c.moveToFirst();
        MDMSong msi = new MDMSong( c );
        c.close();
        close();
        return msi;
    }
}
