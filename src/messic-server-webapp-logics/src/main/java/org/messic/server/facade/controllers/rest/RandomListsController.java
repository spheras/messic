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
import java.util.List;

import org.apache.log4j.Logger;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiError;
import org.jsondoc.core.annotation.ApiErrors;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiParam;
import org.jsondoc.core.annotation.ApiResponseObject;
import org.jsondoc.core.pojo.ApiParamType;
import org.jsondoc.core.pojo.ApiVerb;
import org.messic.server.api.APIRandomLists;
import org.messic.server.api.datamodel.RandomList;
import org.messic.server.api.datamodel.User;
import org.messic.server.datamodel.dao.DAOUser;
import org.messic.server.facade.controllers.rest.exceptions.NotAuthorizedMessicRESTException;
import org.messic.server.facade.controllers.rest.exceptions.UnknownMessicRESTException;
import org.messic.server.facade.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping( "/randomlists" )
@Api( name = "RandomList services", description = "Methods for managing random lists" )
public class RandomListsController
{
    private static Logger log = Logger.getLogger( RandomListsController.class );

    @Autowired
    public APIRandomLists randomListsAPI;

    @Autowired
    public DAOUser userDAO;

    @ApiMethod( path = "/services/randomlists?filterRandomListName=xxxx", verb = ApiVerb.GET, description = "Get random lists of music. It has the posibility to filter by a randomlist name", produces = {
        MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ),
        @ApiError( code = NotAuthorizedMessicRESTException.VALUE, description = "Forbidden access" ) } )
    @RequestMapping( value = "", method = RequestMethod.GET )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    @ApiResponseObject
    public List<RandomList> getAll( @RequestParam( value = "filterRandomListName", required = false ) @ApiParam( name = "filterRandomListName", description = "select a concrete randomlist by the name of it", paramType = ApiParamType.QUERY, required = false ) String filterRandomListName )
        throws UnknownMessicRESTException, NotAuthorizedMessicRESTException
    {
        User user = SecurityUtil.getCurrentUser();
        try
        {
            if ( filterRandomListName == null || filterRandomListName.length() <= 0 )
            {
                List<RandomList> lists = randomListsAPI.getAllLists( user );
                return lists;
            }
            else
            {
                List<RandomList> result = new ArrayList<RandomList>();
                RandomList rl = randomListsAPI.getList( user, filterRandomListName );
                result.add( rl );
                return result;
            }
        }
        catch ( Exception e )
        {
            log.error( "failed!", e );
            throw new UnknownMessicRESTException( e );
        }
    }

}
