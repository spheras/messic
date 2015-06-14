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
package org.messic.android.player;

import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MessicPlayerService
    extends Service
{

    private MessicPlayerQueue playerqueue = null;

    private final IBinder musicBind = new MusicBinder();

    private MessicPlayerNotification playernotification = null;

    public MessicPlayerQueue getPlayer()
    {
        return this.playerqueue;
    }

    public class MusicBinder
        extends Binder
    {
        public MessicPlayerService getService()
        {
            return MessicPlayerService.this;
        }
    }

    @Override
    public void onCreate()
    {
        Log.d( "MessicPlayerService", "onCreate" );
        // Toast.makeText( this, "onCreate", Toast.LENGTH_SHORT ).show();
        super.onCreate();
    }

    @Override
    public void onDestroy()
    {
        Log.d( "MessicPlayerService", "onDestroy" );
        // Toast.makeText( this, "onDestroy", Toast.LENGTH_SHORT ).show();
        super.onDestroy();
    }

    @Override
    public int onStartCommand( Intent intent, int flags, int startId )
    {
        Log.d( "MessicPlayerService", "onStartCommands" );
        // Toast.makeText( this, "onStartCommands", Toast.LENGTH_SHORT ).show();
        return super.onStartCommand( intent, flags, startId );
    }

    @Override
    public IBinder onBind( Intent intent )
    {
        Log.d( "MessicPlayerService", "onBind" );
        // Toast.makeText( this, "onBind", Toast.LENGTH_SHORT ).show();

        this.playerqueue = new MessicPlayerQueue( this );
        this.playernotification = new MessicPlayerNotification( this, this.playerqueue );
        this.playerqueue.addListener( this.playernotification );

        return musicBind;
    }

    @Override
    public boolean onUnbind( Intent intent )
    {
        Log.d( "MessicPlayerService", "onUnbind" );
        // Toast.makeText( this, "onUnbind", Toast.LENGTH_SHORT ).show();

        playerqueue.stop();

        List<PlayerEventListener> listeners = this.playerqueue.getListeners();
        for ( PlayerEventListener listener : listeners )
        {
            listener.disconnected();
        }

        playerqueue = null;

        return false;
    }

}
