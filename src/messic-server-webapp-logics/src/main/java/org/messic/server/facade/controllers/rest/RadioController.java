package org.messic.server.facade.controllers.rest;

import java.io.IOException;
import java.io.InputStream;

import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiError;
import org.jsondoc.core.annotation.ApiErrors;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiParam;
import org.jsondoc.core.annotation.ApiResponseObject;
import org.jsondoc.core.pojo.ApiParamType;
import org.jsondoc.core.pojo.ApiVerb;
import org.messic.server.Util;
import org.messic.server.api.APIRadio;
import org.messic.server.api.datamodel.User;
import org.messic.server.api.exceptions.ResourceNotFoundMessicException;
import org.messic.server.api.exceptions.SidNotFoundMessicException;
import org.messic.server.api.plugin.radio.MessicRadioInfo;
import org.messic.server.facade.controllers.rest.exceptions.IOMessicRESTException;
import org.messic.server.facade.controllers.rest.exceptions.NotAuthorizedMessicRESTException;
import org.messic.server.facade.controllers.rest.exceptions.NotFoundMessicRESTException;
import org.messic.server.facade.controllers.rest.exceptions.UnknownMessicRESTException;
import org.messic.server.facade.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping( "/radio" )
@Api( name = "Search services", description = "Methods for search things" )
public class RadioController
{
    @Autowired
    public APIRadio radioAPI;

    @ApiMethod( path = "/services/radio/start", verb = ApiVerb.PUT, description = "Start the messic radio cast", produces = {
        MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ),
        @ApiError( code = NotAuthorizedMessicRESTException.VALUE, description = "Forbidden access" ) } )
    @RequestMapping( value = "/start", method = RequestMethod.PUT )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    @ApiResponseObject
    public String start()
        throws NotAuthorizedMessicRESTException, UnknownMessicRESTException
    {

        User user = SecurityUtil.getCurrentUser();
        try
        {
            radioAPI.startRadio();
            MessicRadioInfo mri = radioAPI.getInfo();
            String castServerPath =
                ( mri.publicURLHost != null && mri.publicURLHost.length() > 0 ? mri.publicURLHost : "" );
            castServerPath = castServerPath + ":" + mri.publicURLPort;
            castServerPath = castServerPath + mri.publicURLPath;

            return castServerPath;
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            throw new UnknownMessicRESTException( e );
        }

    }

    @ApiMethod( path = "/services/radio/stop", verb = ApiVerb.PUT, description = "Start the messic radio cast", produces = {
        MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ),
        @ApiError( code = NotAuthorizedMessicRESTException.VALUE, description = "Forbidden access" ) } )
    @RequestMapping( value = "/stop", method = RequestMethod.PUT )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    @ApiResponseObject
    public void stop()
        throws NotAuthorizedMessicRESTException, UnknownMessicRESTException
    {

        User user = SecurityUtil.getCurrentUser();
        try
        {
            radioAPI.stopRadio();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            throw new UnknownMessicRESTException( e );
        }
    }

    @ApiMethod( path = "/services/radio/{songSid}?songQueuePosition={songQueuePosition}", verb = ApiVerb.POST, description = "Cast the song with sid sidSong", produces = {
        MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ),
        @ApiError( code = NotAuthorizedMessicRESTException.VALUE, description = "Forbidden access" ) } )
    @RequestMapping( value = "/{songSid}", method = RequestMethod.POST )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    @ApiResponseObject
    public void setSong( @PathVariable @ApiParam( name = "songSid", description = "SID of the album resource we want to play", paramType = ApiParamType.PATH, required = true ) Long songSid,
                         @RequestParam( value = "songQueuePosition", required = false ) @ApiParam( name = "songQueuePosition", description = "Index Position of the song in the client queue", paramType = ApiParamType.QUERY, required = true ) Integer songQueuePosition )
        throws NotAuthorizedMessicRESTException, UnknownMessicRESTException
    {

        User user = SecurityUtil.getCurrentUser();
        try
        {
            radioAPI.playSong( user.getLogin(), songSid, songQueuePosition );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            throw new UnknownMessicRESTException( e );
        }
    }

    @ApiMethod( path = "/services/radio/status", verb = ApiVerb.GET, description = "get the status of the radio service", produces = {
        MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ),
        @ApiError( code = NotAuthorizedMessicRESTException.VALUE, description = "Forbidden access" ) } )
    @RequestMapping( value = "/status", method = RequestMethod.GET )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    @ApiResponseObject
    public MessicRadioInfo getStatus()
        throws NotAuthorizedMessicRESTException, UnknownMessicRESTException
    {

        User user = SecurityUtil.getCurrentUser();
        try
        {
            return radioAPI.getInfo();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            throw new UnknownMessicRESTException( e );
        }
    }

    @ApiMethod( path = "/services/radio/cover", verb = ApiVerb.GET, description = "Get cover of the album being played", produces = { MediaType.IMAGE_JPEG_VALUE } )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ),
        @ApiError( code = NotAuthorizedMessicRESTException.VALUE, description = "Forbidden access" ),
        @ApiError( code = NotFoundMessicRESTException.VALUE, description = "Album or Cover not found" ),
        @ApiError( code = IOMessicRESTException.VALUE, description = "IO internal server error" ), } )
    @RequestMapping( value = "/cover", method = RequestMethod.GET )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    @ApiResponseObject
    public ResponseEntity<byte[]> getRadioAlbumCover( @RequestParam( value = "preferredWidth", required = false ) @ApiParam( name = "preferredWidth", description = "desired width for the image returned.  The service will try to provide the desired width, it is just only informative, to try to optimize the performance, avoiding to return images too much big", paramType = ApiParamType.QUERY, required = false, format = "Integer" ) Integer preferredWidth,
                                                      @RequestParam( value = "preferredHeight", required = false ) @ApiParam( name = "preferredHeight", description = "desired height for the image returned.  The service will try to provide the desired height, it is just only informative, to try to optimize the performance, avoiding to return images too much big", paramType = ApiParamType.QUERY, required = false, format = "Integer" ) Integer preferredHeight )
        throws UnknownMessicRESTException, NotAuthorizedMessicRESTException, NotFoundMessicRESTException,
        IOMessicRESTException
    {

        try
        {
            byte[] content = radioAPI.getAlbumCover( preferredWidth, preferredHeight );
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

        }
        catch ( IOException e )
        {
            // throw new IOMessicRESTException( e );
            e.printStackTrace();
        }

        InputStream is = AlbumController.class.getResourceAsStream( "/org/messic/img/unknowncover.jpg" );
        byte[] content = null;
        try
        {
            content = Util.readInputStream( is );
        }
        catch ( IOException e1 )
        {
            throw new NotFoundMessicRESTException( e1 );
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType( MediaType.IMAGE_JPEG );
        return new ResponseEntity<byte[]>( content, headers, HttpStatus.OK );
    }
}
