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

import org.messic.android.activities.adapters.AlbumAdapter;
import org.messic.android.activities.fragments.ExploreFragment;
import org.messic.android.datamodel.MDMAlbum;
import org.messic.android.util.RestJSONClient;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.Toast;

public class ExploreController
{
    private static MDMAlbum[] albums = null;

    public void getExploreAlbums( final AlbumAdapter adapter, final Activity activity, final ExploreFragment rf,
                                  boolean refresh, final SwipeRefreshLayout srl )
    {
        if ( albums == null || refresh )
        {
            final String baseURL =
                Configuration.getBaseUrl() + "/services/albums?songsInfo=true&authorInfo=true&messic_token="
                    + Configuration.getLastToken();
            RestJSONClient.get( baseURL, MDMAlbum[].class, new RestJSONClient.RestListener<MDMAlbum[]>()
            {
                public void response( MDMAlbum[] response )
                {
                    albums = response;
                    refreshData( response, adapter, activity, rf, srl );
                }

                public void fail( final Exception e )
                {
                    Log.e( "Random", e.getMessage(), e );
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
            if ( albums != null )
            {
                refreshData( albums, adapter, activity, rf, srl );
            }
        }
    }

    private void refreshData( MDMAlbum[] response, final AlbumAdapter adapter, final Activity activity,
                              final ExploreFragment rf, final SwipeRefreshLayout srl )
    {
        adapter.clear();
        activity.runOnUiThread( new Runnable()
        {
            public void run()
            {
                adapter.notifyDataSetChanged();
            }
        } );

        for ( int i = 0; i < response.length; i++ )
        {
            adapter.addAlbum( response[i] );
        }

        rf.eventExploreInfoLoaded();
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
