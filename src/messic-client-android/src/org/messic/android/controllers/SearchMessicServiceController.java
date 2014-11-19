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

import org.messic.android.activities.adapters.SearchMessicServiceAdapter;
import org.messic.android.controllers.messicdiscovering.MessicDiscovering;
import org.messic.android.datamodel.MDMMessicServerInstance;
import org.messic.android.datamodel.dao.DAOServerInstance;

import android.content.Context;
import android.os.AsyncTask;

public class SearchMessicServiceController
{

    private MessicDiscovering service = null;

    public interface SearchListener
    {
        void messicServiceFound( MDMMessicServerInstance md );
    }

    /**
     * Fill the adapter with the stored messic services
     * 
     * @param context
     * @param adapter
     */
    public void getSavedSessions( Context context, SearchMessicServiceAdapter adapter )
    {
        DAOServerInstance dao = new DAOServerInstance( context );
        List<MDMMessicServerInstance> servers = dao.getAll();
        for ( MDMMessicServerInstance instance : servers )
        {
            adapter.addInstance( instance );
        }
    }

    /**
     * Cancel the service searching a messic service
     */
    public void cancelSearch()
    {
        if ( service != null )
        {
            service.cancel();
        }
    }

    /**
     * Function to search messic services over the network. This function sends a broadcast over the network to find any
     * messic service
     * 
     * @param sl {@link SearchListener} to search events
     */
    public void searchMessicServices( final SearchListener sl )
    {
        AsyncTask<Void, Void, Void> at = new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected Void doInBackground( Void... params )
            {
                if ( service != null )
                {
                    service.cancel();
                }
                service = new MessicDiscovering();
                service.searchMessicServices( sl );
                return null;
            }
        };
        at.execute();
    }

}
