package org.messic.server.api.radio.icecast2;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.messic.server.api.plugin.radio.MessicRadioInfo;
import org.messic.server.api.plugin.radio.MessicRadioSong;
import org.messic.server.api.plugin.radio.MessicRadioStatus;
import org.messic.server.api.radio.icecast2.libshout.LibShout;

public class MRPCastThread
    extends Thread
{

    private boolean flagStop = false;

    private MessicRadioSong currentFile = null;

    private MessicRadioSong nextFile = null;

    private MessicRadioInfo info;

    public MRPCastThread( MessicRadioInfo info )
    {
        super();
        this.setInfo( info );

    }

    /**
     * We stop sending music
     */
    public synchronized void stopCast()
    {
        flagStop = true;
        notify();
    }

    /**
     * We put the next file to be played
     * 
     * @param song
     */
    public synchronized void setNextFile( MessicRadioSong song )
    {
        nextFile = song;
        notify();
    }

    @Override
    public void run()
    {
        LibShout icecast = LibShout.get();
        String snoise = "/org/messic/server/api/radio/icecast2/noise.mp3";
        InputStream noisemp3is = new BufferedInputStream( MRPCastThread.class.getResourceAsStream( snoise ) );
        noisemp3is.mark( 1 << 32 );
        InputStream mp3is = null;

        try
        {

            flagStop = false;
            while ( !flagStop )
            {

                this.getInfo().status = MessicRadioStatus.WAITING;

                this.getInfo().albumName = "";
                this.getInfo().authorName = "";
                this.getInfo().songName = "";
                this.getInfo().songSid = -1;
                // noise mp3
                icecast.setMeta( null );

                // while nothing to be played, we send a noise to the buffer
                while ( nextFile == null && flagStop == false )
                {
                    byte[] buffer = new byte[512];
                    noisemp3is.reset();
                    int read = noisemp3is.read( buffer );

                    while ( read > 0 && flagStop == false && nextFile == null )
                    {
                        synchronized ( this )
                        {
                            icecast.send( buffer, read );
                            read = noisemp3is.read( buffer );
                            icecast.sync();
                        }
                    }

                    // if we must go to another file on the playlist
                    if ( nextFile != null )
                    {
                        // we send a last stream to prepare the next song
                        noisemp3is.reset();
                        buffer = new byte[1024];
                        read = noisemp3is.read( buffer );
                        icecast.send( buffer, read, false );
                    }
                }

                if ( flagStop == false )
                {
                    currentFile = nextFile;
                    this.getInfo().albumName = currentFile.albumName;
                    this.getInfo().authorName = currentFile.authorName;
                    this.getInfo().songName = currentFile.songName;
                    this.getInfo().songSid = currentFile.songSid;
                    nextFile = null;

                    this.getInfo().status = MessicRadioStatus.PLAYING;

                    byte[] buffer = new byte[1024];

                    mp3is = new BufferedInputStream( new FileInputStream( currentFile.songFile ) );
                    icecast.setMeta( currentFile );

                    int read = mp3is.read( buffer );
                    icecast.sync();// sync before starting sending
                    while ( read > 0 && !flagStop && nextFile == null )
                    {
                        icecast.send( buffer, read );
                        read = mp3is.read( buffer );
                    }

                    mp3is.close();
                }

            }

        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if ( mp3is != null )
                {
                    mp3is.close();
                }
            }
            catch ( IOException e )
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try
            {
                if ( noisemp3is != null )
                {
                    noisemp3is.close();
                }
            }
            catch ( IOException e )
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        this.getInfo().status = MessicRadioStatus.NOT_STARTED;
    }

    /**
     * @return the info
     */
    public MessicRadioInfo getInfo()
    {
        return info;
    }

    /**
     * @param info the info to set
     */
    public void setInfo( MessicRadioInfo info )
    {
        this.info = info;
    }

}
