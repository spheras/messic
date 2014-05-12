package org.messic.server.facade.controllers.pages;

import java.util.ArrayList;
import java.util.List;

import org.messic.server.Util;
import org.messic.server.api.APIAuthor;
import org.messic.server.api.datamodel.Author;
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
    public APIAuthor apiauthor;

    @Autowired
    public DAOUser userDAO;

    @RequestMapping( "/author.do" )
    protected ModelAndView view( @RequestParam( value = "authorSid", required = true )
    Long authorSid )
        throws Exception
    {
        ModelAndView model = new ModelAndView( "author" );

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
        Author result = apiauthor.getAuthor( mdouser, authorSid, true, true);
        model.addObject( "author", result );

        List<MusicInfoPlugin> plugins = getMusicInfoPlugins();
        model.addObject( "plugins", plugins );

        return model;
    }

}
