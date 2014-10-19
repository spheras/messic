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
package org.messic.android.activities;

import java.util.ArrayList;
import java.util.List;
import org.messic.android.R;
import org.messic.android.controllers.messicdiscovering.MessicServerInstance;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SearchMessicServiceAdapter
    extends BaseAdapter
{
    private List<MessicServerInstance> instances = new ArrayList<MessicServerInstance>();

    private LayoutInflater inflater = null;

    public SearchMessicServiceAdapter( Context context )
    {
        this.inflater = LayoutInflater.from( context );
    }

    public void clear(){
        instances=new ArrayList<MessicServerInstance>();
    }
    
    public int getCount()
    {
        return instances.size();
    }

    public Object getItem( int arg0 )
    {
        return this.instances.get( arg0 );
    }

    public long getItemId( int arg0 )
    {
        return arg0;
    }

    public void addInstance( MessicServerInstance instance )
    {
        this.instances.add( instance );
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
        MessicServerInstance msi = this.instances.get( position );
        hostname.setText( msi.hostname );
        ip.setText( msi.ip );
        version.setText( msi.version );
        version.setTag( position );

        return counterView;
    }

}
