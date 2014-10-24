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

import org.messic.android.R;
import org.messic.android.activities.LoginActivity;
import org.messic.android.datamodel.MDMSong;
import org.messic.android.util.AlbumCoverCache;
import org.messic.android.util.ImageUtil;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.RemoteViews;

public class MessicPlayerNotification
    implements PlayerEventListener
{
    private static final int ONGOING_NOTIFICATION_ID = 7553;

    public static final String ACTION_BACK = "org.messic.android.action.MESSIC_BACK";

    public static final String ACTION_PLAY = "org.messic.android.action.MESSIC_PLAY";

    public static final String ACTION_PAUSE = "org.messic.android.action.MESSIC_PAUSE";

    public static final String ACTION_NEXT = "org.messic.android.action.MESSIC_NEXT";

    public static final String ACTION_CLOSE = "org.messic.android.action.MESSIC_CLOSE";

    // linked service
    private Service service;

    // to update the notification later on.
    private NotificationManager mNotificationManager;

    //
    // private PendingIntent pintentBack;
    //
    // private PendingIntent pintentPlay;
    //
    // private PendingIntent pintentPause;
    //
    // private PendingIntent pintentNext;
    //
    // private PendingIntent pintentClose;
    //
    /** current cover used at notification bar */
    private Bitmap currentNotificationCover = null;

    private Notification notification = null;

    private MessicPlayerQueue player;

    public MessicPlayerNotification( Service service, MessicPlayerQueue player )
    {
        this.player = player;
        this.service = service;

    }

    private void registerBroadcastActions()
    {
        IntentFilter filter = new IntentFilter();
        filter.addAction( ACTION_BACK );
        filter.addAction( ACTION_PLAY );
        filter.addAction( ACTION_PAUSE );
        filter.addAction( ACTION_NEXT );
        filter.addAction( ACTION_CLOSE );
        BroadcastReceiver receiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive( Context context, Intent intent )
            {
                if ( intent.getAction().equals( ACTION_BACK ) )
                {
                    player.prevSong();

                }
                else if ( intent.getAction().equals( ACTION_PLAY ) )
                {
                    player.resumeSong();
                }
                else if ( intent.getAction().equals( ACTION_PAUSE ) )
                {
                    player.pauseSong();
                }
                else if ( intent.getAction().equals( ACTION_NEXT ) )
                {
                    player.nextSong();
                }
                else if ( intent.getAction().equals( ACTION_CLOSE ) )
                {
                    service.stopForeground( true );
                    service.unregisterReceiver( this );
                    mNotificationManager.cancel( ONGOING_NOTIFICATION_ID );

                    Intent ssa = new Intent( MessicPlayerNotification.this.service, LoginActivity.class );
                    ssa.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
                    ssa.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
                    MessicPlayerNotification.this.service.getApplication().startActivity( ssa );
                }
            }
        };

        this.service.registerReceiver( receiver, filter );
    }

    private void createNotification()
    {
        if(this.notification!=null){
            return;
        }
        
        mNotificationManager = (NotificationManager) this.service.getSystemService( Context.NOTIFICATION_SERVICE );
        RemoteViews contentView =
            new RemoteViews( this.service.getPackageName(), R.layout.bignotification_player_layout );
        NotificationCompat.Builder mBuilder =
            new NotificationCompat.Builder( this.service ).setSmallIcon( R.drawable.ic_launcher ).setContentTitle( "title" ).setPriority( Notification.PRIORITY_MAX ).setContent( contentView );

        Notification n = mBuilder.build();
        n.bigContentView = contentView;

        Intent intentClose = new Intent( ACTION_CLOSE );
        PendingIntent pintentClose = PendingIntent.getBroadcast( this.service, 0, intentClose, 0 );
        contentView.setOnClickPendingIntent( R.id.base_ivclose, pintentClose );
        Intent intentBack = new Intent( ACTION_BACK );
        PendingIntent pintentBack = PendingIntent.getBroadcast( this.service, 0, intentBack, 0 );
        contentView.setOnClickPendingIntent( R.id.base_ivback, pintentBack );
        Intent intentPlay = new Intent( ACTION_PLAY );
        PendingIntent pintentPlay = PendingIntent.getBroadcast( this.service, 0, intentPlay, 0 );
        contentView.setOnClickPendingIntent( R.id.base_ivplay, pintentPlay );
        Intent intentPause = new Intent( ACTION_PAUSE );
        PendingIntent pintentPause = PendingIntent.getBroadcast( this.service, 0, intentPause, 0 );
        contentView.setOnClickPendingIntent( R.id.base_ivpause, pintentPause );
        Intent intentNext = new Intent( ACTION_NEXT );
        PendingIntent pintentNext = PendingIntent.getBroadcast( this.service, 0, intentNext, 0 );
        contentView.setOnClickPendingIntent( R.id.base_ivnext, pintentNext );

        this.notification = n;
        this.service.startForeground( ONGOING_NOTIFICATION_ID, notification );
        this.registerBroadcastActions();
    }

    private void refreshContentData( MDMSong song )
    {
        createNotification();
        this.notification.bigContentView.setTextViewText( R.id.base_tvcurrent_author,
                                                          song.getAlbum().getAuthor().getName() );
        this.notification.bigContentView.setTextViewText( R.id.base_tvcurrent_song, song.getName() );
        constructNotificationCover( song );
        mNotificationManager.notify( ONGOING_NOTIFICATION_ID, this.notification );
    }

    public void paused( MDMSong song, int index )
    {
        createNotification();
        this.notification.bigContentView.setViewVisibility( R.id.base_ivplay, View.VISIBLE );
        this.notification.bigContentView.setViewVisibility( R.id.base_ivpause, View.INVISIBLE );
        refreshContentData( song );
    }

    public void playing( MDMSong song, boolean resumed, int index )
    {
        createNotification();
        if ( !resumed )
        {
            this.currentNotificationCover = null;
        }
        this.notification.bigContentView.setViewVisibility( R.id.base_ivplay, View.INVISIBLE );
        this.notification.bigContentView.setViewVisibility( R.id.base_ivpause, View.VISIBLE );
        refreshContentData( song );
    }

    public void completed( int index )
    {
        // TODO Auto-generated method stub

    }

    /**
     * Method to construct the notification cover
     * 
     * @param playSong
     */
    private void constructNotificationCover( final MDMSong playSong )
    {
        createNotification();
        if ( currentNotificationCover == null )
        {
            Bitmap cover = AlbumCoverCache.getCover( playSong.getAlbum().getSid(), new AlbumCoverCache.CoverListener()
            {

                public void setCover( Bitmap bitmap )
                {
                    // we need to recreate the remote view due to memory buffer problems with remote views and the image
                    // we send
                    RemoteViews contentView =
                        new RemoteViews( service.getPackageName(), R.layout.bignotification_player_layout );
                    notification.bigContentView = contentView;
                    notification.bigContentView.setTextViewText( R.id.base_tvcurrent_author,
                                                                 playSong.getAlbum().getAuthor().getName() );
                    notification.bigContentView.setTextViewText( R.id.base_tvcurrent_song, playSong.getName() );

                    Bitmap cover = ImageUtil.resizeToNotificationImageSize( service.getApplicationContext(), bitmap );
                    notification.bigContentView.setImageViewBitmap( R.id.base_ivcurrent_cover, cover );
                    currentNotificationCover = cover;
                    mNotificationManager.notify( ONGOING_NOTIFICATION_ID, notification );
                }

                public void failed( Exception e )
                {
                    // TODO Auto-generated method stub

                }
            } );
            if ( cover != null )
            {
                // we need to recreate the remote view due to memory buffer problems with remote views and the image we
                // send
                RemoteViews contentView =
                    new RemoteViews( this.service.getPackageName(), R.layout.bignotification_player_layout );
                notification.bigContentView = contentView;
                this.notification.bigContentView.setTextViewText( R.id.base_tvcurrent_author,
                                                                  playSong.getAlbum().getAuthor().getName() );
                this.notification.bigContentView.setTextViewText( R.id.base_tvcurrent_song, playSong.getName() );

                cover = ImageUtil.resizeToNotificationImageSize( this.service.getApplicationContext(), cover );
                notification.bigContentView.setImageViewBitmap( R.id.base_ivcurrent_cover, cover );
                this.currentNotificationCover = cover;
            }
            else
            {
                notification.bigContentView.setImageViewResource( R.id.base_ivcurrent_cover, R.drawable.ic_launcher );
            }
        }
    }
}
