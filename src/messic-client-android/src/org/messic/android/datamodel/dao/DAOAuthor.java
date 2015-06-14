package org.messic.android.datamodel.dao;

import org.messic.android.datamodel.MDMAlbum;
import org.messic.android.datamodel.MDMAuthor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class DAOAuthor
    extends DAO
{

    public DAOAuthor( Context context )
    {
        super( context, MDMAuthor.TABLE_NAME, MDMAuthor.getColumns() );
    }

    public void create()
    {
        getDatabase().execSQL( MDMAuthor.TABLE_CREATE );
    }

    public MDMAuthor save( Context context, MDMAuthor author, boolean saveAlbums )
    {
        open();
        ContentValues cv = new ContentValues();
        cv.put( MDMAuthor.COLUMN_NAME, author.getName() );
        cv.put( MDMAuthor.COLUMN_SERVER_SID, author.getSid() );
        cv.put( MDMAuthor.COLUMN_SERVER_FILENAME, author.getFileName() );
        cv.put( MDMAuthor.COLUMN_LOCAL_FILENAME, author.getLfileName() );

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

        if ( saveAlbums )
        {
            if ( author.getAlbums() != null && author.getAlbums().size() > 0 )
            {
                DAOAlbum daoalbum = new DAOAlbum( context );
                daoalbum.open();
                for ( int i = 0; i < author.getAlbums().size(); i++ )
                {
                    MDMAlbum album = author.getAlbums().get( i );
                    album.setAuthor( msi );
                    daoalbum.save( album, true );
                }
                daoalbum.close();
            }
        }

        return msi;
    }
}
