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

    private Context context;

    public DAO( Context context, String tableName, String[] columns )
    {
        this.columns = columns;
        this.tableName = tableName;
        dbHelper = new MySQLiteHelper( context );
        this.context = context;
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

    public abstract void create();

    public void _recreate()
    {
        open();
        _drop();
        create();
        close();
    }

    public void _drop()
    {
        database.execSQL( "DROP TABLE IF EXISTS " + getTableName() );
    }

    public Cursor _getAll()
    {
        return _getAll( null );
    }

    public Cursor _getAll( String orderBy )
    {
        return _getAll( null, orderBy );
    }

    public Cursor _getAll( String where, String orderBy )
    {
        Cursor cursor = database.query( this.tableName, this.columns, where, null, null, null, orderBy, null );
        cursor.moveToFirst();
        return cursor;
    }

    public Cursor _get( int sid )
    {
        Cursor cursor =
            database.query( this.tableName, this.columns, "lsid=?", new String[] { "" + sid }, null, null, null );
        cursor.moveToFirst();
        return cursor;
    }

    public int _count()
    {
        Cursor mCount = database.rawQuery( "SELECT count(*) FROM " + this.tableName, null );
        mCount.moveToFirst();
        int count = mCount.getInt( 0 );
        mCount.close();
        return count;
    }

    public Cursor _getByServerSid( long serverSid )
    {
        Cursor cursor =
            database.query( this.tableName, this.columns, "sid=?", new String[] { "" + serverSid }, null, null, null );
        cursor.moveToFirst();
        return cursor;
    }

    public boolean _delete( long lsid )
    {
        return database.delete( this.tableName, "lsid=?", new String[] { "" + lsid } ) > 0;
    }

    public Cursor _save( ContentValues values )
    {
        long lsid = database.insert( this.tableName, null, values );
        return _get( (int) lsid );
    }

    public Cursor _update( ContentValues values, int lsid )
    {
        database.update( this.tableName, values, "lsid=?", new String[] { "" + lsid } );
        return _get( lsid );
    }

    /**
     * @return the context
     */
    public Context getContext()
    {
        return context;
    }

    /**
     * @param context the context to set
     */
    public void setContext( Context context )
    {
        this.context = context;
    }

    /**
     * @return the tableName
     */
    public String getTableName()
    {
        return tableName;
    }

    /**
     * @param tableName the tableName to set
     */
    public void setTableName( String tableName )
    {
        this.tableName = tableName;
    }

}
