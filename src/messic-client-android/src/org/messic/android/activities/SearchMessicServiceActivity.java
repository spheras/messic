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
import org.messic.android.controllers.Configuration;
import org.messic.android.controllers.SearchMessicServiceController;
import org.messic.android.controllers.messicdiscovering.MessicServerInstance;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

public class SearchMessicServiceActivity
    extends Activity
{
    private SearchMessicServiceController controller = new SearchMessicServiceController();

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_search_messic_service );

        final SearchMessicServiceAdapter adapter = new SearchMessicServiceAdapter( this );
        ListView lv = (ListView) findViewById( R.id.searchmessicservice_lvresults );
        lv.setAdapter( adapter );

        lv.setOnItemClickListener( new AdapterView.OnItemClickListener()
        {
            public void onItemClick( AdapterView<?> parent, View view, int position, long id )
            {
                MessicServerInstance msi = (MessicServerInstance) adapter.getItem( position );
                Configuration.setMessicService( getApplicationContext(), msi );
                Intent ssa = new Intent( SearchMessicServiceActivity.this, LoginActivity.class );
                SearchMessicServiceActivity.this.startActivity( ssa );
            }
        } );

        ( (Button) findViewById( R.id.searchmessicservice_bsearch ) ).setOnClickListener( new View.OnClickListener()
        {

            public void onClick( View v )
            {
                final Button b = ( (Button) findViewById( R.id.searchmessicservice_bsearch ) );
                b.setEnabled( false );
                adapter.clear();

                new CountDownTimer( 30000, 1000 )
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
                        b.setEnabled( true );
                        b.setText( R.string.searchMessicService_searchaction );
                        controller.cancelSearch();
                    }
                }.start();

                controller.searchMessicServices( new SearchMessicServiceController.SearchListener()
                {

                    public void messicServiceFound( final MessicServerInstance md )
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
                                ;
                            }
                        } );
                    }
                } );

            }
        } );
        ;
    }
}
