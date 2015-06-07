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
package org.messic.android.activities.fragments;

import org.messic.android.R;
import org.messic.android.activities.adapters.SongAdapter;
import org.messic.android.activities.adapters.SongAdapter.SongAdapterType;
import org.messic.android.controllers.AlbumController;
import org.messic.android.controllers.Configuration;
import org.messic.android.controllers.QueueController;
import org.messic.android.datamodel.MDMAlbum;
import org.messic.android.datamodel.MDMPlaylist;
import org.messic.android.datamodel.MDMSong;
import org.messic.android.player.PlayerEventListener;
import org.messic.android.util.UtilMusicPlayer;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class PlayQueueFragment
    extends Fragment
    implements PlayerEventListener
{
    private QueueController controller = new QueueController();

    private SongAdapter sa = null;

    @Override
    public void onStart()
    {
        super.onStart();
        controller.getQueueSongs( sa, getActivity(), this, false, null );
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        View rootView = inflater.inflate( R.layout.fragment_queue, container, false );

        ListView gv = (ListView) rootView.findViewById( R.id.queue_lvitems );
        if ( sa == null )
        {
            sa = new SongAdapter( getActivity(), new SongAdapter.EventListener()
            {

                public void textTouch( MDMSong song, int index )
                {
                    if ( Configuration.isOffline() )
                    {
                        AlbumController.getAlbumInfoOffline( PlayQueueFragment.this.getActivity(), song.getAlbum() );
                    }
                    else
                    {
                        AlbumController.getAlbumInfoOnline( PlayQueueFragment.this.getActivity(),
                                                            song.getAlbum().getSid() );
                    }
                }

                public void coverTouch( MDMSong song, int index )
                {
                    UtilMusicPlayer.setSong( PlayQueueFragment.this.getActivity(), index );
                    UtilMusicPlayer.playSong( PlayQueueFragment.this.getActivity() );
                }

                public void coverLongTouch( MDMSong song, int index )
                {
                    UtilMusicPlayer.setSong( PlayQueueFragment.this.getActivity(), index );
                    UtilMusicPlayer.playSong( PlayQueueFragment.this.getActivity() );
                }

                public void elementRemove( MDMSong song, int index )
                {
                    sa.removeElement( index );
                    UtilMusicPlayer.removeSong( PlayQueueFragment.this.getActivity(), index );
                    if ( index < sa.getCurrentSong() )
                    {
                        sa.setCurrentSong( sa.getCurrentSong() - 1 );
                    }

                    getActivity().runOnUiThread( new Runnable()
                    {
                        public void run()
                        {
                            sa.notifyDataSetChanged();
                        }
                    } );

                }

                public void playlistTouch( MDMPlaylist playlist, int index )
                {
                    // TODO Auto-generated method stub

                }
            }, SongAdapterType.detailed );

            sa.setCurrentSong( UtilMusicPlayer.getCursor( this.getActivity() ) );
        }
        gv.setAdapter( sa );

        final SwipeRefreshLayout srl = (SwipeRefreshLayout) rootView.findViewById( R.id.queue_swipe );
        srl.setColorSchemeColors( Color.RED, Color.GREEN, Color.BLUE, Color.CYAN );
        srl.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener()
        {
            public void onRefresh()
            {
                new Handler().post( new Runnable()
                {
                    public void run()
                    {
                        getActivity().findViewById( R.id.queue_progress ).setVisibility( View.VISIBLE );
                        controller.getQueueSongs( sa, getActivity(), PlayQueueFragment.this, true, srl );
                    }
                } );
            }
        } );

        UtilMusicPlayer.addListener( this.getActivity(), this );

        return rootView;
    }

    /**
     * The info have been loaded, and we need to remove the progress component
     */
    public void eventRandomInfoLoaded()
    {
        getActivity().runOnUiThread( new Runnable()
        {

            public void run()
            {
                Activity activity = getActivity();
                if ( activity != null )
                {
                    View qp = activity.findViewById( R.id.queue_progress );
                    if ( qp != null )
                    {
                        qp.setVisibility( View.GONE );
                    }
                }

            }
        } );
    }

    public void update()
    {
        controller.getQueueSongs( sa, getActivity(), this, true, null );
    }

    public void paused( MDMSong song, int index )
    {
        // TODO Auto-generated method stub

    }

    public void playing( MDMSong song, boolean resumed, int index )
    {
        sa.setCurrentSong( index );
        if ( !resumed && isVisible() )
        {
            getActivity().runOnUiThread( new Runnable()
            {
                public void run()
                {
                    sa.notifyDataSetChanged();
                }
            } );
        }
    }

    public void completed( int index )
    {
        // TODO Auto-generated method stub

    }

    public void added( MDMSong song )
    {
        update();

    }

    public void added( MDMAlbum album )
    {
        update();

    }

    public void added( MDMPlaylist playlist )
    {
        update();

    }

    public void connected()
    {

        sa.setCurrentSong( UtilMusicPlayer.getCursor( PlayQueueFragment.this.getActivity() ) );
        Activity activity = getActivity();
        if ( activity != null )
        {
            activity.runOnUiThread( new Runnable()
            {
                public void run()
                {
                    sa.notifyDataSetChanged();
                }
            } );
        }

        update();
    }

    public void disconnected()
    {
        // TODO Auto-generated method stub

    }
}
