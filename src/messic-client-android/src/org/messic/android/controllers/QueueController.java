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

import org.messic.android.activities.adapters.SongAdapter;
import org.messic.android.activities.fragments.PlayQueueFragment;
import org.messic.android.datamodel.MDMSong;
import org.messic.android.player.MessicPlayerService;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;

public class QueueController
{

    public void getQueueSongs( final SongAdapter adapter, final Activity activity, final PlayQueueFragment rf,
                               boolean refresh, final SwipeRefreshLayout srl, MessicPlayerService mps )
    {
        List<MDMSong> queue = mps.getPlayer().getQueue();

        adapter.clear();
        activity.runOnUiThread( new Runnable()
        {
            public void run()
            {
                adapter.notifyDataSetChanged();
            }
        } );

        for ( int i = 0; i < queue.size(); i++ )
        {
            MDMSong song = queue.get( i );
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
