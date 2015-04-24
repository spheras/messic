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
import java.util.List;

import org.messic.android.datamodel.MDMAlbum;
import org.messic.android.datamodel.MDMSong;
import org.messic.android.datamodel.dao.DAOSong;
import org.messic.android.util.AlbumCoverCache;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.util.LongSparseArray;
import android.util.Log;

public class DownloadManagerService
    extends Service
{
    private final IBinder downloadManagerBind = new DownloadManagerBinder();

    private LongSparseArray<MDMSong> pendingDownloads = new LongSparseArray<MDMSong>();

    private DownloadManager dm;

    public static final String DESTINATION_FOLDER = "/messic/downloads";

    public void onCreate()
    {
        super.onCreate();
        dm = (DownloadManager) getSystemService( Activity.DOWNLOAD_SERVICE );

        BroadcastReceiver receiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive( Context context, Intent intent )
            {
                String action = intent.getAction();
                if ( DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals( action ) )
                {
                    long downloadId = intent.getLongExtra( DownloadManager.EXTRA_DOWNLOAD_ID, 0 );
                    MDMSong song = pendingDownloads.get( downloadId );
                    saveCover( song );
                    saveDatabase( song );
                    pendingDownloads.remove( downloadId );
                    // Uri uri = dm.getUriForDownloadedFile( downloadId );
                    // System.out.println( uri );
                    // dm.remove( downloadId );
                }
            }
        };
        registerReceiver( receiver, new IntentFilter( DownloadManager.ACTION_DOWNLOAD_COMPLETE ) );
    }

    private void saveDatabase( MDMSong song )
    {
        DAOSong daosong = new DAOSong( this.getApplicationContext() );
        daosong.save( song );
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
            cover.compress( Bitmap.CompressFormat.JPEG, 1, baos );
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

    public void addDownload( MDMAlbum album )
    {
        List<MDMSong> songs = album.getSongs();
        for ( MDMSong mdmSong : songs )
        {
            Request request = new Request( Uri.parse( mdmSong.getURL() ) );
            String folder = mdmSong.calculateExternalStorageFolder();
            request.setDestinationInExternalPublicDir( folder, mdmSong.getFileName() );
            long id = dm.enqueue( request );
            pendingDownloads.put( id, mdmSong );
        }
    }

    public void addDownload( MDMSong song )
    {
        Request request = new Request( Uri.parse( song.getURL() ) );
        String folder = song.calculateExternalStorageFolder();
        request.setDestinationInExternalPublicDir( folder, song.getFileName() );
        long id = dm.enqueue( request );
        pendingDownloads.put( id, song );
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
