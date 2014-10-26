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

import org.messic.android.controllers.messicdiscovering.MessicServerInstance;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class MessicPreferences
{
    public static final String PREFERENCE_MESSIC_SERVER_IP = "MESSIC_SERVER_IP";

    public static final String PREFERENCE_MESSIC_SERVER_NAME = "MESSIC_SERVER_NAME";

    public static final String PREFERENCE_MESSIC_SERVER_PORT = "MESSIC_SERVER_PORT";

    public static final String PREFERENCE_MESSIC_SERVER_SECURE = "MESSIC_SERVER_SECURE";

    public static final String PREFERENCE_MESSIC_SERVER_VERSION = "MESSIC_SERVER_VERSION";

    public static final String PREFERENCE_MESSIC_SERVER_LASTUSER = "MESSIC_SERVER_LASTUSER";

    public static final String PREFERENCE_MESSIC_SERVER_LASTPASSWORD = "MESSIC_SERVER_LASTPASSWORD";

    public static final String PREFERENCE_MESSIC_SERVER_REMEMBER = "MESSIC_SERVER_REMEMBER";

    private SharedPreferences sp = null;

    public MessicPreferences( Context context )
    {
        this.sp = PreferenceManager.getDefaultSharedPreferences( context );

    }

    public void setLastMessicServerUsed( MessicServerInstance msi )
    {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString( PREFERENCE_MESSIC_SERVER_IP, msi.ip );
        editor.putString( PREFERENCE_MESSIC_SERVER_NAME, msi.name );
        editor.putInt( PREFERENCE_MESSIC_SERVER_PORT, msi.port );
        editor.putBoolean( PREFERENCE_MESSIC_SERVER_SECURE, msi.secured );
        editor.putString( PREFERENCE_MESSIC_SERVER_VERSION, msi.version );
        editor.commit();
    }

    public MessicServerInstance getLastMessicServerUsed()
    {
        MessicServerInstance msi = new MessicServerInstance();
        msi.ip = this.sp.getString( PREFERENCE_MESSIC_SERVER_IP, "" );
        msi.name = this.sp.getString( PREFERENCE_MESSIC_SERVER_NAME, "" );
        msi.port = this.sp.getInt( PREFERENCE_MESSIC_SERVER_PORT, 80 );
        msi.secured = this.sp.getBoolean( PREFERENCE_MESSIC_SERVER_SECURE, false );
        msi.version = this.sp.getString( PREFERENCE_MESSIC_SERVER_VERSION, "" );

        if ( msi.ip != null && msi.ip.length() > 0 )
        {
            return msi;
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

    public String getLastUser()
    {
        return this.sp.getString( PREFERENCE_MESSIC_SERVER_LASTUSER, "" );
    }

    public String getLastPassword()
    {
        return this.sp.getString( PREFERENCE_MESSIC_SERVER_LASTPASSWORD, "" );
    }

    public void setRemember( boolean remember, String username, String password )
    {
        SharedPreferences.Editor editor = sp.edit();
        if ( remember )
        {
            editor.putBoolean( PREFERENCE_MESSIC_SERVER_REMEMBER, true );
            editor.putString( PREFERENCE_MESSIC_SERVER_LASTUSER, username );
            editor.putString( PREFERENCE_MESSIC_SERVER_LASTPASSWORD, password );
        }
        else
        {
            editor.putBoolean( PREFERENCE_MESSIC_SERVER_REMEMBER, false );
            editor.putString( PREFERENCE_MESSIC_SERVER_LASTUSER, "" );
            editor.putString( PREFERENCE_MESSIC_SERVER_LASTPASSWORD, "" );
        }
        editor.commit();
    }
}
