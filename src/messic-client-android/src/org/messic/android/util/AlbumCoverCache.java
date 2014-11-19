package org.messic.android.util;

import java.net.URL;

import org.messic.android.controllers.Configuration;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;

public class AlbumCoverCache
{
    private static LruCache<String, Bitmap> mMemoryCache;

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

    public static Bitmap getCover( final long albumSid, final CoverListener listener )
    {
        Bitmap result = getBitmapFromMemCache( "" + albumSid );
        if ( result != null )
        {
            return result;
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
//                        Resources res = context.getResources();
//                        int height = (int) res.getDimension( android.R.dimen.notification_large_icon_height );
//                        int width = (int) res.getDimension( android.R.dimen.notification_large_icon_width );

                        String baseURL =
                            Configuration.getBaseUrl() + "/services/albums/" + albumSid + "/cover?preferredWidth=100&preferredHeight=100&messic_token="
                                + Configuration.getLastToken();
                        System.gc();
                        Bitmap bmp = BitmapFactory.decodeStream( new URL( baseURL ).openConnection().getInputStream() );
                        addBitmapToMemoryCache( "" + albumSid, bmp );
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
