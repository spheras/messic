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
import org.messic.android.datamodel.MDMAlbum;
import org.messic.android.util.AlbumCoverCache;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AlbumAdapter
    extends BaseAdapter
{
    private List<MDMAlbum> albums = new ArrayList<MDMAlbum>();

    private LayoutInflater inflater = null;

    private Activity activity = null;

    private EventListener listener = null;

    public interface EventListener
    {
        void coverTouch( MDMAlbum song );

        void coverLongTouch( MDMAlbum song );

        void textTouch( MDMAlbum song );
    }

    public AlbumAdapter( Activity activity, EventListener listener )
    {
        this.inflater = LayoutInflater.from( activity );
        this.activity = activity;
        this.listener = listener;
    }

    public void clear()
    {
        albums = new ArrayList<MDMAlbum>();
    }

    public int getCount()
    {
        return albums.size();
    }

    public Object getItem( int arg0 )
    {
        return this.albums.get( arg0 );
    }

    public long getItemId( int arg0 )
    {
        return arg0;
    }

    public void addAlbum( MDMAlbum album )
    {
        this.albums.add( album );
    }

    @SuppressLint( "InflateParams" )
    public View getView( int position, View counterView, ViewGroup parent )
    {
        if ( counterView == null )
        {
            counterView = this.inflater.inflate( R.layout.album, null );
        }

        final MDMAlbum album = this.albums.get( position );

        final ImageView icover = (ImageView) counterView.findViewById( R.id.album_icover );
        TextView tauthor = (TextView) counterView.findViewById( R.id.album_tauthor );
        TextView talbum = (TextView) counterView.findViewById( R.id.album_talbum );
        tauthor.setText( album.getAuthor().getName() );
        talbum.setText( album.getName() );
        talbum.setPaintFlags( talbum.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG );
        icover.setImageResource( android.R.color.white );
        counterView.setTag( position );
        final int fposition = position;
        final View fCounterView = counterView;

        tauthor.setOnClickListener( new View.OnClickListener()
        {
            public void onClick( View v )
            {
                listener.textTouch( album );
            }
        } );
        talbum.setOnClickListener( new View.OnClickListener()
        {
            public void onClick( View v )
            {
                listener.textTouch( album );
            }
        } );

        icover.setOnClickListener( new View.OnClickListener()
        {

            public void onClick( View v )
            {
                listener.coverTouch( album );
            }
        } );
        icover.setOnLongClickListener( new View.OnLongClickListener()
        {

            public boolean onLongClick( View v )
            {
                listener.coverLongTouch( album );
                return false;
            }
        } );

        Bitmap bm = AlbumCoverCache.getCover( album.getSid(), new AlbumCoverCache.CoverListener()
        {
            public void setCover( final Bitmap bitmap )
            {
                // just checking if the view is yet the hoped view (and haven't been recycled)
                if ( ( (Integer) fCounterView.getTag() ) == fposition )
                {
                    activity.runOnUiThread( new Runnable()
                    {

                        public void run()
                        {
                            icover.setImageBitmap( bitmap );
                            icover.invalidate();
                        }
                    } );
                }
            }

            public void failed( Exception e )
            {
                Log.e( "AlbumAdapter!", e.getMessage(), e );
            }
        } );
        if ( bm != null )
        {
            icover.setImageBitmap( bm );
        }

        return counterView;
    }
}
