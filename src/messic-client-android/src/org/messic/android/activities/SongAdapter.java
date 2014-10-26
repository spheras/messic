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
package org.messic.android.activities;

import java.util.ArrayList;
import java.util.List;

import org.messic.android.R;
import org.messic.android.datamodel.MDMPlaylist;
import org.messic.android.datamodel.MDMSong;
import org.messic.android.util.AlbumCoverCache;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SongAdapter
    extends BaseAdapter
{
    private List<MDMSong> songs = new ArrayList<MDMSong>();

    private LayoutInflater inflater = null;

    private Activity activity = null;

    private EventListener listener = null;

    private SongAdapterType type = SongAdapterType.cover;

    // <0 to indicate that not current song want to be highlighted
    private int currentSong = -1;

    public enum SongAdapterType
    {
        track, cover, detailed
    }

    public interface EventListener
    {
        void coverTouch( MDMSong song, int index );

        void coverLongTouch( MDMSong song, int index );

        void textTouch( MDMSong song, int index );

        void elementRemove( MDMSong song, int index );

        void playlistTouch( MDMPlaylist playlist, int index );
    }

    public SongAdapter( Activity activity, EventListener listener )
    {
        this( activity, listener, SongAdapterType.cover );
    }

    public SongAdapter( Activity activity, EventListener listener, SongAdapterType type )
    {
        this.inflater = LayoutInflater.from( activity );
        this.activity = activity;
        this.listener = listener;
        this.type = type;
    }

    public void clear()
    {
        songs = new ArrayList<MDMSong>();
    }

    public int getCount()
    {
        return songs.size();
    }

    public Object getItem( int arg0 )
    {
        return this.songs.get( arg0 );
    }

    public long getItemId( int arg0 )
    {
        return arg0;
    }

    public void addSong( MDMSong song )
    {
        this.songs.add( song );
    }

    public void removeElement( int index )
    {
        this.songs.remove( index );
    }

    @SuppressLint( "InflateParams" )
    public View getView( final int position, View counterView, ViewGroup parent )
    {
        if ( counterView == null )
        {
            switch ( type )
            {
                case cover:
                    counterView = this.inflater.inflate( R.layout.songcover, null );
                    break;
                case detailed:
                    counterView = this.inflater.inflate( R.layout.songdetailed, null );
                    break;
                case track:
                    counterView = this.inflater.inflate( R.layout.songtrack, null );
                    break;
            }
        }

        final MDMSong song = this.songs.get( position );

        ImageView icover = null;
        TextView tauthor = null;
        TextView tsongname = null;
        TextView talbum = null;
        TextView tsongtrack = null;

        if ( type == SongAdapterType.cover )
        {
            icover = (ImageView) counterView.findViewById( R.id.song_icover );
            tauthor = (TextView) counterView.findViewById( R.id.song_tauthor );
            talbum = (TextView) counterView.findViewById( R.id.song_talbum );
            tsongname = (TextView) counterView.findViewById( R.id.song_tsongname );
        }
        else if ( type == SongAdapterType.detailed )
        {
            icover = (ImageView) counterView.findViewById( R.id.songdetailed_icover );
            tauthor = (TextView) counterView.findViewById( R.id.songdetailed_tauthor );
            talbum = (TextView) counterView.findViewById( R.id.songdetailed_talbum );
            tsongname = (TextView) counterView.findViewById( R.id.songdetailed_tsong );
        }
        else if ( type == SongAdapterType.track )
        {
            tsongname = (TextView) counterView.findViewById( R.id.songtrack_tsongname );
            tsongtrack = (TextView) counterView.findViewById( R.id.songtrack_ttrack );
            ImageView ivplay = (ImageView) counterView.findViewById( R.id.songtrack_ivplay );
            ivplay.setOnClickListener( new View.OnClickListener()
            {
                public void onClick( View v )
                {
                    listener.coverTouch( song, position );
                }
            } );
        }

        final ImageView ficover = icover;

        if ( tauthor != null )
        {
            tauthor.setText( song.getAlbum().getAuthor().getName() );
            tauthor.setOnClickListener( new View.OnClickListener()
            {
                public void onClick( View v )
                {
                    listener.textTouch( song, position );
                }
            } );
        }
        if ( talbum != null )
        {
            talbum.setText( song.getAlbum().getName() );
            talbum.setPaintFlags( talbum.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG );
            talbum.setOnClickListener( new View.OnClickListener()
            {
                public void onClick( View v )
                {
                    listener.textTouch( song, position );
                }
            } );
        }
        if ( tsongname != null )
        {
            tsongname.setText( song.getName() );
            tsongname.setOnClickListener( new View.OnClickListener()
            {
                public void onClick( View v )
                {
                    listener.textTouch( song, position );
                }
            } );
        }
        if ( tsongtrack != null )
        {
            tsongtrack.setText( "" + song.getTrack() );
            tsongtrack.setOnClickListener( new View.OnClickListener()
            {
                public void onClick( View v )
                {
                    listener.textTouch( song, position );
                }
            } );
        }
        if ( icover != null )
        {
            icover.setImageResource( android.R.color.white );
            icover.setOnClickListener( new View.OnClickListener()
            {

                public void onClick( View v )
                {
                    listener.coverTouch( song, position );
                }
            } );
            icover.setOnLongClickListener( new View.OnLongClickListener()
            {

                public boolean onLongClick( View v )
                {
                    listener.coverLongTouch( song, position );
                    return false;
                }
            } );

            final int fposition = position;
            final View fCounterView = counterView;

            Bitmap bm = AlbumCoverCache.getCover( song.getAlbum().getSid(), new AlbumCoverCache.CoverListener()
            {
                public void setCover( final Bitmap bitmap )
                {
                    // just checking if the view is yet the hoped view (and haven't been recycled)
                    if ( ( (Integer) fCounterView.getTag() ) == fposition )
                    {
                        activity.runOnUiThread( new Runnable()
                        {

                            public void run()
                            {
                                ficover.setImageBitmap( bitmap );
                                ficover.invalidate();
                            }
                        } );
                    }
                }

                public void failed( Exception e )
                {
                    Log.e( "SongAdapter!", e.getMessage(), e );
                }
            } );
            if ( bm != null )
            {
                icover.setImageBitmap( bm );
            }
        }

        counterView.setTag( position );

        if ( type == SongAdapterType.detailed )
        {
            ImageView ivremove = (ImageView) counterView.findViewById( R.id.songdetailed_ivremove );
            ivremove.setOnClickListener( new View.OnClickListener()
            {

                public void onClick( View v )
                {
                    listener.elementRemove( song, position );
                }
            } );
        }

        View v = counterView.findViewById( R.id.songdetailed_rlbase );
        if ( v != null && position == getCurrentSong() )
        {
            v.setBackgroundColor( 0x66FF0000 );
        }
        else if ( v != null )
        {
            v.setBackgroundColor( 0x00FFFFFF );
        }

        return counterView;
    }

    /**
     * @return the currentSong
     */
    public int getCurrentSong()
    {
        return currentSong;
    }

    /**
     * @param currentSong the currentSong to set
     */
    public void setCurrentSong( int currentSong )
    {
        this.currentSong = currentSong;
    }
}
