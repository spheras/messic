package org.messic.server.facade.controllers.rest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import org.messic.server.api.exceptions.ResourceNotFoundMessicException;
import org.messic.server.api.exceptions.SidNotFoundMessicException;
import org.messic.server.datamodel.MDOUser;
import org.messic.server.datamodel.dao.DAOUser;
import org.messic.server.facade.controllers.rest.exceptions.IOMessicRESTException;
import org.messic.server.facade.controllers.rest.exceptions.MusicTagsMessicRESTException;
import org.messic.server.facade.controllers.rest.exceptions.NotAuthorizedMessicRESTException;
import org.messic.server.facade.controllers.rest.exceptions.NotFoundMessicRESTException;
import org.messic.server.facade.controllers.rest.exceptions.UnknownMessicRESTException;
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
    @Autowired
    public APIAlbum albumAPI;

    @Autowired
    public DAOUser userDAO;

    @Autowired
    public APITagWizard wizardAPI;

    @ApiMethod( path = "/albums?filterAuthorSid=xxxx&filterName=xxxx&songsInfo=true|false&authorInfo=true|false", verb = ApiVerb.GET, description = "Get all albums", produces = {
        MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ),
        @ApiError( code = NotAuthorizedMessicRESTException.VALUE, description = "Forbidden access" ) } )
    @RequestMapping( value = "", method = RequestMethod.GET )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    @ApiResponseObject
    public List<Album> getAll( @RequestParam( value = "filterAuthorSid", required = false )
                               @ApiParam( name = "filterAuthorSid", description = "SID of the author to filter albums", paramType = ApiParamType.QUERY, required = false )
                               Integer filterAuthorSid,
                               @RequestParam( value = "filterName", required = false )
                               @ApiParam( name = "filterName", description = "partial name of the album to search", paramType = ApiParamType.QUERY, required = false )
                               String filterName,
                               @RequestParam( value = "authorInfo", required = false )
                               @ApiParam( name = "authorInfo", description = "flag to return also the author info of the albums or not. By default, true", paramType = ApiParamType.QUERY, required = false, allowedvalues = {
                                   "true", "false" }, format = "Boolean" )
                               Boolean authorInfo,
                               @RequestParam( value = "songsInfo", required = false )
                               @ApiParam( name = "songsInfo", description = "flag to return also the songs info of the albums or not. By default, false", paramType = ApiParamType.QUERY, required = false, allowedvalues = {
                                   "true", "false" }, format = "Boolean" )
                               Boolean songsInfo,
                               @RequestParam( value = "resourcesInfo", required = false )
                               @ApiParam( name = "resourcesInfo", description = "flag to return also the artworks and others info of the albums or not. By default, the same value of songsInfo", paramType = ApiParamType.QUERY, required = false, allowedvalues = {
                                   "true", "false" }, format = "Boolean" )
                               Boolean resourcesInfo )
        throws UnknownMessicRESTException, NotAuthorizedMessicRESTException
    {

        MDOUser mdouser = null;
        try
        {
            mdouser = Util.getAuthentication( userDAO );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            throw new NotAuthorizedMessicRESTException( e );
        }

        try
        {
            List<Album> albums = null;
            if ( filterAuthorSid == null && filterName == null )
            {
                albums =
                    albumAPI.getAll( mdouser, ( authorInfo != null ? authorInfo : true ),
                                     ( songsInfo != null ? songsInfo : false ), ( resourcesInfo != null ? resourcesInfo
                                                     : ( songsInfo != null ? songsInfo : false ) ) );
            }
            else
            {
                if ( filterAuthorSid != null && filterName == null )
                {
                    albums =
                        albumAPI.getAll( mdouser, filterAuthorSid, ( authorInfo != null ? authorInfo : true ),
                                         ( songsInfo != null ? songsInfo : false ),
                                         ( resourcesInfo != null ? resourcesInfo : ( songsInfo != null ? songsInfo
                                                         : false ) ) );
                }
                else
                {
                    albums =
                        albumAPI.findSimilar( mdouser, filterAuthorSid, filterName, ( authorInfo != null ? authorInfo
                                                              : true ), ( songsInfo != null ? songsInfo : false ),
                                              ( resourcesInfo != null ? resourcesInfo : ( songsInfo != null ? songsInfo
                                                              : false ) ) );
                }
            }

            return albums;
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            throw new UnknownMessicRESTException( e );
        }
    }

    @ApiMethod( path = "/albums/{albumSid}/zip", verb = ApiVerb.GET, description = "Get the album binary, get the whole album zipped", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE } )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ),
        @ApiError( code = NotAuthorizedMessicRESTException.VALUE, description = "Forbidden access" ),
        @ApiError( code = IOMessicRESTException.VALUE, description = "IO error trying to get the album resource" ) } )
    @RequestMapping( value = "/{albumSid}/zip", method = RequestMethod.GET )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    @ApiResponseObject
    public void getAlbumZip( @ApiParam( name = "albumSid", description = "SID of the album resource we want to download", paramType = ApiParamType.PATH, required = true )
                             @PathVariable
                             Long albumSid, HttpServletResponse response )
        throws NotAuthorizedMessicRESTException, IOMessicRESTException, UnknownMessicRESTException
    {

        MDOUser mdouser = null;
        try
        {
            mdouser = Util.getAuthentication( userDAO );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            throw new NotAuthorizedMessicRESTException( e );
        }

        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            albumAPI.getAlbumZip( mdouser, albumSid, baos );
            FileOutputStream fos = new FileOutputStream( new File( "/home/spheras/estoquees.zip" ) );
            fos.write( baos.toByteArray() );
            fos.close();

            albumAPI.getAlbumZip( mdouser, albumSid, response.getOutputStream() );
            // // Prepare acceptable media type
            // List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
            // acceptableMediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
            //
            // // Prepare header
            // HttpHeaders headers = new HttpHeaders();
            // headers.setAccept(acceptableMediaTypes);
            // HttpEntity<String> entity = new HttpEntity<String>(headers);
            //
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

    @ApiMethod( path = "/albums/{albumSid}", verb = ApiVerb.DELETE, description = "Remove an album with sid {albumSid}", produces = {} )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ),
        @ApiError( code = NotAuthorizedMessicRESTException.VALUE, description = "Forbidden access" ) } )
    @RequestMapping( value = "/{albumSid}", method = RequestMethod.DELETE )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    @ApiResponseObject
    public void removeAlbum( @PathVariable
                             @ApiParam( name = "albumSid", description = "Sid of the album to get", paramType = ApiParamType.PATH, required = true )
                             Long albumSid )
        throws UnknownMessicRESTException, NotAuthorizedMessicRESTException
    {
        MDOUser mdouser = null;
        try
        {
            mdouser = Util.getAuthentication( userDAO );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            throw new NotAuthorizedMessicRESTException( e );
        }

        try
        {
            albumAPI.remove( mdouser, albumSid );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            throw new UnknownMessicRESTException( e );
        }
    }

    @ApiMethod( path = "/albums/{albumSid}?songsInfo=true|false&authorInfo=true|false", verb = ApiVerb.GET, description = "Get album with id {albumSid}", produces = {
        MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ),
        @ApiError( code = NotAuthorizedMessicRESTException.VALUE, description = "Forbidden access" ) } )
    @RequestMapping( value = "/{albumSid}", method = RequestMethod.GET )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    @ApiResponseObject
    public Album getAlbum( @PathVariable
                           @ApiParam( name = "albumSid", description = "Sid of the album to get", paramType = ApiParamType.PATH, required = true )
                           Long albumSid,
                           @RequestParam( value = "songsInfo", required = false )
                           @ApiParam( name = "songsInfo", description = "flag to return also the songs info of the albums or not. By default, false", paramType = ApiParamType.QUERY, required = false, allowedvalues = {
                               "true", "false" }, format = "Boolean" )
                           Boolean songsInfo,
                           @RequestParam( value = "authorInfo", required = false )
                           @ApiParam( name = "authorInfo", description = "flag to return also the author info of the albums or not. By default, true", paramType = ApiParamType.QUERY, required = false, allowedvalues = {
                               "true", "false" }, format = "Boolean" )
                           Boolean authorInfo,
                           @RequestParam( value = "resourcesInfo", required = false )
                           @ApiParam( name = "resourcesInfo", description = "flag to return also the artworks and others info of the albums or not. By default, the same value of songsInfo", paramType = ApiParamType.QUERY, required = false, allowedvalues = {
                               "true", "false" }, format = "Boolean" )
                           Boolean resourcesInfo )
        throws UnknownMessicRESTException, NotAuthorizedMessicRESTException
    {

        MDOUser mdouser = null;
        try
        {
            mdouser = Util.getAuthentication( userDAO );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            throw new NotAuthorizedMessicRESTException( e );
        }

        try
        {
            Album album =
                albumAPI.getAlbum( mdouser, albumSid, ( authorInfo != null ? authorInfo : true ),
                                   ( songsInfo != null ? songsInfo : false ), ( resourcesInfo != null ? resourcesInfo
                                                   : ( songsInfo != null ? songsInfo : false ) ) );
            return album;
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            throw new UnknownMessicRESTException( e );
        }
    }

    @ApiMethod( path = "/albums/{resourceSid}/resource", verb = ApiVerb.GET, description = "Get a resource of an album", produces = { MediaType.IMAGE_JPEG_VALUE } )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ),
        @ApiError( code = NotAuthorizedMessicRESTException.VALUE, description = "Forbidden access" ),
        @ApiError( code = NotFoundMessicRESTException.VALUE, description = "Artwork not found" ),
        @ApiError( code = IOMessicRESTException.VALUE, description = "IO internal server error" ), } )
    @RequestMapping( value = "/{resourceSid}/resource", method = RequestMethod.GET )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    @ApiResponseObject
    public ResponseEntity<byte[]> getAlbumResource( @PathVariable
                                                    @ApiParam( name = "resourceSid", description = "SID of the resource to get", paramType = ApiParamType.PATH, required = true )
                                                    Long resourceSid )
        throws UnknownMessicRESTException, NotAuthorizedMessicRESTException, NotFoundMessicRESTException,
        IOMessicRESTException
    {

        MDOUser mdouser = null;
        try
        {
            mdouser = Util.getAuthentication( userDAO );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            throw new NotAuthorizedMessicRESTException( e );
        }

        try
        {
            byte[] content = albumAPI.getAlbumResource( mdouser, resourceSid );
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

    @ApiMethod( path = "/albums/{albumSid}/cover", verb = ApiVerb.GET, description = "Get cover for a certain album", produces = { MediaType.IMAGE_JPEG_VALUE } )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ),
        @ApiError( code = NotAuthorizedMessicRESTException.VALUE, description = "Forbidden access" ),
        @ApiError( code = NotFoundMessicRESTException.VALUE, description = "Album or Cover not found" ),
        @ApiError( code = IOMessicRESTException.VALUE, description = "IO internal server error" ), } )
    @RequestMapping( value = "/{albumSid}/cover", method = RequestMethod.GET )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    @ApiResponseObject
    public ResponseEntity<byte[]> getAlbumCover( @PathVariable
                                                 @ApiParam( name = "albumSid", description = "SID of the album to get the cover", paramType = ApiParamType.PATH, required = true )
                                                 Long albumSid )
        throws UnknownMessicRESTException, NotAuthorizedMessicRESTException, NotFoundMessicRESTException,
        IOMessicRESTException
    {

        MDOUser mdouser = null;
        try
        {
            mdouser = Util.getAuthentication( userDAO );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            throw new NotAuthorizedMessicRESTException( e );
        }

        try
        {
            byte[] content = albumAPI.getAlbumCover( mdouser, albumSid );
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

    @ApiMethod( path = "/albums", verb = ApiVerb.POST, description = "Create or Update an album.  Before creation you need to upload the resources!", produces = {
        MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, consumes = {
        MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ),
        @ApiError( code = NotAuthorizedMessicRESTException.VALUE, description = "Forbidden access" ),
        @ApiError( code = IOMessicRESTException.VALUE, description = "IO internal server error" ), } )
    @RequestMapping( value = "", method = RequestMethod.POST )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    @ApiResponseObject
    public ResponseEntity<HttpStatus> createOrUpdateAlbum( @ApiBodyObject
    @RequestBody
    Album album )
        throws UnknownMessicRESTException, NotAuthorizedMessicRESTException, NotFoundMessicRESTException,
        IOMessicRESTException
    {

        MDOUser mdouser = null;
        try
        {
            mdouser = Util.getAuthentication( userDAO );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            throw new NotAuthorizedMessicRESTException( e );
        }

        try
        {
            albumAPI.createOrUpdateAlbum( mdouser, album );
            return new ResponseEntity<HttpStatus>( HttpStatus.OK );
        }
        catch ( IOException e )
        {
            e.printStackTrace();
            throw new IOMessicRESTException( e );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            throw new UnknownMessicRESTException( e );
        }
    }

    @ApiMethod( path = "/albums/{albumCode}?fileName=xxxxx", verb = ApiVerb.PUT, description = "Upload a resource for an album. This resources are stored at the temporal folder, waiting until save Album process. The client must post the binary content of the resource.", produces = {
        MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, consumes = { MediaType.APPLICATION_OCTET_STREAM_VALUE } )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ),
        @ApiError( code = NotAuthorizedMessicRESTException.VALUE, description = "Forbidden access" ),
        @ApiError( code = IOMessicRESTException.VALUE, description = "IO internal server error" ), } )
    @RequestMapping( value = "/{albumCode}", method = RequestMethod.PUT )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    @ApiResponseObject
    public HttpEntity<HttpStatus> uploadResource( @ApiBodyObject
                                                  HttpEntity<byte[]> requestEntity,
                                                  HttpServletResponse response,
                                                  HttpSession session,
                                                  @ApiParam( name = "albumCode", description = "code for the album owner of the resource.. This code is the reference for others resources that could be uploaded, and so on", paramType = ApiParamType.PATH, required = true )
                                                  @PathVariable
                                                  String albumCode,
                                                  @ApiParam( name = "fileName", description = "file name of the resource", paramType = ApiParamType.QUERY, required = true )
                                                  @RequestParam( "fileName" )
                                                  String fileName )
        throws IOMessicRESTException, UnknownMessicRESTException, NotAuthorizedMessicRESTException
    {

        MDOUser mdouser = null;
        try
        {
            mdouser = Util.getAuthentication( userDAO );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            throw new NotAuthorizedMessicRESTException( e );
        }

        try
        {
            byte[] payload = requestEntity.getBody();
            albumAPI.uploadResource( mdouser, albumCode, HtmlUtils.htmlUnescape( fileName ), payload );
        }
        catch ( IOException ioe )
        {
            throw new IOMessicRESTException( ioe );
        }
        catch ( Exception e )
        {
            throw new UnknownMessicRESTException( e );
        }

        return new HttpEntity<HttpStatus>( HttpStatus.OK );
    }

    @ApiMethod( path = "/albums/clear?albumCode=xxxx", verb = ApiVerb.POST, description = "Clear all the temporal files that have been uploaded previously. You can pass a json object with all the files you don't want to delate (the algorithm will erase all files that aren't in the list)", produces = {
        MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ),
        @ApiError( code = NotAuthorizedMessicRESTException.VALUE, description = "Forbidden access" ),
        @ApiError( code = IOMessicRESTException.VALUE, description = "IO internal server error" ), } )
    @RequestMapping( value = "/clear", method = RequestMethod.POST )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    @ApiResponseObject
    public List<org.messic.server.api.datamodel.File> clear( @ApiParam( name = "albumCode", description = "temporal code of the album we want to clear.  This code is the same the client give to the server when upload a resource for an album.  If not give this code, messic will remove all the temporal files uploaded until now.", paramType = ApiParamType.QUERY, required = false )
                                                             @RequestParam( value = "albumCode", required = false )
                                                             String albumCode, @ApiBodyObject
                                                             @RequestBody( required = false )
                                                             List<org.messic.server.api.datamodel.File> exceptFiles )
        throws NotAuthorizedMessicRESTException, IOMessicRESTException, UnknownMessicRESTException
    {

        MDOUser mdouser = null;
        try
        {
            mdouser = Util.getAuthentication( userDAO );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            throw new NotAuthorizedMessicRESTException( e );
        }

        try
        {
            List<org.messic.server.api.datamodel.File> existingFiles =
                albumAPI.clearTemporal( mdouser, albumCode, exceptFiles );
            return existingFiles;// new HttpEntity<HttpStatus>( HttpStatus.OK );
        }
        catch ( IOException e )
        {
            throw new IOMessicRESTException( e );
        }
        catch ( Exception e )
        {
            throw new UnknownMessicRESTException( e );
        }

    }

    @ApiMethod( path = "/albums/{albumCode}/wizard", verb = ApiVerb.GET, description = "Get 'magic' info from the resources that have been uploaded previously.  This resources are suposed to be of the same album, messic try to get the album info from them using several mechanisms. If the {pluginName} param is not present it returns only the basic info with all the available plugins. ALSO SUPPORT POST TO SUBMIT THE PARAMETER ALBUMHELPINFO!.", produces = {
        MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ),
        @ApiError( code = NotAuthorizedMessicRESTException.VALUE, description = "Forbidden access" ),
        @ApiError( code = IOMessicRESTException.VALUE, description = "IO internal server error" ),
        @ApiError( code = MusicTagsMessicRESTException.VALUE, description = "Song Tags error" ), } )
    @RequestMapping( value = "/{albumCode}/wizard", method = { RequestMethod.GET, RequestMethod.POST } )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    @ApiResponseObject
    public List<TAGWizardPlugin> getWizardAlbum( @ApiParam( name = "albumCode", description = "temporal code of the album we want to analyze. This code have been set previously while uploading resources. Messic will use the resources linked with these album code", paramType = ApiParamType.PATH, required = true )
                                                 @PathVariable
                                                 String albumCode,
                                                 @ApiParam( name = "pluginName", description = "Name of the plugin we want to use.. It's not required. If this paremeter is not present the function will return only the basic informartion with all the available plugins names to query", paramType = ApiParamType.PATH, required = false )
                                                 @RequestParam( value = "pluginName", required = false )
                                                 String pluginName, @ApiBodyObject
                                                 @RequestBody(required=false)
                                                 Album albumHelpInfo )
        throws NotAuthorizedMessicRESTException, UnknownMessicRESTException, IOMessicRESTException,
        MusicTagsMessicRESTException
    {

        MDOUser mdouser = null;
        try
        {
            mdouser = Util.getAuthentication( userDAO );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            throw new NotAuthorizedMessicRESTException( e );
        }

        try
        {
            List<TAGWizardPlugin> result = new ArrayList<TAGWizardPlugin>();
            if ( pluginName != null && pluginName.length() > 0 )
            {
                TAGWizardPlugin album = wizardAPI.getWizardAlbum( mdouser, pluginName, albumHelpInfo, albumCode );
                result.add( album );
            }
            else
            {
                result = wizardAPI.getWizards( mdouser, albumCode );
            }
            return result;
        }
        catch ( IOException e )
        {
            throw new MusicTagsMessicRESTException( e );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            throw new UnknownMessicRESTException( e );
        }
    }

}
