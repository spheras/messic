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
    public IBinder onBind( Intent intent )
    {

        this.playerqueue = new MessicPlayerQueue( this );
        this.playernotification = new MessicPlayerNotification( this, this.playerqueue );
        this.playerqueue.addListener( this.playernotification );

        return musicBind;
    }

    @Override
    public boolean onUnbind( Intent intent )
    {
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
