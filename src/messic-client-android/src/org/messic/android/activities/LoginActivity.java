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
import org.messic.android.controllers.LoginController;
import org.messic.android.util.MessicPreferences;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity
    extends Activity
{
    private LoginController controller = new LoginController();

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );

        ( (TextView) findViewById( R.id.login_thostname ) ).setText( Configuration.getCurrentMessicService().name );
        MessicPreferences p = new MessicPreferences( this );
        ( (CheckBox) findViewById( R.id.login_cbremember ) ).setChecked( p.getRemember() );
        if ( p.getRemember() )
        {
            ( (TextView) findViewById( R.id.login_tusername ) ).setText( p.getLastUser() );
            ( (TextView) findViewById( R.id.login_tpassword ) ).setText( p.getLastPassword() );
        }

        if ( Configuration.getLastToken() != null )
        {
            // trying to avoid login again
            if ( controller.check( this ) )
            {
                Intent ssa = new Intent( this, BaseActivity.class );
                this.startActivity( ssa );
            }
        }

        ImageButton bsearchServer = (ImageButton) findViewById( R.id.login_bsearchserver );
        bsearchServer.setOnClickListener( new View.OnClickListener()
        {
            public void onClick( View v )
            {
                Intent ssa = new Intent( LoginActivity.this, SearchMessicServiceActivity.class );
                LoginActivity.this.startActivity( ssa );
                finish();
            }
        } );

        Button loginaction = (Button) findViewById( R.id.login_bloginaction );
        loginaction.setOnClickListener( new View.OnClickListener()
        {
            public void onClick( View v )
            {
                String username = ( (TextView) findViewById( R.id.login_tusername ) ).getText().toString();
                String password = ( (TextView) findViewById( R.id.login_tpassword ) ).getText().toString();
                boolean remember = ( (CheckBox) findViewById( R.id.login_cbremember ) ).isChecked();
                try
                {
                    ProgressDialog dialog = ProgressDialog.show( LoginActivity.this, "Loading", "Please wait...", true );
                    controller.login( LoginActivity.this, remember, username, password, dialog);
                }
                catch ( Exception e )
                {
                    Toast.makeText( LoginActivity.this, "Error while trying to login", Toast.LENGTH_LONG ).show();
                }
            }
        } );
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.login, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item )
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if ( id == R.id.action_settings )
        {
            return true;
        }
        return super.onOptionsItemSelected( item );
    }
}
