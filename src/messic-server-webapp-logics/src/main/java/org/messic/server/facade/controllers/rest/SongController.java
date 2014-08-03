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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.messic.server.api.APIAuthor;
import org.messic.server.api.APISong;
import org.messic.server.api.datamodel.Song;
import org.messic.server.api.datamodel.User;
import org.messic.server.api.exceptions.SidNotFoundMessicException;
import org.messic.server.datamodel.dao.DAOUser;
import org.messic.server.facade.controllers.rest.exceptions.IOMessicRESTException;
import org.messic.server.facade.controllers.rest.exceptions.NotAuthorizedMessicRESTException;
import org.messic.server.facade.controllers.rest.exceptions.NotFoundMessicRESTException;
import org.messic.server.facade.controllers.rest.exceptions.UnknownMessicRESTException;
import org.messic.server.facade.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
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

/**
 * @author spheras
 */
@Controller
@RequestMapping( "/songs" )
@Api( name = "Song services", description = "Methods for managing songs" )
public class SongController
{
    @Autowired
    public APISong songAPI;

    @Autowired
    public APIAlbum albumAPI;

    @Autowired
    public APIAuthor authorAPI;

    @Autowired
    public DAOUser userDAO;

    @ApiMethod( path = "/songs/{songFileName}/wizard", verb = ApiVerb.GET, description = "Investigate the filename to get the track number and trackname", produces = {
        MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ) } )
    @RequestMapping( value = "/{songFileName}/wizard", method = RequestMethod.GET )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    @ApiResponseObject
    public Song wizardFileName( @PathVariable
                                @ApiParam( name = "songFileName", description = "FileName or fulpath to get the info", paramType = ApiParamType.PATH, required = true )
                                String songFileName )
        throws UnknownMessicRESTException
    {

        try
        {
            return songAPI.getSongInfoFromFileName( songFileName );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            throw new UnknownMessicRESTException( e );
        }
    }

    @ApiMethod( path = "/songs/{songSid}", verb = ApiVerb.DELETE, description = "Remove a song with sid {songSid}", produces = {} )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ),
        @ApiError( code = NotAuthorizedMessicRESTException.VALUE, description = "Forbidden access" ) } )
    @RequestMapping( value = "/{songSid}", method = RequestMethod.DELETE )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    @ApiResponseObject
    public void removeSong( @PathVariable
                            @ApiParam( name = "songSid", description = "Sid of the song to get", paramType = ApiParamType.PATH, required = true )
                            Long songSid )
        throws UnknownMessicRESTException, NotAuthorizedMessicRESTException
    {

        User user = SecurityUtil.getCurrentUser();
        try
        {
            songAPI.remove( user, songSid );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            throw new UnknownMessicRESTException( e );
        }
    }

    @ApiMethod( path = "/songs/{songSid}/audio?dlna={true|false}", verb = ApiVerb.GET, description = "Get the audio binary from a song resource of an album", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE } )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ),
        @ApiError( code = NotAuthorizedMessicRESTException.VALUE, description = "Forbidden access" ),
        @ApiError( code = IOMessicRESTException.VALUE, description = "IO error trying to get the audio resource" ) } )
    @RequestMapping( value = "/{songSid}/audio", method = { RequestMethod.GET, RequestMethod.HEAD } )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    @ApiResponseObject
    public ResponseEntity getSong( @ApiParam( name = "songSid", description = "SID of the song resource we want to download", paramType = ApiParamType.PATH, required = true )
                                   @PathVariable
                                   Long songSid,
                                   @RequestParam( value = "dlna", required = false )
                                   @ApiParam( name = "dlna", description = "flag to know if it is a dlna petition.  DLNA petitions should return specific headers. By default is false", paramType = ApiParamType.QUERY, required = false, allowedvalues = {
                                       "true", "false" }, format = "Boolean" )
                                   Boolean dlna, HttpServletRequest request )
        throws NotAuthorizedMessicRESTException, IOMessicRESTException, UnknownMessicRESTException
    {
        User user = SecurityUtil.getCurrentUser();

        try
        {

            HttpHeaders headers = new HttpHeaders();

            // TODO some mp3 songs fail with application/octet-stream
            // MP3 files must have the content type of audio/mpeg or application/octet-stream
            // ogg files must have the content type of application/ogg

            headers.setContentType( MediaType.parseMediaType( "audio/mpeg" ) );
            if ( dlna != null && dlna == true )
            {
                headers.setConnection( "close" );
                headers.add( "EXT", null );
                headers.add( "Accept-Ranges", "bytes" );
                headers.add( "transferMode.dlna.org", "Streaming" );
                headers.add( "contentFeatures.dlna.org", "DLNA.ORG_PN=MP3;DLNA.ORG_OP=01;DLNA.ORG_CI=0" );
                headers.add( "realTimeInfo.dlna.org", "DLNA.ORG_TLAG=*" );
                headers.setDate( System.currentTimeMillis() );
            }

            if ( request.getHeader( "Range" ) == null )
            {
                APISong.AudioSongStream ass = songAPI.getAudioSong( user, songSid );
                headers.setContentLength( ass.contentLength );
                headers.add( "Content-Range", "bytes 0-" + ass.contentLength + "/" + ass.contentLength );

                InputStreamResource inputStreamResource = new InputStreamResource( ass.is );
                if ( request.getMethod().equalsIgnoreCase( "GET" ) )
                {
                    return new ResponseEntity( inputStreamResource, headers, HttpStatus.OK );
                }
                else
                {
                    return new ResponseEntity<byte[]>( new byte[0], headers, HttpStatus.OK );
                }
            }
            else
            {
                APISong.AudioSongStream ass = songAPI.getAudioSong( user, songSid );
                byte[] content = Util.readInputStream( ass.is );

                String range = request.getHeader( "Range" );
                String[] sbytes = range.split( "=" )[1].split( "-" );
                int from = Integer.valueOf( sbytes[0] );
                int to = (int) ass.contentLength;
                if ( sbytes.length > 1 )
                {
                    to = Integer.valueOf( sbytes[1] );
                }
                byte[] destino = new byte[to - from];

                System.arraycopy( content, from, destino, 0, to - from );

                headers.setContentLength( destino.length );
                headers.add( "Content-Range", "bytes " + from + "-" + to + "/" + ass.contentLength );
                content = destino;

                if ( request.getMethod().equalsIgnoreCase( "GET" ) )
                {
                    return new ResponseEntity<byte[]>( content, headers, HttpStatus.OK );
                }
                else
                {
                    return new ResponseEntity<byte[]>( new byte[0], headers, HttpStatus.OK );
                }
            }

        }
        catch ( IOException ioe )
        {
            ioe.printStackTrace();
            throw new IOMessicRESTException( ioe );
        }
        catch ( Exception e )
        {
            throw new UnknownMessicRESTException( e );
        }
    }

    @ApiMethod( path = "/songs/{songSids}/zip", verb = ApiVerb.GET, description = "Get a set of songs zipped", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE } )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ),
        @ApiError( code = NotAuthorizedMessicRESTException.VALUE, description = "Forbidden access" ),
        @ApiError( code = IOMessicRESTException.VALUE, description = "IO error trying to get the songs resources" ) } )
    @RequestMapping( value = "/{songSids}/zip", method = RequestMethod.GET )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    @ApiResponseObject
    public void getSongsZip( @ApiParam( name = "songSids", description = "song sids SEPARATED BY :", paramType = ApiParamType.PATH, required = true )
                             @PathVariable
                             String songSids, HttpServletResponse response )
        throws NotAuthorizedMessicRESTException, IOMessicRESTException, UnknownMessicRESTException
    {

        User user = SecurityUtil.getCurrentUser();
        try
        {
            ArrayList<Long> sids = new ArrayList<Long>();
            String[] s = songSids.split( ":" );
            for ( String sid : s )
            {
                sids.add( Long.valueOf( sid ) );
            }

            songAPI.getSongsZip( user, sids, response.getOutputStream() );
        }
        catch ( IOException ioe )
        {
            ioe.printStackTrace();
            throw new IOMessicRESTException( ioe );
        }
        catch ( Exception e )
        {
            throw new UnknownMessicRESTException( e );
        }
    }

    @ApiMethod( path = "/songs", verb = ApiVerb.POST, description = "Update a song.", produces = {
        MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, consumes = {
        MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ),
        @ApiError( code = NotAuthorizedMessicRESTException.VALUE, description = "Forbidden access" ),
        @ApiError( code = IOMessicRESTException.VALUE, description = "IO internal server error" ), } )
    @RequestMapping( value = "", method = RequestMethod.POST )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    @ApiResponseObject
    public ResponseEntity<HttpStatus> updateSong( @ApiBodyObject
    @RequestBody
    Song song )
        throws UnknownMessicRESTException, NotAuthorizedMessicRESTException, NotFoundMessicRESTException,
        IOMessicRESTException
    {

        User user = SecurityUtil.getCurrentUser();
        try
        {
            songAPI.updateSong( user, song );
            return new ResponseEntity<HttpStatus>( HttpStatus.OK );
        }
        catch ( IOException e )
        {
            e.printStackTrace();
            throw new IOMessicRESTException( e );
        }
        catch ( SidNotFoundMessicException sm )
        {
            sm.printStackTrace();
            throw new NotFoundMessicRESTException( sm );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            throw new UnknownMessicRESTException( e );
        }
    }

    @ApiMethod( path = "/songs?filterSongSid=xxxx&filterAlbumSid=xxxx&filterAuthorSid=xxxx&albumInfo=true|false&authorInfo=true|false", verb = ApiVerb.GET, description = "Get all songs. They can be filtered by songSid, albumSid or authorSid (not combined). You can also espcify what information should be returned (with album information or not, for exmaple)", produces = {
        MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ),
        @ApiError( code = NotAuthorizedMessicRESTException.VALUE, description = "Forbidden access" ) } )
    @RequestMapping( value = "", method = RequestMethod.GET )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    @ApiResponseObject
    public List<Song> getAll( @RequestParam( value = "filterSongSid", required = false )
                              @ApiParam( name = "filterSongSid", description = "SID of the song to get", paramType = ApiParamType.QUERY, required = false )
                              Integer filterSongSid,
                              @RequestParam( value = "filterAlbumSid", required = false )
                              @ApiParam( name = "filterAlbumSid", description = "SID of the album to filter songs", paramType = ApiParamType.QUERY, required = false )
                              Integer filterAlbumSid,
                              @RequestParam( value = "filterAuthorSid", required = false )
                              @ApiParam( name = "filterAuthorSid", description = "SID of the author to filter songs", paramType = ApiParamType.QUERY, required = false )
                              Integer filterAuthorSid,
                              @RequestParam( value = "albumInfo", required = false )
                              @ApiParam( name = "albumInfo", description = "flag to return also the album info of the songs or not. By default, false", paramType = ApiParamType.QUERY, required = false, allowedvalues = {
                                  "true", "false" }, format = "Boolean" )
                              Boolean albumInfo,
                              @RequestParam( value = "authorInfo", required = false )
                              @ApiParam( name = "authorInfo", description = "flag to return also the author info of the album of each song or not. By default, false", paramType = ApiParamType.QUERY, required = false, allowedvalues = {
                                  "true", "false" }, format = "Boolean" )
                              Boolean authorInfo )
        throws UnknownMessicRESTException, NotAuthorizedMessicRESTException
    {
        User user = SecurityUtil.getCurrentUser();

        try
        {
            List<Song> songs = null;
            boolean albumi = ( albumInfo != null ? albumInfo : false );
            boolean authori = ( authorInfo != null ? authorInfo : false );

            if ( filterSongSid != null )
            {
                songs = new ArrayList<Song>();
                Song song = songAPI.getSong( user, filterSongSid, albumi, authori );
                songs.add( song );
            }
            else if ( filterAlbumSid != null )
            {
                songs = songAPI.getSongsOfAlbum( user, filterAlbumSid, albumi, authori );
            }
            else if ( filterAuthorSid != null )
            {
                songs = songAPI.getSongsOfAuthor( user, filterAuthorSid, albumi, authori );
            }

            return songs;
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            throw new UnknownMessicRESTException( e );
        }
    }
}
