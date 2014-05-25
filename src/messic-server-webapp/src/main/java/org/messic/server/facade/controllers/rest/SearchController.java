package org.messic.server.facade.controllers.rest;

import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiError;
import org.jsondoc.core.annotation.ApiErrors;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiParam;
import org.jsondoc.core.annotation.ApiResponseObject;
import org.jsondoc.core.pojo.ApiParamType;
import org.jsondoc.core.pojo.ApiVerb;
import org.messic.server.api.APISearch;
import org.messic.server.api.datamodel.RandomList;
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
@RequestMapping( "/search" )
@Api( name = "Search services", description = "Methods for search things" )
public class SearchController
{
    @Autowired
    public APISearch searchAPI;

    @Autowired
    public DAOUser userDAO;

    @ApiMethod( path = "/search?content=xxxx", verb = ApiVerb.GET, description = "Search at messic by everything", produces = {
        MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ),
        @ApiError( code = NotAuthorizedMessicRESTException.VALUE, description = "Forbidden access" ) } )
    @RequestMapping( value = "", method = RequestMethod.GET )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    @ApiResponseObject
    public RandomList find( @ApiParam( name = "content", description = "content to search. This content will be used to compare with a lot of things.  The content will be separated by spaces, allowing multiple searches.  If some words are quoted, then the search will be the whole quoted phrase.", paramType = ApiParamType.QUERY, required = true )
                            @RequestParam( value = "content", required = true )
                            String content )
        throws NotAuthorizedMessicRESTException, UnknownMessicRESTException
    {

        User user = SecurityUtil.getCurrentUser();
        try
        {
            RandomList list = searchAPI.search( user.getLogin(), content );
            return list;
        }
        catch ( Exception e )
        {
            throw new UnknownMessicRESTException( e );
        }
    }

}
