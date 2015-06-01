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
import org.messic.android.activities.SearchMessicServiceActivity;
import org.messic.android.activities.adapters.AlbumAdapter;
import org.messic.android.activities.swipedismiss.SwipeDismissListViewTouchListener;
import org.messic.android.controllers.AlbumController;
import org.messic.android.controllers.DownloadedController;
import org.messic.android.datamodel.MDMAlbum;
import org.messic.android.datamodel.MDMMessicServerInstance;
import org.messic.android.datamodel.MDMSong;
import org.messic.android.datamodel.dao.DAOAlbum;
import org.messic.android.player.MessicPlayerService;
import org.messic.android.player.MessicPlayerService.MusicBinder;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class DownloadedFragment
    extends Fragment
{
    private DownloadedController controller = new DownloadedController();

    private Intent playIntent;

    private MessicPlayerService musicSrv;

    private boolean musicBound = false;

    private AlbumAdapter sa = null;

    @Override
    public void onStart()
    {
        super.onStart();
        getActivity().findViewById( R.id.download_progress ).setVisibility( View.VISIBLE );
        controller.getDownloadAlbums( sa, getActivity(), this, false, null );
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        View rootView = inflater.inflate( R.layout.fragment_download, container, false );

        getMessicService();

        ListView gv = (ListView) rootView.findViewById( R.id.download_lvitems );

        sa = new AlbumAdapter( getActivity(), new AlbumAdapter.EventListener()
        {

            public void textTouch( MDMAlbum album )
            {
                AlbumController.getAlbumInfo( DownloadedFragment.this.getActivity(), album );
            }

            public void coverTouch( MDMAlbum album )
            {
                musicSrv.getPlayer().addAlbum( album );
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
                    musicSrv.getPlayer().addSong( song );
                }
            }
        } );
        gv.setAdapter( sa );

        // Create a ListView-specific touch listener. ListViews are given special treatment because
        // by default they handle touches for their list items... i.e. they're in charge of drawing
        // the pressed state (the list selector), handling list item clicks, etc.
        SwipeDismissListViewTouchListener touchListener =
            new SwipeDismissListViewTouchListener( gv, new SwipeDismissListViewTouchListener.DismissCallbacks()
            {
                public boolean canDismiss( int position )
                {
                    return true;
                }

                public void onDismiss( ListView listView, int[] reverseSortedPositions )
                {
                    for ( int position : reverseSortedPositions )
                    {
                        final int pos = position;
                        AlertDialog.Builder alert = new AlertDialog.Builder( DownloadedFragment.this.getActivity() );

                        alert.setTitle( "Remove Downloaded Album" );
                        alert.setMessage( "Are you sure to remove the album? (it is only on the local device)" );

                        alert.setPositiveButton( "Ok", new DialogInterface.OnClickListener()
                        {
                            public void onClick( DialogInterface dialog, int whichButton )
                            {
                                DAOAlbum album = new DAOAlbum( DownloadedFragment.this.getActivity() );
                                album._delete( ( (MDMAlbum) sa.getItem( pos ) ).getLsid() );
                                sa.notifyDataSetChanged();
                            }
                        } );

                        alert.setNegativeButton( "Cancel", new DialogInterface.OnClickListener()
                        {
                            public void onClick( DialogInterface dialog, int whichButton )
                            {
                                // Canceled.
                            }
                        } );

                        alert.show();
                    }
                }
            } );
        gv.setOnTouchListener( touchListener );
        // Setting this scroll listener is required to ensure that during ListView scrolling,
        // we don't look for swipes.
        gv.setOnScrollListener( touchListener.makeScrollListener() );

        final SwipeRefreshLayout srl = (SwipeRefreshLayout) rootView.findViewById( R.id.download_swipe );
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
                            View v = getActivity().findViewById( R.id.download_progress );
                            if ( v != null )
                            {
                                v.setVisibility( View.VISIBLE );
                                controller.getDownloadAlbums( sa, getActivity(), DownloadedFragment.this, true, srl );
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
    public void eventDownloadedInfoLoaded()
    {
        getActivity().runOnUiThread( new Runnable()
        {

            public void run()
            {
                if ( getActivity() != null )
                {
                    View progress = getActivity().findViewById( R.id.download_progress );
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
