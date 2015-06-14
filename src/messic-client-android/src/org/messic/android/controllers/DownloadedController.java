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

import org.messic.android.activities.adapters.AlbumAdapter;
import org.messic.android.activities.fragments.DownloadedFragment;
import org.messic.android.datamodel.MDMAlbum;
import org.messic.android.datamodel.dao.DAOAlbum;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;

public class DownloadedController
{
    private static List<MDMAlbum> albums = new ArrayList<MDMAlbum>();

    public void getDownloadAlbums( final AlbumAdapter adapter, final Activity activity, final DownloadedFragment df,
                                   final boolean refresh, final SwipeRefreshLayout srl )
    {
        if ( albums == null || albums.size() == 0 || refresh )
        {
            albums = new ArrayList<MDMAlbum>();
            adapter.clear();
            activity.runOnUiThread( new Runnable()
            {
                public void run()
                {
                    adapter.notifyDataSetChanged();
                }
            } );

            AsyncTask<Void, MDMAlbum, Void> at = new AsyncTask<Void, MDMAlbum, Void>()
            {
                private DAOAlbum.AlbumPublisher p = new DAOAlbum.AlbumPublisher()
                {
                    public void publish( MDMAlbum album )
                    {
                        publishProgress( album );
                    }
                };

                @Override
                protected void onProgressUpdate( MDMAlbum... values )
                {
                    super.onProgressUpdate( values );
                    for ( MDMAlbum mdmAlbum : values )
                    {
                        albums.add( mdmAlbum );
                        adapter.addAlbum( mdmAlbum );
                    }

                    df.eventDownloadedInfoLoaded();
                    activity.runOnUiThread( new Runnable()
                    {
                        public void run()
                        {
                            adapter.notifyDataSetChanged();
                        }
                    } );
                }

                @Override
                protected Void doInBackground( Void... arg0 )
                {
                    DAOAlbum daoAlbum = new DAOAlbum( activity );
                    List<MDMAlbum> albums = daoAlbum.getAllByAuthor( p );
                    // refreshData( albums, adapter, activity, df, srl );
                    if ( srl != null )
                        srl.setRefreshing( false );

                    df.eventDownloadedInfoLoaded();

                    return null;
                }

            };
            at.execute();
        }
        else
        {
            if ( albums != null )
            {
                refreshData( albums, adapter, activity, df, srl );
            }
        }
    }

    private void refreshData( List<MDMAlbum> response, final AlbumAdapter adapter, final Activity activity,
                              final DownloadedFragment df, final SwipeRefreshLayout srl )
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

        df.eventDownloadedInfoLoaded();
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
