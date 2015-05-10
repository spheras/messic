/*
 * Copyright (C) 2013
 *
 *  This file is part of Messic.
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.messic.server.facade.controllers.pages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.messic.server.api.APIAlbum;
import org.messic.server.api.datamodel.Album;
import org.messic.server.api.datamodel.User;
import org.messic.server.api.musicinfo.service.MusicInfoPlugin;
import org.messic.server.datamodel.dao.DAOUser;
import org.messic.server.facade.security.SecurityUtil;
import org.messic.server.facade.security.TokenManagementFilter;
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
    private Logger log = Logger.getLogger( AlbumViewController.class );

    @Autowired
    public APIAlbum apialbum;

    @Autowired
    public DAOUser userDAO;

    @Autowired
    public TokenManagementFilter tmf;

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

            Collections.sort( result, new Comparator<MusicInfoPlugin>()
            {

                @Override
                public int compare( MusicInfoPlugin o1, MusicInfoPlugin o2 )
                {
                    return o1.getName().compareTo( o2.getName() );
                }
            } );
        }
        catch ( Exception e )
        {
            log.error( "failed!", e );
        }
        return result;
    }

    @RequestMapping( "/album.do" )
    protected ModelAndView view( @RequestParam( value = "albumSid", required = true ) Long albumSid,
                                 HttpServletRequest req )
        throws Exception
    {
        ModelAndView model = new ModelAndView( "album" );

        User user = SecurityUtil.getCurrentUser();
        // getting the first characters of the authors, to allow listing them by start letter
        Album result = apialbum.getAlbum( user, albumSid, true, true, true );
        model.addObject( "token", this.tmf.obtainToken( req, tmf.getTokenParameter() ) );
        model.addObject( "album", result );

        List<MusicInfoPlugin> plugins = getMusicInfoPlugins();
        model.addObject( "plugins", plugins );

        return model;
    }

}
