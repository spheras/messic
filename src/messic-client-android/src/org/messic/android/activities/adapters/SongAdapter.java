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
package org.messic.android.activities.adapters;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.messic.android.R;
import org.messic.android.controllers.Configuration;
import org.messic.android.datamodel.MDMPlaylist;
import org.messic.android.datamodel.MDMSong;
import org.messic.android.download.DownloadListener;
import org.messic.android.util.AlbumCoverCache;
import org.messic.android.util.UtilDownloadService;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

    private Animation anim = null;

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
        this.anim = AnimationUtils.loadAnimation( activity, android.R.anim.fade_in );
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
        if ( this.songs.size() > index )
        {
            this.songs.remove( index );
        }
    }

    @SuppressLint( "InflateParams" )
    public View getView( final int position, View counterView, ViewGroup parent )
    {
        if ( counterView == null )
        {
            switch ( type )
            {
                case cover:
                    counterView = this.inflater.inflate( R.layout.songtrack_detailed, null );
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

        boolean playableSong =
            ( ( Configuration.isOffline() && song.isDownloaded( activity ) ) || !Configuration.isOffline() );

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
            tsongname = (TextView) counterView.findViewById( R.id.songdetaileddd_tsong );
        }
        else if ( type == SongAdapterType.track )
        {
            tsongname = (TextView) counterView.findViewById( R.id.songtrack_tsongname );
            tsongtrack = (TextView) counterView.findViewById( R.id.songtrack_ttrack );
            final ImageView ivplay = (ImageView) counterView.findViewById( R.id.songtrack_ivplay );

            if ( playableSong )
            {
                ivplay.setOnClickListener( new View.OnClickListener()
                {
                    public void onClick( View v )
                    {
                        ivplay.startAnimation( anim );
                        listener.coverTouch( song, position );
                    }
                } );
                counterView.findViewById( R.id.songtrack_ivplay ).setVisibility( View.VISIBLE );
            }
            else
            {
                counterView.findViewById( R.id.songtrack_ivplay ).setVisibility( View.GONE );
            }

            if ( !Configuration.isOffline() )
            {
                ImageView ivdownload = (ImageView) counterView.findViewById( R.id.songtrack_ivdownload );
                ImageView ivremove = (ImageView) counterView.findViewById( R.id.songtrack_ivremove );
                if ( song.isDownloaded( activity ) )
                {
                    ivdownload.setVisibility( View.GONE );
                    ivremove.setVisibility( View.VISIBLE );
                    counterView.setBackgroundResource( R.color.dowloaded_song_background );
                    ivremove.setOnClickListener( new View.OnClickListener()
                    {
                        public void onClick( View v )
                        {
                            listener.elementRemove( song, position );
                        }
                    } );
                }
                else
                {
                    ivdownload.setVisibility( View.VISIBLE );
                    ivremove.setVisibility( View.GONE );
                    counterView.setBackgroundResource( R.color.dowloaded_false_song_background );
                    ivdownload.setOnClickListener( new View.OnClickListener()
                    {
                        public void onClick( View v )
                        {
                            UtilDownloadService.addDownload( SongAdapter.this.activity, song, new DownloadListener()
                            {
                                public void downloadUpdated( MDMSong song, float percent )
                                {
                                }

                                public void downloadStarted( MDMSong song )
                                {
                                }

                                public void downloadFinished( MDMSong song, File fdownloaded )
                                {
                                    activity.runOnUiThread( new Runnable()
                                    {

                                        public void run()
                                        {
                                            SongAdapter.this.notifyDataSetChanged();
                                        }
                                    } );
                                }

                                public void downloadAdded( MDMSong song )
                                {
                                }

                                public void disconnected()
                                {
                                }

                                public void connected()
                                {
                                }
                            } );
                        }
                    } );
                }
            }
            else
            {
                counterView.findViewById( R.id.songtrack_ivdownload ).setVisibility( View.GONE );
                if ( playableSong )
                {
                    counterView.findViewById( R.id.songtrack_ivremove ).setVisibility( View.VISIBLE );
                }
                else
                {
                    counterView.findViewById( R.id.songtrack_ivremove ).setVisibility( View.GONE );
                }
            }
        }

        final ImageView ficover = icover;

        if ( tauthor != null )
        {
            tauthor.setText( song.getAlbum().getAuthor().getName() );
            if ( playableSong )
            {
                tauthor.setOnClickListener( new View.OnClickListener()
                {
                    public void onClick( View v )
                    {
                        listener.textTouch( song, position );
                    }
                } );
            }
        }
        if ( talbum != null )
        {
            talbum.setText( song.getAlbum().getName() );
            if ( playableSong )
            {
                talbum.setPaintFlags( talbum.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG );
                talbum.setOnClickListener( new View.OnClickListener()
                {
                    public void onClick( View v )
                    {
                        listener.textTouch( song, position );
                    }
                } );
            }
        }
        if ( tsongname != null )
        {
            if ( type == SongAdapterType.cover )
            {
                tsongname.setText( song.getTrack() + "-" + song.getName() );
            }
            else
            {
                tsongname.setText( song.getName() );
            }
            if ( playableSong )
            {
                tsongname.setOnClickListener( new View.OnClickListener()
                {
                    public void onClick( View v )
                    {
                        listener.textTouch( song, position );
                    }
                } );
            }
        }
        if ( tsongtrack != null )
        {
            tsongtrack.setText( "" + song.getTrack() );
            if ( playableSong )
            {
                tsongtrack.setOnClickListener( new View.OnClickListener()
                {
                    public void onClick( View v )
                    {
                        listener.textTouch( song, position );
                    }
                } );
            }
        }
        if ( icover != null )
        {
            View.OnClickListener playListener = new View.OnClickListener()
            {

                public void onClick( View v )
                {
                    ficover.startAnimation( anim );
                    listener.coverTouch( song, position );
                }
            };
            icover.setImageResource( android.R.color.white );
            icover.setOnClickListener( playListener );
            final View vtemp = counterView.findViewById( R.id.songtrack_detailed_playcontent );
            if ( vtemp != null )
            {
                vtemp.setOnClickListener( playListener );
            }

            icover.setOnLongClickListener( new View.OnLongClickListener()
            {

                public boolean onLongClick( View v )
                {
                    ficover.startAnimation( anim );
                    listener.coverLongTouch( song, position );
                    return false;
                }
            } );

            final int fposition = position;
            final View fCounterView = counterView;

            Bitmap bm = AlbumCoverCache.getCover( song.getAlbum(), new AlbumCoverCache.CoverListener()
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
        if ( type == SongAdapterType.track )
        {
            ImageView ivremove = (ImageView) counterView.findViewById( R.id.songtrack_ivremove );
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
            v.setBackgroundResource( R.color.queue_current_song_background );
        }
        else if ( v != null )
        {
            v.setBackgroundResource( R.drawable.listitem_gradient );
        }

        if ( !playableSong )
        {
            counterView.setAlpha( 0.4f );
            counterView.setEnabled( false );
        }
        else
        {
            counterView.setAlpha( 1f );
            counterView.setEnabled( true );
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
