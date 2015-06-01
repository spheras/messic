package org.messic.android.datamodel.dao;

import java.util.ArrayList;
import java.util.List;

import org.messic.android.datamodel.MDMAlbum;
import org.messic.android.datamodel.MDMAuthor;
import org.messic.android.datamodel.MDMGenre;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class DAOAlbum
    extends DAO
{

    public DAOAlbum( Context context )
    {
        super( context, MDMAlbum.TABLE_NAME, MDMAlbum.getColumns() );
    }

    public List<MDMAlbum> getAll()
    {
        open();
        List<MDMAlbum> result = new ArrayList<MDMAlbum>();
        Cursor cursor = super._getAll();
        while ( !cursor.isAfterLast() )
        {
            MDMAlbum msi = new MDMAlbum( cursor, getContext(), true );
            result.add( msi );
            cursor.moveToNext();
        }

        cursor.close();
        close();
        return result;
    }

    public MDMAlbum save( MDMAlbum album, boolean saveSongs )
    {
        open();
        ContentValues cv = new ContentValues();
        cv.put( MDMAlbum.COLUMN_COMMENTS, album.getComments() );
        cv.put( MDMAlbum.COLUMN_FILENAME, album.getFileName() );
        cv.put( MDMAlbum.COLUMN_NAME, album.getName() );
        cv.put( MDMAlbum.COLUMN_YEAR, album.getYear() );
        cv.put( MDMAlbum.COLUMN_SERVER_SID, album.getSid() );

        if ( album.getGenre() == null )
        {
            // there is no genre to link! :(
            cv.put( MDMAlbum.COLUMN_FK_GENRE, 0 );
        }
        else
        {
            if ( album.getGenre().getLsid() > 0 )
            {
                // it has a local sid, so it is an existen album from the database, linking!
                cv.put( MDMAlbum.COLUMN_FK_GENRE, album.getGenre().getLsid() );
            }
            else
            {
                // It is not an entity created from database, maybe it exist at database or maybe not, let's see
                DAOGenre daogenre = new DAOGenre( getContext() );
                daogenre.open();
                Cursor cGenre = daogenre._getByServerSid( album.getGenre().getSid() );
                if ( cGenre.getCount() > 0 )
                {
                    // it exist at the database!, so, just linking to it
                    MDMGenre genre = new MDMGenre( cGenre );
                    cv.put( MDMAlbum.COLUMN_FK_GENRE, genre.getLsid() );
                }
                else
                {
                    // it doesn't exists at the database, we need to create it previously
                    MDMGenre genre = daogenre.save( album.getGenre() );
                    cv.put( MDMAlbum.COLUMN_FK_GENRE, genre.getLsid() );
                }
                daogenre.close();

            }
        }
        if ( album.getAuthor() == null )
        {
            // there is no author to link! :(
            cv.put( MDMAlbum.COLUMN_FK_AUTHOR, 0 );
        }
        else
        {
            if ( album.getGenre().getLsid() > 0 )
            {
                // it has a local sid, so it is an existen album from the database, linking!
                cv.put( MDMAlbum.COLUMN_FK_AUTHOR, album.getAuthor().getLsid() );
            }
            else
            {
                // It is not an entity created from database, maybe it exist at database or maybe not, let's see
                DAOAuthor daoauthor = new DAOAuthor( getContext() );
                daoauthor.open();
                Cursor cAuthor = daoauthor._getByServerSid( album.getAuthor().getSid() );
                if ( cAuthor.getCount() > 0 )
                {
                    // it exist at the database!, so, just linking to it
                    MDMAuthor author = new MDMAuthor( cAuthor );
                    cv.put( MDMAlbum.COLUMN_FK_AUTHOR, author.getLsid() );
                }
                else
                {
                    // it doesn't exists at the database, we need to create it previously
                    MDMAuthor author = daoauthor.save( album.getAuthor() );
                    cv.put( MDMAlbum.COLUMN_FK_AUTHOR, author.getLsid() );
                }
                daoauthor.close();

            }
        }

        Cursor c = null;
        if ( album.getLsid() > 0 )
        {
            cv.put( MDMAuthor.COLUMN_LOCAL_SID, album.getLsid() );
            c = super._update( cv, album.getLsid() );
        }
        else
        {
            c = super._save( cv );
        }

        // we save the albums
        if ( album.getSongs() != null && saveSongs )
        {
            DAOSong ds = new DAOSong( getContext() );
            for ( int i = 0; i < album.getSongs().size(); i++ )
            {
                ds.save( album.getSongs().get( i ) );
            }
        }

        c.moveToFirst();
        MDMAlbum msi = new MDMAlbum( c, this.getContext(), true );
        c.close();
        close();
        return msi;
    }
}
