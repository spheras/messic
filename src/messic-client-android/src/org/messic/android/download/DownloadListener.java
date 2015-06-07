package org.messic.android.download;

import org.messic.android.datamodel.MDMSong;

public interface DownloadListener
{
    void downloadAdded( MDMSong song );

    void downloadUpdated( MDMSong song, float percent );

    void downloadFinished( MDMSong song );

    void downloadStarted( MDMSong song );

}
