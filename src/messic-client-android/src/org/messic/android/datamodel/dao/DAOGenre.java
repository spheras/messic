package org.messic.android.datamodel.dao;

import org.messic.android.datamodel.MDMGenre;
import org.messic.android.datamodel.MDMSong;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class DAOGenre
    extends DAO
{

    public DAOGenre( Context context )
    {
        super( context, MDMGenre.TABLE_NAME, MDMGenre.getColumns() );
    }

    public MDMGenre save( MDMGenre genre )
    {
        open();
        ContentValues cv = new ContentValues();
        cv.put( MDMGenre.COLUMN_NAME, genre.getName() );
        cv.put( MDMGenre.COLUMN_SERVER_SID, genre.getSid() );

        Cursor c = null;
        if ( genre.getLsid() > 0 )
        {
            cv.put( MDMGenre.COLUMN_LOCAL_SID, genre.getLsid() );
            c = super._update( cv, genre.getLsid() );
        }
        else
        {
            c = super._save( cv );
        }

        c.moveToFirst();
        MDMGenre msi = new MDMGenre( c );
        c.close();
        close();
        return msi;
    }
}
