package org.messic.server.api;

import java.io.File;
import java.io.IOException;

import org.messic.configuration.MessicConfig;
import org.messic.server.api.datamodel.User;
import org.messic.server.api.exceptions.ResourceNotFoundMessicException;
import org.messic.server.api.exceptions.SidNotFoundMessicException;
import org.messic.server.api.plugin.radio.MessicRadioInfo;
import org.messic.server.api.plugin.radio.MessicRadioPlugin;
import org.messic.server.api.plugin.radio.MessicRadioSong;
import org.messic.server.api.plugin.radio.MessicRadioStatus;
import org.messic.server.datamodel.MDOAlbumResource;
import org.messic.server.datamodel.MDOSong;
import org.messic.server.datamodel.dao.DAOMessicSettings;
import org.messic.server.datamodel.dao.DAOSong;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class APIRadio
{
    private static MessicConfig messicConfig = new MessicConfig();

    private static MessicRadioPlugin plugin = null;

    @Autowired
    private DAOSong daoSong;

    @Autowired
    private DAOMessicSettings daoSettings;

    @Autowired
    private APIAlbum apialbum;

    public byte[] getAlbumCover( Integer preferredWidth, Integer preferredHeight )
        throws SidNotFoundMessicException, ResourceNotFoundMessicException, IOException
    {

        long songSid = getInfo().songSid;
        if ( songSid > 0 )
        {
            MDOSong song = daoSong.get( songSid );
            if ( song != null )
            {
                return apialbum.getAlbumCover( new User( song.getOwner() ), song.getAlbum().getSid(), preferredWidth,
                                               preferredHeight );
            }
        }
        return null;
    }

    private static MessicRadioPlugin getPlugin()
    {

        if ( plugin == null )
        {
            BundleContext context = FrameworkUtil.getBundle( MessicRadioPlugin.class ).getBundleContext();
            // Query for all service references matching any TAGWizard plugin
            ServiceReference<?>[] refs;
            try
            {
                refs =
                    context.getServiceReferences( MessicRadioPlugin.class.getName(), "("
                        + MessicRadioPlugin.MESSIC_RADIO_PLUGIN_NAME + "=*)" );
            }
            catch ( InvalidSyntaxException e )
            {
                e.printStackTrace();
                return null;
            }

            if ( refs.length >= 1 )
            {
                MessicRadioPlugin mrp = (MessicRadioPlugin) context.getService( refs[0] );
                plugin = mrp;
                plugin.setConfiguration( messicConfig.getConfiguration() );
            }
        }

        return plugin;

    }

    public MessicRadioStatus getStatus()
    {
        MessicRadioPlugin mrp = getPlugin();
        if ( mrp != null )
        {
            return mrp.getStatus();
        }
        else
        {
            return MessicRadioStatus.NOT_AVAILABLE;
        }
    }

    public MessicRadioInfo getInfo()
    {
        MessicRadioPlugin mrp = getPlugin();
        if ( mrp != null )
        {
            return mrp.getInfo();
        }
        else
        {
            MessicRadioInfo mri = new MessicRadioInfo();
            mri.status = MessicRadioStatus.NOT_AVAILABLE;
            return mri;
        }

    }

    public void stopRadio()
    {
        MessicRadioPlugin mrp = getPlugin();
        if ( mrp != null )
        {
            mrp.stopCast();
        }

    }

    public void playSong( String username, long sidSong, int clientQueuePosition )
        throws IOException
    {
        MDOSong song = daoSong.get( username, sidSong );
        if ( song != null )
        {
            MessicRadioPlugin mrp = getPlugin();
            File mp3song = new File( song.calculateAbsolutePath( daoSettings.getSettings() ) );
            if ( mp3song.exists() )
            {
                MessicRadioSong mrs = new MessicRadioSong();
                mrs.songFile = mp3song;
                mrs.authorName = song.getAlbum().getAuthor().getName();
                mrs.albumName = song.getAlbum().getName();
                mrs.albumyear = song.getAlbum().getYear();
                MDOAlbumResource mar = song.getAlbum().getCover();
                if ( mar != null )
                {
                    mrs.coverImage = new File( mar.calculateAbsolutePath( daoSettings.getSettings() ) );
                }
                mrs.songSid = song.getSid();
                mrs.songName = song.getName();
                mrs.trackNumber = song.getTrack();
                mrs.albumGenre = ( song.getAlbum().getGenre() != null ? song.getAlbum().getGenre().getName() : "" );
                mrs.albumComments = song.getAlbum().getComments();
                mrs.clientQueuePosition = clientQueuePosition;
                mrp.castSong( mrs );
            }
        }
    }

    public void startRadio()
        throws Exception
    {
        MessicRadioPlugin mrp = getPlugin();
        if ( mrp != null )
        {
            mrp.startCast();

        }
        else
        {
            throw new Exception( "Not available" );
        }
    }
}
