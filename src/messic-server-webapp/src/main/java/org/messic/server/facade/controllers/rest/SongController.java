package org.messic.server.facade.controllers.rest;

import java.io.IOException;

import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiError;
import org.jsondoc.core.annotation.ApiErrors;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiParam;
import org.jsondoc.core.annotation.ApiResponseObject;
import org.jsondoc.core.pojo.ApiParamType;
import org.jsondoc.core.pojo.ApiVerb;
import org.messic.server.Util;
import org.messic.server.api.APISong;
import org.messic.server.datamodel.MDOUser;
import org.messic.server.datamodel.dao.DAOUser;
import org.messic.server.facade.controllers.rest.exceptions.IOMessicRESTException;
import org.messic.server.facade.controllers.rest.exceptions.NotAuthorizedMessicRESTException;
import org.messic.server.facade.controllers.rest.exceptions.UnknownMessicRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/songs")
@Api(name = "Song services", description = "Methods for managing songs")
public class SongController
{
	@Autowired
	public APISong songAPI;
	@Autowired
	public DAOUser userDAO;

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
            songAPI.remove( mdouser, songSid );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            throw new UnknownMessicRESTException( e );
        }
    }

	@ApiMethod(path = "/songs/{songSid}/audio", verb = ApiVerb.GET, description = "Get the audio binary from a song resource of an album", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE})
	@ApiErrors(apierrors = { 
			@ApiError(code = UnknownMessicRESTException.VALUE, description = "Unknown error"), 
			@ApiError(code = NotAuthorizedMessicRESTException.VALUE, description = "Forbidden access"),
			@ApiError(code = IOMessicRESTException.VALUE, description = "IO error trying to get the audio resource")
			})
	@RequestMapping(value="/{songSid}/audio",method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody @ApiResponseObject
	public ResponseEntity<byte[]> getSong(
    		@ApiParam(name = "songSid", description = "SID of the song resource we want to download", paramType=ApiParamType.PATH, required=true)
			@PathVariable Long songSid
			) throws NotAuthorizedMessicRESTException, IOMessicRESTException, UnknownMessicRESTException{
		
		MDOUser mdouser=null;
		try{
			mdouser=Util.getAuthentication(userDAO);
		}catch(Exception e){
			e.printStackTrace();
			throw new NotAuthorizedMessicRESTException(e);
		}

		try{
			   byte[] content = songAPI.getAudioSong(mdouser, songSid);
			   HttpHeaders headers = new HttpHeaders();
			   headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			   return new ResponseEntity<byte[]>(content, headers, HttpStatus.OK);
		}catch(IOException ioe){
			ioe.printStackTrace();
			throw new IOMessicRESTException(ioe);
		}catch(Exception e){
			throw new UnknownMessicRESTException(e);
		}
	}

}
