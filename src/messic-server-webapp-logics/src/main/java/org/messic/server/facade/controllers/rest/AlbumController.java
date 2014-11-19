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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import org.messic.server.Util;
import org.messic.server.api.APIAlbum;
import org.messic.server.api.APITagWizard;
import org.messic.server.api.datamodel.Album;
import org.messic.server.api.datamodel.TAGWizardPlugin;
import org.messic.server.api.datamodel.User;
import org.messic.server.api.exceptions.ExistingMessicException;
import org.messic.server.api.exceptions.ResourceNotFoundMessicException;
import org.messic.server.api.exceptions.SidNotFoundMessicException;
import org.messic.server.datamodel.dao.DAOUser;
import org.messic.server.facade.controllers.rest.exceptions.DuplicatedMessicRESTException;
import org.messic.server.facade.controllers.rest.exceptions.IOMessicRESTException;
import org.messic.server.facade.controllers.rest.exceptions.MusicTagsMessicRESTException;
import org.messic.server.facade.controllers.rest.exceptions.NotAuthorizedMessicRESTException;
import org.messic.server.facade.controllers.rest.exceptions.NotFoundMessicRESTException;
import org.messic.server.facade.controllers.rest.exceptions.UnknownMessicRESTException;
import org.messic.server.facade.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.HtmlUtils;

/**
 * Album Rest Controller
 * 
 * @author spheras
 */
@Controller
@RequestMapping( "/albums" )
@Api( name = "Album services", description = "Methods for managing albums" )
public class AlbumController
{
    private Logger log = Logger.getLogger( AlbumController.class );

    @Autowired
    public APIAlbum albumAPI;

    @Autowired
    public DAOUser userDAO;

    @Autowired
    public APITagWizard wizardAPI;

