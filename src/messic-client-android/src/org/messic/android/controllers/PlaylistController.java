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
package org.messic.android.controllers;

import org.messic.android.activities.adapters.PlaylistAdapter;
import org.messic.android.activities.fragments.PlaylistFragment;
import org.messic.android.datamodel.MDMPlaylist;
import org.messic.android.util.RestJSONClient;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.Toast;

public class PlaylistController
{

    private static MDMPlaylist[] rlist = null;

    public void getPlaylistMusic( final PlaylistAdapter adapter, final Activity activity, final PlaylistFragment pf,
                                  boolean refresh, final SwipeRefreshLayout srl )
    {
        if ( rlist == null || refresh )
        {
            final String baseURL =
                Configuration.getBaseUrl() + "/services/playlists?songsInfo=true&messic_token="
                    + Configuration.getLastToken();
            RestJSONClient.get( baseURL, MDMPlaylist[].class, new RestJSONClient.RestListener<MDMPlaylist[]>()
            {
                public void response( MDMPlaylist[] response )
                {
                    rlist = response;
                    refreshData( adapter, activity, pf, srl );
                }

                public void fail( final Exception e )
                {
                    Log.e( "Playlist", e.getMessage(), e );
                    activity.runOnUiThread( new Runnable()
                    {

                        public void run()
                        {
                            Toast.makeText( activity, "Error:" + e.getMessage(), Toast.LENGTH_LONG ).show();

                        }
                    } );
                }

            } );
        }
        else
        {
            if ( rlist != null )
            {
                refreshData( adapter, activity, pf, srl );
            }
        }
    }

    private void refreshData( final PlaylistAdapter adapter, final Activity activity, final PlaylistFragment pf,
                              final SwipeRefreshLayout srl )
    {
        adapter.clear();
        activity.runOnUiThread( new Runnable()
        {
            public void run()
            {
                adapter.notifyDataSetChanged();
            }
        } );

        for ( int i = 0; i < rlist.length; i++ )
        {
            adapter.addPlaylist( rlist[i] );
        }
        pf.eventPlaylistInfoLoaded();
        activity.runOnUiThread( new Runnable()
        {
            public void run()
            {
                adapter.notifyDataSetChanged();
            }
        } );

        if ( srl != null )
            srl.setRefreshing( false );

    }
}
