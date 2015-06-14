package org.messic.android.download;

import java.io.File;

import org.messic.android.R;
import org.messic.android.datamodel.MDMSong;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.util.LongSparseArray;

public class DownloadNotification
    implements DownloadListener
{
    private static final int ONGOING_NOTIFICATION_ID = 7556;

    // linked service
    private Service service;

    // to update the notification later on.
    private NotificationManager mNotifyManager = null;

    private NotificationCompat.Builder mBuilder = null;

    private LongSparseArray<MDMSong> pendingDownloads = new LongSparseArray<MDMSong>();

    private MDMSong currentDownloadingSong = null;

    public DownloadNotification( Service service )
    {
        this.service = service;
    }

    public void createNotification( String title, String subtitle )
    {
        mNotifyManager = (NotificationManager) this.service.getSystemService( Context.NOTIFICATION_SERVICE );
        mBuilder = new NotificationCompat.Builder( this.service );
        Bitmap licon = BitmapFactory.decodeResource( this.service.getResources(), R.drawable.downloading );
        mBuilder.setContentTitle( title ).setContentText( subtitle ).setSmallIcon( R.drawable.downloading ).setLargeIcon( licon );
        mBuilder.setTicker( "" );
        mBuilder.setProgress( 0, 0, true );
        mNotifyManager.notify( ONGOING_NOTIFICATION_ID, mBuilder.build() );
        // // Start a lengthy operation in a background thread
        // new Thread( new Runnable()
        // {
        // public void run()
        // {
        // int incr;
        // // Do the "lengthy" operation 20 times
        // for ( incr = 0; incr <= 100; incr += 5 )
        // {
        // // Sets the progress indicator to a max value, the
        // // current completion percentage, and "determinate"
        // // state
        // mBuilder.setProgress( 0, 0, false );
        // // Displays the progress bar for the first time.
        // mNotifyManager.notify( ONGOING_NOTIFICATION_ID, mBuilder.build() );
        // // Sleeps the thread, simulating an operation
        // // that takes time
        // try
        // {
        // // Sleep for 5 seconds
        // Thread.sleep( 5 * 1000 );
        // }
        // catch ( InterruptedException e )
        // {
        // Log.d( "DownloadNotification", "sleep failure" );
        // }
        // }
        // // When the loop is finished, updates the notification
        // mBuilder.setContentText( "Download complete" )
        // // Removes the progress bar
        // .setProgress( 0, 0, false );
        // mNotifyManager.notify( ONGOING_NOTIFICATION_ID, mBuilder.build() );
        // }
        // }
        // // Starts the thread by calling the run() method in its Runnable
        // ).start();
    }

    public void downloadAdded( MDMSong song )
    {
        pendingDownloads.append( song.getSid(), song );
        if ( mNotifyManager == null )
        {
            String title = "mownloads (" + this.pendingDownloads.size() + ")";
            String subtitle = song.getAlbum().getName() + " - " + song.getName();
            createNotification( title, subtitle );
        }
        else
        {
            update();
        }
    }

    public void downloadStarted( MDMSong song )
    {
        this.currentDownloadingSong = song;
        update();
    }

    public void downloadUpdated( MDMSong song, float percent )
    {
        update();
    }

    public void downloadFinished( MDMSong song, File fdownloaded )
    {
        pendingDownloads.remove( song.getSid() );
        if ( song.getSid() == this.currentDownloadingSong.getSid() )
        {
            this.currentDownloadingSong = null;
        }
        update();
    }

    private void update()
    {
        if ( mNotifyManager == null )
        {
            createNotification( "", "" );
        }

        if ( pendingDownloads.size() == 0 )
        {
            String title = "Download Completed";
            Bitmap licon = BitmapFactory.decodeResource( this.service.getResources(), R.drawable.dowloading_07 );
            mBuilder.setContentTitle( title ).setSmallIcon( R.drawable.downloading_mini_07 ).setLargeIcon( licon )
            // Removes the progress bar
            .setProgress( 0, 0, false );

            mNotifyManager.notify( ONGOING_NOTIFICATION_ID, mBuilder.build() );
        }
        else
        {
            if ( this.currentDownloadingSong != null )
            {
                // When the loop is finished, updates the notification
                String title = "downloads (" + this.pendingDownloads.size() + ")";
                String subtitle =
                    this.currentDownloadingSong.getAlbum().getName() + " - " + this.currentDownloadingSong.getName();

                Bitmap licon = BitmapFactory.decodeResource( this.service.getResources(), R.drawable.downloading );
                mBuilder.setContentTitle( title ).setContentText( subtitle ).setProgress( 0, 0, true ).setSmallIcon( R.drawable.downloading_mini ).setLargeIcon( licon ).setTicker( "" );
                mNotifyManager.notify( ONGOING_NOTIFICATION_ID, mBuilder.build() );
            }
            else
            {

            }
        }

    }

    public void connected()
    {
        // Nothing to do
    }

    public void disconnected()
    {
        // Nothing to do
    }
}
