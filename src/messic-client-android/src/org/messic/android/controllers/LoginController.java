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
package org.messic.android.controllers;

import java.io.IOException;
import java.net.URL;

import org.messic.android.activities.BaseActivity;
import org.messic.android.datamodel.MDMLogin;
import org.messic.android.util.MessicPreferences;
import org.messic.android.util.Network;
import org.messic.android.util.RestJSONClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class LoginController
{

    public boolean check( final Context context )
    {
        String surl = Configuration.getBaseUrl() + "/services/check??messic_token=" + Configuration.getLastToken();
        try
        {
            URL url = new URL( surl );
            url.openConnection();
            return true;
        }
        catch ( IOException e )
        {
            return false;
        }
    }

    public void login( final Context context, final boolean remember, final String username, final String password, final ProgressDialog pd )
        throws Exception
    {
        Network.nukeNetwork();

        final String baseURL = Configuration.getBaseUrl() + "/messiclogin";
        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<String, Object>();
        formData.add( "j_username", username );
        formData.add( "j_password", password );
        try
        {
            RestJSONClient.post( baseURL, formData, MDMLogin.class, new RestJSONClient.RestListener<MDMLogin>()
            {
                public void response( MDMLogin response )
                {
                    MessicPreferences mp = new MessicPreferences( context );
                    mp.setRemember( remember, username, password );
                    Configuration.setToken( response.getMessic_token() );

                    pd.dismiss();
                    Intent ssa = new Intent( context, BaseActivity.class );
                    context.startActivity( ssa );
                }

                public void fail( Exception e )
                {
                    pd.dismiss();
                    Toast.makeText( context, "Error", Toast.LENGTH_LONG ).show();
                }
            } );
        }
        catch ( Exception e )
        {
            pd.dismiss();
            Log.e( "login", e.getMessage(), e );
            throw e;
        }
    }
}
