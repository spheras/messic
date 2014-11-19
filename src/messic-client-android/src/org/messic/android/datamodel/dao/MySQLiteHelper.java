package org.messic.android.datamodel.dao;

import org.messic.android.datamodel.MDMMessicServerInstance;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper
    extends SQLiteOpenHelper
{

    private static final String DATABASE_NAME = "messic.db";

    private static final int DATABASE_VERSION = 2;

    public MySQLiteHelper( Context context )
    {
        super( context, DATABASE_NAME, null, DATABASE_VERSION );
    }

    @Override
    public void onCreate( SQLiteDatabase database )
    {
        database.execSQL( MDMMessicServerInstance.TABLE_CREATE );
    }

    @Override
    public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion )
    {
        Log.w( MySQLiteHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion
            + ", which will destroy all old data" );
        db.execSQL( "DROP TABLE IF EXISTS " + MDMMessicServerInstance.TABLE_NAME );
        onCreate( db );
    }

}