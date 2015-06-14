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
import org.messic.android.controllers.AlbumController;
import org.messic.android.controllers.RandomController;
import org.messic.android.datamodel.MDMPlaylist;
import org.messic.android.datamodel.MDMSong;
import org.messic.android.util.UtilMusicPlayer;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

public class RandomFragment
    extends Fragment
    implements TitleFragment
{
    private RandomController controller = new RandomController();

    private SongAdapter sa = null;

    private String title;

    public RandomFragment( String title )
    {
        super();
        this.title = title;
    }

    public RandomFragment()
    {
        super();
        this.title = "";
    }

    public String getTitle()
    {
        return this.title;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        getActivity().findViewById( R.id.random_progress ).setVisibility( View.VISIBLE );
        controller.getRandomMusic( sa, getActivity(), this, false, null );
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        View rootView = inflater.inflate( R.layout.fragment_random, container, false );

        UtilMusicPlayer.getMessicPlayerService( getActivity() );

        ListView gv = (ListView) rootView.findViewById( R.id.random_gvitems );
        sa = new SongAdapter( getActivity(), new SongAdapter.EventListener()
        {

            public void textTouch( MDMSong song, int index )
            {
                AlbumController.getAlbumInfoOnline( RandomFragment.this.getActivity(), song.getAlbum().getSid() );
            }

            public void coverTouch( MDMSong song, int index )
            {
                UtilMusicPlayer.addSong( getActivity(), song );
                Toast.makeText( getActivity(), getResources().getText( R.string.player_added ) + song.getName(),
                                Toast.LENGTH_SHORT ).show();
            }

            public void coverLongTouch( MDMSong song, int index )
            {
                UtilMusicPlayer.addSongAndPlay( getActivity(), song );
            }

            public void elementRemove( MDMSong song, int index )
            {
                // TODO Auto-generated method stub

            }

            public void playlistTouch( MDMPlaylist playlist, int index )
            {
                // TODO Auto-generated method stub

            }
        } );
        gv.setAdapter( sa );

        final SwipeRefreshLayout srl = (SwipeRefreshLayout) rootView.findViewById( R.id.random_swipe );
        srl.setColorSchemeColors( Color.RED, Color.GREEN, Color.BLUE, Color.CYAN );

        srl.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener()
        {
            public void onRefresh()
            {
                new Handler().post( new Runnable()
                {
                    public void run()
                    {
                        if ( getActivity() != null )
                        {
                            View rp = getActivity().findViewById( R.id.random_progress );
                            if ( rp != null )
                            {
                                rp.setVisibility( View.VISIBLE );
                                controller.getRandomMusic( sa, getActivity(), RandomFragment.this, true, srl );
                            }
                        }
                    }
                } );
            }
        } );

        return rootView;
    }

    /**
     * The info have been loaded, and we need to remove the progress component
     */
    public void eventRandomInfoLoaded()
    {
        if ( getActivity() != null )
        {
            getActivity().runOnUiThread( new Runnable()
            {

                public void run()
                {
                    if ( getActivity() != null )
                    {
                        View rp = getActivity().findViewById( R.id.random_progress );
                        if ( rp != null )
                        {
                            rp.setVisibility( View.GONE );
                        }
                    }
                }
            } );

        }
    }

}
