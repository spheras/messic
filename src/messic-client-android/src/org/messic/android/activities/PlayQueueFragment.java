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

import org.messic.android.R;
import org.messic.android.controllers.QueueController;
import org.messic.android.datamodel.MDMSong;
import org.messic.android.player.MessicPlayerService;
import org.messic.android.player.MessicPlayerService.MusicBinder;
import org.messic.android.player.PlayerEventListener;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class PlayQueueFragment
    extends Fragment
{
    private QueueController controller = new QueueController();

    private Intent playIntent;

    private MessicPlayerService musicSrv;

    private boolean musicBound = false;

    private SongAdapter sa = null;

    @Override
    public void onStart()
    {
        super.onStart();
        getActivity().findViewById( R.id.queue_progress ).setVisibility( View.VISIBLE );
        update();
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        View rootView = inflater.inflate( R.layout.fragment_queue, container, false );

        getMessicService();

        ListView gv = (ListView) rootView.findViewById( R.id.queue_lvitems );
        sa = new SongAdapter( getActivity(), new SongAdapter.EventListener()
        {

            public void textTouch( MDMSong song, int index )
            {
                // TODO Auto-generated method stub
            }

            public void coverTouch( MDMSong song, int index )
            {
                musicSrv.getPlayer().setSong( index );
                musicSrv.getPlayer().playSong();
            }

            public void coverLongTouch( MDMSong song, int index )
            {
                musicSrv.getPlayer().setSong( index );
                musicSrv.getPlayer().playSong();
            }

            public void elementRemove( MDMSong song, int index )
            {
                sa.removeElement( index );
                musicSrv.getPlayer().removeSong( index );
                getActivity().runOnUiThread( new Runnable()
                {
                    public void run()
                    {
                        sa.notifyDataSetChanged();
                    }
                } );

            }
        }, true );
        gv.setAdapter( sa );

        final SwipeRefreshLayout srl = (SwipeRefreshLayout) rootView.findViewById( R.id.queue_swipe );
        srl.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener()
        {
            public void onRefresh()
            {
                new Handler().postDelayed( new Runnable()
                {
                    public void run()
                    {
                        getActivity().findViewById( R.id.queue_progress ).setVisibility( View.VISIBLE );
                        controller.getQueueSongs( sa, getActivity(), PlayQueueFragment.this, true, srl, musicSrv );
                    }
                }, 5000 );
            }
        } );

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
                getActivity().findViewById( R.id.queue_progress ).setVisibility( View.GONE );
            }
        } );
    }

    private void getMessicService()
    {
        if ( playIntent == null )
        {
            playIntent = new Intent( getActivity(), MessicPlayerService.class );
            getActivity().bindService( playIntent, musicConnection, Context.BIND_AUTO_CREATE );
            getActivity().startService( playIntent );
        }
    }

    private void update()
    {
        if ( this.musicSrv != null )
            controller.getQueueSongs( sa, getActivity(), this, false, null, musicSrv );
    }

    // connect to the service
    private ServiceConnection musicConnection = new ServiceConnection()
    {
        public void onServiceConnected( ComponentName name, IBinder service )
        {
            MusicBinder binder = (MusicBinder) service;
            // get service
            musicSrv = binder.getService();
            sa.setCurrentSong( musicSrv.getPlayer().getCursor() );
            getActivity().runOnUiThread( new Runnable()
            {
                public void run()
                {
                    sa.notifyDataSetChanged();
                }
            } );

            musicSrv.getPlayer().addListener( new PlayerEventListener()
            {
                public void playing( MDMSong song, boolean resumed, int index )
                {
                    sa.setCurrentSong( index );
                    if ( !resumed )
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

                public void paused( MDMSong song, int index )
                {
                }

                public void completed( int index )
                {
                }
            } );
            // pass list
            // musicSrv.setList( songList );
            musicBound = true;
            update();
        }

        public void onServiceDisconnected( ComponentName name )
        {
            musicBound = false;
        }
    };
}
