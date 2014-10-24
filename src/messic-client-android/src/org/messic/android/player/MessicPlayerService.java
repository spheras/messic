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

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class MessicPlayerService
    extends Service
{
    private MessicPlayerQueue playerqueue = null;

    private final IBinder musicBind = new MusicBinder();

    private MessicPlayerNotification playernotification = null;

    public void onCreate()
    {
        super.onCreate();
        this.playerqueue = new MessicPlayerQueue( this );
        this.playernotification = new MessicPlayerNotification( this, this.playerqueue );
        this.playerqueue.addListener( this.playernotification );
    }

    public MessicPlayerQueue getPlayer()
    {
        return this.playerqueue;
    }

    @Override
    public int onStartCommand( Intent intent, int flags, int startId )
    {
        return super.onStartCommand( intent, flags, startId );
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
    public IBinder onBind( Intent intent )
    {
        return musicBind;
    }

    @Override
    public boolean onUnbind( Intent intent )
    {
        playerqueue.stop();
        playerqueue = null;
        return false;
    }

}
