package org.messic.android.util;

import java.util.ArrayList;
import java.util.List;

import org.messic.android.datamodel.MDMAlbum;
import org.messic.android.datamodel.MDMPlaylist;
import org.messic.android.datamodel.MDMSong;
import org.messic.android.player.MessicPlayerService;
import org.messic.android.player.PlayerEventListener;
import org.messic.android.player.MessicPlayerService.MusicBinder;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

public class UtilMusicPlayer
{
    private static MessicPlayerService musicService = null;

    private static boolean musicBound = false;

    /** listener list to store there the pending listeners to notify to the music service while connecting */
    private static List<PlayerEventListener> pendingListeners = new ArrayList<PlayerEventListener>();

    public static void startMessicMusicService( Context context )
    {
        Context appctx = context.getApplicationContext();
        Intent messicPlayerIntent = new Intent( appctx, MessicPlayerService.class );
        context.bindService( messicPlayerIntent, messicPlayerConnection, Context.BIND_AUTO_CREATE );
        context.startService( messicPlayerIntent );
    }

    public static MessicPlayerService getMessicPlayerService( Context ctx )
    {
        if ( musicBound )
        {
            return musicService;
        }
        else
        {
            startMessicMusicService( ctx );
            return null;
        }
    }

    public static void clearAndStopAll( Context ctx )
    {
        MessicPlayerService mps = getMessicPlayerService( ctx );
        if ( mps != null && mps.getPlayer() != null )
        {
            mps.getPlayer().stop();
            android.os.Process.killProcess( android.os.Process.myPid() );
        }
    }

    // connect to the service
    private static ServiceConnection messicPlayerConnection = new ServiceConnection()
    {

        public void onServiceConnected( ComponentName name, IBinder service )
        {
            MusicBinder binder = (MusicBinder) service;
            // get service
            musicService = binder.getService();
            // pass list
            musicBound = true;

            for ( int i = 0; i < pendingListeners.size(); i++ )
            {
                pendingListeners.get( i ).connected();

                musicService.getPlayer().addListener( pendingListeners.get( i ) );

                MDMSong song = musicService.getPlayer().getCurrentSong();
                if ( song != null )
                {
                    if ( musicService.getPlayer().isPlaying() )
                    {
                        pendingListeners.get( i ).playing( song, false, 0 );
                    }
                    else
                    {
                        pendingListeners.get( i ).playing( song, false, 0 );
                        pendingListeners.get( i ).paused( song, 0 );
                    }
                }
            }
            pendingListeners.clear();
        }

        public void onServiceDisconnected( ComponentName name )
        {
            musicService = null;
            musicBound = false;
            for ( int i = 0; i < pendingListeners.size(); i++ )
            {
                pendingListeners.get( i ).disconnected();
            }
        }

    };

    public static boolean prevSong( Context ctx )
    {
        MessicPlayerService mps = getMessicPlayerService( ctx );
        if ( mps != null && mps.getPlayer() != null )
        {
            mps.getPlayer().prevSong();
            return true;
        }
        return false;
    }

    public static boolean addAlbum( Context ctx, MDMAlbum album )
    {
        MessicPlayerService mps = getMessicPlayerService( ctx );
        if ( mps != null && mps.getPlayer() != null )
        {
            mps.getPlayer().addAlbum( album );
            return true;
        }
        return false;
    }

    public static boolean addPlaylist( Context ctx, MDMPlaylist playlist )
    {
        MessicPlayerService mps = getMessicPlayerService( ctx );
        if ( mps != null && mps.getPlayer() != null )
        {
            mps.getPlayer().addPlaylist( playlist );
            return true;
        }
        return false;
    }

    public static boolean addSongAndPlay( Context ctx, MDMSong song )
    {
        MessicPlayerService mps = getMessicPlayerService( ctx );
        if ( mps != null && mps.getPlayer() != null )
        {
            mps.getPlayer().addAndPlay( song );
            return true;
        }
        return false;
    }

    public static boolean addSong( Context ctx, MDMSong song )
    {
        MessicPlayerService mps = getMessicPlayerService( ctx );
        if ( mps != null && mps.getPlayer() != null )
        {
            mps.getPlayer().addSong( song );
            return true;
        }
        return false;
    }

    public static boolean nextSong( Context ctx )
    {
        MessicPlayerService mps = getMessicPlayerService( ctx );
        if ( mps != null && mps.getPlayer() != null )
        {
            mps.getPlayer().nextSong();
            return true;
        }
        return false;
    }

    public static boolean resumeSong( Context ctx )
    {
        MessicPlayerService mps = getMessicPlayerService( ctx );
        if ( mps != null && mps.getPlayer() != null )
        {
            mps.getPlayer().resumeSong();
            return true;
        }
        return false;
    }

    public static boolean pauseSong( Context ctx )
    {
        MessicPlayerService mps = getMessicPlayerService( ctx );
        if ( mps != null && mps.getPlayer() != null )
        {
            mps.getPlayer().pauseSong();
            return true;
        }
        return false;
    }

    public static MDMSong getCurrentSong( Context ctx )
    {
        MessicPlayerService mps = getMessicPlayerService( ctx );
        if ( mps != null && mps.getPlayer() != null )
        {
            return mps.getPlayer().getCurrentSong();
        }
        return null;

    }

    public static boolean isPlaying( Context ctx )
    {
        MessicPlayerService mps = getMessicPlayerService( ctx );
        if ( mps != null && mps.getPlayer() != null )
        {
            return mps.getPlayer().isPlaying();
        }
        return false;
    }

    public static boolean setSong( Context ctx, int index )
    {
        MessicPlayerService mps = getMessicPlayerService( ctx );
        if ( mps != null && mps.getPlayer() != null )
        {
            mps.getPlayer().setSong( index );
            return true;
        }
        return false;
    }

    public static boolean removeSong( Context ctx, int index )
    {
        MessicPlayerService mps = getMessicPlayerService( ctx );
        if ( mps != null && mps.getPlayer() != null )
        {
            mps.getPlayer().removeSong( index );
            return true;
        }
        return false;
    }

    public static boolean playSong( Context ctx )
    {
        MessicPlayerService mps = getMessicPlayerService( ctx );
        if ( mps != null && mps.getPlayer() != null )
        {
            mps.getPlayer().playSong();
            return true;
        }
        return false;
    }

    public static int getCursor( Context ctx )
    {
        MessicPlayerService mps = getMessicPlayerService( ctx );
        if ( mps != null && mps.getPlayer() != null )
        {
            return mps.getPlayer().getCursor();
        }
        return -1;
    }

    public static void addListener( Context ctx, PlayerEventListener listener )
    {
        MessicPlayerService mps = getMessicPlayerService( ctx );
        if ( mps != null && mps.getPlayer() != null )
        {
            if ( mps.getPlayer() != null )
            {
                mps.getPlayer().addListener( listener );
            }
        }
        else
        {
            pendingListeners.add( listener );
        }
    }

    public static List<MDMSong> getQueue( Context ctx )
    {
        MessicPlayerService mps = getMessicPlayerService( ctx );
        if ( mps != null && mps.getPlayer() != null )
        {
            return mps.getPlayer().getQueue();
        }
        return null;

    }
}
