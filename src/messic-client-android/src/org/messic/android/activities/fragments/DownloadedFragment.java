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

import java.util.ArrayList;
import java.util.List;

import org.messic.android.R;
import org.messic.android.activities.adapters.AlbumAdapter;
import org.messic.android.activities.swipedismiss.SwipeDismissListViewTouchListener;
import org.messic.android.controllers.AlbumController;
import org.messic.android.controllers.DownloadedController;
import org.messic.android.datamodel.MDMAlbum;
import org.messic.android.datamodel.MDMSong;
import org.messic.android.datamodel.dao.DAOAlbum;
import org.messic.android.util.UtilDownloadService;
import org.messic.android.util.UtilMusicPlayer;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

public class DownloadedFragment
    extends Fragment
    implements TitleFragment
{
    private DownloadedController controller = new DownloadedController();

    private AlbumAdapter sa = null;

    private String title;

    public DownloadedFragment( String title )
    {
        super();
        this.title = title;
    }

    public DownloadedFragment()
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
                AlbumController.getAlbumInfoOffline( DownloadedFragment.this.getActivity(), album );
            }

            public void coverTouch( MDMAlbum album )
            {
                addDownloadedAlbum( album );
            }

            public void coverLongTouch( MDMAlbum album )
            {
                playNowDownloadedAlbum( album );
            }

            public void moreTouch( final MDMAlbum album, View anchor, final int index )
            {
                // Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu( DownloadedFragment.this.getActivity(), anchor );

                // Inflating the Popup using xml file
                popup.getMenuInflater().inflate( R.menu.menu_album, popup.getMenu() );
                popup.getMenu().removeItem( R.id.menu_album_item_download );

                // registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener( new PopupMenu.OnMenuItemClickListener()
                {
                    public boolean onMenuItemClick( MenuItem item )
                    {
                        switch ( item.getItemId() )
                        {
                            case R.id.menu_album_item_play:
                                addDownloadedAlbum( album );
                                break;
                            case R.id.menu_album_item_playnow:
                                playNowDownloadedAlbum( album );
                                break;
                            case R.id.menu_album_item_remove:
                                removeDownloadedAlbum( album, index );
                                break;
                        }
                        return true;
                    }
                } );

                popup.show();// showing popup menu
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
        if ( getActivity() != null )
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
    }

    private void getMessicService()
    {
        UtilMusicPlayer.getMessicPlayerService( getActivity() );
    }

    /**
     * Play a downloaded album
     * 
     * @param album
     */
    private void playNowDownloadedAlbum( MDMAlbum album )
    {
        List<MDMSong> songs = album.getSongs();
        List<MDMSong> fsongs = new ArrayList<MDMSong>();
        for ( int i = 0; i < songs.size(); i++ )
        {
            if ( songs.get( i ).isDownloaded( getActivity() ) )
            {
                MDMSong song = songs.get( i );
                song.setAlbum( album );
                fsongs.add( songs.get( i ) );
            }
        }

        UtilMusicPlayer.addSongsAndPlay( getActivity(), fsongs );
    }

    private void addDownloadedAlbum( MDMAlbum album )
    {
        UtilMusicPlayer.addAlbum( getActivity(), album );
        Toast.makeText( getActivity(), getResources().getText( R.string.player_added ) + album.getName(),
                        Toast.LENGTH_SHORT ).show();

    }

    private void removeDownloadedAlbum( final MDMAlbum album, final int index )
    {

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener()
        {
            public void onClick( DialogInterface dialog, int which )
            {
                switch ( which )
                {
                    case DialogInterface.BUTTON_POSITIVE:
                        UtilDownloadService.removeAlbum( getActivity(), album );
                        sa.remove( index );
                        getActivity().runOnUiThread( new Runnable()
                        {
                            public void run()
                            {
                                sa.notifyDataSetChanged();
                            }
                        } );
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        // No button clicked
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder( getActivity() );
        builder.setMessage( getString( R.string.action_remove_local_album ) ).setPositiveButton( getString( R.string.yes ),
                                                                                                 dialogClickListener ).setNegativeButton( getString( R.string.no ),
                                                                                                                                          dialogClickListener ).show();
    }

}
