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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.messic.android.activities.adapters.AlbumAdapter;
import org.messic.android.activities.fragments.ExploreFragment;
import org.messic.android.datamodel.MDMAlbum;
import org.messic.android.util.UtilRestJSONClient;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.Toast;

public class ExploreController
{
    private static List<MDMAlbum> albums = new ArrayList<MDMAlbum>();
    public static boolean downloading=false;

    public void getExploreAlbums( final AlbumAdapter adapter, final Activity activity, final ExploreFragment rf,
                                  final boolean refresh, final SwipeRefreshLayout srl, int from, int max )
    {
        if ( albums == null || refresh || ( from + max ) >= albums.size() )
        {
            final String baseURL =
                Configuration.getBaseUrl() + "/services/albums?pageFromResult=" + from + "&pageMaxResults=" + max
                    + "&songsInfo=true&authorInfo=true&orderDesc=false&orderByAuthor=true&messic_token=" + Configuration.getLastToken();
            downloading=true;
            UtilRestJSONClient.get( baseURL, MDMAlbum[].class, new UtilRestJSONClient.RestListener<MDMAlbum[]>()
            {
                public void response( MDMAlbum[] response )
                {
                    if ( refresh )
                    {
                        albums = new ArrayList<MDMAlbum>();
                    }
                    List<MDMAlbum> newList = Arrays.asList( response );
                    albums.addAll( newList );
                    refreshData( albums, adapter, activity, rf, srl );
                    downloading=false;
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
                    downloading=false;
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

    private void refreshData( List<MDMAlbum> response, final AlbumAdapter adapter, final Activity activity,
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

        adapter.setAlbums( response );

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
