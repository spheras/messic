package org.messic.server.facade.controllers.pages;

import java.util.ArrayList;
import java.util.List;

import org.messic.server.Util;
import org.messic.server.api.APIAlbum;
import org.messic.server.api.datamodel.Album;
import org.messic.server.api.musicinfo.service.MusicInfoPlugin;
import org.messic.server.datamodel.MDOUser;
import org.messic.server.datamodel.dao.DAOUser;
import org.messic.server.facade.controllers.rest.exceptions.NotAuthorizedMessicRESTException;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AlbumViewController
{
    @Autowired
    public APIAlbum apialbum;

    @Autowired
    public DAOUser userDAO;

    /**
     * Obtain the list musicInfo plugins
     * 
     * @return List<MusicInfo/> the list of plugins to execute
     */
    private List<MusicInfoPlugin> getMusicInfoPlugins()
    {

        BundleContext context = FrameworkUtil.getBundle( MusicInfoPlugin.class ).getBundleContext();
        ArrayList<MusicInfoPlugin> result = new ArrayList<MusicInfoPlugin>();

        try
        {
            // Query for all service references matching any TAGWizard plugin
            ServiceReference<?>[] refs =
                context.getServiceReferences( MusicInfoPlugin.class.getName(), "("
                    + MusicInfoPlugin.MUSIC_INFO_PLUGIN_NAME + "=*)" );
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
            e.printStackTrace( );
        }
        return result;
    }

    @RequestMapping( "/album.do" )
    protected ModelAndView upload( @RequestParam( value = "albumSid", required = true )
    Long albumSid )
        throws Exception
    {
        ModelAndView model = new ModelAndView( "album" );

        MDOUser mdouser = null;
        try
        {
            mdouser = Util.getAuthentication( userDAO );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            throw new NotAuthorizedMessicRESTException( e );
        }

        // getting the first characters of the authors, to allow listing them by start letter
        Album result = apialbum.getAlbum( mdouser, albumSid, true, true );
        model.addObject( "album", result );

        List<MusicInfoPlugin> plugins = getMusicInfoPlugins();
        model.addObject( "plugins", plugins );

        return model;
    }

}
