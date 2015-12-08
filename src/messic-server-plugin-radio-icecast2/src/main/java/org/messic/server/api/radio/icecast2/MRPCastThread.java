package org.messic.server.api.radio.icecast2;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.messic.server.api.plugin.radio.MessicRadioInfo;
import org.messic.server.api.plugin.radio.MessicRadioSong;
import org.messic.server.api.plugin.radio.MessicRadioStatus;

import com.gmail.kunicins.olegs.libshout.Libshout;

public class MRPCastThread
    extends Thread
{

    private boolean flagStop = false;

    private MessicRadioSong currentFile = null;

    private MessicRadioSong nextFile = null;

    private InputStream mp3is = null;

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
        try
        {
            Libshout icecast = Libshout.getInstance();
            flagStop = false;
            while ( !flagStop )
            {

                this.getInfo().status = MessicRadioStatus.WAITING;

                // while nothing to be played, we send a noise to the buffer
                while ( nextFile == null && flagStop == false )
                {
                    this.getInfo().albumName = "";
                    this.getInfo().authorName = "";
                    this.getInfo().songName = "";
                    this.getInfo().songSid = -1;
                    // noise mp3
                    String snoise = "/org/messic/server/api/radio/icecast2/noise.mp3";
                    mp3is = new BufferedInputStream( MRPCastThread.class.getResourceAsStream( snoise ) );
                    byte[] buffer = new byte[1024];
                    int read = mp3is.read( buffer );

                    while ( read > 0 && flagStop == false && nextFile == null )
                    {
                        synchronized ( this )
                        {
                            icecast.send( buffer, read );
                            read = mp3is.read( buffer );
                        }
                    }
                    mp3is.close();
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
                    icecast.setMeta( "song", currentFile.authorName + " - " + currentFile.songName );

                    int read = mp3is.read( buffer );
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

            if ( mp3is != null )
            {
                try
                {
                    mp3is.close();
                }
                catch ( IOException e1 )
                {
                    e1.printStackTrace();
                }
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
