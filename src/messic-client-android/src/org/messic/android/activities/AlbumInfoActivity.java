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

import java.io.File;
import java.util.List;

import org.messic.android.R;
import org.messic.android.activities.adapters.SongAdapter;
import org.messic.android.activities.adapters.SongAdapter.SongAdapterType;
import org.messic.android.controllers.AlbumController;
import org.messic.android.controllers.Configuration;
import org.messic.android.datamodel.MDMAlbum;
import org.messic.android.datamodel.MDMPlaylist;
import org.messic.android.datamodel.MDMSong;
import org.messic.android.download.DownloadListener;
import org.messic.android.util.AlbumCoverCache;
import org.messic.android.util.UtilDownloadService;
import org.messic.android.util.UtilMusicPlayer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

public class AlbumInfoActivity
    extends Activity
{

    public static final String EXTRA_ALBUM_SID = "ALBUM_SID";

    private AlbumController controller = new AlbumController();

    private SongAdapter adapter;

    private MDMAlbum album;

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

    }

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        getActionBar().setDisplayHomeAsUpEnabled( true );

        setContentView( R.layout.albuminfo );

        this.album = (MDMAlbum) getIntent().getExtras().get( EXTRA_ALBUM_SID );

        if ( this.album.getSongs() == null || this.album.getSongs().size() == 0 )
        {
            if ( !Configuration.isOffline() )
            {
                AlbumController.getAlbumInfoOnline( this, this.album.getSid() );
            }
            else
            {
                AlbumController.getAlbumInfoOffline( this, this.album );
            }
        }

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

                public void elementRemove( final MDMSong song, int index )
                {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener()
                    {
                        public void onClick( DialogInterface dialog, int which )
                        {
                            switch ( which )
                            {
                                case DialogInterface.BUTTON_POSITIVE:
                                    UtilDownloadService.removeSong( AlbumInfoActivity.this, song );
                                    runOnUiThread( new Runnable()
                                    {
                                        public void run()
                                        {
                                            adapter.notifyDataSetChanged();
                                        }
                                    } );
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    // No button clicked
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder( AlbumInfoActivity.this );
                    builder.setMessage( getString( R.string.action_remove_local_song ) ).setPositiveButton( getString( R.string.yes ),
                                                                                                            dialogClickListener ).setNegativeButton( getString( R.string.no ),
                                                                                                                                                     dialogClickListener ).show();
                }

                public void coverTouch( MDMSong song, int index )
                {
                    UtilMusicPlayer.addSong( AlbumInfoActivity.this, song );
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
                    UtilMusicPlayer.addAlbum( AlbumInfoActivity.this, album );
                    Toast.makeText( AlbumInfoActivity.this,
                                    getResources().getText( R.string.player_added ) + album.getName(),
                                    Toast.LENGTH_SHORT ).show();
                }
            }
        } );

        eventAlbumInfoLoaded( album );
    }

    public void eventAlbumInfoLoaded( final MDMAlbum album )
    {
        this.album = album;
        adapter.clear();
        runOnUiThread( new Runnable()
        {
            public void run()
            {
                adapter.notifyDataSetChanged();
                TextView tvauthor = (TextView) findViewById( R.id.albuminfo_tauthor );
                TextView tvalbum = (TextView) findViewById( R.id.albuminfo_talbum );
                final ImageView ivcover = (ImageView) findViewById( R.id.albuminfo_icover );

                tvauthor.setText( album.getAuthor().getName() );
                tvalbum.setText( album.getName() );
                ivcover.setImageResource( android.R.color.white );
                Bitmap bm = AlbumCoverCache.getCover( album, new AlbumCoverCache.CoverListener()
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

                for ( int i = 0; i < album.getSongs().size(); i++ )
                {
                    MDMSong song = album.getSongs().get( i );
                    song.setAlbum( album );
                    adapter.addSong( album.getSongs().get( i ) );
                }

                adapter.notifyDataSetChanged();

            }
        } );

    }

    private void getMessicService()
    {
        UtilMusicPlayer.getMessicPlayerService( this );
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.albuminfo_activity_actions, menu );

        return super.onCreateOptionsMenu( menu );
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item )
    {
        // Handle presses on the action bar items
        switch ( item.getItemId() )
        {
            case R.id.action_settings:
                openSettings();
                return true;
            default:
                return super.onOptionsItemSelected( item );
        }

    }

    private void openSettings()
    {
        View anchor = findViewById( R.id.action_settings );

        // Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu( this, anchor );

        // Inflating the Popup using xml file
        popup.getMenuInflater().inflate( R.menu.menu_album, popup.getMenu() );
        if ( Configuration.isOffline() )
        {
            popup.getMenu().removeItem( R.id.menu_album_item_download );
        }

        // registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener( new PopupMenu.OnMenuItemClickListener()
        {
            public boolean onMenuItemClick( MenuItem item )
            {
                switch ( item.getItemId() )
                {
                    case R.id.menu_album_item_download:
                        downloadAlbum();
                        break;
                    case R.id.menu_album_item_play:
                        UtilMusicPlayer.addAlbum( AlbumInfoActivity.this, album );
                        break;
                    case R.id.menu_album_item_playnow:
                        UtilMusicPlayer.addSongsAndPlay( AlbumInfoActivity.this, album.getSongs() );
                        break;
                    case R.id.menu_album_item_remove:
                        removeAlbum( AlbumInfoActivity.this, album );
                        break;
                }
                return true;
            }
        } );

        popup.show();// showing popup menu
    }

    private void downloadAlbum()
    {
        UtilDownloadService.addDownload( this, album, new DownloadListener()
        {

            public void downloadUpdated( MDMSong song, float percent )
            {
                // TODO Auto-generated method stub

            }

            public void downloadStarted( MDMSong song )
            {
                // TODO Auto-generated method stub

            }

            public void downloadFinished( MDMSong song, File fdownloaded )
            {
                // TODO Auto-generated method stub

            }

            public void downloadAdded( MDMSong song )
            {
                // TODO Auto-generated method stub

            }

            public void disconnected()
            {
                // TODO Auto-generated method stub

            }

            public void connected()
            {
                // TODO Auto-generated method stub

            }
        } );
    }

    public static void removeAlbum( final Context ctx, final MDMAlbum album )
    {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener()
        {
            public void onClick( DialogInterface dialog, int which )
            {
                switch ( which )
                {
                    case DialogInterface.BUTTON_POSITIVE:
                        boolean result = UtilDownloadService.removeAlbum( ctx, album );
                        if ( result )
                        {
                            List<MDMSong> songs = album.getSongs();
                            for ( int i = 0; i < songs.size(); i++ )
                            {
                                MDMSong song = songs.get( i );
                                song.setLfileName( null );
                            }
                        }

                        Toast.makeText( ctx, ctx.getString( R.string.action_local_album_removed ) + album.getName(),
                                        Toast.LENGTH_SHORT ).show();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        // No button clicked
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder( ctx );
        builder.setMessage( ctx.getString( R.string.action_remove_local_album ) );
        builder.setPositiveButton( ctx.getString( R.string.yes ), dialogClickListener );
        builder.setNegativeButton( ctx.getString( R.string.no ), dialogClickListener );
        builder.show();

    }

}