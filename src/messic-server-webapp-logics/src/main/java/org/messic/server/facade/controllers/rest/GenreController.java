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

import java.util.List;

import org.apache.log4j.Logger;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiBodyObject;
import org.jsondoc.core.annotation.ApiError;
import org.jsondoc.core.annotation.ApiErrors;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiParam;
import org.jsondoc.core.annotation.ApiResponseObject;
import org.jsondoc.core.pojo.ApiParamType;
import org.jsondoc.core.pojo.ApiVerb;
import org.messic.server.api.APIGenre;
import org.messic.server.api.datamodel.Genre;
import org.messic.server.api.datamodel.User;
import org.messic.server.api.exceptions.SidNotFoundMessicException;
import org.messic.server.datamodel.dao.DAOUser;
import org.messic.server.facade.controllers.rest.exceptions.NotAuthorizedMessicRESTException;
import org.messic.server.facade.controllers.rest.exceptions.NotFoundMessicRESTException;
import org.messic.server.facade.controllers.rest.exceptions.UnknownMessicRESTException;
import org.messic.server.facade.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping( "/genres" )
@Api( name = "Genre services", description = "Methods for managing genres" )
public class GenreController
{
    private static Logger log = Logger.getLogger( GenreController.class );

    @Autowired
    public APIGenre genreAPI;

    @Autowired
    public DAOUser userDAO;

    @ApiMethod( path = "/genres/{genreSid}", verb = ApiVerb.DELETE, description = "Remove an existing genre with sid {genreSid}", produces = {} )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ),
        @ApiError( code = NotAuthorizedMessicRESTException.VALUE, description = "Forbidden access" ) } )
    @RequestMapping( value = "/{genreSid}", method = RequestMethod.DELETE )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    @ApiResponseObject
    public void removeGenre( @PathVariable
                             @ApiParam( name = "genreSid", description = "Sid of the genre to remove", paramType = ApiParamType.PATH, required = true )
                             Long genreSid )
        throws UnknownMessicRESTException, NotAuthorizedMessicRESTException
    {
        User user = SecurityUtil.getCurrentUser();
        try
        {
            genreAPI.removeGenre( user, genreSid );
        }
        catch ( Exception e )
        {
            log.error( "failed!", e );
            throw new UnknownMessicRESTException( e );
        }
    }

    @ApiMethod( path = "/genres?filterName=xxxx", verb = ApiVerb.GET, description = "Get all genres", produces = {
        MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ),
        @ApiError( code = NotAuthorizedMessicRESTException.VALUE, description = "Forbidden access" ) } )
    @RequestMapping( value = "", method = RequestMethod.GET )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    @ApiResponseObject
    public List<Genre> getAll( @RequestParam( value = "filterName", required = false )
                               @ApiParam( name = "filterName", description = "partial name of the genre to search", paramType = ApiParamType.QUERY, required = false )
                               String filterName )
        throws UnknownMessicRESTException, NotAuthorizedMessicRESTException
    {

        User user = SecurityUtil.getCurrentUser();
        try
        {
            List<Genre> genres = null;
            if ( filterName == null )
            {
                genres = genreAPI.getAll( user );
            }
            else
            {
                genres = genreAPI.findSimilar( user, filterName );
            }

            return genres;
        }
        catch ( Exception e )
        {
            log.error( "failed!", e );
            throw new UnknownMessicRESTException( e );
        }
    }

    @ApiMethod( path = "/genres/{genreSid}?newName={newGenreName}", verb = ApiVerb.POST, description = "Update a genre, basically rename it", produces = {
        MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ),
        @ApiError( code = NotAuthorizedMessicRESTException.VALUE, description = "Forbidden access" ),
        @ApiError( code = NotFoundMessicRESTException.VALUE, description = "Genre Not found" ) } )
    @RequestMapping( value = "/{genreSid}", method = RequestMethod.POST )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    @ApiResponseObject
    public void renameGenre( @PathVariable
                             @ApiParam( name = "genreSid", description = "Sid of the genre to update", paramType = ApiParamType.PATH, required = true )
                             Long genreSid,

                             @RequestParam( value = "newName", required = true )
                             @ApiParam( name = "newName", description = "new Name for the genre", paramType = ApiParamType.QUERY, required = true )
                             String newName )
        throws UnknownMessicRESTException, NotAuthorizedMessicRESTException, NotFoundMessicRESTException
    {

        User user = SecurityUtil.getCurrentUser();
        try
        {
            genreAPI.renameGenre( user, genreSid, newName );
        }
        catch ( SidNotFoundMessicException snfme )
        {
            throw new NotFoundMessicRESTException( snfme );
        }
        catch ( Exception e )
        {
            log.error( "failed!", e );
            throw new UnknownMessicRESTException( e );
        }
    }

    @ApiMethod( path = "/genres/fuse?newName={newNameValue}", verb = ApiVerb.POST, description = "Fuse a set of ids", produces = {
        MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ),
        @ApiError( code = NotAuthorizedMessicRESTException.VALUE, description = "Forbidden access" ),
        @ApiError( code = NotFoundMessicRESTException.VALUE, description = "Genre Not found" ) } )
    @RequestMapping( value = "/fuse", method = RequestMethod.POST )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    @ApiResponseObject
    public long fuseGenres( @RequestParam( value = "newName", required = true )
                            @ApiParam( name = "newName", description = "new Name for the genre that will fuse all the genres", paramType = ApiParamType.QUERY, required = true )
                            String newName, @ApiBodyObject
                            @RequestBody( required = true )
                            List<Long> genreSids )
        throws UnknownMessicRESTException, NotAuthorizedMessicRESTException, NotFoundMessicRESTException
    {

        User user = SecurityUtil.getCurrentUser();
        try
        {
            return genreAPI.fuseGenres( user, genreSids, newName );
        }
        catch ( Exception e )
        {
            log.error( "failed!", e );
            throw new UnknownMessicRESTException( e );
        }
    }

}
