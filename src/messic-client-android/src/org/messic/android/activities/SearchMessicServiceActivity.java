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
import org.messic.android.activities.adapters.SearchMessicServiceAdapter;
import org.messic.android.activities.swipedismiss.SwipeDismissListViewTouchListener;
import org.messic.android.controllers.Configuration;
import org.messic.android.controllers.LoginController;
import org.messic.android.controllers.SearchMessicServiceController;
import org.messic.android.datamodel.MDMMessicServerInstance;
import org.messic.android.datamodel.dao.DAOServerInstance;
import org.messic.android.util.UtilDownloadService;
import org.messic.android.util.UtilMusicPlayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class SearchMessicServiceActivity
    extends Activity
{
    private SearchMessicServiceController controller = new SearchMessicServiceController();

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_search_messic_service );

        UtilMusicPlayer.startMessicMusicService( this );
        UtilDownloadService.startDownloadService( this );

        final SearchMessicServiceAdapter adapter = new SearchMessicServiceAdapter( this );
        controller.getSavedSessions( this, adapter );
        ListView lv = (ListView) findViewById( R.id.searchmessicservice_lvresults );
        lv.setAdapter( adapter );
        if ( adapter.getInstances().size() > 0 )
        {
            findViewById( R.id.searchmessicservice_lempty ).setVisibility( View.GONE );
        }

        // Create a ListView-specific touch listener. ListViews are given special treatment because
        // by default they handle touches for their list items... i.e. they're in charge of drawing
        // the pressed state (the list selector), handling list item clicks, etc.
        SwipeDismissListViewTouchListener touchListener =
            new SwipeDismissListViewTouchListener( lv, new SwipeDismissListViewTouchListener.DismissCallbacks()
            {
                public boolean canDismiss( int position )
                {
                    MDMMessicServerInstance msi = (MDMMessicServerInstance) adapter.getItem( position );
                    if ( msi.getLastCheckedStatus() == MDMMessicServerInstance.STATUS_RUNNING )
                    {
                        return false;
                    }
                    else
                    {
                        return true;
                    }
                }

                public void onDismiss( ListView listView, int[] reverseSortedPositions )
                {
                    for ( int position : reverseSortedPositions )
                    {
                        MDMMessicServerInstance msi = (MDMMessicServerInstance) adapter.getItem( position );
                        controller.removeSavedSession( SearchMessicServiceActivity.this, msi );
                        adapter.removeItem( position );
                    }
                    adapter.notifyDataSetChanged();
                }
            } );
        lv.setOnTouchListener( touchListener );
        // Setting this scroll listener is required to ensure that during ListView scrolling,
        // we don't look for swipes.
        lv.setOnScrollListener( touchListener.makeScrollListener() );

        lv.setOnItemClickListener( new AdapterView.OnItemClickListener()
        {
            public void onItemClick( AdapterView<?> parent, View view, int position, long id )
            {
                MDMMessicServerInstance msi = (MDMMessicServerInstance) adapter.getItem( position );
                if ( !( msi.getLastCheckedStatus() == MDMMessicServerInstance.STATUS_DOWN ) )
                {
                    DAOServerInstance dao = new DAOServerInstance( getApplicationContext() );
                    msi = dao.save( msi );
                    Configuration.setMessicService( getApplicationContext(), msi );
                    Intent ssa = new Intent( SearchMessicServiceActivity.this, LoginActivity.class );
                    SearchMessicServiceActivity.this.startActivity( ssa );
                }
                else
                {
                    Toast.makeText( SearchMessicServiceActivity.this,
                                    getString( R.string.searchMessicService_notavailable ), Toast.LENGTH_LONG ).show();
                }
            }
        } );

        ( (Button) findViewById( R.id.searchmessicservice_bsearch ) ).setOnClickListener( new View.OnClickListener()
        {

            public void onClick( View v )
            {
                final Button b = ( (Button) findViewById( R.id.searchmessicservice_bsearch ) );
                b.setEnabled( false );

                final CountDownTimer cdt = new CountDownTimer( 15000, 1000 )
                {
                    @Override
                    public void onTick( long millisUntilFinished )
                    {

                        b.setText( getString( R.string.searchMessicService_countdown_searching ) + " ("
                            + ( millisUntilFinished / 1000 )
                            + getString( R.string.searchMessicService_countdown_seconds ) + ")" );
                    }

                    @Override
                    public void onFinish()
                    {
                        b.post( new Runnable()
                        {
                            public void run()
                            {
                                b.setEnabled( true );
                                b.setText( R.string.searchMessicService_searchaction );
                            }
                        } );
                        controller.cancelSearch();
                    }
                };
                cdt.start();

                controller.searchMessicServices( new SearchMessicServiceController.SearchListener()
                {

                    public boolean messicServiceFound( final MDMMessicServerInstance md )
                    {
                        // let's see if the instance was found already
                        if ( !adapter.existInstance( md ) )
                        {
                            SearchMessicServiceActivity.this.runOnUiThread( new Runnable()
                            {
                                public void run()
                                {
                                    findViewById( R.id.searchmessicservice_lempty ).setVisibility( View.GONE );
                                    if ( adapter.addInstance( md ) )
                                    {
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            } );

                            cdt.cancel();
                            cdt.onFinish();
                            return true;
                        }
                        return false;
                    }
                } );

            }
        } );

        View voffline = findViewById( R.id.searchmessicservice_offline );
        if ( LoginController.checkEmptyDatabase( this ) )
        {
            voffline.setVisibility( View.GONE );
        }
        else
        {
            voffline.setOnClickListener( new View.OnClickListener()
            {
                public void onClick( View v )
                {
                    Configuration.setOffline( true );
                    Intent ssa = new Intent( SearchMessicServiceActivity.this, BaseActivity.class );
                    SearchMessicServiceActivity.this.startActivity( ssa );
                }
            } );
        }
    }
}
