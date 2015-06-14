package org.messic.android.activities.adapters;

import java.util.ArrayList;
import java.util.List;

import org.messic.android.R;
import org.messic.android.activities.adapters.SongAdapter.EventListener;
import org.messic.android.datamodel.MDMPlaylist;
import org.messic.android.datamodel.MDMSong;
import org.messic.android.util.AlbumCoverCache;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PlaylistAdapter
    extends BaseExpandableListAdapter
{
    private List<MDMPlaylist> playlists = new ArrayList<MDMPlaylist>();

    private LayoutInflater inflater = null;

    private Activity activity = null;

    private EventListener listener = null;

    private Animation anim = null;

    public PlaylistAdapter( Activity activity, EventListener listener )
    {
        this.inflater = LayoutInflater.from( activity );
        this.activity = activity;
        this.listener = listener;
        this.anim = AnimationUtils.loadAnimation( activity, android.R.anim.fade_in );
    }

    public void clear()
    {
        playlists = new ArrayList<MDMPlaylist>();
    }

    public void addPlaylist( MDMPlaylist playlist )
    {
        if ( this.playlists == null )
        {
            this.playlists = new ArrayList<MDMPlaylist>();
        }
        this.playlists.add( playlist );
    }

    public Object getChild( int groupPosition, int childPosition )
    {
        return this.playlists.get( groupPosition ).getSongs().get( childPosition );
    }

    public long getChildId( int groupPosition, int childPosition )
    {
        return this.playlists.get( groupPosition ).getSongs().get( childPosition ).getSid();
    }

    public View getChildView( int groupPosition, final int childPosition, boolean isLastChild, View convertView,
                              ViewGroup parent )
    {
        if ( convertView == null )
        {
            convertView = this.inflater.inflate( R.layout.songdetailed, null );
        }

        final MDMSong song = this.playlists.get( groupPosition ).getSongs().get( childPosition );
        ImageView icover = null;
        TextView tauthor = null;
        TextView tsongname = null;
        TextView talbum = null;
        icover = (ImageView) convertView.findViewById( R.id.songdetailed_icover );
        tauthor = (TextView) convertView.findViewById( R.id.songdetailed_tauthor );
        talbum = (TextView) convertView.findViewById( R.id.songdetailed_talbum );
        tsongname = (TextView) convertView.findViewById( R.id.songdetaileddd_tsong );

        final ImageView ficover = icover;

        ImageView ivremove = (ImageView) convertView.findViewById( R.id.songdetailed_ivremove );
        ivremove.setVisibility( View.GONE );

        tauthor.setText( song.getAlbum().getAuthor().getName() );
        talbum.setText( song.getAlbum().getName() );
        talbum.setPaintFlags( talbum.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG );
        tsongname.setText( song.getName() );
        icover.setImageResource( android.R.color.white );
        convertView.setTag( childPosition );
        final int fposition = childPosition;
        final View fCounterView = convertView;

        tauthor.setOnClickListener( new View.OnClickListener()
        {
            public void onClick( View arg0 )
            {
                listener.textTouch( song, childPosition );
            }
        } );
        talbum.setOnClickListener( new View.OnClickListener()
        {
            public void onClick( View arg0 )
            {
                listener.textTouch( song, childPosition );
            }
        } );
        tsongname.setOnClickListener( new View.OnClickListener()
        {
            public void onClick( View arg0 )
            {
                listener.textTouch( song, childPosition );
            }
        } );

        icover.setOnClickListener( new View.OnClickListener()
        {

            public void onClick( View v )
            {
                v.startAnimation( anim );
                listener.coverTouch( song, childPosition );
            }
        } );
        icover.setOnLongClickListener( new View.OnLongClickListener()
        {

            public boolean onLongClick( View v )
            {
                v.startAnimation( anim );
                listener.coverLongTouch( song, childPosition );
                return false;
            }
        } );

        Bitmap bm = AlbumCoverCache.getCover( song.getAlbum(), new AlbumCoverCache.CoverListener()
        {
            public void setCover( final Bitmap bitmap )
            {
                // just checking if the view is yet the hoped view (and haven't been recycled)
                if ( ( (Integer) fCounterView.getTag() ) == fposition )
                {
                    activity.runOnUiThread( new Runnable()
                    {

                        public void run()
                        {
                            ficover.setImageBitmap( bitmap );
                            ficover.invalidate();
                        }
                    } );
                }
            }

            public void failed( Exception e )
            {
                Log.e( "PlaylistAdapter!", e.getMessage(), e );
            }
        } );
        if ( bm != null )
        {
            icover.setImageBitmap( bm );
        }

        return convertView;

    }

    public int getChildrenCount( int groupPosition )
    {
        return this.playlists.get( groupPosition ).getSongs().size();
    }

    public Object getGroup( int groupPosition )
    {
        return this.playlists.get( groupPosition );
    }

    public int getGroupCount()
    {
        return this.playlists.size();
    }

    public long getGroupId( int groupPosition )
    {
        return this.playlists.get( groupPosition ).getSid();
    }

    public View getGroupView( final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent )
    {
        if ( convertView == null )
        {
            convertView = this.inflater.inflate( R.layout.playlist_parent, null );
        }
        TextView textView = (TextView) convertView.findViewById( R.id.playlist_tname );
        textView.setText( this.playlists.get( groupPosition ).getName() );

        ImageView ivplay = (ImageView) convertView.findViewById( R.id.playlist_ivplay );
        ivplay.setOnClickListener( new View.OnClickListener()
        {
            public void onClick( View arg0 )
            {
                listener.playlistTouch( playlists.get( groupPosition ), groupPosition );
            }
        } );
        return convertView;
    }

    public boolean hasStableIds()
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isChildSelectable( int groupPosition, int childPosition )
    {
        // TODO Auto-generated method stub
        return false;
    }

}
