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
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
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
import org.messic.server.UtilSubInputStream;
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
import org.messic.server.facade.controllers.rest.range.Range;
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
    private static Logger log = Logger.getLogger( SongController.class );

    @Autowired
    public APISong songAPI;

    @Autowired
    public APIAlbum albumAPI;

    @Autowired
    public APIAuthor authorAPI;

    @Autowired
    public DAOUser userDAO;

    @ApiMethod( path = "/services/songs/{songFileName}/wizard", verb = ApiVerb.GET, description = "Investigate the filename to get the track number and trackname", produces = {
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
            log.error( "failed!", e );
            throw new UnknownMessicRESTException( e );
        }
    }

    @ApiMethod( path = "/services/songs/{songSid}", verb = ApiVerb.DELETE, description = "Remove a song with sid {songSid}", produces = {} )
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
            log.error( "failed!", e );
            throw new UnknownMessicRESTException( e );
        }
    }

    @ApiMethod( path = "/services/songs/{songSid}/audio", verb = ApiVerb.GET, description = "Get the audio binary from a song resource of an album", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE } )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ),
        @ApiError( code = NotAuthorizedMessicRESTException.VALUE, description = "Forbidden access" ),
        @ApiError( code = IOMessicRESTException.VALUE, description = "IO error trying to get the audio resource" ) } )
    @RequestMapping( value = "/{songSid}/audio", method = { RequestMethod.GET, RequestMethod.HEAD } )
    @ResponseStatus( HttpStatus.OK )
    public void getSongWithRanges( @ApiParam( name = "songSid", description = "SID of the song resource we want to download", paramType = ApiParamType.PATH, required = true )
                                   @PathVariable
                                   Long songSid, HttpServletRequest request, HttpServletResponse response )
        throws NotAuthorizedMessicRESTException, IOMessicRESTException, UnknownMessicRESTException
    {
        User user = SecurityUtil.getCurrentUser();

        try
        {
            //http://balusc.blogspot.com.es/2009/02/fileservlet-supporting-resume-and.html
            // Whether the request body should be written (GET) or not (HEAD).
            boolean content = request.getMethod().equalsIgnoreCase( "GET" );
            APISong.AudioSongStream ass = songAPI.getAudioSong( user, songSid );

            // Validate and process Range and If-Range headers.
            String eTag = songSid + "_" + ass.contentLength + "_" + ass.lastModified;
            long expires = System.currentTimeMillis() + Range.DEFAULT_EXPIRE_TIME;

            // Validate request headers for caching ---------------------------------------------------

            // If-None-Match header should contain "*" or ETag. If so, then return 304.
            String ifNoneMatch = request.getHeader( "If-None-Match" );
            if ( ifNoneMatch != null && Range.matches( ifNoneMatch, eTag ) )
            {
                response.setStatus( HttpServletResponse.SC_NOT_MODIFIED );
                response.setHeader( "ETag", eTag ); // Required in 304.
                response.setDateHeader( "Expires", expires ); // Postpone cache with 1 week.
                return;
            }

            // If-Modified-Since header should be greater than LastModified. If so, then return 304.
            // This header is ignored if any If-None-Match header is specified.
            long ifModifiedSince = request.getDateHeader( "If-Modified-Since" );
            if ( ifNoneMatch == null && ifModifiedSince != -1 && ifModifiedSince + 1000 > ass.lastModified )
            {
                response.setStatus( HttpServletResponse.SC_NOT_MODIFIED );
                response.setHeader( "ETag", eTag ); // Required in 304.
                response.setDateHeader( "Expires", expires ); // Postpone cache with 1 week.
                return;
            }

            // Validate request headers for resume ----------------------------------------------------
            // If-Match header should contain "*" or ETag. If not, then return 412.
            String ifMatch = request.getHeader( "If-Match" );
            if ( ifMatch != null && !Range.matches( ifMatch, eTag ) )
            {
                response.sendError( HttpServletResponse.SC_PRECONDITION_FAILED );
                return;
            }

            // If-Unmodified-Since header should be greater than LastModified. If not, then return 412.
            long ifUnmodifiedSince = request.getDateHeader( "If-Unmodified-Since" );
            if ( ifUnmodifiedSince != -1 && ifUnmodifiedSince + 1000 <= ass.lastModified )
            {
                response.sendError( HttpServletResponse.SC_PRECONDITION_FAILED );
                return;
            }

            // Validate and process range -------------------------------------------------------------
            // Prepare some variables. The full Range represents the complete file.
            Range full = new Range( 0, ass.contentLength - 1, ass.contentLength );
            List<Range> ranges = new ArrayList<Range>();

            String range = request.getHeader( "Range" );
            if ( range != null )
            {
                // Range header should match format "bytes=n-n,n-n,n-n...". If not, then return 416.
                if ( !range.matches( "^bytes=\\d*-\\d*(,\\d*-\\d*)*$" ) )
                {
                    response.setHeader( "Content-Range", "bytes */" + ass.contentLength ); // Required in 416.
                    response.sendError( HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE );
                    return;
                }

                // If-Range header should either match ETag or be greater then LastModified. If not,
                // then return full file.
                String ifRange = request.getHeader( "If-Range" );
                if ( ifRange != null && !ifRange.equals( eTag ) )
                {
                    try
                    {
                        long ifRangeTime = request.getDateHeader( "If-Range" ); // Throws IAE if invalid.
                        if ( ifRangeTime != -1 && ifRangeTime + 1000 < ass.lastModified )
                        {
                            ranges.add( full );
                        }
                    }
                    catch ( IllegalArgumentException ignore )
                    {
                        ranges.add( full );
                    }
                }

                // If any valid If-Range header, then process each part of byte range.
                if ( ranges.isEmpty() )
                {
                    for ( String part : range.substring( 6 ).split( "," ) )
                    {
                        // Assuming a file with length of 100, the following examples returns bytes at:
                        // 50-80 (50 to 80), 40- (40 to length=100), -20 (length-20=80 to length=100).
                        long start = Range.sublong( part, 0, part.indexOf( "-" ) );
                        long end = Range.sublong( part, part.indexOf( "-" ) + 1, part.length() );

                        if ( start == -1 )
                        {
                            start = ass.contentLength - end;
                            end = ass.contentLength - 1;
                        }
                        else if ( end == -1 || end > ass.contentLength - 1 )
                        {
                            end = ass.contentLength - 1;
                        }

                        // Check if Range is syntactically valid. If not, then return 416.
                        if ( start > end )
                        {
                            response.setHeader( "Content-Range", "bytes */" + ass.contentLength ); // Required in 416.
                            response.sendError( HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE );
                            return;
                        }

                        // Add range.
                        ranges.add( new Range( start, end, ass.contentLength ) );
                    }
                }
            }

            // Prepare and initialize response --------------------------------------------------------

            // Get content type by file name and set default GZIP support and content disposition.
            String contentType = "audio/mpeg";
            boolean acceptsGzip = false;
            String disposition = "inline";

            // // If content type is unknown, then set the default value.
            // // For all content types, see: http://www.w3schools.com/media/media_mimeref.asp
            // // To add new content types, add new mime-mapping entry in web.xml.
            // if ( contentType == null )
            // {
            // contentType = "application/octet-stream";
            // }

            // If content type is text, then determine whether GZIP content encoding is supported by
            // the browser and expand content type with the one and right character encoding.
            if ( contentType.startsWith( "text" ) )
            {
                String acceptEncoding = request.getHeader( "Accept-Encoding" );
                acceptsGzip = acceptEncoding != null && Range.accepts( acceptEncoding, "gzip" );
                contentType += ";charset=UTF-8";
            }

            // Else, expect for images, determine content disposition. If content type is supported by
            // the browser, then set to inline, else attachment which will pop a 'save as' dialogue.
            else if ( !contentType.startsWith( "image" ) )
            {
                String accept = request.getHeader( "Accept" );
                disposition = accept != null && Range.accepts( accept, contentType ) ? "inline" : "attachment";
            }

            // Initialize response.
            response.reset();
            response.setBufferSize( Range.DEFAULT_BUFFER_SIZE );
            response.setHeader( "Content-Disposition", disposition + ";filename=\"" + ass.songFileName + "\"" );
            response.setHeader( "Accept-Ranges", "bytes" );
            response.setHeader( "ETag", eTag );
            response.setDateHeader( "Last-Modified", ass.lastModified );
            response.setDateHeader( "Expires", expires );

            // Send requested file (part(s)) to client ------------------------------------------------

            // Prepare streams.
            OutputStream output = null;

            try
            {
                // Open streams.
                output = response.getOutputStream();

                if ( ranges.isEmpty() || ranges.get( 0 ) == full )
                {

                    // Return full file.
                    Range r = full;
                    response.setContentType( contentType );
                    response.setHeader( "Content-Range", "bytes " + r.start + "-" + r.end + "/" + r.total );

                    if ( content )
                    {
                        if ( acceptsGzip )
                        {
                            // The browser accepts GZIP, so GZIP the content.
                            response.setHeader( "Content-Encoding", "gzip" );
                            output = new GZIPOutputStream( output, Range.DEFAULT_BUFFER_SIZE );
                        }
                        else
                        {
                            // Content length is not directly predictable in case of GZIP.
                            // So only add it if there is no means of GZIP, else browser will hang.
                            response.setHeader( "Content-Length", String.valueOf( r.length ) );
                        }

                        // Copy full range.
                        Range.copy( ass.raf, output, r.start, r.length );
                    }

                }
                else if ( ranges.size() == 1 )
                {

                    // Return single part of file.
                    Range r = ranges.get( 0 );
                    response.setContentType( contentType );
                    response.setHeader( "Content-Range", "bytes " + r.start + "-" + r.end + "/" + r.total );
                    response.setHeader( "Content-Length", String.valueOf( r.length ) );
                    response.setStatus( HttpServletResponse.SC_PARTIAL_CONTENT ); // 206.

                    if ( content )
                    {
                        // Copy single part range.
                        Range.copy( ass.raf, output, r.start, r.length );
                    }

                }
                else
                {

                    // Return multiple parts of file.
                    response.setContentType( "multipart/byteranges; boundary=" + Range.MULTIPART_BOUNDARY );
                    response.setStatus( HttpServletResponse.SC_PARTIAL_CONTENT ); // 206.

                    if ( content )
                    {
                        // Cast back to ServletOutputStream to get the easy println methods.
                        ServletOutputStream sos = (ServletOutputStream) output;

                        // Copy multi part range.
                        for ( Range r : ranges )
                        {
                            // Add multipart boundary and header fields for every range.
                            sos.println();
                            sos.println( "--" + Range.MULTIPART_BOUNDARY );
                            sos.println( "Content-Type: " + contentType );
                            sos.println( "Content-Range: bytes " + r.start + "-" + r.end + "/" + r.total );

                            // Copy single part range of multi part range.
                            Range.copy( ass.raf, output, r.start, r.length );
                        }

                        // End with multipart boundary.
                        sos.println();
                        sos.println( "--" + Range.MULTIPART_BOUNDARY + "--" );
                    }
                }
            }
            finally
            {
                // Gently close streams.
                Range.close( output );
                Range.close( ass.is );
                Range.close( ass.raf );
            }
            return;
        }
        catch ( IOException ioe )
        {
            log.error( "failed!", ioe );
            throw new IOMessicRESTException( ioe );
        }
        catch ( Exception e )
        {
            throw new UnknownMessicRESTException( e );
        }
    }

    @ApiMethod( path = "/services/songs/{songSid}/dlna", verb = ApiVerb.GET, description = "Get the audio binary from a song resource of an album for a dlna service", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE } )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ),
        @ApiError( code = NotAuthorizedMessicRESTException.VALUE, description = "Forbidden access" ),
        @ApiError( code = IOMessicRESTException.VALUE, description = "IO error trying to get the audio resource" ) } )
    @RequestMapping( value = "/{songSid}/dlna", method = { RequestMethod.GET, RequestMethod.HEAD } )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    @ApiResponseObject
    public ResponseEntity getSongDLNA( @ApiParam( name = "songSid", description = "SID of the song resource we want to download", paramType = ApiParamType.PATH, required = true )
                                       @PathVariable
                                       Long songSid, HttpServletRequest request, HttpServletResponse response )
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

            headers.setConnection( "close" );
            headers.add( "EXT", null );
            headers.add( "Accept-Ranges", "bytes" );
            headers.add( "transferMode.dlna.org", "Streaming" );
            headers.add( "contentFeatures.dlna.org", "DLNA.ORG_PN=MP3;DLNA.ORG_OP=01;DLNA.ORG_CI=0" );
            headers.add( "realTimeInfo.dlna.org", "DLNA.ORG_TLAG=*" );
            headers.setDate( System.currentTimeMillis() );

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

                String range = request.getHeader( "Range" );
                String[] sbytes = range.split( "=" )[1].split( "-" );
                int from = Integer.valueOf( sbytes[0] );
                int to = (int) ass.contentLength;
                if ( sbytes.length > 1 )
                {
                    to = Integer.valueOf( sbytes[1] );
                }

                headers.setContentLength( to - from );
                headers.add( "Content-Range", "bytes " + from + "-" + to + "/" + ass.contentLength );

                if ( request.getMethod().equalsIgnoreCase( "GET" ) )
                {
                    UtilSubInputStream usi = new UtilSubInputStream( ass.is, from, to );
                    InputStreamResource inputStreamResource = new InputStreamResource( usi );
                    return new ResponseEntity( inputStreamResource, headers, HttpStatus.OK );
                }
                else
                {
                    return new ResponseEntity<byte[]>( new byte[0], headers, HttpStatus.OK );
                }
            }
        }
        catch ( IOException ioe )
        {
            log.error( "failed!", ioe );
            throw new IOMessicRESTException( ioe );
        }
        catch ( Exception e )
        {
            throw new UnknownMessicRESTException( e );
        }
    }

    @ApiMethod( path = "/services/songs/{songSids}/zip", verb = ApiVerb.GET, description = "Get a set of songs zipped", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE } )
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

            String fileName = URLEncoder.encode( "messic-songlist.zip", "UTF-8" );
            response.setHeader( "Content-disposition", "attachment; filename='" + fileName + "'" );

            songAPI.getSongsZip( user, sids, response.getOutputStream() );
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

    @ApiMethod( path = "/services/songs", verb = ApiVerb.POST, description = "Update a song.", produces = {
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
            log.error( "failed!", e );
            throw new IOMessicRESTException( e );
        }
        catch ( SidNotFoundMessicException sm )
        {
            log.error( "failed!", sm );
            throw new NotFoundMessicRESTException( sm );
        }
        catch ( Exception e )
        {
            log.error( "failed!", e );
            throw new UnknownMessicRESTException( e );
        }
    }

    @ApiMethod( path = "/services/songs?filterSongSid=xxxx&filterAlbumSid=xxxx&filterAuthorSid=xxxx&albumInfo=true|false&authorInfo=true|false", verb = ApiVerb.GET, description = "Get all songs. They can be filtered by songSid, albumSid or authorSid (not combined). You can also espcify what information should be returned (with album information or not, for exmaple)", produces = {
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
            log.error( "failed!", e );
            throw new UnknownMessicRESTException( e );
        }
    }
}
