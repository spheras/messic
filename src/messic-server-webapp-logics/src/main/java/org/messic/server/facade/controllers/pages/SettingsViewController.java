package org.messic.server.facade.controllers.pages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.messic.server.api.datamodel.MessicSettings;
import org.messic.server.api.datamodel.User;
import org.messic.server.datamodel.MDOMessicSettings;
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

            if ( settings.isAllowUserCreation() || user.getAdministrator())
            {
                model.addObject( "creation", true );
                user.setStorePath( settings.getGenericBaseStorePath() );
            }
            else
            {
                // TODO take note
                throw new Exception( "Creation Users NOT ALLOWED! Who are you?" );
            }
        }
        else
        {
            model.addObject( "creation", false );
        }

        if ( user.getAdministrator() )
        {
            MessicSettings ms = new MessicSettings( settings );
            model.addObject( "settings", ms );
        }

        model.addObject( "user", user );
        model.addObject( "allowSpecificMusicFolder", ( user.getAdministrator() || settings.isAllowUserSpecificFolder() ) );

        return model;
    }

}
