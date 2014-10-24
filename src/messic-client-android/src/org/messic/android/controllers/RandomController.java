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

import java.util.List;

import org.messic.android.activities.RandomFragment;
import org.messic.android.activities.SongAdapter;
import org.messic.android.datamodel.MDMRandomList;
import org.messic.android.datamodel.MDMSong;
import org.messic.android.util.RestJSONClient;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.Toast;

public class RandomController
{

    private static MDMRandomList[] rlist = null;

    public void getRandomMusic( final SongAdapter adapter, final Activity activity, final RandomFragment rf,
                                boolean refresh, final SwipeRefreshLayout srl )
    {
        if ( rlist == null || refresh )
        {
            final String baseURL =
                Configuration.getBaseUrl()
                    + "/services/randomlists?filterRandomListName=RandomListName-Random&messic_token="
                    + Configuration.getLastToken();
            RestJSONClient.get( baseURL, MDMRandomList[].class, new RestJSONClient.RestListener<MDMRandomList[]>()
            {
                public void response( MDMRandomList[] response )
                {
                    rlist = response;
                    refreshData( adapter, activity, rf, srl );
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
            if ( rlist != null )
            {
                refreshData( adapter, activity, rf, srl );
            }
        }
    }

    private void refreshData( final SongAdapter adapter, final Activity activity, final RandomFragment rf,
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
            List<MDMSong> songs = rlist[i].getSongs();
            for ( int j = 0; j < songs.size(); j++ )
            {
                MDMSong song = songs.get( j );
                adapter.addSong( song );
            }
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
