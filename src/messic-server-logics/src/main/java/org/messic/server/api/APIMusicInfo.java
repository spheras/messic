package org.messic.server.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.messic.server.api.datamodel.MusicInfo;
import org.messic.server.api.musicinfo.service.MusicInfoPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.springframework.stereotype.Component;

@Component
public class APIMusicInfo
{

    /**
     * return the representative provider icon for the music infor plugin wich name is as pluginName
     * 
     * @param pluginName {@link String} name of the plugin
     * @return byte[] the image
     */
    public byte[] getMusicInfoProviderIcon( String pluginName )
    {
        MusicInfoPlugin plugin = getMusicInfoPlugin( pluginName );
        if ( plugin != null )
        {
            return plugin.getProviderIcon();
        }
        else
        {
            return null;
        }
    }

    private List<MusicInfoPlugin> getMusicInfoPlugins( String pluginName )
    {
        BundleContext context = FrameworkUtil.getBundle( MusicInfoPlugin.class ).getBundleContext();
        ArrayList<MusicInfoPlugin> result = new ArrayList<MusicInfoPlugin>();

        try
        {
            // Query for all service references matching any TAGWizard plugin
            ServiceReference<?>[] refs =
                context.getServiceReferences( MusicInfoPlugin.class.getName(), "("
                    + MusicInfoPlugin.MUSIC_INFO_PLUGIN_NAME + "=" + pluginName + ")" );
            if ( refs != null )
            {
                for ( int i = 0; i < refs.length; i++ )
                {
                    result.add( (MusicInfoPlugin) context.getService( refs[i] ) );
                }
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

        return result;

    }

    /**
     * Obtain the musicInfo plugin with the name
     * 
     * @return MusicInfoPlugin the plugins to execute
     */
    private MusicInfoPlugin getMusicInfoPlugin( String pluginName )
    {
        List<MusicInfoPlugin> result = getMusicInfoPlugins( pluginName );
        return result.get( 0 );
    }

    /**
     * Return the list of musicinfoplugins available
     * 
     * @return {@link List}<MusicInfoPlugin/> list of plugins availables
     */
    public List<org.messic.server.api.datamodel.MusicInfoPlugin> getMusicInfoPlugins()
    {
        List<MusicInfoPlugin> results = getMusicInfoPlugins( "*" );
        List<org.messic.server.api.datamodel.MusicInfoPlugin> result =
            new ArrayList<org.messic.server.api.datamodel.MusicInfoPlugin>();
        for ( int i = 0; i < results.size(); i++ )
        {
            MusicInfoPlugin mip = results.get( i );
            org.messic.server.api.datamodel.MusicInfoPlugin mip2 =
                new org.messic.server.api.datamodel.MusicInfoPlugin( mip.getName(), mip.getProviderName() );
            result.add( mip2 );
        }

        return result;
    }

    public MusicInfo getMusicInfo( Locale locale, String pluginName, String authorName, String albumName,
                                   String songName )
    {
        MusicInfoPlugin mip = getMusicInfoPlugin( pluginName );
        if ( mip != null )
        {
            if ( authorName != null && albumName != null && songName != null )
            {
                return new MusicInfo( mip.getSongInfo( locale, authorName, albumName, songName ) );
            }
            else if ( albumName != null && authorName != null )
            {
                return new MusicInfo( mip.getAlbumInfo( locale, authorName, albumName ) );
            }
            else if ( authorName != null )
            {
                return new MusicInfo( mip.getAuthorInfo( locale, authorName ) );
            }
            else
            {
                return new MusicInfo( "Not enough Information to obtain Music Info" );
            }
        }
        else
        {
            return new MusicInfo( "Plugin not found!" );
        }
    }
}
