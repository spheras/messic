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

import java.util.Locale;

import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiError;
import org.jsondoc.core.annotation.ApiErrors;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiParam;
import org.jsondoc.core.annotation.ApiResponseObject;
import org.jsondoc.core.pojo.ApiParamType;
import org.jsondoc.core.pojo.ApiVerb;
import org.messic.server.api.APIMusicInfo;
import org.messic.server.api.datamodel.MusicInfo;
import org.messic.server.facade.controllers.rest.exceptions.IOMessicRESTException;
import org.messic.server.facade.controllers.rest.exceptions.NotAuthorizedMessicRESTException;
import org.messic.server.facade.controllers.rest.exceptions.NotFoundMessicRESTException;
import org.messic.server.facade.controllers.rest.exceptions.UnknownMessicRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Album Rest Controller
 * 
 * @author spheras
 */
@Controller
@RequestMapping( "/musicinfo" )
@Api( name = "MusicInfo services", description = "Methods to obtain music info from external providers" )
public class MusicInfoController
{
    @Autowired
    public APIMusicInfo musicInfoAPI;

    @ApiMethod( path = "/musicinfo/providericon?pluginName=xxxx", verb = ApiVerb.GET, description = "Get icon image of a certain musicinfo plugin", produces = { MediaType.IMAGE_JPEG_VALUE } )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ),
        @ApiError( code = NotAuthorizedMessicRESTException.VALUE, description = "Forbidden access" ),
        @ApiError( code = NotFoundMessicRESTException.VALUE, description = "Plugin not found" ),
        @ApiError( code = IOMessicRESTException.VALUE, description = "IO internal server error" ), } )
    @RequestMapping( value = "/providericon", method = RequestMethod.GET )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    @ApiResponseObject
    public ResponseEntity<byte[]> getProviderIcon( @RequestParam( value = "pluginName", required = true )
                                                   @ApiParam( name = "pluginName", description = "Name of the plugin to obtain information", paramType = ApiParamType.QUERY, required = true )
                                                   String pluginName )
        throws UnknownMessicRESTException, NotAuthorizedMessicRESTException, NotFoundMessicRESTException,
        IOMessicRESTException
    {

        byte[] content = musicInfoAPI.getMusicInfoProviderIcon( pluginName );
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType( MediaType.IMAGE_JPEG );
        return new ResponseEntity<byte[]>( content, headers, HttpStatus.OK );
    }

    @ApiMethod( path = "/musicinfo?pluginName=xxxx&songName=xxxx&albumName=xxxx&authorName=xxxxx", verb = ApiVerb.GET, description = "Get Music Info for album/song/author using the plugin called like the pluginName parameter. If no pluginName parameter is presented, then this will return all the availables plugins names that can be used.", produces = {
        MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ),
        @ApiError( code = NotAuthorizedMessicRESTException.VALUE, description = "Forbidden access" ) } )
    @RequestMapping( value = "", method = RequestMethod.GET )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    @ApiResponseObject
    public MusicInfo getInfo( @RequestParam( value = "pluginName", required = false )
                              @ApiParam( name = "pluginName", description = "Name of the plugin to obtain information.. if this information is not present, the function will return all the plugin names availables at the system.", paramType = ApiParamType.QUERY, required = false )
                              String pluginName,
                              @RequestParam( value = "songName", required = false )
                              @ApiParam( name = "songName", description = "Name of the song to search", paramType = ApiParamType.QUERY, required = false )
                              String songName,
                              @RequestParam( value = "albumName", required = false )
                              @ApiParam( name = "albumName", description = "Name of the album to search", paramType = ApiParamType.QUERY, required = false )
                              String albumName,
                              @RequestParam( value = "authorName", required = false )
                              @ApiParam( name = "authorName", description = "Name of the author to search.. is required only if a pluginName is presented, if not, it's not necessary.", paramType = ApiParamType.QUERY, required = true )
                              String authorName, Locale locale )
        throws UnknownMessicRESTException, NotAuthorizedMessicRESTException
    {
        try
        {
            if ( pluginName != null )
            {
                return musicInfoAPI.getMusicInfo( locale, pluginName, authorName, albumName, songName );
            }
            else
            {
                MusicInfo mi = new MusicInfo( "" );
                mi.setProviders( musicInfoAPI.getMusicInfoPlugins() );
                return mi;
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            throw new UnknownMessicRESTException( e );
        }
    }

}