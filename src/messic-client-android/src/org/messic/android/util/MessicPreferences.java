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
package org.messic.android.util;

import org.messic.android.controllers.Configuration;
import org.messic.android.datamodel.MDMMessicServerInstance;
import org.messic.android.datamodel.dao.DAOServerInstance;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class MessicPreferences
{
    public static final String PREFERENCE_LAST_MESSIC_SERVER = "LAST_MESSIC_SERVER";

    public static final String PREFERENCE_MESSIC_SERVER_REMEMBER = "MESSIC_SERVER_REMEMBER";

    private SharedPreferences sp = null;

    private Context context;

    public MessicPreferences( Context context )
    {
        this.sp = PreferenceManager.getDefaultSharedPreferences( context );
        this.context = context;
    }

    public void setLastMessicServerUsed( MDMMessicServerInstance msi )
    {
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt( PREFERENCE_LAST_MESSIC_SERVER, msi.lsid );
        editor.commit();
    }

    public MDMMessicServerInstance getLastMessicServerUsed()
    {
        int sid = this.sp.getInt( PREFERENCE_LAST_MESSIC_SERVER, 0 );
        if ( sid > 0 )
        {
            DAOServerInstance dao = new DAOServerInstance( context );
            return dao.get( sid );
        }
        else
        {
            return null;
        }
    }

    public boolean getRemember()
    {
        return this.sp.getBoolean( PREFERENCE_MESSIC_SERVER_REMEMBER, false );
    }

    public void setRemember( boolean remember, String username, String password )
    {
        SharedPreferences.Editor editor = sp.edit();
        if ( remember )
        {
            editor.putBoolean( PREFERENCE_MESSIC_SERVER_REMEMBER, true );
            MDMMessicServerInstance instance = Configuration.getCurrentMessicService();
            instance.lastUser = username;
            instance.lastPassword = password;
            DAOServerInstance dao = new DAOServerInstance( context );
            dao.save( instance );
        }
        else
        {
            editor.putBoolean( PREFERENCE_MESSIC_SERVER_REMEMBER, false );
            MDMMessicServerInstance instance = Configuration.getCurrentMessicService();
            instance.lastUser = "";
            instance.lastPassword = "";
            DAOServerInstance dao = new DAOServerInstance( context );
            dao.save( instance );
        }
        editor.commit();
    }
}
