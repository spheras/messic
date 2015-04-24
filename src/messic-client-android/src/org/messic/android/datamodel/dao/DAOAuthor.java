package org.messic.android.datamodel.dao;

import org.messic.android.datamodel.MDMAuthor;
import org.messic.android.datamodel.MDMSong;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class DAOAuthor
    extends DAO
{

    public DAOAuthor( Context context )
    {
        super( context, MDMAuthor.TABLE_NAME, MDMSong.getColumns() );
    }

    public MDMAuthor save( MDMAuthor author )
    {
        open();
        ContentValues cv = new ContentValues();
        cv.put( MDMAuthor.COLUMN_NAME, author.getName() );
        cv.put( MDMAuthor.COLUMN_SERVER_SID, author.getSid() );

        Cursor c = null;
        if ( author.getLsid() > 0 )
        {
            cv.put( MDMAuthor.COLUMN_LOCAL_SID, author.getLsid() );
            c = super._update( cv, author.getLsid() );
        }
        else
        {
            c = super._save( cv );
        }

        c.moveToFirst();
        MDMAuthor msi = new MDMAuthor( c );
        c.close();
        close();
        return msi;
    }
}
