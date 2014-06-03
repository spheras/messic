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
import org.messic.server.api.APIGenre;
import org.messic.server.api.datamodel.Genre;
import org.messic.server.api.datamodel.User;
import org.messic.server.datamodel.dao.DAOUser;
import org.messic.server.facade.controllers.rest.exceptions.NotAuthorizedMessicRESTException;
import org.messic.server.facade.controllers.rest.exceptions.UnknownMessicRESTException;
import org.messic.server.facade.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping( "/genres" )
@Api( name = "Genre services", description = "Methods for managing genres" )
public class GenreController
{
    @Autowired
    public APIGenre genreAPI;

    @Autowired
    public DAOUser userDAO;

    @ApiMethod( path = "/genres?filterName=xxxx", verb = ApiVerb.GET, description = "Get all genres", produces = {
        MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ),
        @ApiError( code = NotAuthorizedMessicRESTException.VALUE, description = "Forbidden access" ) } )
    @RequestMapping( value = "", method = RequestMethod.GET )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    @ApiResponseObject
    public List<Genre> getAll( @RequestParam( value = "filterName", required = false )
                               @ApiParam( name = "filterName", description = "partial name of the genre to search", paramType = ApiParamType.QUERY, required = false )
                               String filterName )
        throws UnknownMessicRESTException, NotAuthorizedMessicRESTException
    {

        User user = SecurityUtil.getCurrentUser();
        try
        {
            List<Genre> genres = null;
            if ( filterName == null )
            {
                genres = genreAPI.getAll();
            }
            else
            {
                genres = genreAPI.findSimilar( user, filterName );
            }

            return genres;
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            throw new UnknownMessicRESTException( e );
        }
    }

}