    @ApiMethod( path = "/services/albums?filterGenreSid=xxxx&filterAuthorSid=xxxx&filterName=xxxx&songsInfo=true|false&authorInfo=true|false", verb = ApiVerb.GET, description = "Get all albums. They can be filtered by authorSid or by genreSid (not combined). You can also espcify what information should be returned (with songs information or not, for exmaple)", produces = {
        MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ),
        @ApiError( code = NotAuthorizedMessicRESTException.VALUE, description = "Forbidden access" ) } )
    @RequestMapping( value = "", method = RequestMethod.GET )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    @ApiResponseObject
    public List<Album> getAll( @RequestParam( value = "filterGenreSid", required = false ) @ApiParam( name = "filterGenreSid", description = "SID of the genre to filter albums", paramType = ApiParamType.QUERY, required = false ) Integer filterGenreSid,
                               @RequestParam( value = "filterAuthorSid", required = false ) @ApiParam( name = "filterAuthorSid", description = "SID of the author to filter albums", paramType = ApiParamType.QUERY, required = false ) Integer filterAuthorSid,
                               @RequestParam( value = "filterName", required = false ) @ApiParam( name = "filterName", description = "partial name of the album to search", paramType = ApiParamType.QUERY, required = false ) String filterName,
                               @RequestParam( value = "authorInfo", required = false ) @ApiParam( name = "authorInfo", description = "flag to return also the author info of the albums or not. By default, true", paramType = ApiParamType.QUERY, required = false, allowedvalues = {
                                   "true", "false" }, format = "Boolean" ) Boolean authorInfo,
                               @RequestParam( value = "songsInfo", required = false ) @ApiParam( name = "songsInfo", description = "flag to return also the songs info of the albums or not. By default, false", paramType = ApiParamType.QUERY, required = false, allowedvalues = {
                                   "true", "false" }, format = "Boolean" ) Boolean songsInfo,
                               @RequestParam( value = "resourcesInfo", required = false ) @ApiParam( name = "resourcesInfo", description = "flag to return also the artworks and others info of the albums or not. By default, the same value of songsInfo", paramType = ApiParamType.QUERY, required = false, allowedvalues = {
                                   "true", "false" }, format = "Boolean" ) Boolean resourcesInfo )
        throws UnknownMessicRESTException, NotAuthorizedMessicRESTException
    {
        User user = SecurityUtil.getCurrentUser();

        try
        {
            List<Album> albums = null;
            if ( filterAuthorSid == null && filterName == null & filterGenreSid == null )
            {
                albums =
                    albumAPI.getAll( user, ( authorInfo != null ? authorInfo : true ), ( songsInfo != null ? songsInfo
                                    : false ), ( resourcesInfo != null ? resourcesInfo
                                    : ( songsInfo != null ? songsInfo : false ) ) );
            }
            else
            {
                if ( filterAuthorSid != null && filterName == null )
                {
                    albums =
                        albumAPI.getAll( user, filterAuthorSid, ( authorInfo != null ? authorInfo : true ),
                                         ( songsInfo != null ? songsInfo : false ),
                                         ( resourcesInfo != null ? resourcesInfo : ( songsInfo != null ? songsInfo
                                                         : false ) ) );
                }
                if ( filterGenreSid != null )
                {
                    albums =
                        albumAPI.getAllOfGenre( user, filterGenreSid, ( authorInfo != null ? authorInfo : true ),
                                                ( songsInfo != null ? songsInfo : false ),
                                                ( resourcesInfo != null ? resourcesInfo
                                                                : ( songsInfo != null ? songsInfo : false ) ) );
                }
                else
                {
                    albums =
                        albumAPI.findSimilar( user, filterAuthorSid, filterName, ( authorInfo != null ? authorInfo
                                                              : true ), ( songsInfo != null ? songsInfo : false ),
                                              ( resourcesInfo != null ? resourcesInfo : ( songsInfo != null ? songsInfo
                                                              : false ) ) );
                }
            }

            return albums;
        }
        catch ( Exception e )
        {
            log.error( "failed!", e );
            throw new UnknownMessicRESTException( e );
        }
    }

    @ApiMethod( path = "/services/albums/{albumSid}/zip", verb = ApiVerb.GET, description = "Get the album binary, get the whole album zipped", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE } )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ),
        @ApiError( code = NotAuthorizedMessicRESTException.VALUE, description = "Forbidden access" ),
        @ApiError( code = IOMessicRESTException.VALUE, description = "IO error trying to get the album resource" ) } )
    @RequestMapping( value = "/{albumSid}/zip", method = RequestMethod.GET )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    @ApiResponseObject
    public void getAlbumZip( @ApiParam( name = "albumSid", description = "SID of the album resource we want to download", paramType = ApiParamType.PATH, required = true ) @PathVariable Long albumSid,
                             HttpServletResponse response )
        throws NotAuthorizedMessicRESTException, IOMessicRESTException, UnknownMessicRESTException
    {

        User user = SecurityUtil.getCurrentUser();
        try
        {
            Album album = albumAPI.getAlbum( user, albumSid, true, false, false );
            String fileName = album.getAuthor().getName() + "-" + album.getName() + ".zip";
            response.setHeader( "Content-disposition", "attachment; filename=\"" + fileName + "\"" );

            albumAPI.getAlbumZip( user, albumSid, response.getOutputStream() );

        }
        catch ( IOException ioe )
        {
            log.error( "failed!", ioe );
            throw new IOMessicRESTException( ioe );
        }
        catch ( Exception e )
        {
            log.error( "failed!", e );
            throw new UnknownMessicRESTException( e );
        }
    }

    @ApiMethod( path = "/services/albums/{albumSid}", verb = ApiVerb.DELETE, description = "Remove an album with sid {albumSid}", produces = {} )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ),
        @ApiError( code = NotAuthorizedMessicRESTException.VALUE, description = "Forbidden access" ) } )
    @RequestMapping( value = "/{albumSid}", method = RequestMethod.DELETE )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    @ApiResponseObject
    public void removeAlbum( @PathVariable @ApiParam( name = "albumSid", description = "Sid of the album to remove", paramType = ApiParamType.PATH, required = true ) Long albumSid )
        throws UnknownMessicRESTException, NotAuthorizedMessicRESTException
    {
        User user = SecurityUtil.getCurrentUser();
        try
        {
            albumAPI.remove( user, albumSid );
        }
        catch ( Exception e )
        {
            log.error( "failed!", e );
            throw new UnknownMessicRESTException( e );
        }
    }

    @ApiMethod( path = "/services/albums/{albumSid}?songsInfo=true|false&authorInfo=true|false", verb = ApiVerb.GET, description = "Get album with id {albumSid}", produces = {
        MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ),
        @ApiError( code = NotAuthorizedMessicRESTException.VALUE, description = "Forbidden access" ) } )
    @RequestMapping( value = "/{albumSid}", method = RequestMethod.GET )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    @ApiResponseObject
    public Album getAlbum( @PathVariable @ApiParam( name = "albumSid", description = "Sid of the album to get", paramType = ApiParamType.PATH, required = true ) Long albumSid,
                           @RequestParam( value = "songsInfo", required = false ) @ApiParam( name = "songsInfo", description = "flag to return also the songs info of the albums or not. By default, false", paramType = ApiParamType.QUERY, required = false, allowedvalues = {
                               "true", "false" }, format = "Boolean" ) Boolean songsInfo,
                           @RequestParam( value = "authorInfo", required = false ) @ApiParam( name = "authorInfo", description = "flag to return also the author info of the albums or not. By default, true", paramType = ApiParamType.QUERY, required = false, allowedvalues = {
                               "true", "false" }, format = "Boolean" ) Boolean authorInfo,
                           @RequestParam( value = "resourcesInfo", required = false ) @ApiParam( name = "resourcesInfo", description = "flag to return also the artworks and others info of the albums or not. By default, the same value of songsInfo", paramType = ApiParamType.QUERY, required = false, allowedvalues = {
                               "true", "false" }, format = "Boolean" ) Boolean resourcesInfo )
        throws UnknownMessicRESTException, NotAuthorizedMessicRESTException
    {

        User user = SecurityUtil.getCurrentUser();
        try
        {
            Album album =
                albumAPI.getAlbum( user, albumSid, ( authorInfo != null ? authorInfo : true ),
                                   ( songsInfo != null ? songsInfo : false ), ( resourcesInfo != null ? resourcesInfo
                                                   : ( songsInfo != null ? songsInfo : false ) ) );
            return album;
        }
        catch ( Exception e )
        {
            log.error( "failed!", e );
            throw new UnknownMessicRESTException( e );
        }
    }

    @ApiMethod( path = "/services/albums/{resourceSid}/resource", verb = ApiVerb.GET, description = "Get a resource of an album", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE } )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ),
        @ApiError( code = NotAuthorizedMessicRESTException.VALUE, description = "Forbidden access" ),
        @ApiError( code = NotFoundMessicRESTException.VALUE, description = "Resource not found" ),
        @ApiError( code = IOMessicRESTException.VALUE, description = "IO internal server error" ), } )
    @RequestMapping( value = "/{resourceSid}/resource", method = RequestMethod.GET )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    @ApiResponseObject
    public ResponseEntity<byte[]> getAlbumResource( @PathVariable @ApiParam( name = "resourceSid", description = "SID of the resource to get", paramType = ApiParamType.PATH, required = true ) Long resourceSid )
        throws UnknownMessicRESTException, NotAuthorizedMessicRESTException, NotFoundMessicRESTException,
        IOMessicRESTException
    {

        User user = SecurityUtil.getCurrentUser();
        try
        {
            byte[] content = albumAPI.getAlbumResource( user, resourceSid );
            if ( content == null || content.length == 0 )
            {
                InputStream is = AlbumController.class.getResourceAsStream( "/org/messic/img/unknowncover.jpg" );
                content = Util.readInputStream( is );
            }
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType( MediaType.IMAGE_JPEG );
            return new ResponseEntity<byte[]>( content, headers, HttpStatus.OK );
        }
        catch ( SidNotFoundMessicException e )
        {
            throw new NotFoundMessicRESTException( e );
        }
        catch ( ResourceNotFoundMessicException e )
        {
            throw new NotFoundMessicRESTException( e );
        }
        catch ( IOException e )
        {
            throw new IOMessicRESTException( e );
        }
    }

    @ApiMethod( path = "/services/albums/{albumSid}/{resourceSid}/cover", verb = ApiVerb.POST, description = "Set cover for a certain album", produces = {
        MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ),
        @ApiError( code = NotAuthorizedMessicRESTException.VALUE, description = "Forbidden access" ),
        @ApiError( code = IOMessicRESTException.VALUE, description = "IO internal server error" ), } )
    @RequestMapping( value = "/{albumSid}/{resourceSid}/cover", method = RequestMethod.POST )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    @ApiResponseObject
    public void setAlbumCover( @PathVariable @ApiParam( name = "albumSid", description = "SID of the album to get the cover", paramType = ApiParamType.PATH, required = true ) Long albumSid,
                               @PathVariable @ApiParam( name = "resourceSid", description = "SID of the resource of the album to set as cover", paramType = ApiParamType.PATH, required = true ) Long resourceSid )
        throws UnknownMessicRESTException, NotAuthorizedMessicRESTException, NotFoundMessicRESTException,
        IOMessicRESTException
    {
        User user = SecurityUtil.getCurrentUser();
        try
        {
            albumAPI.setAlbumCover( user, albumSid, resourceSid );
        }
        catch ( Exception e )
        {
            log.error( "failed!", e );
            throw new UnknownMessicRESTException( e );
        }
    }

    @ApiMethod( path = "/services/albums/{albumSid}/cover", verb = ApiVerb.GET, description = "Get cover for a certain album", produces = { MediaType.IMAGE_JPEG_VALUE } )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ),
        @ApiError( code = NotAuthorizedMessicRESTException.VALUE, description = "Forbidden access" ),
        @ApiError( code = NotFoundMessicRESTException.VALUE, description = "Album or Cover not found" ),
        @ApiError( code = IOMessicRESTException.VALUE, description = "IO internal server error" ), } )
    @RequestMapping( value = "/{albumSid}/cover", method = RequestMethod.GET )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    @ApiResponseObject
    public ResponseEntity<byte[]> getAlbumCover( @PathVariable @ApiParam( name = "albumSid", description = "SID of the album to get the cover", paramType = ApiParamType.PATH, required = true ) Long albumSid,
                                                 @RequestParam( value = "preferredWidth", required = false ) @ApiParam( name = "preferredWidth", description = "desired width for the image returned.  The service will try to provide the desired width, it is just only informative, to try to optimize the performance, avoiding to return images too much big", paramType = ApiParamType.QUERY, required = false, format = "Integer" ) Integer preferredWidth,
                                                 @RequestParam( value = "preferredHeight", required = false ) @ApiParam( name = "preferredHeight", description = "desired height for the image returned.  The service will try to provide the desired height, it is just only informative, to try to optimize the performance, avoiding to return images too much big", paramType = ApiParamType.QUERY, required = false, format = "Integer" ) Integer preferredHeight )
        throws UnknownMessicRESTException, NotAuthorizedMessicRESTException, NotFoundMessicRESTException,
        IOMessicRESTException
    {

        User user = SecurityUtil.getCurrentUser();
        try
        {
            byte[] content = albumAPI.getAlbumCover( user, albumSid, preferredWidth, preferredHeight );
            if ( content == null || content.length == 0 )
            {
                InputStream is = AlbumController.class.getResourceAsStream( "/org/messic/img/unknowncover.jpg" );
                content = Util.readInputStream( is );
            }
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType( MediaType.IMAGE_JPEG );
            return new ResponseEntity<byte[]>( content, headers, HttpStatus.OK );
        }
        catch ( SidNotFoundMessicException e )
        {
            throw new NotFoundMessicRESTException( e );
        }
        catch ( ResourceNotFoundMessicException e )
        {
            InputStream is = AlbumController.class.getResourceAsStream( "/org/messic/img/unknowncover.jpg" );
            byte[] content = null;
            try
            {
                content = Util.readInputStream( is );
            }
            catch ( IOException e1 )
            {
                throw new NotFoundMessicRESTException( e );
            }
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType( MediaType.IMAGE_JPEG );
            return new ResponseEntity<byte[]>( content, headers, HttpStatus.OK );
        }
        catch ( IOException e )
        {
            throw new IOMessicRESTException( e );
        }
    }

    @ApiMethod( path = "/services/albums", verb = ApiVerb.POST, description = "Create or Update an album.  Before creation you need to upload the resources!.  Return the Sid of the album created/updated", produces = {
        MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, consumes = {
        MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } )
    @ApiErrors( apierrors = {
        @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ),
        @ApiError( code = NotAuthorizedMessicRESTException.VALUE, description = "Forbidden access" ),
        @ApiError( code = IOMessicRESTException.VALUE, description = "IO internal server error" ),
        @ApiError( code = DuplicatedMessicRESTException.VALUE, description = "The album is duplicated, the same album name for the same author name" ), } )
    @RequestMapping( value = "", method = RequestMethod.POST )
    @ResponseBody
    @ApiResponseObject
    public Long createOrUpdateAlbum( @ApiBodyObject @RequestBody Album album )
        throws UnknownMessicRESTException, NotAuthorizedMessicRESTException, NotFoundMessicRESTException,
        IOMessicRESTException, DuplicatedMessicRESTException
    {

        User user = SecurityUtil.getCurrentUser();
        try
        {
            Long newSid = albumAPI.createOrUpdateAlbum( user, album );
            return newSid;// new ResponseEntity<HttpStatus>( HttpStatus.OK );
        }
        catch ( IOException e )
        {
            log.error( "failed!", e );
            throw new IOMessicRESTException( e );
        }
        catch ( ExistingMessicException e )
        {
            log.error( "failed!", e );
            throw new DuplicatedMessicRESTException( e );
        }
        catch ( Exception e )
        {
            log.error( "failed!", e );
            throw new UnknownMessicRESTException( e );
        }

    }

    @ApiMethod( path = "/services/albums/{albumCode}?fileName=xxxxx", verb = ApiVerb.PUT, description = "Upload a resource for an album. This resources are stored at the temporal folder, waiting until save Album process. The client must post the binary content of the resource.", produces = {
        MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, consumes = { MediaType.APPLICATION_OCTET_STREAM_VALUE } )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ),
        @ApiError( code = NotAuthorizedMessicRESTException.VALUE, description = "Forbidden access" ),
        @ApiError( code = IOMessicRESTException.VALUE, description = "IO internal server error" ), } )
    @RequestMapping( value = "/{albumCode}", method = RequestMethod.PUT )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    @ApiResponseObject
    public HttpEntity<HttpStatus> uploadResource( @ApiBodyObject HttpEntity<byte[]> requestEntity,
                                                  HttpServletResponse response,
                                                  HttpSession session,
                                                  @ApiParam( name = "albumCode", description = "code for the album owner of the resource.. This code is the reference for others resources that could be uploaded, and so on", paramType = ApiParamType.PATH, required = true ) @PathVariable String albumCode,
                                                  @ApiParam( name = "fileName", description = "file name of the resource", paramType = ApiParamType.QUERY, required = true ) @RequestParam( "fileName" ) String fileName )
        throws IOMessicRESTException, UnknownMessicRESTException, NotAuthorizedMessicRESTException
    {

        User user = SecurityUtil.getCurrentUser();
        try
        {
            byte[] payload = requestEntity.getBody();
            albumAPI.uploadResource( user, albumCode, HtmlUtils.htmlUnescape( fileName ), payload );
        }
        catch ( IOException ioe )
        {
            log.error( "failed!", ioe );
            throw new IOMessicRESTException( ioe );
        }
        catch ( Exception e )
        {
            log.error( "failed!", e );
            throw new UnknownMessicRESTException( e );
        }

        return new HttpEntity<HttpStatus>( HttpStatus.OK );
    }

    @ApiMethod( path = "/services/albums/clear?albumCode=xxxx", verb = ApiVerb.POST, description = "Clear all the temporal files that have been uploaded previously. You can pass a json object with all the files you don't want to delate (the algorithm will erase all files that aren't in the list)", produces = {
        MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ),
        @ApiError( code = NotAuthorizedMessicRESTException.VALUE, description = "Forbidden access" ),
        @ApiError( code = IOMessicRESTException.VALUE, description = "IO internal server error" ), } )
    @RequestMapping( value = "/clear", method = RequestMethod.POST )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    @ApiResponseObject
    public List<org.messic.server.api.datamodel.File> clear( @ApiParam( name = "albumCode", description = "temporal code of the album we want to clear.  This code is the same the client give to the server when upload a resource for an album.  If not give this code, messic will remove all the temporal files uploaded until now.", paramType = ApiParamType.QUERY, required = false ) @RequestParam( value = "albumCode", required = false ) String albumCode,
                                                             @ApiBodyObject @RequestBody( required = false ) List<org.messic.server.api.datamodel.File> exceptFiles )
        throws NotAuthorizedMessicRESTException, IOMessicRESTException, UnknownMessicRESTException
    {

        User user = SecurityUtil.getCurrentUser();
        try
        {
            List<org.messic.server.api.datamodel.File> existingFiles =
                albumAPI.clearTemporal( user, albumCode, exceptFiles );
            return existingFiles;
        }
        catch ( IOException e )
        {
            log.error( "failed!", e );
            throw new IOMessicRESTException( e );
        }
        catch ( Exception e )
        {
            log.error( "failed!", e );
            throw new UnknownMessicRESTException( e );
        }

    }

    @ApiMethod( path = "/services/albums/{albumCode}/wizard", verb = ApiVerb.GET, description = "Get 'magic' info from the resources that have been uploaded previously.  This resources are suposed to be of the same album, messic try to get the album info from them using several mechanisms. If the {pluginName} param is not present it returns only the basic info with all the available plugins. ALSO SUPPORT POST TO SUBMIT THE PARAMETER ALBUMHELPINFO!.", produces = {
        MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ),
        @ApiError( code = NotAuthorizedMessicRESTException.VALUE, description = "Forbidden access" ),
        @ApiError( code = IOMessicRESTException.VALUE, description = "IO internal server error" ),
        @ApiError( code = MusicTagsMessicRESTException.VALUE, description = "Song Tags error" ), } )
    @RequestMapping( value = "/{albumCode}/wizard", method = { RequestMethod.GET, RequestMethod.POST } )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    @ApiResponseObject
    public List<TAGWizardPlugin> getWizardAlbum( @ApiParam( name = "albumCode", description = "temporal code of the album we want to analyze. This code have been set previously while uploading resources. Messic will use the resources linked with these album code", paramType = ApiParamType.PATH, required = true ) @PathVariable String albumCode,
                                                 @ApiParam( name = "pluginName", description = "Name of the plugin we want to use.. It's not required. If this paremeter is not present the function will return only the basic informartion with all the available plugins names to query", paramType = ApiParamType.PATH, required = false ) @RequestParam( value = "pluginName", required = false ) String pluginName,
                                                 @ApiBodyObject @RequestBody( required = false ) Album albumHelpInfo )
        throws NotAuthorizedMessicRESTException, UnknownMessicRESTException, IOMessicRESTException,
        MusicTagsMessicRESTException
    {
        User user = SecurityUtil.getCurrentUser();

        try
        {
            List<TAGWizardPlugin> result = new ArrayList<TAGWizardPlugin>();
            if ( pluginName != null && pluginName.length() > 0 )
            {
                TAGWizardPlugin album = wizardAPI.getWizardAlbum( user, pluginName, albumHelpInfo, albumCode );
                result.add( album );
            }
            else
            {
                result = wizardAPI.getWizards( user, albumCode );
            }
            return result;
        }
        catch ( IOException e )
        {
            throw new MusicTagsMessicRESTException( e );
        }
        catch ( Exception e )
        {
            log.error( "failed!", e );
            throw new UnknownMessicRESTException( e );
        }
    }

}
