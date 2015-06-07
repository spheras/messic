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

import java.util.List;

import org.messic.android.R;
import org.messic.android.activities.adapters.AlbumAdapter;
import org.messic.android.controllers.AlbumController;
import org.messic.android.controllers.ExploreController;
import org.messic.android.datamodel.MDMAlbum;
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
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

public class ExploreFragment
    extends Fragment
{
    private ExploreController controller = new ExploreController();

    private AlbumAdapter sa = null;

    @Override
    public void onStart()
    {
        super.onStart();
        getActivity().findViewById( R.id.explore_progress ).setVisibility( View.VISIBLE );
        controller.getExploreAlbums( sa, getActivity(), this, false, null, 0, 10 );
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        View rootView = inflater.inflate( R.layout.fragment_explore, container, false );

        getMessicService();

        ListView gv = (ListView) rootView.findViewById( R.id.explore_lvitems );

        sa = new AlbumAdapter( getActivity(), new AlbumAdapter.EventListener()
        {

            public void textTouch( MDMAlbum album )
            {
                // AlbumController.getAlbumInfo( ExploreFragment.this.getActivity(), album.getSid() );
                AlbumController.getAlbumInfoOffline( ExploreFragment.this.getActivity(), album );
            }

            public void coverTouch( MDMAlbum album )
            {
                UtilMusicPlayer.addAlbum( getActivity(), album );
                Toast.makeText( getActivity(), getResources().getText( R.string.player_added ) + album.getName(),
                                Toast.LENGTH_SHORT ).show();
            }

            public void coverLongTouch( MDMAlbum album )
            {
                List<MDMSong> songs = album.getSongs();
                for ( int i = 0; i < songs.size(); i++ )
                {
                    MDMSong song = songs.get( i );
                    song.setAlbum( album );
                    UtilMusicPlayer.addSong( getActivity(), song );
                }
            }
        } );
        gv.setAdapter( sa );

        gv.setOnScrollListener( new AbsListView.OnScrollListener()
        {

            public void onScrollStateChanged( AbsListView view, int scrollState )
            {
            }

            public void onScroll( AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount )
            {
                if ( totalItemCount > 0 && !ExploreController.downloading )
                {
                    int lastInScreen = firstVisibleItem + visibleItemCount;
                    if ( lastInScreen == totalItemCount )
                    {
                        getActivity().findViewById( R.id.explore_progress ).setVisibility( View.VISIBLE );
                        controller.getExploreAlbums( sa, getActivity(), ExploreFragment.this, false, null,
                                                     totalItemCount, visibleItemCount * 2 );
                    }
                }
            }
        } );

        final SwipeRefreshLayout srl = (SwipeRefreshLayout) rootView.findViewById( R.id.explore_swipe );
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
                            View v = getActivity().findViewById( R.id.explore_progress );
                            if ( v != null )
                            {
                                v.setVisibility( View.VISIBLE );
                                controller.getExploreAlbums( sa, getActivity(), ExploreFragment.this, true, srl, 0, 10 );
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
    public void eventExploreInfoLoaded()
    {
        getActivity().runOnUiThread( new Runnable()
        {

            public void run()
            {
                if ( getActivity() != null )
                {
                    View progress = getActivity().findViewById( R.id.explore_progress );
                    if ( progress != null )
                    {
                        progress.setVisibility( View.GONE );
                    }
                }
            }
        } );
    }

    private void getMessicService()
    {
        UtilMusicPlayer.getMessicPlayerService( getActivity() );
    }

}
