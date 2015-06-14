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

import org.messic.android.datamodel.MDMAlbum;
import org.messic.android.datamodel.MDMPlaylist;
import org.messic.android.datamodel.MDMSong;

/**
 * Interface to listen events
 */
public interface PlayerEventListener
{
    /** the current song has been paused */
    void paused( MDMSong song, int index );

    /** We are playing a song */
    void playing( MDMSong song, boolean resumed, int index );

    /** The queue have been finished */
    void completed( int index );

    /** song added to the queue */
    void added( MDMSong song );

    /** album added to the queue */
    void added( MDMAlbum album );

    /** playlist added to the queue */
    void added( MDMPlaylist playlist );

    /** song removed from queue */
    void removed( MDMSong song );

    /** the queue has been empty */
    void empty();

    /** the player service has been connected */
    void connected();

    /** the player service has been disconected */
    void disconnected();
}