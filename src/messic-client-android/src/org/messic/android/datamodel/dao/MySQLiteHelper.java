package org.messic.android.datamodel.dao;

import org.messic.android.datamodel.MDMAlbum;
import org.messic.android.datamodel.MDMAuthor;
import org.messic.android.datamodel.MDMGenre;
import org.messic.android.datamodel.MDMMessicServerInstance;
import org.messic.android.datamodel.MDMSong;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper
    extends SQLiteOpenHelper
{

    private static final String DATABASE_NAME = "messic.db";

    private static final int DATABASE_VERSION = 12;

    public MySQLiteHelper( Context context )
    {
        super( context, DATABASE_NAME, null, DATABASE_VERSION );
    }

    @Override
    public void onCreate( SQLiteDatabase database )
    {
        database.execSQL( MDMMessicServerInstance.TABLE_CREATE );
        database.execSQL( MDMGenre.TABLE_CREATE );
        database.execSQL( MDMAuthor.TABLE_CREATE );
        database.execSQL( MDMAlbum.TABLE_CREATE );
        database.execSQL( MDMSong.TABLE_CREATE );
    }

    @Override
    public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion )
    {
        Log.w( MySQLiteHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion
            + ", which will destroy all old data" );
        db.execSQL( "DROP TABLE IF EXISTS " + MDMMessicServerInstance.TABLE_NAME );
        db.execSQL( "DROP TABLE IF EXISTS " + MDMGenre.TABLE_NAME );
        db.execSQL( "DROP TABLE IF EXISTS " + MDMAuthor.TABLE_NAME );
        db.execSQL( "DROP TABLE IF EXISTS " + MDMAlbum.TABLE_NAME );
        db.execSQL( "DROP TABLE IF EXISTS " + MDMSong.TABLE_NAME );
        onCreate( db );
    }

}