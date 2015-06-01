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
package org.messic.android.activities.adapters;

import java.util.ArrayList;
import java.util.List;

import org.messic.android.R;
import org.messic.android.datamodel.MDMMessicServerInstance;
import org.messic.android.util.Network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SearchMessicServiceAdapter
    extends BaseAdapter
{
    private List<MDMMessicServerInstance> instances = new ArrayList<MDMMessicServerInstance>();

    private LayoutInflater inflater = null;

    public SearchMessicServiceAdapter( Context context )
    {
        this.inflater = LayoutInflater.from( context );
    }

    public void clear()
    {
        instances = new ArrayList<MDMMessicServerInstance>();
    }

    public int getCount()
    {
        return instances.size();
    }

    public List<MDMMessicServerInstance> getInstances()
    {
        return instances;
    }

    public Object getItem( int count )
    {
        if ( count < this.instances.size() )
        {
            return this.instances.get( count );
        }
        else
        {
            return null;
        }
    }

    public long getItemId( int arg0 )
    {
        return arg0;
    }

    public boolean existInstance( MDMMessicServerInstance instance )
    {
        for ( MDMMessicServerInstance i : instances )
        {
            if ( i.ip.equalsIgnoreCase( instance.ip ) && i.description.equalsIgnoreCase( instance.description )
                && i.name.equalsIgnoreCase( instance.name ) && i.version.equalsIgnoreCase( instance.version )
                && i.port == instance.port && i.secured == instance.secured )
            {
                return true;
            }
        }

        return false;
    }

    public void removeItem( int count )
    {
        this.instances.remove( count );
    }

    public boolean addInstance( MDMMessicServerInstance instance )
    {
        for ( int i = 0; i < instances.size(); i++ )
        {
            MDMMessicServerInstance md = instances.get( i );
            if ( md.ip.equals( instance.ip ) && md.port == instance.port && md.secured == instance.secured )
            {
                return false;
            }
        }
        this.instances.add( instance );
        return true;
    }

    @SuppressLint( "InflateParams" )
    public View getView( int position, View counterView, ViewGroup parent )
    {
        if ( counterView == null )
        {
            counterView = this.inflater.inflate( R.layout.search_messic_service_item, null );
        }

        TextView hostname = (TextView) counterView.findViewById( R.id.searchmessicservice_item_hostname );
        TextView ip = (TextView) counterView.findViewById( R.id.searchmessicservice_item_ip );
        TextView version = (TextView) counterView.findViewById( R.id.searchmessicservice_item_version );
        final View vstatus = (View) counterView.findViewById( R.id.searchmessicservice_item_hostname_vstatus );

        vstatus.setBackgroundColor( Color.YELLOW );
        final MDMMessicServerInstance msi = this.instances.get( position );
        hostname.setText( msi.name );
        ip.setText( msi.ip );
        version.setText( msi.version );
        version.setTag( position );
        final View parentView = counterView;

        // start checking the availability
        Network.MessicServerStatusListener listener = new Network.MessicServerStatusListener()
        {
            public void setResponse( final boolean reachable, final boolean running )
            {
                parentView.post( new Runnable()
                {
                    public void run()
                    {
                        if ( reachable && running )
                        {
                            msi.setLastCheckedStatus( MDMMessicServerInstance.STATUS_RUNNING );
                            vstatus.setBackgroundColor( Color.GREEN );
                        }
                        else
                        {
                            msi.setLastCheckedStatus( MDMMessicServerInstance.STATUS_DOWN );
                            vstatus.setBackgroundColor( Color.RED );
                        }
                    }
                } );
            }
        };
        Network.checkMessicServerUpAndRunning( msi, listener );

        return counterView;
    }
}
