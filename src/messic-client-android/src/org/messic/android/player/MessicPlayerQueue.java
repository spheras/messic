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

import org.messic.android.datamodel.MDMAlbum;
import org.messic.android.datamodel.MDMSong;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.PowerManager;
import android.util.Log;

public class MessicPlayerQueue
    implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener
{
    // media player
    private MediaPlayer player;

    // song list
    private List<MDMSong> queue = new ArrayList<MDMSong>();

    // current position
    private int cursor;

    // flag to know if the player is playing or not, mediaplayer is not as effective as desired
    private boolean playing = false;

    // event listeners for the messic player service
    private List<PlayerEventListener> listeners = new ArrayList<PlayerEventListener>();

    public MessicPlayerQueue( Context context )
    {
        // initialize position
        this.cursor = 0;
        // create player
        this.player = new MediaPlayer();
        this.player.setWakeMode( context, PowerManager.PARTIAL_WAKE_LOCK );
        this.player.setAudioStreamType( AudioManager.STREAM_MUSIC );

        this.player.setOnPreparedListener( this );
        this.player.setOnCompletionListener( this );
        this.player.setOnErrorListener( this );
    }

    public void addListener( PlayerEventListener listener )
    {
        this.listeners.remove( listener );
        this.listeners.add( listener );
    }

    public void removeListener( PlayerEventListener listener )
    {
        this.listeners.remove( listener );
    }

    public boolean isPlaying()
    {
        return this.playing;
    }

    public void setList( ArrayList<MDMSong> theSongs )
    {
        queue = theSongs;
    }

    public void onCompletion( MediaPlayer mp )
    {
        this.playing = false;
        nextSong();
    }

    public boolean onError( MediaPlayer mp, int what, int extra )
    {
        return false;
    }

    public void onPrepared( MediaPlayer mp )
    {
        mp.start();
    }

    public void setSong( int songIndex )
    {
        cursor = songIndex;
    }

    public void addAndPlay( MDMSong song )
    {
        this.queue.add( cursor + 1, song );
        playSong();

        for ( PlayerEventListener eventListener : listeners )
        {
            eventListener.playing( song, false, cursor );
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
            for ( PlayerEventListener eventListener : listeners )
            {
                eventListener.completed( cursor );
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
            // String message = getString( R.string.player_added ) + " " + song.getName();
            // Toast.makeText( getApplicationContext(), message, Toast.LENGTH_SHORT ).show();
        }
    }

    public int getCursor()
    {
        return this.cursor;
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

    public void playSong()
    {
        if ( cursor >= queue.size() )
            return;

        this.playing = true;
        // get song
        MDMSong playSong = queue.get( cursor );

        for ( PlayerEventListener eventListener : listeners )
        {
            eventListener.playing( playSong, false, cursor );
        }

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

    public void stop()
    {
        this.player.stop();
        this.playing = false;
    }

    @Override
    protected void finalize()
        throws Throwable
    {
        this.player.release();
        super.finalize();
    }

    public void pauseSong()
    {
        if ( cursor >= queue.size() )
            return;

        this.playing = false;
        player.pause();
        MDMSong playSong = queue.get( cursor );

        for ( PlayerEventListener eventListener : listeners )
        {
            eventListener.paused( playSong, cursor );
        }

    }

    public void removeSong( int index )
    {
        if ( index < this.queue.size() )
        {
            this.queue.remove( index );
            if ( this.queue.size() <= 0 )
            {
                stop();
                return;
            }

            if ( index >= this.queue.size() )
            {
                index = this.queue.size() - 1;
                if ( index < 0 )
                {
                    index = 0;
                }
            }

            if ( index == this.cursor )
            {
                if ( this.isPlaying() )
                {
                    this.stop();
                    this.playSong();
                }
            }
        }
    }

    public void resumeSong()
    {
        if ( cursor >= queue.size() )
            return;

        this.playing = true;
        player.start();

        MDMSong playSong = queue.get( cursor );

        for ( PlayerEventListener eventListener : listeners )
        {
            eventListener.playing( playSong, true, cursor );
        }

    }
}
