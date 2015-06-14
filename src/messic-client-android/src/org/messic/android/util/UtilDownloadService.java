package org.messic.android.util;

import java.io.File;
import java.util.List;

import org.messic.android.R;
import org.messic.android.activities.LoginActivity;
import org.messic.android.controllers.Configuration;
import org.messic.android.datamodel.MDMAlbum;
import org.messic.android.datamodel.MDMAuthor;
import org.messic.android.datamodel.MDMSong;
import org.messic.android.datamodel.dao.DAOAlbum;
import org.messic.android.datamodel.dao.DAOAuthor;
import org.messic.android.datamodel.dao.DAOGenre;
import org.messic.android.datamodel.dao.DAOSong;
import org.messic.android.download.DownloadListener;
import org.messic.android.download.DownloadManagerService;
import org.messic.android.download.DownloadManagerService.DownloadManagerBinder;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.IBinder;

public class UtilDownloadService
{
    private static DownloadManagerService downloadService = null;

    private static boolean downloadBound = false;

    public static void startDownloadService( Context context )
    {
        if ( !downloadBound )
        {
            Context appctx = context.getApplicationContext();
            Intent downloadIntent = new Intent( appctx, DownloadManagerService.class );
            appctx.startService( downloadIntent );

            appctx.bindService( downloadIntent, downloadConnection, Context.BIND_AUTO_CREATE );
        }
    }

    public static void stopDownloadService( Context context )
    {
        if ( downloadBound )
        {
            Context appctx = context.getApplicationContext();
            Intent downloadIntent = new Intent( appctx, DownloadManagerService.class );
            appctx.unbindService( downloadConnection );
            appctx.stopService( downloadIntent );
        }
    }

    public static DownloadManagerService getDownloadService( Context ctx )
    {
        if ( downloadBound )
        {
            return downloadService;
        }
        else
        {
            startDownloadService( ctx );
            return null;
        }
    }

    // connect to the service
    private static ServiceConnection downloadConnection = new ServiceConnection()
    {

        public void onServiceConnected( ComponentName name, IBinder service )
        {
            DownloadManagerBinder binder = (DownloadManagerBinder) service;
            // get service
            downloadService = binder.getService();
            // pass list
            downloadBound = true;
        }

        public void onServiceDisconnected( ComponentName name )
        {
            downloadService = null;
            downloadBound = false;
        }

    };

    public static boolean addDownload( Context ctx, MDMSong song, DownloadListener listener )
    {
        DownloadManagerService mps = getDownloadService( ctx );
        if ( mps != null )
        {
            mps.addDownload( song, ctx, listener, true );
            return true;
        }
        return false;
    }

    public static boolean addDownload( Context ctx, MDMAlbum album, DownloadListener listener )
    {
        DownloadManagerService mps = getDownloadService( ctx );
        if ( mps != null )
        {
            mps.addDownload( album, ctx, listener );
            return true;
        }
        return false;
    }

    /**
     * Empty all the local database and remove all the local files!
     * 
     * @param ctx
     * @return
     */
    public static boolean emptyLocal( final Context ctx )
    {
        final ProgressDialog dialog =
            ProgressDialog.show( ctx, ctx.getString( R.string.action_emptying_localdatabase_title ),
                                 ctx.getString( R.string.action_emptying_localdatabase_content ), true );

        AsyncTask<Void, Void, Void> removeTask = new AsyncTask<Void, Void, Void>()
        {

            @Override
            protected Void doInBackground( Void... params )
            {
                DAOAuthor daoauthor = new DAOAuthor( ctx );
                DAOAlbum daoalbum = new DAOAlbum( ctx );
                DAOSong daosong = new DAOSong( ctx );
                DAOGenre daogenre = new DAOGenre( ctx );

                daoauthor._recreate();
                daoalbum._recreate();
                daosong._recreate();
                daogenre._recreate();

                File fbase = new File( UtilFile.getMessicFolderAbsolutePath() );
                if ( fbase.exists() )
                {
                    UtilFile.deleteDirectory( fbase );
                }

                dialog.dismiss();

                if ( Configuration.isOffline() )
                {
                    Intent ssa = new Intent( ctx, LoginActivity.class );
                    ctx.startActivity( ssa );
                }

                return null;
            }

        };
        removeTask.execute();
        return true;
    }

