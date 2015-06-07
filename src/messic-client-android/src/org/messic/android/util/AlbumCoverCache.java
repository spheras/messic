package org.messic.android.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;

import org.messic.android.controllers.Configuration;
import org.messic.android.datamodel.MDMAlbum;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;

public class AlbumCoverCache
{
    private static LruCache<String, Bitmap> mMemoryCache;

    public static String COVER_OFFLINE_FILENAME = "cover.jpg";

    static
    {
        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory = (int) ( Runtime.getRuntime().maxMemory() / 1024 );

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>( cacheSize )
        {
            protected int sizeOf( String key, Bitmap bitmap )
            {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };

    }

    public interface CoverListener
    {
        void setCover( Bitmap bitmap );

        void failed( Exception e );
    }

    public static Bitmap getCover( final MDMAlbum album, final CoverListener listener )
    {
        Bitmap result = getBitmapFromMemCache( "" + album.getSid() );
        if ( result != null )
        {
            return result;
        }
        else
        {
            if ( Configuration.isOffline() )
            {
                String albumPath = album.getLfileName();
                File coverPath = new File( albumPath + "/" + COVER_OFFLINE_FILENAME );
                if ( coverPath.exists() )
                {
                    Bitmap bmp;
                    try
                    {
                        bmp = BitmapFactory.decodeStream( new FileInputStream( coverPath ) );
                        addBitmapToMemoryCache( "" + album.getSid(), bmp );
                        return bmp;
                    }
                    catch ( FileNotFoundException e )
                    {
                        Log.e( "AlbumCoverCache", e.getMessage(), e );
                        e.printStackTrace();
                    }
                }
            }
            else
            {
                AsyncTask<Void, Void, Void> at = new AsyncTask<Void, Void, Void>()
                {
                    @Override
                    protected Void doInBackground( Void... params )
                    {
                        try
                        {
                            String baseURL =
                                Configuration.getBaseUrl() + "/services/albums/" + album.getSid()
                                    + "/cover?preferredWidth=100&preferredHeight=100&messic_token="
                                    + Configuration.getLastToken();
                            System.gc();
                            Bitmap bmp =
                                BitmapFactory.decodeStream( new URL( baseURL ).openConnection().getInputStream() );
                            addBitmapToMemoryCache( "" + album.getSid(), bmp );
                            listener.setCover( bmp );
                        }
                        catch ( Exception e )
                        {
                            listener.failed( e );
                        }
                        return null;
                    }

                };
                at.execute();
            }
            return null;
        }
    }

    public static void addBitmapToMemoryCache( String key, Bitmap bitmap )
    {
        if ( getBitmapFromMemCache( key ) == null )
        {
            mMemoryCache.put( key, bitmap );
        }
    }

    public static Bitmap getBitmapFromMemCache( String key )
    {
        return mMemoryCache.get( key );
    }
}
