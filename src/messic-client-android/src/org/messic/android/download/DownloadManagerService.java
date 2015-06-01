/*
 * Copyright (C) 2013
 *
 *  This file is part of Messic.
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.messic.android.download;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.messic.android.datamodel.MDMAlbum;
import org.messic.android.datamodel.MDMSong;
import org.messic.android.datamodel.dao.DAOAlbum;
import org.messic.android.datamodel.dao.DAOSong;
import org.messic.android.util.AlbumCoverCache;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class DownloadManagerService
    extends Service
{
    private final IBinder downloadManagerBind = new DownloadManagerBinder();

    public static final String DESTINATION_FOLDER = "/messic/offline";

    public void onCreate()
    {
        super.onCreate();
    }

    private void saveDatabase( MDMSong song )
    {

        DAOSong daosong = new DAOSong( this.getApplicationContext() );
        Cursor csong=daosong._getByServerSid( song.getSid() );
        if(csong.moveToFirst()){
            //the song exist, so, we only need to updatesdfsdfs
        }
asdfasf        

        song.getAlbum();
        DAOAlbum daoalbum = new DAOAlbum( this.getApplicationContext() );
        daoalbum.open();
        Cursor cAlbum = daoalbum._get( song.getAlbum().getLsid() );
        if ( cAlbum.moveToFirst() )
        {
            // the album exist previously, lets get the song
        }

        daoalbum.save( song.getAlbum(), true );
    }

    private void saveCover( MDMSong song )
    {
        String folder = song.getAlbum().calculateExternalStorageFolder();
        String scover = folder + "/cover.jpg";
        final File fcover = new File( scover );
        if ( !fcover.exists() )
        {
            Bitmap cover = AlbumCoverCache.getCover( song.getAlbum().getSid(), new AlbumCoverCache.CoverListener()
            {

                public void setCover( Bitmap bitmap )
                {
                    saveCover( bitmap, fcover );
                }

                public void failed( Exception e )
                {
                    Log.e( "DownloadManagerService", "cover error", e );
                }
            } );
            if ( cover != null )
            {
                saveCover( cover, fcover );
            }
        }
    }

    private void saveCover( Bitmap cover, File fcover )
    {
        try
        {
            FileOutputStream baos = new FileOutputStream( fcover );
            cover.compress( Bitmap.CompressFormat.JPEG, 100, baos );
            baos.close();
        }
        catch ( FileNotFoundException e1 )
        {
            Log.e( "DownloadManagerService", "cover error", e1 );
        }
        catch ( IOException e1 )
        {
            Log.e( "DownloadManagerService", "cover error", e1 );
        }
    }

    public void addDownload( MDMAlbum album, Context context )
    {
        List<MDMSong> songs = album.getSongs();
        for ( MDMSong mdmSong : songs )
        {
            addDownload( mdmSong, context );
        }
    }

    public void addDownload( MDMSong song, Context context )
    {
        String path = song.calculateExternalStorageFolder();
        File fpath = new File( path );
        fpath.mkdirs();
        try
        {
            DownloadQueue.DownloadQueueListener listener = new DownloadQueue.DownloadQueueListener()
            {
                public void received( MDMSong song, File fdownloaded )
                {
                    saveCover( song );
                    saveDatabase( song );
                }
            };

            DownloadQueue.addDownload( new URL( song.getURL() ), song, listener );

        }
        catch ( MalformedURLException e )
        {
            Log.e( "DownloadManagerService", "error downloading!" );
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand( Intent intent, int flags, int startId )
    {
        return super.onStartCommand( intent, flags, startId );
    }

    public class DownloadManagerBinder
        extends Binder
    {
        public DownloadManagerService getService()
        {
            return DownloadManagerService.this;
        }
    }

    @Override
    public IBinder onBind( Intent intent )
    {
        return downloadManagerBind;
    }

    @Override
    public boolean onUnbind( Intent intent )
    {
        return false;
    }

}
