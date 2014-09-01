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
package org.messic.server.facade.controllers.rest;

import java.util.ArrayList;

import javax.servlet.ServletContext;

import org.jsondoc.core.pojo.JSONDoc;
import org.jsondoc.core.util.JSONDocUtils;
import org.messic.configuration.MessicConfig;
import org.messic.server.api.datamodel.Album;
import org.messic.server.facade.security.SecurityLoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping( "/jsondoc" )
public class JSONDocController
{
    @Autowired
    private ServletContext servletContext;

    private String version;

    private String basePath;

    public JSONDocController()
    {
        setVersion( MessicConfig.getCurrentVersion().toString() );
        setBasePath( "messic/services" );
    }

    public void setVersion( String version )
    {
        this.version = version;
    }

    public void setBasePath( String basePath )
    {
        this.basePath = basePath;
    }

    @RequestMapping( method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE )
    public @ResponseBody
    JSONDoc getApi()
    {
        ArrayList<String> packagesList = new ArrayList<String>();
        packagesList.add( SecurityLoginSuccessHandler.class.getPackage().getName() );
        packagesList.add( this.getClass().getPackage().getName() );
        packagesList.add( Album.class.getPackage().getName() );
        return JSONDocUtils.getApiDoc( version, basePath, packagesList );
    }

}
