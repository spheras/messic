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
import org.messic.android.controllers.PlaylistController;
import org.messic.android.datamodel.MDMPlaylist;
import org.messic.android.datamodel.MDMSong;
import org.messic.android.player.MessicPlayerService;
import org.messic.android.player.MessicPlayerService.MusicBinder;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

public class PlaylistFragment
    extends Fragment
{
    private PlaylistController controller = new PlaylistController();

    private Intent playIntent;

    private MessicPlayerService musicSrv;

    private boolean musicBound = false;

    private PlaylistAdapter sa = null;

    @Override
    public void onStart()
    {
        super.onStart();
        getActivity().findViewById( R.id.playlist_progress ).setVisibility( View.VISIBLE );
        controller.getPlaylistMusic( sa, getActivity(), this, false, null );
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        View rootView = inflater.inflate( R.layout.fragment_playlist, container, false );

        getMessicService();

        ExpandableListView gv = (ExpandableListView) rootView.findViewById( R.id.playlist_elistview );
        if ( sa == null )
        {
            sa = new PlaylistAdapter( getActivity(), new SongAdapter.EventListener()
            {

                public void textTouch( MDMSong song, int index )
                {
                    // TODO Auto-generated method stub
                }

                public void coverTouch( MDMSong song, int index )
                {
                    musicSrv.getPlayer().addSong( song );
                    Toast.makeText( getActivity(), getResources().getText( R.string.player_added ) + song.getName(),
                                    Toast.LENGTH_SHORT ).show();
                }

                public void coverLongTouch( MDMSong song, int index )
                {
                    musicSrv.getPlayer().addAndPlay( song );
                }

                public void elementRemove( MDMSong song, int index )
                {
                    // TODO Auto-generated method stub
                }

                public void playlistTouch( MDMPlaylist playlist, int index )
                {
                    musicSrv.getPlayer().addPlaylist( playlist );
                    Toast.makeText( getActivity(),
                                    getResources().getText( R.string.player_added ) + playlist.getName(),
                                    Toast.LENGTH_SHORT ).show();
                }
            } );
        }
        gv.setAdapter( sa );

        final SwipeRefreshLayout srl = (SwipeRefreshLayout) rootView.findViewById( R.id.playlist_swipe );
        // sets the colors used in the refresh animation
        srl.setColorSchemeColors( Color.RED, Color.GREEN, Color.BLUE, Color.CYAN );
        srl.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener()
        {
            public void onRefresh()
            {
                new Handler().post( new Runnable()
                {
                    public void run()
                    {
                        getActivity().findViewById( R.id.playlist_progress ).setVisibility( View.VISIBLE );
                        controller.getPlaylistMusic( sa, getActivity(), PlaylistFragment.this, true, srl );
                    }
                } );
            }
        } );

        return rootView;
    }

    /**
     * The info have been loaded, and we need to remove the progress component
     */
    public void eventPlaylistInfoLoaded()
    {
        getActivity().runOnUiThread( new Runnable()
        {

            public void run()
            {
                if ( getActivity() != null )
                {
                    View rp = getActivity().findViewById( R.id.playlist_progress );
                    if ( rp != null )
                    {
                        rp.setVisibility( View.GONE );
                    }
                }
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

    // connect to the service
    private ServiceConnection musicConnection = new ServiceConnection()
    {
        public void onServiceConnected( ComponentName name, IBinder service )
        {
            MusicBinder binder = (MusicBinder) service;
            // get service
            musicSrv = binder.getService();
            // pass list
            // musicSrv.setList( songList );
            musicBound = true;
        }

        public void onServiceDisconnected( ComponentName name )
        {
            musicBound = false;
        }
    };
}
