package org.messic.server.facade.controllers.pages;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.messic.server.api.APIGenre;
import org.messic.server.api.datamodel.MessicSettings;
import org.messic.server.api.datamodel.User;
import org.messic.server.datamodel.MDOMessicSettings;
import org.messic.server.datamodel.MDOUser;
import org.messic.server.datamodel.dao.DAOMessicSettings;
import org.messic.server.datamodel.dao.DAOUser;
import org.messic.server.facade.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SettingsViewController
{

    @Autowired
    private DAOMessicSettings daoSettings;

    @Autowired
    private DAOUser daoUser;

    @Autowired
    private APIGenre apiGenre;

    @RequestMapping( "/settings.do" )
    /**
     * This controller launches the creation user window. 
     * @param arg0
     * @param arg1
     * @return
     * @throws Exception
     */
    protected ModelAndView showSettings( HttpServletRequest arg0, HttpServletResponse arg1 )
        throws Exception
    {
        ModelAndView model = new ModelAndView( "settings" );

        MDOMessicSettings settings = this.daoSettings.getSettings();

        User user = SecurityUtil.getCurrentUser( true, this.daoUser );

        if ( user == null )
        {
            user = new User();
            if ( daoUser.getCount() == 0 )
            {
                user.setAdministrator( true );
            }
            else if ( !daoUser.existAdminUser() )
            {
                user.setAdministrator( true );
            }

            if ( settings.isAllowUserCreation() || user.getAdministrator() )
            {
                model.addObject( "creation", true );
            }
            else
            {
                // TODO take note
                throw new Exception(
                                     "Creation Users NOT ALLOWED! Who the hell are you? ...mmmm.. taking note: not to allow bad people like this guy to messic" );
            }
        }
        else
        {
            model.addObject( "creation", false );
        }

        if ( user.getAdministrator() )
        {
            // putting messic settings
            MessicSettings ms = new MessicSettings( settings );
            model.addObject( "settings", ms );

            // presenting all users (except administrator)
            List<MDOUser> mdousers = daoUser.getAll();
            List<User> users = new ArrayList<User>();
            for ( int i = 0; i < mdousers.size(); i++ )
            {
                if ( !mdousers.get( i ).getAdministrator() )
                {
                    users.add( new User( mdousers.get( i ) ) );
                }
            }
            if ( users.size() > 0 )
            {
                model.addObject( "users", users );
            }
        }

        model.addObject( "user", user );
        model.addObject( "genres", apiGenre.getAll( user ) );

        return model;
    }
}
