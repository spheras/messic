package org.messic.android.views;

import java.util.ArrayList;
import java.util.List;

import org.messic.android.R;
import org.messic.android.datamodel.MDMSong;
import org.messic.android.player.MessicPlayerService;
import org.messic.android.player.MessicPlayerService.MusicBinder;
import org.messic.android.player.PlayerEventListener;
import org.messic.android.util.AlbumCoverCache;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.IBinder;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PlayerView
    extends RelativeLayout
    implements PlayerEventListener
{
    private Intent playIntent;

    public static MessicPlayerService musicSrv;

    private static boolean musicBound = false;

    public PlayerView( Context context )
    {
        super( context );
        init();
    }

    public PlayerView( Context context, AttributeSet attr )
    {
        super( context, attr );
        init();
    }

    public PlayerView( Context context, AttributeSet attr, int defStyle )
    {
        super( context, attr, defStyle );
        init();
    }

    private void init()
    {
        LayoutInflater inflater = LayoutInflater.from( getContext() );
        View v = inflater.inflate( R.layout.player_layout, this, true );
        if ( playIntent == null )
        {
            playIntent = new Intent( getContext(), MessicPlayerService.class );
            getContext().bindService( playIntent, musicConnection, Context.BIND_AUTO_CREATE );
            getContext().startService( playIntent );
        }
        fillData( null );

        addListener( this );

        final ImageView ivprevsong = (ImageView) v.findViewById( R.id.base_ivback );
        ivprevsong.setOnClickListener( new View.OnClickListener()
        {
            public void onClick( View v )
            {
                musicSrv.getPlayer().prevSong();
            }

        } );
        final ImageView ivnextsong = (ImageView) v.findViewById( R.id.base_ivnext );
        ivnextsong.setOnClickListener( new View.OnClickListener()
        {
            public void onClick( View v )
            {
                musicSrv.getPlayer().nextSong();
            }

        } );

        final ImageView ivplaypause = (ImageView) v.findViewById( R.id.base_ivplaypause );
        ivplaypause.setOnClickListener( new View.OnClickListener()
        {

            public void onClick( View v )
            {
                if ( ivplaypause.getTag().equals( STATUS_PLAY ) )
                {
                    musicSrv.getPlayer().resumeSong();
                }
                else
                {
                    musicSrv.getPlayer().pauseSong();
                }
            }
        } );

        if ( musicSrv != null )
        {
            update();
        }
    }

    private void update()
    {
        MDMSong song = musicSrv.getPlayer().getCurrentSong();
        if ( song != null && musicSrv.getPlayer().isPlaying() )
        {
            playing( song, false,0 );
        }
        else
        {
            playing( song, false,0 );
            paused( song ,0);
        }
    }

    private void fillData( MDMSong song )
    {
        String authorname = ( song != null ? song.getAlbum().getAuthor().getName() : "" );
        String songname = ( song != null ? song.getName() : "" );

        ( (TextView) findViewById( R.id.base_tvcurrent_author ) ).setText( authorname );
        ( (TextView) findViewById( R.id.base_tvcurrent_song ) ).setText( songname );

        if ( song != null )
        {
            final ImageView ivcover = (ImageView) findViewById( R.id.base_ivcurrent_cover );
            Bitmap bm = AlbumCoverCache.getCover( song.getAlbum().getSid(), new AlbumCoverCache.CoverListener()
            {

                public void setCover( final Bitmap bitmap )
                {
                    PlayerView.this.post( new Runnable()
                    {
                        public void run()
                        {
                            ivcover.setBackground( new BitmapDrawable( getResources(), bitmap ) );
                        }
                    } );
                }

                public void failed( Exception e )
                {
                    // TODO Auto-generated method stub

                }
            } );
            if ( bm != null )
            {
                ivcover.setBackground( new BitmapDrawable( getResources(), bm ) );
            }
            else
            {
                ivcover.setBackgroundResource( R.drawable.ic_launcher );
            }
        }
        else
        {

        }
    }

    private static List<PlayerEventListener> listeners = new ArrayList<PlayerEventListener>();

    public static void addListener( PlayerEventListener listener )
    {
        if ( musicSrv != null )
        {
            musicSrv.getPlayer().addListener( listener );
        }
        else
        {
            listeners.add( listener );
        }
    }

    // connect to the service
    private static ServiceConnection musicConnection = new ServiceConnection()
    {

        public void onServiceConnected( ComponentName name, IBinder service )
        {
            MusicBinder binder = (MusicBinder) service;
            // get service
            musicSrv = binder.getService();
            // pass list
            // musicSrv.setList( songList );
            musicBound = true;

            for ( int i = 0; i < listeners.size(); i++ )
            {
                musicSrv.getPlayer().addListener( listeners.get( i ) );

                MDMSong song = musicSrv.getPlayer().getCurrentSong();
                if ( song != null && musicSrv.getPlayer().isPlaying() )
                {
                    listeners.get( i ).playing( song, false, 0 );
                }
                else
                {
                    listeners.get( i ).playing( song, false, 0 );
                    listeners.get( i ).paused( song, 0 );
                }
            }
            listeners.clear();

        }

        public void onServiceDisconnected( ComponentName name )
        {
            musicBound = false;
        }
    };

    public void paused( MDMSong song, int index )
    {
        ImageView ivplaypause = (ImageView) findViewById( R.id.base_ivplaypause );
        ivplaypause.setTag( STATUS_PLAY );
        ivplaypause.setBackgroundResource( R.drawable.miniplay_w_30 );
        ivplaypause.invalidate();
    }

    public void playing( MDMSong song, boolean resumed, int index )
    {
        ImageView ivplaypause = (ImageView) findViewById( R.id.base_ivplaypause );
        ivplaypause.setTag( STATUS_PAUSE );
        ivplaypause.setBackgroundResource( R.drawable.minipause_w_30 );
        ivplaypause.invalidate();
        if ( !resumed )
        {
            fillData( song );
        }
    }

    private static final Integer STATUS_PLAY = 1;

    private static final Integer STATUS_PAUSE = 2;

    public void completed( int index )
    {
        ImageView ivplaypause = (ImageView) findViewById( R.id.base_ivplaypause );
        ivplaypause.setTag( STATUS_PLAY );
        ivplaypause.setBackgroundResource( R.drawable.miniplay_w_30 );
        ivplaypause.invalidate();
    }

}
