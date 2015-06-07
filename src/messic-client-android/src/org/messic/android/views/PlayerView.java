package org.messic.android.views;

import org.messic.android.R;
import org.messic.android.datamodel.MDMAlbum;
import org.messic.android.datamodel.MDMPlaylist;
import org.messic.android.datamodel.MDMSong;
import org.messic.android.player.PlayerEventListener;
import org.messic.android.util.AlbumCoverCache;
import org.messic.android.util.UtilMusicPlayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
        this.setVisibility( View.GONE );

        LayoutInflater inflater = LayoutInflater.from( getContext() );
        View v = inflater.inflate( R.layout.player_layout, this, true );

        fillData( null );

        addListener( this.getContext(), this );

        final ImageView ivprevsong = (ImageView) v.findViewById( R.id.base_ivback );
        ivprevsong.setOnClickListener( new View.OnClickListener()
        {
            public void onClick( View v )
            {
                UtilMusicPlayer.prevSong( PlayerView.this.getContext() );
            }

        } );
        final ImageView ivnextsong = (ImageView) v.findViewById( R.id.base_ivnext );
        ivnextsong.setOnClickListener( new View.OnClickListener()
        {
            public void onClick( View v )
            {
                UtilMusicPlayer.nextSong( PlayerView.this.getContext() );
            }

        } );

        final ImageView ivplaypause = (ImageView) v.findViewById( R.id.base_ivplaypause );
        ivplaypause.setOnClickListener( new View.OnClickListener()
        {

            public void onClick( View v )
            {
                if ( ivplaypause.getTag().equals( STATUS_PLAY ) )
                {
                    UtilMusicPlayer.resumeSong( PlayerView.this.getContext() );
                }
                else
                {
                    UtilMusicPlayer.pauseSong( PlayerView.this.getContext() );
                }
            }
        } );

        update();
    }

    private void update()
    {
        MDMSong song = UtilMusicPlayer.getCurrentSong( PlayerView.this.getContext() );
        if ( song != null && UtilMusicPlayer.isPlaying( PlayerView.this.getContext() ) )
        {
            playing( song, false, 0 );
        }
        else
        {
            playing( song, false, 0 );
            paused( song, 0 );
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
            Bitmap bm = AlbumCoverCache.getCover( song.getAlbum(), new AlbumCoverCache.CoverListener()
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

    public static void addListener( Context ctx, PlayerEventListener listener )
    {
        UtilMusicPlayer.addListener( ctx, listener );
    }

    public void paused( MDMSong song, int index )
    {
        this.setVisibility( View.VISIBLE );
        ImageView ivplaypause = (ImageView) findViewById( R.id.base_ivplaypause );
        ivplaypause.setTag( STATUS_PLAY );
        ivplaypause.setBackgroundResource( R.drawable.ic_play_arrow_white_24dp );
        ivplaypause.invalidate();
    }

    public void playing( MDMSong song, boolean resumed, int index )
    {
        this.setVisibility( View.VISIBLE );
        ImageView ivplaypause = (ImageView) findViewById( R.id.base_ivplaypause );
        ivplaypause.setTag( STATUS_PAUSE );
        ivplaypause.setBackgroundResource( R.drawable.ic_pause_white_24dp );
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
        this.setVisibility( View.VISIBLE );
        ImageView ivplaypause = (ImageView) findViewById( R.id.base_ivplaypause );
        ivplaypause.setTag( STATUS_PLAY );
        ivplaypause.setBackgroundResource( R.drawable.ic_play_arrow_white_24dp );
        ivplaypause.invalidate();
    }

    public void added( MDMSong song )
    {
        // nothing to do
    }

    public void added( MDMAlbum album )
    {
        // nothing to do
    }

    public void added( MDMPlaylist playlist )
    {
        // nothing to do
    }

    public void disconnected()
    {
        // nothing to do
    }

    public void connected()
    {
        // nothing to do
    }

}
