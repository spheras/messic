package org.messic.android.datamodel.dao;

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
            //there is no album to link! :(
            cv.put( MDMSong.COLUMN_FK_ALBUM, 0 );
        }
        else
        {
            if ( song.getAlbum().getLsid() > 0 )
            {
                //it has a local sid, so it is an existen album from the database, linking!
                cv.put( MDMSong.COLUMN_FK_ALBUM, song.getAlbum().getLsid() );
            }
            else
            {
                //It is not an entity created from database, maybe it exist at database or maybe not, let's see
                DAOAlbum daoalbum = new DAOAlbum( getContext() );
                Cursor cAlbum = daoalbum._getByServerSid( song.getAlbum().getSid() );
                if ( cAlbum.getCount() > 0 )
                {
                    //it exist at the database!, so, just linking to it
                    MDMAlbum album = new MDMAlbum( cAlbum, getContext() );
                    cv.put( MDMSong.COLUMN_FK_ALBUM, album.getLsid() );
                }
                else
                {
                    //it doesn't exists at the database, we need to create it previously
                    MDMAlbum album=daoalbum.save( song.getAlbum() );
                    cv.put( MDMSong.COLUMN_FK_ALBUM, album.getLsid() );
                }
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
        MDMSong msi = new MDMSong( c, getContext() );
        c.close();
        close();
        return msi;
    }
}
