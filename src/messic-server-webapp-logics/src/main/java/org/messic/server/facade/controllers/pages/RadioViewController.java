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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.messic.server.api.APIRadio;
import org.messic.server.api.plugin.radio.MessicRadioInfo;
import org.messic.server.datamodel.MDOMessicSettings;
import org.messic.server.datamodel.dao.DAOMessicSettings;
import org.messic.server.datamodel.dao.DAOUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.io.Closeables;

@Controller
public class RadioViewController
{

    @Autowired
    private DAOMessicSettings daosettings;

    @Autowired
    private APIRadio apiradio;

    @Autowired
    private DAOUser daouser;

    /**
     * Obtain a timestamp based on maven. This timestamp allow to the .css and .js files to force to be updated by the
     * navigator. If a new version of messic is released, this number will change and the navigator will update those
     * files.
     * 
     * @return {@link String} the timestamp
     */
    private String getTimestamp()
    {

        ClassPathResource resource =
            new ClassPathResource( "/org/messic/server/facade/controllers/pages/timestamp.properties" );
        Properties p = new Properties();
        InputStream inputStream = null;
        try
        {
            inputStream = resource.getInputStream();
            p.load( inputStream );
        }
        catch ( IOException e )
        {
            return "12345";
        }
        finally
        {
            Closeables.closeQuietly( inputStream );
        }

        return p.getProperty( "messic.timestamp" );
    }

    @RequestMapping( "/radio.do" )
    protected ModelAndView login( HttpServletRequest arg0, HttpServletResponse arg1 )
        throws Exception
    {
        ModelAndView model = new ModelAndView( "radio" );

        MDOMessicSettings settings = daosettings.getSettings();

        model.addObject( "version", settings.getVersion() );
        model.addObject( "timestamp", getTimestamp() );

        // checking radio availability
        switch ( apiradio.getStatus() )
        {
            case NONE:
            case NOT_AVAILABLE:
            case NOT_ENABLED:
            case NOT_STARTED:
                model.addObject( "radioStatus", false );
                break;
            default:
                model.addObject( "radioStatus", true );
                MessicRadioInfo mri = apiradio.getInfo();
                model.addObject( "radioInfo", mri );
                
                String radioInfoUrl="";
                if(mri.publicURLHost!=null && mri.publicURLHost.length()>0){
                    radioInfoUrl="http://"+mri.publicURLHost+":"+mri.publicURLPort+mri.publicURLPath;
                }else{
                    radioInfoUrl="http://"+arg0.getServerName()+":"+mri.publicURLPort+mri.publicURLPath;
                }
                model.addObject( "radioInfoURL", radioInfoUrl );
        }

        return model;
    }
}
