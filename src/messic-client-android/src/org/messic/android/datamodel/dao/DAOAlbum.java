package org.messic.android.datamodel.dao;

import java.util.ArrayList;
import java.util.List;

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

    public interface AlbumPublisher
    {
        void publish( MDMAlbum album );
    }

    public DAOAlbum( Context context )
    {
        super( context, MDMAlbum.TABLE_NAME, MDMAlbum.getColumns() );
    }

    public void create()
    {
        getDatabase().execSQL( MDMAlbum.TABLE_CREATE );
    }

    public List<MDMAlbum> getAllByAlbum()
    {
        open();
        List<MDMAlbum> result = new ArrayList<MDMAlbum>();
        Cursor cursor = super._getAll( MDMAlbum.COLUMN_NAME );
        while ( !cursor.isAfterLast() )
        {
            MDMAlbum msi = new MDMAlbum( cursor, getContext(), true, false );
            result.add( msi );
            cursor.moveToNext();
        }

        cursor.close();
        close();
        return result;
    }

    public void removeAlbum( int lsid )
    {
        open();

        Cursor c = _get( lsid );
        if ( c.moveToFirst() )
        {
            MDMAlbum album = new MDMAlbum( c, getContext(), true, false );
            _delete( album.getLsid() );
            List<MDMSong> songs = album.getSongs();
            DAOSong daosong = new DAOSong( getContext() );
            daosong.open();
            for ( MDMSong mdmSong : songs )
            {
                daosong._delete( mdmSong.getLsid() );
            }
            daosong.close();
        }
        c.close();
        close();
    }

    public List<MDMAlbum> getAllByAuthor( AlbumPublisher publisher )
    {
        List<MDMAlbum> result = new ArrayList<MDMAlbum>();

        open();

        String query =
            "SELECT a.* FROM " + MDMAlbum.TABLE_NAME + " as a," + MDMAuthor.TABLE_NAME + " as au WHERE a."
                + MDMAlbum.COLUMN_FK_AUTHOR + "=au." + MDMAuthor.COLUMN_LOCAL_SID + " ORDER BY au."
                + MDMAuthor.COLUMN_NAME;
        Cursor c = getDatabase().rawQuery( query, null );

        if ( c != null && c.moveToFirst() )
        {
            do
            {
                MDMAlbum album = new MDMAlbum( c, getContext(), true, false );
                result.add( album );
                if ( publisher != null )
                {
                    publisher.publish( album );
                }
            }
            while ( c.moveToNext() );
        }

        c.close();
        close();
        return result;
    }

    public List<MDMAlbum> getAllByAuthorLSid( int authorLSid, boolean loadSongs )
    {
        open();
        List<MDMAlbum> result = new ArrayList<MDMAlbum>();

        Cursor c = _getAll( MDMAlbum.COLUMN_FK_AUTHOR + "=" + authorLSid, MDMAlbum.COLUMN_NAME );
        if ( c.moveToFirst() )
        {
            MDMAlbum album = new MDMAlbum( c, getContext(), loadSongs, false );
            result.add( album );
            c.moveToNext();
        }
        c.close();
        close();
        return result;
    }

    public MDMAlbum save( MDMAlbum album, boolean saveSongs )
    {
        open();
        ContentValues albumCV = new ContentValues();
        albumCV.put( MDMAlbum.COLUMN_COMMENTS, album.getComments() );
        albumCV.put( MDMAlbum.COLUMN_SERVER_FILENAME, album.getFileName() );
        albumCV.put( MDMAlbum.COLUMN_LOCAL_FILENAME, album.getLfileName() );
        albumCV.put( MDMAlbum.COLUMN_NAME, album.getName() );
        albumCV.put( MDMAlbum.COLUMN_YEAR, album.getYear() );
        albumCV.put( MDMAlbum.COLUMN_SERVER_SID, album.getSid() );

        // lets save the genre
        if ( album.getGenre() == null )
        {
            // there is no genre to link! :(
            albumCV.put( MDMAlbum.COLUMN_FK_GENRE, 0 );
        }
        else
        {
            if ( album.getGenre().getLsid() > 0 )
            {
                // it has a local sid, so it is an existen album from the database, linking!
                albumCV.put( MDMAlbum.COLUMN_FK_GENRE, album.getGenre().getLsid() );
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
                    albumCV.put( MDMAlbum.COLUMN_FK_GENRE, genre.getLsid() );
                }
                else
                {
                    // it doesn't exists at the database, we need to create it previously
                    MDMGenre genre = daogenre.save( album.getGenre() );
                    albumCV.put( MDMAlbum.COLUMN_FK_GENRE, genre.getLsid() );
                }
                daogenre.close();

            }
        }

        // lets save the author
        if ( album.getAuthor() == null )
        {
            // there is no author to link! :(
            albumCV.put( MDMAlbum.COLUMN_FK_AUTHOR, 0 );
        }
        else
        {
            if ( album.getAuthor().getLsid() > 0 )
            {
                // it has a local sid, so it is an existen album from the database, linking!
                albumCV.put( MDMAlbum.COLUMN_FK_AUTHOR, album.getAuthor().getLsid() );
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
                    albumCV.put( MDMAlbum.COLUMN_FK_AUTHOR, author.getLsid() );
                }
                else
                {
                    // it doesn't exists at the database, we need to create it previously
                    MDMAuthor author = daoauthor.save( getContext(), album.getAuthor(), false );
                    albumCV.put( MDMAlbum.COLUMN_FK_AUTHOR, author.getLsid() );
                }
                daoauthor.close();

            }
        }

        Cursor c = null;
        if ( album.getLsid() > 0 )
        {
            albumCV.put( MDMAuthor.COLUMN_LOCAL_SID, album.getLsid() );
            c = super._update( albumCV, album.getLsid() );
        }
        else
        {
            c = super._save( albumCV );
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
        MDMAlbum msi = new MDMAlbum( c, this.getContext(), true, false );
        c.close();
        close();
        return msi;
    }
}
