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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.jsondoc.core.annotation.ApiBodyObject;
import org.jsondoc.core.annotation.ApiError;
import org.jsondoc.core.annotation.ApiErrors;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiParam;
import org.jsondoc.core.annotation.ApiResponseObject;
import org.jsondoc.core.pojo.ApiParamType;
import org.jsondoc.core.pojo.ApiVerb;
import org.messic.server.api.APIUser;
import org.messic.server.api.datamodel.MessicSettings;
import org.messic.server.api.datamodel.User;
import org.messic.server.api.exceptions.NotAllowedMessicException;
import org.messic.server.datamodel.dao.DAOMessicSettings;
import org.messic.server.facade.controllers.rest.exceptions.NotAuthorizedMessicRESTException;
import org.messic.server.facade.controllers.rest.exceptions.NotValidUserNameMessicRESTException;
import org.messic.server.facade.controllers.rest.exceptions.UnknownMessicRESTException;
import org.messic.server.facade.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

@Controller
@RequestMapping( "/settings" )
public class SettingsController
{
    private static Logger log = Logger.getLogger( SettingsController.class );

    private static final long ONE_HOUR = ( 6000 * 60 );

    @Autowired
    private APIUser userAPI;

    @Autowired
    private DAOMessicSettings daoSettings;

    @ApiMethod( path = "/services/settings", verb = ApiVerb.GET, description = "Get the user settings", produces = {
        MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ),
        @ApiError( code = NotAuthorizedMessicRESTException.VALUE, description = "Not authorized to obtain this info" ) } )
    @RequestMapping( value = "", method = RequestMethod.GET )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    protected User getUser()
        throws NotAuthorizedMessicRESTException, UnknownMessicRESTException
    {
        try
        {
            User user = SecurityUtil.getCurrentUser();
            User result = userAPI.getUserByLogin( user.getLogin() );
            return result;
        }
        catch ( NotAuthorizedMessicRESTException e )
        {
            throw new NotAuthorizedMessicRESTException( e );
        }
        catch ( Exception e )
        {
            throw new UnknownMessicRESTException( e );
        }
    }

    @ApiMethod( path = "/services/settings/${userSid}?removeMusicContent={true|false}", verb = ApiVerb.DELETE, description = "Remove an existing user", produces = {
        MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ),
        @ApiError( code = NotAuthorizedMessicRESTException.VALUE, description = "Not authorized remove this user" ) } )
    @RequestMapping( value = "/{userSid}", method = RequestMethod.DELETE )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    protected void removeUser( @ApiParam( name = "userSid", description = "Sid of the user to remove", paramType = ApiParamType.PATH, required = true )
                               @PathVariable
                               Long userSid,
                               @RequestParam( value = "removeMusicContent", required = false )
                               @ApiParam( name = "removeMusicContent", description = "flag to know if the music content folder of the user should be removed also. By default, true", paramType = ApiParamType.QUERY, required = false, allowedvalues = {
                                   "true", "false" }, format = "Boolean" )
                               Boolean removeMusicContent )
        throws NotAuthorizedMessicRESTException, UnknownMessicRESTException
    {
        try
        {
            User user = SecurityUtil.getCurrentUser();
            User result = userAPI.getUserByLogin( user.getLogin() );
            if ( result.getAdministrator() || result.getSid().equals( userSid ) )
            {
                userAPI.removeUser( userSid, removeMusicContent );

            }
            else
            {
                // only administrators can remove any other user...
                throw new NotAuthorizedMessicRESTException( new Exception( "Forbidden" ) );
            }
        }
        catch ( Exception e )
        {
            throw new UnknownMessicRESTException( e );
        }
    }

    @ApiMethod( path = "/services/settings/${userSid}/resetPassword", verb = ApiVerb.POST, description = "Reset the password of an existing user to 123456", produces = {
        MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ),
        @ApiError( code = NotAuthorizedMessicRESTException.VALUE, description = "Not authorized remove this user" ) } )
    @RequestMapping( value = "/{userSid}/resetPassword", method = RequestMethod.POST )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    protected void resetPassword( @ApiParam( name = "userSid", description = "Sid of the user to remove", paramType = ApiParamType.PATH, required = true )
                                  @PathVariable
                                  Long userSid )
        throws NotAuthorizedMessicRESTException, UnknownMessicRESTException
    {
        try
        {
            User user = SecurityUtil.getCurrentUser();
            User result = userAPI.getUserByLogin( user.getLogin() );
            if ( result.getAdministrator() )
            {
                userAPI.resetPassword( userSid );

            }
            else
            {
                // only administrators can reset password for any other user...
                throw new NotAuthorizedMessicRESTException( new Exception( "Forbidden" ) );
            }
        }
        catch ( Exception e )
        {
            throw new UnknownMessicRESTException( e );
        }
    }

    @ApiMethod( path = "/services/settings/admin", verb = ApiVerb.GET, description = "Get the Messic settings", produces = {
        MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ),
        @ApiError( code = NotAuthorizedMessicRESTException.VALUE, description = "Not authorized to obtain this info" ) } )
    @RequestMapping( value = "/admin", method = RequestMethod.GET )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    protected MessicSettings getMessicSettings()
        throws NotAuthorizedMessicRESTException, UnknownMessicRESTException
    {
        try
        {
            User user = SecurityUtil.getCurrentUser();
            User result = userAPI.getUserByLogin( user.getLogin() );
            if ( result.getAdministrator() )
            {
                MessicSettings ms = new MessicSettings( daoSettings.getSettings() );
                return ms;
            }
            else
            {
                throw new NotAuthorizedMessicRESTException( new Exception( "User not administrator" ) );
            }
        }
        catch ( NotAuthorizedMessicRESTException e )
        {
            throw new NotAuthorizedMessicRESTException( e );
        }
        catch ( Exception e )
        {
            throw new UnknownMessicRESTException( e );
        }
    }

    // just a flag to avoid brute force attacks
    private static int validateNewUserNameCounts = 0;

    private static long LastValidationTime = 0;

    @ApiMethod( path = "/services/settings/{userName}/validate", verb = ApiVerb.POST, description = "Validate the username", produces = {
        MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ),
        @ApiError( code = NotValidUserNameMessicRESTException.VALUE, description = "Not a valid username" ) } )
    @RequestMapping( value = "/{userName}/validate", method = RequestMethod.POST )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    @ApiResponseObject
    public Boolean validateNewUsername( @ApiParam( name = "userName", description = "UserName to test if its valid or not", paramType = ApiParamType.PATH, required = true )
                                        @PathVariable
                                        String userName )
        throws UnknownMessicRESTException, NotValidUserNameMessicRESTException
    {
        long currentTime = System.currentTimeMillis();
        if ( LastValidationTime == 0 )
        {
            LastValidationTime = currentTime;
        }
        if ( currentTime - LastValidationTime > ONE_HOUR )
        {
            validateNewUserNameCounts = 0;
        }
        validateNewUserNameCounts++;
        if ( currentTime - LastValidationTime < 5000 || validateNewUserNameCounts > 50 )
        {
            LastValidationTime = currentTime;

            // Security issue? Are we being attacked?
            // TODO inform? BLACKLIST?
            try
            {
                Thread.sleep( validateNewUserNameCounts * 100 );
            }
            catch ( InterruptedException e )
            {
                log.error( "failed!", e );
            }
        }

        try
        {
            boolean result = userAPI.validateNewUserName( userName );
            if ( result )
            {
                return true;
            }
            else
            {
                throw new NotValidUserNameMessicRESTException( new Exception( "Not valid username to be created." ) );
            }
        }
        catch ( Exception e )
        {
            throw new UnknownMessicRESTException( e );
        }
    }

    @ApiMethod( path = "/services/settings", verb = ApiVerb.POST, description = "Create or Update an User.", produces = {
        MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, consumes = {
        MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ) } )
    @RequestMapping( value = "", method = RequestMethod.POST )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    @ApiResponseObject
    protected void createOrUpdate( @ApiBodyObject
    @RequestBody
    User user )
        throws Exception
    {

        User loginuser = SecurityUtil.getCurrentUser();
        User result = null;
        if ( loginuser != null )
        {
            result = userAPI.getUserByLogin( loginuser.getLogin() );
        }

        if ( result == null )
        {
            userAPI.createUser( user );
        }
        else
        {
            user.setSid( result.getSid() );
            user.setLogin( result.getLogin() ); // forbiden to change the username
            userAPI.updateUser( user );
        }
    }

    @ApiMethod( path = "/services/settings/admin", verb = ApiVerb.POST, description = "Save Messic Settings.", produces = {
        MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, consumes = {
        MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ),
        @ApiError( code = NotAuthorizedMessicRESTException.VALUE, description = "User not authorized to save settings" ) } )
    @RequestMapping( value = "/admin", method = RequestMethod.POST )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    @ApiResponseObject
    protected void saveSettings( @ApiBodyObject
    @RequestBody
    MessicSettings settings )
        throws NotAuthorizedMessicRESTException, UnknownMessicRESTException
    {
        try
        {
            User user = SecurityUtil.getCurrentUser();
            userAPI.saveSettings( user, settings );
        }
        catch ( NotAllowedMessicException e )
        {
            throw new NotAuthorizedMessicRESTException( e );
        }
        catch ( Exception e )
        {
            throw new UnknownMessicRESTException( e );
        }
    }

    @InitBinder
    protected void initBinder( HttpServletRequest request, ServletRequestDataBinder binder )
        throws ServletException
    {
        binder.registerCustomEditor( byte[].class, new ByteArrayMultipartFileEditor() );
    }

}
