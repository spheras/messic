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
package org.messic.android.player;

import java.util.ArrayList;
import java.util.List;

import org.messic.android.R;
import org.messic.android.datamodel.MDMAlbum;
import org.messic.android.datamodel.MDMSong;
import org.messic.android.util.AlbumCoverCache;
import org.messic.android.util.ImageUtil;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class MessicPlayerService
    extends Service
    implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener
{
    private static final int ONGOING_NOTIFICATION_ID = 7553;

    public static final String ACTION_BACK = "org.messic.android.action.MESSIC_BACK";

    public static final String ACTION_PLAY = "org.messic.android.action.MESSIC_PLAY";

    public static final String ACTION_PAUSE = "org.messic.android.action.MESSIC_PAUSE";

    public static final String ACTION_NEXT = "org.messic.android.action.MESSIC_NEXT";

    public static final String ACTION_CLOSE = "org.messic.android.action.MESSIC_CLOSE";

    // media player
    private MediaPlayer player;

    // song list
    private List<MDMSong> queue = new ArrayList<MDMSong>();

    // current position
    private int cursor;

    private final IBinder musicBind = new MusicBinder();

    // notification builder
    private NotificationCompat.Builder mBuilder;

    // to update the notification later on.
    private NotificationManager mNotificationManager;

    private PendingIntent pintentBack;

    private PendingIntent pintentPlay;

    private PendingIntent pintentPause;

    private PendingIntent pintentNext;

    private PendingIntent pintentClose;

    /** current cover used at notification bar */
    private Bitmap currentNotificationCover = null;

    private List<EventListener> listeners = new ArrayList<MessicPlayerService.EventListener>();

    /**
     * Interface to listen events
     */
    public interface EventListener
    {
        /** the current song has been paused */
        void paused( MDMSong song );

        /** We are playing a song */
        void playing( MDMSong song, boolean resumed );

        /** The queue have been finished */
        void completed();
    }

    public void addListener( EventListener listener )
    {
        this.listeners.remove( listener );
        this.listeners.add( listener );
    }

    public void removeListener( EventListener listener )
    {
        this.listeners.remove( listener );
    }

    public boolean isPlaying()
    {
        return this.player.isPlaying();
    }

    public void onCreate()
    {
        // create the service
        super.onCreate();
        // initialize position
        cursor = 0;
        // create player
        player = new MediaPlayer();
        initMusicPlayer();

        Intent intentBack = new Intent( ACTION_BACK );
        pintentBack = PendingIntent.getBroadcast( this, 0, intentBack, 0 );
        Intent intentPlay = new Intent( ACTION_PLAY );
        pintentPlay = PendingIntent.getBroadcast( this, 0, intentPlay, 0 );
        Intent intentPause = new Intent( ACTION_PAUSE );
        pintentPause = PendingIntent.getBroadcast( this, 0, intentPause, 0 );
        Intent intentNext = new Intent( ACTION_NEXT );
        pintentNext = PendingIntent.getBroadcast( this, 0, intentNext, 0 );
        Intent intentClose = new Intent( ACTION_CLOSE );
        pintentClose = PendingIntent.getBroadcast( this, 0, intentClose, 0 );

        IntentFilter filter = new IntentFilter();
        filter.addAction( ACTION_BACK );
        filter.addAction( ACTION_PLAY );
        filter.addAction( ACTION_PAUSE );
        filter.addAction( ACTION_NEXT );

        BroadcastReceiver receiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive( Context context, Intent intent )
            {
                if ( intent.getAction().equals( ACTION_BACK ) )
                {
                    prevSong();
                }
                else if ( intent.getAction().equals( ACTION_PLAY ) )
                {
                    resumeSong();
                }
                else if ( intent.getAction().equals( ACTION_PAUSE ) )
                {
                    pauseSong();
                }
                else if ( intent.getAction().equals( ACTION_NEXT ) )
                {
                    nextSong();
                }
            }
        };

        registerReceiver( receiver, filter );

        mNotificationManager = (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );

        createBuilder();
        mBuilder.setContentTitle( getText( R.string.app_name ) );
        mBuilder.setContentText( "" );

