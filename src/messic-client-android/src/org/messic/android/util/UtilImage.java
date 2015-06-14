package org.messic.android.util;

import org.messic.android.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;

public class UtilImage
{

    /**
     * Resize a bitmap to the notification status bar size dimensions
     * 
     * @param context {@link Context} utility
     * @param bm {@link Bitmap} to resize
     * @return resized bitmap
     */
    public static Bitmap resizeToNotificationImageSize( Context context, Bitmap bm )
    {
        Resources res = context.getResources();
        int height = (int) res.getDimension( R.dimen.bignotification_cover_width );
        int width = (int) res.getDimension( R.dimen.bignotification_cover_width );
        return Bitmap.createScaledBitmap( bm, width, height, false );
    }
}
