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

import org.messic.android.R;
import org.messic.android.activities.AlbumInfoActivity;
import org.messic.android.datamodel.MDMAlbum;
import org.messic.android.util.RestJSONClient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class AlbumController
{
    public static void getAlbumInfo( final Activity originActivity, MDMAlbum album )
    {
        Intent ssa = new Intent( originActivity, AlbumInfoActivity.class );
        ssa.putExtra( AlbumInfoActivity.EXTRA_ALBUM_SID, album );
        originActivity.startActivity( ssa );
    }

    public static void getAlbumInfo( final Activity originActivity, long sid )
    {
        final ProgressDialog dialog =
            ProgressDialog.show( originActivity, originActivity.getResources().getString( R.string.albuminfo_loading ),
                                 originActivity.getResources().getString( R.string.albuminfo_wait ), true );
        dialog.show();

        final String baseURL =
            Configuration.getBaseUrl() + "/services/albums/" + sid + "?songsInfo=true&authorInfo=true&messic_token="
                + Configuration.getLastToken();
        RestJSONClient.get( baseURL, MDMAlbum.class, new RestJSONClient.RestListener<MDMAlbum>()
        {
            public void response( MDMAlbum response )
            {
                Intent ssa = new Intent( originActivity, AlbumInfoActivity.class );
                ssa.putExtra( AlbumInfoActivity.EXTRA_ALBUM_SID, response );
                originActivity.startActivity( ssa );
                dialog.dismiss();
            }

            public void fail( final Exception e )
            {
                dialog.dismiss();

                Log.e( "Random", e.getMessage(), e );
                originActivity.runOnUiThread( new Runnable()
                {

                    public void run()
                    {
                        Toast.makeText( originActivity, "Error:" + e.getMessage(), Toast.LENGTH_LONG ).show();

                    }
                } );
            }

        } );

    }

}
