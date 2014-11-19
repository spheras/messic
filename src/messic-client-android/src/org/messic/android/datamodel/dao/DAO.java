package org.messic.android.datamodel.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public abstract class DAO
{
    public static final int BOOLEAN_TRUE = 0;

    public static final int BOOLEAN_FALSE = -1;

    private MySQLiteHelper dbHelper;

    private SQLiteDatabase database;

    private String tableName;

    private String[] columns;

    public DAO( Context context, String tableName, String[] columns )
    {
        this.columns = columns;
        this.tableName = tableName;
        dbHelper = new MySQLiteHelper( context );
    }

    public static int getBoolean( boolean b )
    {
        if ( b )
        {
            return BOOLEAN_TRUE;
        }
        else
        {
            return BOOLEAN_FALSE;
        }
    }

    public static boolean getBoolean( int i )
    {
        if ( i == BOOLEAN_TRUE )
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void open()
        throws SQLException
    {
        database = dbHelper.getWritableDatabase();
    }

    public void close()
    {
        dbHelper.close();
    }

    /**
     * @return the database
     */
    public SQLiteDatabase getDatabase()
    {
        return database;
    }

    /**
     * @param database the database to set
     */
    public void setDatabase( SQLiteDatabase database )
    {
        this.database = database;
    }

    protected Cursor _getAll()
    {
        Cursor cursor = database.query( this.tableName, this.columns, null, null, null, null, null );
        cursor.moveToFirst();
        return cursor;
    }

    protected Cursor _get( int sid )
    {
        Cursor cursor =
            database.query( this.tableName, this.columns, "sid=?", new String[] { "" + sid }, null, null, null );
        cursor.moveToFirst();
        return cursor;
    }

    protected Cursor _save( ContentValues values )
    {
        int insertId = (int) database.insert( this.tableName, null, values );
        return _get( insertId );
    }

    protected Cursor _update( ContentValues values, int sid )
    {
        int insertId = (int) database.update( this.tableName, values, "sid=?", new String[] { "" + sid } );
        return _get( insertId );
    }

}
