package org.messic.android.datamodel.dao;

import org.messic.android.datamodel.MDMAlbum;
import org.messic.android.datamodel.MDMAuthor;
import org.messic.android.datamodel.MDMGenre;
import org.messic.android.datamodel.MDMSong;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class DAOAlbum
    extends DAO
{

    public DAOAlbum( Context context )
    {
        super( context, MDMAlbum.TABLE_NAME, MDMSong.getColumns() );
    }

    public MDMAlbum save( MDMAlbum album )
    {
        open();
        ContentValues cv = new ContentValues();
        cv.put( MDMAlbum.COLUMN_COMMENTS, album.getComments() );
        cv.put( MDMAlbum.COLUMN_FILENAME, album.getFileName() );
        cv.put( MDMAlbum.COLUMN_NAME, album.getName() );
        cv.put( MDMAlbum.COLUMN_YEAR, album.getYear() );

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

        c.moveToFirst();
        MDMAlbum msi = new MDMAlbum( c, this.getContext() );
        c.close();
        close();
        return msi;
    }
}
