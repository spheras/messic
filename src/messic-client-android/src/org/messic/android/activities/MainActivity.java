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

import org.messic.android.controllers.Configuration;
import org.messic.android.controllers.SearchMessicServiceController;
import org.messic.android.datamodel.MDMMessicServerInstance;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity
    extends Activity
{
    private SearchMessicServiceController controller = new SearchMessicServiceController();

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );

        // First, we test if the last configured server is online yet
        MDMMessicServerInstance prefferedServer = Configuration.getLastMessicServerUsed( this );
        if ( prefferedServer != null && prefferedServer.ip.trim().length() > 0 )
        {
            // must test if the server is online
            // TODO
            // Let's try to login with this sever
            Intent ssa = new Intent( MainActivity.this, LoginActivity.class );
            ssa.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK );
            MainActivity.this.startActivity( ssa );

        }
        else
        {
            // We don't have any preffered server, we must search a valid one
            Intent ssa = new Intent( MainActivity.this, SearchMessicServiceActivity.class );
            ssa.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK );
            MainActivity.this.startActivity( ssa );
        }
    }
}
