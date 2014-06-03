package org.messic.server.facade.controllers.rest;

import java.util.List;

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
import org.messic.server.api.datamodel.User;
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
    @Autowired
    public APIAuthor authorAPI;

    @Autowired
    public DAOUser userDAO;

    @ApiMethod( path = "/authors?filterName=xxxx&albumsInfo=true|false&songsInfo=true|false", verb = ApiVerb.GET, description = "Get all authors", produces = {
        MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ),
        @ApiError( code = NotAuthorizedMessicRESTException.VALUE, description = "Forbidden access" ) } )
    @RequestMapping( value = "", method = RequestMethod.GET )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    @ApiResponseObject
    public List<Author> getAll( @RequestParam( value = "filterName", required = false )
                                @ApiParam( name = "filterName", description = "partial name of the author to search", paramType = ApiParamType.QUERY, required = false )
                                String filterName,
                                @RequestParam( value = "contains", required = false )
                                @ApiParam( name = "contains", description = "True if filtering is by contains (default), False if filtering is searching any author with the starting filter search.", paramType = ApiParamType.QUERY, required = false, allowedvalues = {
                                    "true", "false" }, format = "Boolean" )
                                Boolean contains,
                                @RequestParam( value = "albumsInfo", required = false )
                                @ApiParam( name = "albumsInfo", description = "flag to return also the albums info of the author or not. By default, false", paramType = ApiParamType.QUERY, required = false, allowedvalues = {
                                    "true", "false" }, format = "Boolean" )
                                Boolean albumsInfo,
                                @RequestParam( value = "songsInfo", required = false )
                                @ApiParam( name = "songsInfo", description = "flag to return also the songs info of the albums or not. By default, false.  Missed if albumsInfo=false", paramType = ApiParamType.QUERY, required = false, allowedvalues = {
                                    "true", "false" }, format = "Boolean" )
                                Boolean songsInfo )
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
                authors =
                    authorAPI.findSimilar( user, filterName, contains, ( albumsInfo != null ? albumsInfo : false ),
                                           ( albumsInfo != null && songsInfo != null && albumsInfo == true ? songsInfo
                                                           : false ) );
            }

            return authors;
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            throw new UnknownMessicRESTException( e );
        }
    }

    @ApiMethod( path = "/authors/{authorSid}?albumsInfo=true|false&songsInfo=true|false", verb = ApiVerb.GET, description = "Get an author", produces = {
        MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ),
        @ApiError( code = NotAuthorizedMessicRESTException.VALUE, description = "Forbidden access" ) } )
    @RequestMapping( value = "/{authorSid}", method = RequestMethod.GET )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    @ApiResponseObject
    public Author getAuthor( @PathVariable
                             @ApiParam( name = "authorSid", description = "Sid of the author to get", paramType = ApiParamType.PATH, required = true )
                             Long authorSid,
                             @RequestParam( value = "albumsInfo", required = false )
                             @ApiParam( name = "albumsInfo", description = "flag to return also the albums info of the author or not. By default, false", paramType = ApiParamType.QUERY, required = false, allowedvalues = {
                                 "true", "false" }, format = "Boolean" )
                             Boolean albumsInfo,
                             @RequestParam( value = "songsInfo", required = false )
                             @ApiParam( name = "songsInfo", description = "flag to return also the songs info of the albums or not. By default, false.  Missed if albumsInfo=false", paramType = ApiParamType.QUERY, required = false, allowedvalues = {
                                 "true", "false" }, format = "Boolean" )
                             Boolean songsInfo )
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
            e.printStackTrace();
            throw new UnknownMessicRESTException( e );
        }
    }

}
