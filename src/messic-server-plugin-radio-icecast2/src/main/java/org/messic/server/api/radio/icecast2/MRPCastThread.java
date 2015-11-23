package org.messic.server.api.radio.icecast2;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.messic.server.api.plugin.radio.MessicRadioSong;

import com.gmail.kunicins.olegs.libshout.Libshout;

public class MRPCastThread
    extends Thread
{

    private boolean flagStop = false;

    private MessicRadioSong currentFile = null;

    private MessicRadioSong nextFile = null;

    private InputStream mp3is = null;

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
            flagStop = false;
            while ( !flagStop )
            {

                // while nothing to be played, we send a noise to the buffer
                while ( nextFile == null && flagStop == false )
                {
                    // noise mp3
                    String snoise = "/org/messic/server/api/radio/icecast2/noise.mp3";
                    mp3is = new BufferedInputStream( MRPCastThread.class.getResourceAsStream( snoise ) );
                    synchronized ( this )
                    {
                        byte[] buffer = new byte[1024];
                        int read = mp3is.read( buffer );
                        Libshout icecast = Libshout.getInstance();
                        while ( read > 0 && flagStop == false && nextFile == null )
                        {
                            icecast.send( buffer, read );
                            read = mp3is.read( buffer );
                        }
                        mp3is.close();
                    }
                }

                if ( flagStop == false )
                {
                    currentFile = nextFile;
                    nextFile = null;

                    byte[] buffer = new byte[1024];
                    Libshout icecast = Libshout.getInstance();

                    mp3is = new BufferedInputStream( new FileInputStream( currentFile.songFile ) );

                    int read = mp3is.read( buffer );
                    while ( read > 0 && !flagStop )
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
    }

}
