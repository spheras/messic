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

import org.messic.android.R;
import org.messic.android.activities.adapters.SongAdapter;
import org.messic.android.activities.adapters.SongAdapter.SongAdapterType;
import org.messic.android.controllers.AlbumController;
import org.messic.android.datamodel.MDMAlbum;
import org.messic.android.datamodel.MDMPlaylist;
import org.messic.android.datamodel.MDMSong;
import org.messic.android.player.MessicPlayerService;
import org.messic.android.player.MessicPlayerService.MusicBinder;
import org.messic.android.util.AlbumCoverCache;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AlbumInfoActivity
    extends Activity
{

    public static final String EXTRA_ALBUM_SID = "ALBUM_SID";

    private AlbumController controller = new AlbumController();

    private SongAdapter adapter;

    private MessicPlayerService musicSrv;

    private Intent playIntent;

    private MDMAlbum album;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.albuminfo );

        this.album = (MDMAlbum) getIntent().getExtras().get( EXTRA_ALBUM_SID );

        getMessicService();

        if ( adapter == null )
        {
            this.adapter = new SongAdapter( this, new SongAdapter.EventListener()
            {

                public void textTouch( MDMSong song, int index )
                {
                }

                public void playlistTouch( MDMPlaylist playlist, int index )
                {
                }

                public void elementRemove( MDMSong song, int index )
                {
                }

                public void coverTouch( MDMSong song, int index )
                {
                    musicSrv.getPlayer().addSong( song );
                    Toast.makeText( AlbumInfoActivity.this,
                                    getResources().getText( R.string.player_added ) + song.getName(),
                                    Toast.LENGTH_SHORT ).show();
                }

                public void coverLongTouch( MDMSong song, int index )
                {
                }
            }, SongAdapterType.track );
        }
        ListView lv = (ListView) findViewById( R.id.albuminfo_lvsongs );
        lv.setAdapter( adapter );

        ImageView ivcover = (ImageView) findViewById( R.id.albuminfo_icover );
        ivcover.setOnClickListener( new View.OnClickListener()
        {
            public void onClick( View v )
            {
                if ( album != null )
                {
                    musicSrv.getPlayer().addAlbum( album );
                    Toast.makeText( AlbumInfoActivity.this,
                                    getResources().getText( R.string.player_added ) + album.getName(),
                                    Toast.LENGTH_SHORT ).show();
                }
            }
        } );

        eventAlbumInfoLoaded( album );
    }

    public void eventAlbumInfoLoaded( final MDMAlbum response )
    {
        this.album = response;
        adapter.clear();
        runOnUiThread( new Runnable()
        {
            public void run()
            {
                adapter.notifyDataSetChanged();
                TextView tvauthor = (TextView) findViewById( R.id.albuminfo_tauthor );
                TextView tvalbum = (TextView) findViewById( R.id.albuminfo_talbum );
                final ImageView ivcover = (ImageView) findViewById( R.id.albuminfo_icover );

                tvauthor.setText( response.getAuthor().getName() );
                tvalbum.setText( response.getName() );
                ivcover.setImageResource( android.R.color.white );
                Bitmap bm = AlbumCoverCache.getCover( response.getSid(), new AlbumCoverCache.CoverListener()
                {
                    public void setCover( final Bitmap bitmap )
                    {
                        runOnUiThread( new Runnable()
                        {

                            public void run()
                            {
                                ivcover.setImageBitmap( bitmap );
                                ivcover.invalidate();
                            }
                        } );
                    }

                    public void failed( Exception e )
                    {
                        Log.e( "AlbumInfoActivity!", e.getMessage(), e );
                    }
                } );
                if ( bm != null )
                {
                    ivcover.setImageBitmap( bm );
                }

                for ( int i = 0; i < response.getSongs().size(); i++ )
                {
                    MDMSong song = response.getSongs().get( i );
                    song.setAlbum( response );
                    adapter.addSong( response.getSongs().get( i ) );
                }

                adapter.notifyDataSetChanged();

            }
        } );

    }

    // connect to the service
    private ServiceConnection musicConnection = new ServiceConnection()
    {
        public void onServiceConnected( ComponentName name, IBinder service )
        {
            MusicBinder binder = (MusicBinder) service;
            // get service
            musicSrv = binder.getService();
            // pass list
        }

        public void onServiceDisconnected( ComponentName name )
        {
        }
    };

    private void getMessicService()
    {
        if ( playIntent == null )
        {
            playIntent = new Intent( this, MessicPlayerService.class );
            this.bindService( playIntent, musicConnection, Context.BIND_AUTO_CREATE );
            this.startService( playIntent );
        }
    }

}
