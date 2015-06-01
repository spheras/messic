package org.messic.android.download;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.messic.android.datamodel.MDMSong;
import org.messic.android.util.FileUtil;

import android.util.SparseArray;

public class DownloadQueue
{
    private static ExecutorService threadPoolExecutor = Executors.newFixedThreadPool( 3 );

    private static SparseArray<MDMSong> pendingSongs = new SparseArray<MDMSong>();

    private static SparseArray<MDMSong> downloadedSongs = new SparseArray<MDMSong>();

    public interface DownloadQueueListener
    {
        void received( MDMSong song, File fdownloaded );
    }

    public static void addDownload( final URL url, final MDMSong song, final DownloadQueueListener listener )
    {
        pendingSongs.append( (int) song.getSid(), song );

        threadPoolExecutor.execute( new Runnable()
        {
            public void run()
            {
                try
                {
                    String folderPath = song.calculateExternalStorageFolder();
                    String filePath = folderPath + "/" + song.calculateExternalFilename();
                    File fFolderPath = new File( folderPath );
                    fFolderPath.mkdirs();
                    File fDestination = new File( filePath );
                    InputStream is = url.openConnection().getInputStream();
                    FileUtil.saveToFile( fDestination, is );

                    song.setFileName( fDestination.getAbsolutePath() );
                    song.getAlbum().setFileName( folderPath );

                    downloadedSongs.append( (int) song.getSid(), song );
                    pendingSongs.remove( (int) song.getSid() );
                    listener.received( song, fDestination );
                }
                catch ( IOException e )
                {
                    e.printStackTrace();
                }
            }
        } );
    }
}
