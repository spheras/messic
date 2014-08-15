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

import java.io.IOException;

import org.apache.log4j.Logger;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiError;
import org.jsondoc.core.annotation.ApiErrors;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiResponseObject;
import org.jsondoc.core.pojo.ApiVerb;
import org.messic.configuration.MessicCheckUpdate;
import org.messic.configuration.MessicConfig;
import org.messic.configuration.MessicVersion;
import org.messic.server.api.datamodel.Update;
import org.messic.server.facade.controllers.rest.exceptions.NotAuthorizedMessicRESTException;
import org.messic.server.facade.controllers.rest.exceptions.UnknownMessicRESTException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author spheras
 */
@Controller
@RequestMapping( "/checkupdate" )
@Api( name = "Check Update Services", description = "Methods for check update version of messic" )
public class CheckUpdateController
{
    private static Logger log = Logger.getLogger( CheckUpdateController.class );

    @ApiMethod( path = "/checkupdate", verb = ApiVerb.GET, description = "Check the last stable version of messic at the server", produces = {} )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ),
        @ApiError( code = NotAuthorizedMessicRESTException.VALUE, description = "Forbidden access" ) } )
    @RequestMapping( value = "", method = RequestMethod.GET )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    @ApiResponseObject
    public Update checkupdate()
        throws UnknownMessicRESTException, NotAuthorizedMessicRESTException, IOException
    { 

        MessicVersion availablemv = MessicCheckUpdate.checkUpdate();
        MessicVersion currentmv = MessicConfig.getCurrentVersion();
        boolean needUpdate = ( availablemv.compareTo( currentmv ) > 0 );
        Update r = new Update( needUpdate, availablemv.sversion, currentmv.sversion );

        log.info( "Version available at the server: " + availablemv.version );
        log.info( "Need Update: " + needUpdate );

        return r;
    }

}