    public static boolean removeAlbum( Context ctx, MDMAlbum album )
    {
        ProgressDialog dialog =
            ProgressDialog.show( ctx, ctx.getString( R.string.action_removing_local_album_title ),
                                 ctx.getString( R.string.action_removing_local_album_content ), true );

        DAOAlbum daoalbum = new DAOAlbum( ctx );
        daoalbum.open();
        daoalbum.removeAlbum( album.getLsid() );
        daoalbum.close();
        String sAlbumFolder = album.calculateExternalStorageFolder();
        File fAlbumFolder = new File( sAlbumFolder );
        UtilFile.deleteDirectory( fAlbumFolder );
        // the last thing is to check if the author has other albums downloaded.. or just we need to remove the
        // author also
        MDMAuthor author = album.getAuthor();
        List<MDMAlbum> palbums = daoalbum.getAllByAuthorLSid( author.getLsid(), false );
        if ( palbums.size() == 0 )
        {
            // the author can be removed too!
            DAOAuthor daoauthor = new DAOAuthor( ctx );
            daoauthor.open();
            daoauthor._delete( author.getLsid() );
            daoauthor.close();
            String sAuthorFolder = author.calculateExternalStorageFolder();
            File fAuthorFolder = new File( sAuthorFolder );
            UtilFile.deleteDirectory( fAuthorFolder );
        }

        dialog.dismiss();

        return true;
    }

    public static boolean removeSong( Context ctx, MDMSong song )
    {
        String lfilename = song.getLfileName();
        File f = new File( lfilename );
        if ( f.exists() )
        {
            f.delete();
        }
        song.setLfileName( "" );
        DAOSong daosong = new DAOSong( ctx );
        daosong.save( song );

        // now we need to check if the album is empty now or not.. to remove the album folder also
        DAOAlbum daoalbum = new DAOAlbum( ctx );
        daoalbum.open();
        Cursor c = daoalbum._get( song.getAlbum().getLsid() );
        if ( c.moveToFirst() )
        {
            MDMAlbum album = new MDMAlbum( c, ctx, true, false );
            boolean flagEmptyAlbum = true;
            List<MDMSong> albumSongs = album.getSongs();
            for ( int i = 0; i < albumSongs.size(); i++ )
            {
                MDMSong asong = albumSongs.get( i );
                if ( asong.isDownloaded( ctx ) )
                {
                    flagEmptyAlbum = false;
                    break;
                }
            }
            if ( flagEmptyAlbum )
            {
                // the ablum is empty (no more songs downloaded).. we need to remove the album folder also
                daoalbum.removeAlbum( album.getLsid() );
                String sAlbumFolder = album.calculateExternalStorageFolder();
                File fAlbumFolder = new File( sAlbumFolder );
                UtilFile.deleteDirectory( fAlbumFolder );

                // the last thing is to check if the author has other albums downloaded.. or just we need to remove the
                // author also
                MDMAuthor author = album.getAuthor();
                List<MDMAlbum> palbums = daoalbum.getAllByAuthorLSid( author.getLsid(), false );
                if ( palbums.size() == 0 )
                {
                    // the author can be removed too!
                    DAOAuthor daoauthor = new DAOAuthor( ctx );
                    daoauthor.open();
                    daoauthor._delete( author.getLsid() );
                    daoauthor.close();
                    String sAuthorFolder = author.calculateExternalStorageFolder();
                    File fAuthorFolder = new File( sAuthorFolder );
                    UtilFile.deleteDirectory( fAuthorFolder );
                }

            }
        }
        daoalbum.close();

        return true;
    }
}
