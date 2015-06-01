package org.messic.android.datamodel.dao;

import java.util.ArrayList;
import java.util.List;

import org.messic.android.datamodel.MDMAlbum;
import org.messic.android.datamodel.MDMSong;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class DAOSong
    extends DAO
{

    public DAOSong( Context context )
    {
        super( context, MDMSong.TABLE_NAME, MDMSong.getColumns() );
    }

    public int countDownloaded()
    {
        open();

        int result = _count();
        close();

        return result;
    }

    /**
     * Check if a certain song has been downloaded
     * 
     * @param songServerSid
     * @return boolean
     */
    public boolean isDownloaded( int songServerSid )
    {
        boolean result = false;

        open();

        Cursor c = _getByServerSid( songServerSid );

        if ( !c.isAfterLast() )
        {
            result = true;
        }

        close();

        return result;
    }

    public List<MDMSong> getSongs( int albumSid )
    {
        List<MDMSong> result = new ArrayList<MDMSong>();

        open();

        Cursor c = getDatabase().rawQuery( "SELECT * FROM " + getTableName() + " WHERE fk_album=" + albumSid, null );

        if ( c != null && c.moveToFirst() )
        {
            do
            {
                MDMSong song = new MDMSong( c, getContext(), false );
                result.add( song );
            }
            while ( c.moveToNext() );
        }

        c.close();
        close();
        return result;
    }

    public MDMSong save( MDMSong song )
    {
        open();

        ContentValues cv = new ContentValues();
        cv.put( MDMSong.COLUMN_TRACK, song.getTrack() );
        cv.put( MDMSong.COLUMN_FILENAME, song.getFileName() );
        cv.put( MDMSong.COLUMN_NAME, song.getName() );
        cv.put( MDMSong.COLUMN_RATE, song.getRate() );
        cv.put( MDMSong.COLUMN_SERVER_SID, song.getSid() );

        if ( song.getAlbum() == null )
        {
            // there is no album to link! :(
            cv.put( MDMSong.COLUMN_FK_ALBUM, 0 );
        }
        else
        {
            if ( song.getAlbum().getLsid() > 0 )
            {
                // it has a local sid, so it is an existen album from the database, linking!
                cv.put( MDMSong.COLUMN_FK_ALBUM, song.getAlbum().getLsid() );
            }
            else
            {
                // It is not an entity created from database, maybe it exist at database or maybe not, let's see
                DAOAlbum daoalbum = new DAOAlbum( getContext() );
                daoalbum.open();
                Cursor cAlbum = daoalbum._getByServerSid( song.getAlbum().getSid() );
                if ( cAlbum.getCount() > 0 )
                {
                    // it exist at the database!, so, just linking to it
                    MDMAlbum album = new MDMAlbum( cAlbum, getContext(), false );
                    cv.put( MDMSong.COLUMN_FK_ALBUM, album.getLsid() );
                }
                else
                {
                    // it doesn't exists at the database, we need to create it previously
                    MDMAlbum album = daoalbum.save( song.getAlbum(), false );
                    cv.put( MDMSong.COLUMN_FK_ALBUM, album.getLsid() );
                }
                daoalbum.close();
            }
        }

        Cursor c = null;
        if ( song.getLsid() > 0 )
        {
            cv.put( MDMSong.COLUMN_LOCAL_SID, song.getLsid() );
            c = super._update( cv, song.getLsid() );
        }
        else
        {
            c = super._save( cv );
        }

        c.moveToFirst();
        MDMSong msi = new MDMSong( c, getContext(), true );
        c.close();
        close();
        return msi;
    }
}