        startForeground( ONGOING_NOTIFICATION_ID, mBuilder.build() );
    }

    @Override
    public int onStartCommand( Intent intent, int flags, int startId )
    {
        // TODO Auto-generated method stub
        return super.onStartCommand( intent, flags, startId );
    }

    public void onCompletion( MediaPlayer mp )
    {
        nextSong();
    }

    public boolean onError( MediaPlayer mp, int what, int extra )
    {
        // TODO Auto-generated method stub
        return false;
    }

    public void onPrepared( MediaPlayer mp )
    {
        // start playback
        mp.start();
    }

    public void setList( ArrayList<MDMSong> theSongs )
    {
        queue = theSongs;
    }

    public class MusicBinder
        extends Binder
    {
        public MessicPlayerService getService()
        {
            return MessicPlayerService.this;
        }
    }

    public void initMusicPlayer()
    {
        player.setWakeMode( getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK );
        player.setAudioStreamType( AudioManager.STREAM_MUSIC );

        player.setOnPreparedListener( this );
        player.setOnCompletionListener( this );
        player.setOnErrorListener( this );
    }

    @Override
    public IBinder onBind( Intent intent )
    {
        return musicBind;
    }

    @Override
    public boolean onUnbind( Intent intent )
    {
        player.stop();
        player.release();
        return false;
    }

    public void setSong( int songIndex )
    {
        cursor = songIndex;
    }

    public void addAndPlay( MDMSong song )
    {
        this.queue.add( cursor + 1, song );
        playSong();

        for ( EventListener eventListener : listeners )
        {
            eventListener.playing( song, false );
        }

    }

    public void nextSong()
    {
        if ( cursor < this.queue.size() - 1 )
        {
            cursor++;
            playSong();
        }
        else
        {
            for ( EventListener eventListener : listeners )
            {
                eventListener.completed();
            }
        }
    }

    public void prevSong()
    {
        if ( cursor > 0 )
        {
            cursor--;
            playSong();
        }
    }

    public void addAlbum( MDMAlbum album )
    {
        List<MDMSong> songs = album.getSongs();
        int playSong = -1;
        if ( this.queue.size() == 0 || !this.player.isPlaying() )
        {
            playSong = this.queue.size();
        }
        for ( int i = 0; i < songs.size(); i++ )
        {
            MDMSong song = songs.get( i );
            song.setAlbum( album );
            this.queue.add( song );
        }
        if ( playSong != -1 )
        {
            this.cursor = playSong;
            playSong();
        }
    }

    public void addSong( MDMSong song )
    {
        this.queue.add( song );
        if ( this.queue.size() == 1 )
        {
            playSong();
        }
        else if ( !this.player.isPlaying() )
        {
            nextSong();
        }
        else
        {
            String message = getString( R.string.player_added ) + " " + song.getName();
            Toast.makeText( getApplicationContext(), message, Toast.LENGTH_SHORT ).show();
        }
    }

    public MDMSong getCurrentSong()
    {
        if ( this.queue.size() > this.cursor )
        {
            return this.queue.get( this.cursor );
        }
        else
        {
            return null;
        }
    }

    private void createBuilder()
    {
        mBuilder = new NotificationCompat.Builder( this );
        mBuilder.setSmallIcon( R.drawable.ic_launcher );
        mBuilder.setLargeIcon( BitmapFactory.decodeResource( getResources(), R.drawable.ic_launcher ) );
        mBuilder.setPriority( NotificationCompat.PRIORITY_MAX );
    }

    public void pauseSong()
    {
        player.pause();

        MDMSong playSong = queue.get( cursor );
        createBuilder();
        mBuilder.setContentTitle( playSong.getAlbum().getName() );
        mBuilder.setContentText( playSong.getName() );
        mBuilder.addAction( R.drawable.miniback_w_30, "", pintentBack );
        mBuilder.addAction( R.drawable.miniplay_w_30, "", pintentPlay );
        mBuilder.addAction( R.drawable.mininext_w_30, "", pintentNext );
        mBuilder.addAction( R.drawable.close, "", pintentClose );
        if ( currentNotificationCover != null )
            mBuilder.setLargeIcon( currentNotificationCover );
        mNotificationManager.notify( ONGOING_NOTIFICATION_ID, mBuilder.build() );

        for ( EventListener eventListener : listeners )
        {
            eventListener.paused( playSong );
        }

    }

    public void resumeSong()
    {

        player.start();

        MDMSong playSong = queue.get( cursor );
        createBuilder();
        mBuilder.setContentTitle( playSong.getAlbum().getName() );
        mBuilder.setContentText( playSong.getName() );
        mBuilder.addAction( R.drawable.miniback_w_30, "", pintentBack );
        mBuilder.addAction( R.drawable.minipause_w_30, "", pintentPause );
        mBuilder.addAction( R.drawable.mininext_w_30, "", pintentNext );
        mBuilder.addAction( R.drawable.close, "", pintentClose );
        if ( currentNotificationCover != null )
            mBuilder.setLargeIcon( currentNotificationCover );
        mNotificationManager.notify( ONGOING_NOTIFICATION_ID, mBuilder.build() );

        for ( EventListener eventListener : listeners )
        {
            eventListener.playing( playSong, true );
        }

    }

    public void playSong()
    {
        // get song
        MDMSong playSong = queue.get( cursor );

        for ( EventListener eventListener : listeners )
        {
            eventListener.playing( playSong, false );
        }

        String message = getString( R.string.player_playing ) + " " + playSong.getName();
        Toast.makeText( getApplicationContext(), message, Toast.LENGTH_SHORT ).show();
        mBuilder.setContentTitle( playSong.getAlbum().getName() );
        mBuilder.setContentText( playSong.getName() );
        mBuilder.addAction( R.drawable.miniback_w_30, "", pintentBack );
        mBuilder.addAction( R.drawable.minipause_w_30, "", pintentPause );
        mBuilder.addAction( R.drawable.mininext_w_30, "", pintentNext );
        mBuilder.addAction( R.drawable.close, "", pintentClose );
        constructNotificationCover( playSong );
        mNotificationManager.notify( ONGOING_NOTIFICATION_ID, mBuilder.build() );

        // play a song
        player.reset();

        // // get id
        // long currSong = playSong.getID();
        // // set uri
        // Uri trackUri =
        // ContentUris.withAppendedId( android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, currSong );
        // player.setDataSource( getApplicationContext(), trackUri );

        try
        {
            // if ( playSong.isLocal() )
            // {
            // player.setDataSource( playSong.getFile() );
            // }
            // else
            // {
            player.setDataSource( playSong.getURL() );
            // }
        }
        catch ( Exception e )
        {
            Log.e( "MUSIC SERVICE", "Error setting data source", e );
        }

        player.prepareAsync();
    }

    /**
     * Method to construct the notification cover
     * 
     * @param playSong
     */
    private void constructNotificationCover( MDMSong playSong )
    {
        Bitmap cover = AlbumCoverCache.getCover( playSong.getAlbum().getSid(), new AlbumCoverCache.CoverListener()
        {

            public void setCover( Bitmap bitmap )
            {
                Bitmap cover = ImageUtil.resizeToNotificationImageSize( getApplicationContext(), bitmap );
                mBuilder.setLargeIcon( cover );
                mNotificationManager.notify( ONGOING_NOTIFICATION_ID, mBuilder.build() );
                currentNotificationCover = cover;
            }

            public void failed( Exception e )
            {
                // TODO Auto-generated method stub

            }
        } );
        if ( cover != null )
        {
            cover = ImageUtil.resizeToNotificationImageSize( getApplicationContext(), cover );
            mBuilder.setLargeIcon( cover );
            this.currentNotificationCover = cover;
        }
        else
        {
            mBuilder.setLargeIcon( BitmapFactory.decodeResource( getResources(), R.drawable.ic_launcher ) );
        }
    }

    /**
     * @return the queue
     */
    public List<MDMSong> getQueue()
    {
        return queue;
    }

    /**
     * @param queue the queue to set
     */
    public void setQueue( List<MDMSong> queue )
    {
        this.queue = queue;
    }

}
