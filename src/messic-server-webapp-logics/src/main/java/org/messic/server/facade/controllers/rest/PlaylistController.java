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
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

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
import org.messic.server.api.APIPlayLists;
import org.messic.server.api.datamodel.Playlist;
import org.messic.server.api.datamodel.User;
import org.messic.server.api.exceptions.ExistingMessicException;
import org.messic.server.api.exceptions.ResourceNotFoundMessicException;
import org.messic.server.api.exceptions.SidNotFoundMessicException;
import org.messic.server.datamodel.dao.DAOUser;
import org.messic.server.facade.controllers.rest.exceptions.DuplicatedMessicRESTException;
import org.messic.server.facade.controllers.rest.exceptions.IOMessicRESTException;
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

/**
 * @author spheras
 */
@Controller
@RequestMapping( "/playlists" )
@Api( name = "Playlists services", description = "Methods for managing playlists" )
public class PlaylistController
{
    private static Logger log = Logger.getLogger( PlaylistController.class );

    @Autowired
    public APIPlayLists playlistAPI;

    @Autowired
    public DAOUser userDAO;

    @ApiMethod( path = "/playlists/{playlistSid}", verb = ApiVerb.DELETE, description = "Remove a playlist with sid {playlistSid}", produces = {} )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ),
        @ApiError( code = NotAuthorizedMessicRESTException.VALUE, description = "Forbidden access" ) } )
    @RequestMapping( value = "/{playlistSid}", method = RequestMethod.DELETE )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    @ApiResponseObject
    public void removePlaylist( @PathVariable
                                @ApiParam( name = "playlistSid", description = "Sid of the playlist to remove", paramType = ApiParamType.PATH, required = true )
                                Long playlistSid )
        throws UnknownMessicRESTException, NotAuthorizedMessicRESTException
    {

        User user = SecurityUtil.getCurrentUser();
        try
        {
            playlistAPI.remove( user, playlistSid );
        }
        catch ( Exception e )
        {
            log.error( "failed!", e );
            throw new UnknownMessicRESTException( e );
        }
    }

    @ApiMethod( path = "/playlists", verb = ApiVerb.POST, description = "Create or Update a playlist", produces = {
        MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ),
        @ApiError( code = DuplicatedMessicRESTException.VALUE, description = "Duplicated playlist name" ),
        @ApiError( code = NotFoundMessicRESTException.VALUE, description = "some sid has not been found" ) } )
    @RequestMapping( value = "", method = RequestMethod.POST )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    @ApiResponseObject
    public void createOrUpdate( @ApiBodyObject
    @RequestBody
    Playlist playlist )
        throws UnknownMessicRESTException, NotAuthorizedMessicRESTException, NotFoundMessicRESTException,
        DuplicatedMessicRESTException
    {
        User user = SecurityUtil.getCurrentUser();

        try
        {
            playlistAPI.createOrUpdate( user, playlist );
        }
        catch ( SidNotFoundMessicException e )
        {
            throw new NotFoundMessicRESTException( e );
        }
        catch ( ExistingMessicException e )
        {
            throw new DuplicatedMessicRESTException( e );
        }
        catch ( Exception e )
        {
            log.error( "failed!", e );
            throw new UnknownMessicRESTException( e );
        }
    }

    @ApiMethod( path = "/playlists/{playlistSid}/zip", verb = ApiVerb.GET, description = "return a zip with the content (songs) of a certain playlist", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE } )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ),
        @ApiError( code = NotFoundMessicRESTException.VALUE, description = "Sid not found" ),
        @ApiError( code = IOMessicRESTException.VALUE, description = "IO error" ) } )
    @RequestMapping( value = "/{playlistSid}/zip", method = RequestMethod.GET )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    @ApiResponseObject
    public void getZip( @ApiParam( name = "playlistSid", description = "sid of the playlist to be zipped", paramType = ApiParamType.PATH, required = true )
                        @PathVariable
                        Long playlistSid, HttpServletResponse response )
        throws UnknownMessicRESTException, NotAuthorizedMessicRESTException, NotFoundMessicRESTException,
        IOMessicRESTException
    {
        User user = SecurityUtil.getCurrentUser();

        try
        {
            playlistAPI.getPlaylistZip( user, playlistSid, response.getOutputStream() );
        }
        catch ( IOException e )
        {
            log.error( "failed!", e );
            throw new IOMessicRESTException( e );
        }
        catch ( SidNotFoundMessicException e )
        {
            throw new NotFoundMessicRESTException( e );
        }
        catch ( Exception e )
        {
            throw new UnknownMessicRESTException( e );
        }

    }

    @ApiMethod( path = "/playlists?filterSid=${playlistSid}&songsInfo=true|false", verb = ApiVerb.GET, description = "Get all playlists", produces = {
        MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ),
        @ApiError( code = NotFoundMessicRESTException.VALUE, description = "Forbidden access" ) } )
    @RequestMapping( value = "", method = RequestMethod.GET )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    @ApiResponseObject
    public List<Playlist> getAll( @RequestParam( value = "filterSid", required = false )
                                  @ApiParam( name = "filterSid", description = "SID of the playlist to filter playlists", paramType = ApiParamType.QUERY, required = false )
                                  Long filterSid,
                                  @RequestParam( value = "songsInfo", required = false )
                                  @ApiParam( name = "songsInfo", description = "flag to return also the songs info of the playlist or not. By default, true", paramType = ApiParamType.QUERY, required = false, allowedvalues = {
                                      "true", "false" }, format = "Boolean" )
                                  Boolean songsInfo )
        throws UnknownMessicRESTException, NotAuthorizedMessicRESTException, NotFoundMessicRESTException
    {
        User user = SecurityUtil.getCurrentUser();

        try
        {
            if ( songsInfo == null )
            {
                songsInfo = new Boolean( true );
            }

            if ( filterSid == null || filterSid <= 0 )
            {
                return this.playlistAPI.getAllLists( user, songsInfo );
            }
            else
            {
                ArrayList<Playlist> result = new ArrayList<Playlist>();
                result.add( this.playlistAPI.getPlaylist( user, filterSid, songsInfo ) );
                return result;
            }
        }
        catch ( ResourceNotFoundMessicException rnfme )
        {
            throw new NotFoundMessicRESTException( rnfme );
        }
        catch ( Exception e )
        {
            log.error( "failed!", e );
            throw new UnknownMessicRESTException( e );
        }
    }

}
