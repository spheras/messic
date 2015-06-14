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
import java.util.List;

import org.messic.android.activities.adapters.SongAdapter;
import org.messic.android.activities.fragments.SearchFragment;
import org.messic.android.datamodel.MDMRandomList;
import org.messic.android.datamodel.MDMSong;
import org.messic.android.datamodel.dao.DAOSong;
import org.messic.android.util.UtilRestJSONClient;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

public class SearchController
{

    private SparseArray<MDMSong> searchResult = null;

    private String lastContent = null;

    private boolean flagSearching = false;

    public void getSearchMusic( final SongAdapter adapter, final Activity activity, final SearchFragment rf,
                                boolean refresh, final SwipeRefreshLayout srl, String searchContent )
    {
        if ( searchResult == null || searchResult.size() == 0
            || ( refresh && ( lastContent != null && lastContent.equals( searchContent ) ) ) )
        {
            if ( searchContent != null )
            {
                lastContent = searchContent;
            }
            if ( Configuration.isOffline() )
            {
                searchOffline( adapter, activity, rf, srl, searchContent );
            }
            else
            {
                searchOnline( adapter, activity, rf, srl, searchContent );
            }
        }
        else
        {
            if ( searchResult != null )
            {
                refreshData( adapter, activity, rf, srl );
            }
        }
    }

    private void searchOffline( final SongAdapter adapter, final Activity activity, final SearchFragment rf,
                                final SwipeRefreshLayout srl, final String searchContent )
    {
        if ( flagSearching )
        {
            return;
        }

        AsyncTask<Void, MDMSong, Void> at = new AsyncTask<Void, MDMSong, Void>()
        {
            private DAOSong.SongPublisher p = new DAOSong.SongPublisher()
            {

                public void publish( MDMSong song )
                {
                    publishProgress( song );
                }
            };

            @Override
            protected void onProgressUpdate( MDMSong... values )
            {
                super.onProgressUpdate( values );
                boolean flagUpdated = false;
                for ( MDMSong song : values )
                {
                    if ( searchResult == null )
                    {
                        searchResult = new SparseArray<MDMSong>();
                    }
                    if ( searchResult.get( song.getLsid() ) == null )
                    {
                        searchResult.put( song.getLsid(), song );
                        adapter.addSong( song );
                        flagUpdated = true;
                    }
                }

                if ( flagUpdated )
                {
                    rf.eventRandomInfoLoaded();
                    activity.runOnUiThread( new Runnable()
                    {
                        public void run()
                        {
                            adapter.notifyDataSetChanged();
                        }
                    } );

                }
            }

            @Override
            protected Void doInBackground( Void... params )
            {
                flagSearching = true;
                List<MDMSong> songs = new ArrayList<MDMSong>();
                DAOSong daoSong = new DAOSong( activity );
                songs = daoSong.searchByAuthor( searchContent, p );
                songs.addAll( daoSong.searchByAlbum( searchContent, p ) );
                songs.addAll( daoSong.searchByName( searchContent, p ) );

                flagSearching = false;
                // Toast.makeText( activity, "Search Finished:" + searchResult.size(), Toast.LENGTH_LONG ).show();
                return null;
            }

        };
        at.execute();
    }

    private void searchOnline( final SongAdapter adapter, final Activity activity, final SearchFragment rf,
                               final SwipeRefreshLayout srl, String searchContent )
    {
        if ( flagSearching )
        {
            return;
        }

        final String baseURL =
            Configuration.getBaseUrl() + "/services/search?content=" + lastContent + "&messic_token="
                + Configuration.getLastToken();
        flagSearching = true;
        UtilRestJSONClient.get( baseURL, MDMRandomList.class, new UtilRestJSONClient.RestListener<MDMRandomList>()
        {
            public void response( MDMRandomList response )
            {
                if ( searchResult == null )
                {
                    searchResult = new SparseArray<MDMSong>();
                }
                List<MDMSong> songs = response.getSongs();
                for ( int i = 0; i < songs.size(); i++ )
                {
                    MDMSong song = songs.get( i );
                    searchResult.put( i, song );
                }
                refreshData( adapter, activity, rf, srl );
                flagSearching = false;
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
                flagSearching = false;
            }

        } );
    }

    private void refreshData( final SongAdapter adapter, final Activity activity, final SearchFragment rf,
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

        for ( int i = 0; i < searchResult.size(); i++ )
        {
            MDMSong song = searchResult.get( i );
            adapter.addSong( song );
        }
        rf.eventRandomInfoLoaded();
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
