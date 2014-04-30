package org.messic.server.facade.controllers.rest;

import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiError;
import org.jsondoc.core.annotation.ApiErrors;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiParam;
import org.jsondoc.core.annotation.ApiResponseObject;
import org.jsondoc.core.pojo.ApiParamType;
import org.jsondoc.core.pojo.ApiVerb;
import org.messic.server.Util;
import org.messic.server.api.APIAlbumResource;
import org.messic.server.datamodel.MDOUser;
import org.messic.server.datamodel.dao.DAOUser;
import org.messic.server.facade.controllers.rest.exceptions.NotAuthorizedMessicRESTException;
import org.messic.server.facade.controllers.rest.exceptions.UnknownMessicRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * @author spheras
 *
 */
@Controller
@RequestMapping("/albumresources")
@Api(name = "Album Resource services", description = "Methods for managing album resources -except songs-")
public class AlbumResourceController
{
	@Autowired
	public APIAlbumResource albumResourceAPI;
	@Autowired
	public DAOUser userDAO;

    @ApiMethod( path = "/albumresources/{resourceSid}", verb = ApiVerb.DELETE, description = "Remove a resource with sid {resourceSid} from the album", produces = {} )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ),
        @ApiError( code = NotAuthorizedMessicRESTException.VALUE, description = "Forbidden access" ) } )
    @RequestMapping( value = "/{resourceSid}", method = RequestMethod.DELETE )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    @ApiResponseObject
    public void removeSong( @PathVariable
                             @ApiParam( name = "resourceSid", description = "Sid of the resource to get", paramType = ApiParamType.PATH, required = true )
                             Long resourceSid )
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
            albumResourceAPI.remove( mdouser, resourceSid );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            throw new UnknownMessicRESTException( e );
        }
    }


}
