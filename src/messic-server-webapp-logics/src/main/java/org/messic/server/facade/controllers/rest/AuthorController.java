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

import java.io.File;
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
import org.messic.server.api.APIAuthor;
import org.messic.server.api.datamodel.Author;
import org.messic.server.api.datamodel.FolderResourceConsistency;
import org.messic.server.api.datamodel.User;
import org.messic.server.datamodel.MDOUser;
import org.messic.server.datamodel.dao.DAOMessicSettings;
import org.messic.server.datamodel.dao.DAOUser;
import org.messic.server.facade.controllers.rest.exceptions.NotAuthorizedMessicRESTException;
import org.messic.server.facade.controllers.rest.exceptions.UnknownMessicRESTException;
import org.messic.server.facade.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping( "/authors" )
@Api( name = "Author services", description = "Methods for managing authors" )
public class AuthorController
{
    private Logger log = Logger.getLogger( AuthorController.class );

    @Autowired
    public APIAuthor authorAPI;

    @Autowired
    public DAOUser userDAO;

    @Autowired
    private DAOMessicSettings daoSettings;

    @ApiMethod( path = "/services/authors/{authorSid}", verb = ApiVerb.DELETE, description = "Remove an author (and all its content) with sid {authorSid}", produces = {} )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ),
        @ApiError( code = NotAuthorizedMessicRESTException.VALUE, description = "Forbidden access" ) } )
    @RequestMapping( value = "/{authorSid}", method = RequestMethod.DELETE )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    @ApiResponseObject
    public void removeAuthor( @PathVariable @ApiParam( name = "authorSid", description = "Sid of the author to remove", paramType = ApiParamType.PATH, required = true ) Long authorSid )
        throws UnknownMessicRESTException, NotAuthorizedMessicRESTException
    {
        User user = SecurityUtil.getCurrentUser();
        try
        {
            authorAPI.remove( user, authorSid );
        }
        catch ( Exception e )
        {
            log.error( "failed!", e );
            throw new UnknownMessicRESTException( e );
        }
    }

    @ApiMethod( path = "/services/authors?filterName=xxxx&albumsInfo=true|false&songsInfo=true|false", verb = ApiVerb.GET, description = "Get all authors", produces = {
        MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ),
        @ApiError( code = NotAuthorizedMessicRESTException.VALUE, description = "Forbidden access" ) } )
    @RequestMapping( value = "", method = RequestMethod.GET )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    @ApiResponseObject
    public List<Author> getAll( @RequestParam( value = "filterName", required = false ) @ApiParam( name = "filterName", description = "partial name of the author to search", paramType = ApiParamType.QUERY, required = false ) String filterName,
                                @RequestParam( value = "contains", required = false ) @ApiParam( name = "contains", description = "True if filtering is by contains (default), False if filtering is searching any author with the starting filter search.", paramType = ApiParamType.QUERY, required = false, allowedvalues = {
                                    "true", "false" }, format = "Boolean" ) Boolean contains,
                                @RequestParam( value = "albumsInfo", required = false ) @ApiParam( name = "albumsInfo", description = "flag to return also the albums info of the author or not. By default, false", paramType = ApiParamType.QUERY, required = false, allowedvalues = {
                                    "true", "false" }, format = "Boolean" ) Boolean albumsInfo,
                                @RequestParam( value = "songsInfo", required = false ) @ApiParam( name = "songsInfo", description = "flag to return also the songs info of the albums or not. By default, false.  Missed if albumsInfo=false", paramType = ApiParamType.QUERY, required = false, allowedvalues = {
                                    "true", "false" }, format = "Boolean" ) Boolean songsInfo )
        throws UnknownMessicRESTException, NotAuthorizedMessicRESTException
    {
        User user = SecurityUtil.getCurrentUser();

        try
        {
            List<Author> authors = null;
            if ( filterName == null )
            {
                authors =
                    authorAPI.getAll( user, ( albumsInfo != null ? albumsInfo : false ), ( albumsInfo != null
                        && songsInfo != null && albumsInfo == true ? songsInfo : false ) );
            }
            else
            {
                if ( contains == null )
                {
                    contains = true;
                }

                authors =
                    authorAPI.findSimilar( user, filterName, contains, ( albumsInfo != null ? albumsInfo : false ),
                                           ( albumsInfo != null && songsInfo != null && albumsInfo == true ? songsInfo
                                                           : false ) );
            }

            return authors;
        }
        catch ( Exception e )
        {
            log.error( "failed!", e );
            throw new UnknownMessicRESTException( e );
        }
    }

    @ApiMethod( path = "/services/authors/{authorSid}?albumsInfo=true|false&songsInfo=true|false", verb = ApiVerb.GET, description = "Get an author", produces = {
        MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ),
        @ApiError( code = NotAuthorizedMessicRESTException.VALUE, description = "Forbidden access" ) } )
    @RequestMapping( value = "/{authorSid}", method = RequestMethod.GET )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    @ApiResponseObject
    public Author getAuthor( @PathVariable @ApiParam( name = "authorSid", description = "Sid of the author to get", paramType = ApiParamType.PATH, required = true ) Long authorSid,
                             @RequestParam( value = "albumsInfo", required = false ) @ApiParam( name = "albumsInfo", description = "flag to return also the albums info of the author or not. By default, false", paramType = ApiParamType.QUERY, required = false, allowedvalues = {
                                 "true", "false" }, format = "Boolean" ) Boolean albumsInfo,
                             @RequestParam( value = "songsInfo", required = false ) @ApiParam( name = "songsInfo", description = "flag to return also the songs info of the albums or not. By default, false.  Missed if albumsInfo=false", paramType = ApiParamType.QUERY, required = false, allowedvalues = {
                                 "true", "false" }, format = "Boolean" ) Boolean songsInfo )
        throws UnknownMessicRESTException, NotAuthorizedMessicRESTException
    {

        User user = SecurityUtil.getCurrentUser();
        try
        {
            Author author =
                authorAPI.getAuthor( user, authorSid, ( albumsInfo != null ? albumsInfo : false ), ( albumsInfo != null
                    && songsInfo != null && albumsInfo == true ? songsInfo : false ) );
            return author;
        }
        catch ( Exception e )
        {
            log.error( "failed!", e );
            throw new UnknownMessicRESTException( e );
        }
    }

    @ApiMethod( path = "/services/authors/checkAuthorFolderConsistency/{authorFolder}?user={userLogin}", verb = ApiVerb.POST, description = "Check the consistency general consistency of the database against the file system... check if there are empty folders, without defined entities at the database, and so on. By default of the current user (you can select the user). Need to be administrator.", produces = {
        MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ),
        @ApiError( code = NotAuthorizedMessicRESTException.VALUE, description = "Not authorized remove this user" ) } )
    @RequestMapping( value = "/checkAuthorFolderConsistency/{authorFolder:.+}", method = RequestMethod.POST )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    @ApiResponseObject
    protected List<FolderResourceConsistency> checkAuthorFolderConsistency( @ApiParam( name = "authorFolder", description = "name of the folder at the author level", paramType = ApiParamType.PATH, required = true ) @PathVariable String authorFolder,
                                                                            @RequestParam( value = "user", required = false ) @ApiParam( name = "user", description = "user from which we want the information.. if not comming, then the current user", paramType = ApiParamType.QUERY, required = false ) String user )
        throws NotAuthorizedMessicRESTException, UnknownMessicRESTException
    {
        try
        {

            User cuser = SecurityUtil.getCurrentUser();
            MDOUser mdoCurrentUser = userDAO.getUserByLogin( cuser.getLogin() );

            if ( mdoCurrentUser != null && mdoCurrentUser.getAdministrator() )
            {
                MDOUser askedUser = mdoCurrentUser;
                if ( user != null )
                {
                    askedUser = userDAO.getUserByLogin( user );
                }

                String userPath = askedUser.calculateAbsolutePath( daoSettings.getSettings() );
                return authorAPI.checkConsistencyFolder( askedUser, new File( userPath + File.separatorChar
                    + authorFolder ) );
            }
            else
            {
                throw new NotAuthorizedMessicRESTException( new Exception() );
            }
        }
        catch ( Exception e )
        {
            throw new UnknownMessicRESTException( e );
        }

    }

    @ApiMethod( path = "/services/authors/getAuthorFolders?user={login}", verb = ApiVerb.GET, description = "get a list with all the folders at the author messic music folder. You can send the login of the user which are inspecting.. if not comming this info, then the current user (need to be administrator)", produces = {
        MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ),
        @ApiError( code = NotAuthorizedMessicRESTException.VALUE, description = "Not authorized remove this user" ) } )
    @RequestMapping( value = "/getAuthorFolders", method = RequestMethod.GET )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    @ApiResponseObject
    protected List<String> getAuthorFolders( @RequestParam( value = "user", required = false ) @ApiParam( name = "user", description = "user from which we want the information.. if not comming, then the current user", paramType = ApiParamType.QUERY, required = false ) String user )
        throws NotAuthorizedMessicRESTException, UnknownMessicRESTException
    {
        try
        {
            User cuser = SecurityUtil.getCurrentUser();
            MDOUser mdoCurrentUser = userDAO.getUserByLogin( cuser.getLogin() );

            if ( mdoCurrentUser != null && mdoCurrentUser.getAdministrator() )
            {
                MDOUser askedUser = mdoCurrentUser;
                if ( user != null )
                {
                    askedUser = userDAO.getUserByLogin( user );
                }

                return authorAPI.getAuthorFolders( askedUser );
            }
            else
            {
                throw new NotAuthorizedMessicRESTException( new Exception() );
            }
        }
        catch ( Exception e )
        {
            throw new UnknownMessicRESTException( e );
        }

    }

}
