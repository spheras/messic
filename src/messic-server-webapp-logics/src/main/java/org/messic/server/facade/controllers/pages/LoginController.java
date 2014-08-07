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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.messic.server.datamodel.MDOMessicSettings;
import org.messic.server.datamodel.dao.DAOMessicSettings;
import org.messic.server.datamodel.dao.DAOUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController
{

    @Autowired
    private DAOMessicSettings daosettings;

    @Autowired
    private DAOUser daouser;

    @RequestMapping( "/login.do" )
    protected ModelAndView login( HttpServletRequest arg0, HttpServletResponse arg1 )
        throws Exception
    {
        ModelAndView model = new ModelAndView( "login" );

        MDOMessicSettings settings = daosettings.getSettings();
        long usersCount = daouser.getCount();

        model.addObject( "version", settings.getVersion() );
        if ( settings.isAllowUserCreation() )
        {
            model.addObject( "allowUserCreation", true );
        }
        else
        {
            // if there is no users in the database, it's allowed (if no there will never exist an user!)
            if ( usersCount <= 0 )
            {
                model.addObject( "allowUserCreation", true );
            }
            else
            {
                model.addObject( "allowUserCreation", false );
            }
        }

        if ( usersCount <= 0 )
        {
            model.addObject( "firstTime", true );
        }
        else
        {
            model.addObject( "firstTime", false );
        }

        return model;
    }

}
