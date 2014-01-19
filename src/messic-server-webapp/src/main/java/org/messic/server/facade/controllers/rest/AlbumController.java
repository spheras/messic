package org.messic.server.facade.controllers.rest;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
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
 * @author spheras
 */
@Controller
@RequestMapping("/albums")
@Api(name = "Album services", description = "Methods for managing albums")
public class AlbumController
{
	@Autowired
	public APIAlbum albumAPI;
	@Autowired
	public DAOUser userDAO;
	@Autowired
	public APITagWizard wizardAPI;
	

	@ApiMethod(path = "/albums?filterAuthorSid=xxxx&filterName=xxxx&songsInfo=true|false&authorInfo=true|false", verb = ApiVerb.GET, description = "Get all albums", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ApiErrors(apierrors = { @ApiError(code = UnknownMessicRESTException.VALUE, description = "Unknown error"), @ApiError(code = NotAuthorizedMessicRESTException.VALUE, description = "Forbidden access")})
	@RequestMapping(value="",method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody @ApiResponseObject
    public List<Album> getAll(
    		@RequestParam(value="filterAuthorSid",required=false)
    		@ApiParam(name = "filterAuthorSid", description = "SID of the author to filter albums", paramType=ApiParamType.QUERY, required=false)
    		Integer filterAuthorSid,
    		@RequestParam(value="filterName",required=false) 
    		@ApiParam(name = "filterName", description = "partial name of the album to search", paramType=ApiParamType.QUERY, required=false)
    		String filterName,
    		@RequestParam(value="songsInfo",required=false) 
    		@ApiParam(name = "songsInfo", description = "flag to return also the songs info of the albums or not. By default, false", paramType=ApiParamType.QUERY, required=false, allowedvalues={"true","false"},format="Boolean")
    		Boolean songsInfo,
    		@RequestParam(value="authorInfo",required=false) 
    		@ApiParam(name = "authorInfo", description = "flag to return also the author info of the albums or not. By default, true", paramType=ApiParamType.QUERY, required=false, allowedvalues={"true","false"},format="Boolean")
    		Boolean authorInfo
    		) throws UnknownMessicRESTException, NotAuthorizedMessicRESTException{

		MDOUser mdouser=null;
		try{
			mdouser=Util.getAuthentication(userDAO);
		}catch(Exception e){
			e.printStackTrace();
			throw new NotAuthorizedMessicRESTException(e);
		}

		try{
			List<Album> albums=null;
			if(filterAuthorSid==null && filterName==null){
				albums=albumAPI.getAll(mdouser,(authorInfo!=null?authorInfo:true),(songsInfo!=null?songsInfo:false));
			}else{
				if(filterAuthorSid!=null && filterName==null){
					albums=albumAPI.getAll(mdouser, filterAuthorSid,(authorInfo!=null?authorInfo:true),(songsInfo!=null?songsInfo:false));
				}else{
					albums=albumAPI.findSimilar(mdouser, filterAuthorSid,filterName,(authorInfo!=null?authorInfo:true),(songsInfo!=null?songsInfo:false));
				}
			}
			
			return albums;
		}catch(Exception e){
			e.printStackTrace();
			throw new UnknownMessicRESTException(e);
		}
	}

	@ApiMethod(path = "/albums/{albumSid}?songsInfo=true|false&authorInfo=true|false", verb = ApiVerb.GET, description = "Get album with id {albumSid}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ApiErrors(apierrors = { @ApiError(code = UnknownMessicRESTException.VALUE, description = "Unknown error"), @ApiError(code = NotAuthorizedMessicRESTException.VALUE, description = "Forbidden access")})
	@RequestMapping(value="/{albumSid}",method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody @ApiResponseObject
    public Album getAlbum(
    		@PathVariable
    		@ApiParam(name = "albumSid", description = "Sid of the album to get", paramType=ApiParamType.PATH, required=true)
    		Long albumSid,
    		@RequestParam(value="songsInfo",required=false) 
    		@ApiParam(name = "songsInfo", description = "flag to return also the songs info of the albums or not. By default, false", paramType=ApiParamType.QUERY, required=false, allowedvalues={"true","false"},format="Boolean")
    		Boolean songsInfo,
    		@RequestParam(value="authorInfo",required=false) 
    		@ApiParam(name = "authorInfo", description = "flag to return also the author info of the albums or not. By default, true", paramType=ApiParamType.QUERY, required=false, allowedvalues={"true","false"},format="Boolean")
    		Boolean authorInfo
    		) throws UnknownMessicRESTException, NotAuthorizedMessicRESTException{

		MDOUser mdouser=null;
		try{
			mdouser=Util.getAuthentication(userDAO);
		}catch(Exception e){
			e.printStackTrace();
			throw new NotAuthorizedMessicRESTException(e);
		}

		try{
			Album album=albumAPI.getAlbum(mdouser, albumSid,(authorInfo!=null?authorInfo:true),(songsInfo!=null?songsInfo:false));
			return album;
		}catch(Exception e){
			e.printStackTrace();
			throw new UnknownMessicRESTException(e);
		}
	}

	@ApiMethod(path = "/albums/{albumSid}/cover", verb = ApiVerb.GET, description = "Get cover for a certain album", produces = { MediaType.IMAGE_JPEG_VALUE})
	@ApiErrors(apierrors = { 
			@ApiError(code = UnknownMessicRESTException.VALUE, description = "Unknown error"), 
			@ApiError(code = NotAuthorizedMessicRESTException.VALUE, description = "Forbidden access"),
			@ApiError(code = NotFoundMessicRESTException.VALUE, description = "Album or Cover not found"),
			@ApiError(code = IOMessicRESTException.VALUE, description = "IO internal server error"),
			})
	@RequestMapping(value="/{albumSid}/cover",method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody @ApiResponseObject
    public ResponseEntity<byte[]> getAlbumCover(
    		@PathVariable 
    		@ApiParam(name = "albumSid", description = "SID of the album to get the cover", paramType=ApiParamType.PATH, required=true)
    		Long albumSid) throws UnknownMessicRESTException, NotAuthorizedMessicRESTException, NotFoundMessicRESTException, IOMessicRESTException{

		MDOUser mdouser=null;
		try{
			mdouser=Util.getAuthentication(userDAO);
		}catch(Exception e){
			e.printStackTrace();
			throw new NotAuthorizedMessicRESTException(e);
		}

		try{
			   byte[] content = albumAPI.getAlbumCover(mdouser, albumSid);
			   HttpHeaders headers = new HttpHeaders();
			   headers.setContentType(MediaType.IMAGE_JPEG);
			   return new ResponseEntity<byte[]>(content, headers, HttpStatus.OK);
		}catch(SidNotFoundMessicException e){
			throw new NotFoundMessicRESTException(e);
		} catch (ResourceNotFoundMessicException e) {
			throw new NotFoundMessicRESTException(e);
		} catch (IOException e) {
			throw new IOMessicRESTException(e);
		}
	}

	@ApiMethod(path = "/albums", verb = ApiVerb.POST, description = "Create or Update an album.  Before creation you need to upload the resources!", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, consumes={ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ApiErrors(apierrors = { 
			@ApiError(code = UnknownMessicRESTException.VALUE, description = "Unknown error"), 
			@ApiError(code = NotAuthorizedMessicRESTException.VALUE, description = "Forbidden access"),
			@ApiError(code = IOMessicRESTException.VALUE, description = "IO internal server error"),
			})
	@RequestMapping(value="",method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody @ApiResponseObject
    public ResponseEntity<HttpStatus> createOrUpdateAlbum(
    		@ApiBodyObject
    		@RequestBody Album album)
    		throws UnknownMessicRESTException, NotAuthorizedMessicRESTException, NotFoundMessicRESTException, IOMessicRESTException{
		
		MDOUser mdouser=null;
		try{
			mdouser=Util.getAuthentication(userDAO);
		}catch(Exception e){
			e.printStackTrace();
			throw new NotAuthorizedMessicRESTException(e);
		}
		
		try{
			albumAPI.createOrUpdateAlbum(mdouser, album);
	        return new ResponseEntity<HttpStatus>(HttpStatus.OK);
		}catch(IOException e){
			throw new IOMessicRESTException(e); 
		}catch(Exception e){
			throw new UnknownMessicRESTException(e);
		}
    }

	@ApiMethod(path = "/albums/{albumCode}?fileName=xxxxx", verb = ApiVerb.POST, description = "Upload a resource for an album. This resources are stored at the temporal folder, waiting until save Album process. The client must post the binary content of the resource.", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, consumes={ MediaType.APPLICATION_OCTET_STREAM_VALUE})
	@ApiErrors(apierrors = { 
			@ApiError(code = UnknownMessicRESTException.VALUE, description = "Unknown error"), 
			@ApiError(code = NotAuthorizedMessicRESTException.VALUE, description = "Forbidden access"),
			@ApiError(code = IOMessicRESTException.VALUE, description = "IO internal server error"),
			})
	@RequestMapping(value = "/{albumCode}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody @ApiResponseObject
	public HttpEntity<HttpStatus> uploadResource(
			@ApiBodyObject
			HttpEntity<byte[]> requestEntity,
			HttpServletResponse response, 
			HttpSession session,
    		@ApiParam(name = "albumCode", description = "code for the album owner of the resource.. This code is the reference for others resources that could be uploaded, and so on", paramType=ApiParamType.PATH, required=true)
			@PathVariable  String albumCode, 
    		@ApiParam(name = "fileName", description = "file name of the resource", paramType=ApiParamType.QUERY, required=true)
			@RequestParam("fileName") String fileName) 
					throws IOMessicRESTException, UnknownMessicRESTException, NotAuthorizedMessicRESTException {
		
		MDOUser mdouser=null;
		try{
			mdouser=Util.getAuthentication(userDAO);
		}catch(Exception e){
			e.printStackTrace();
			throw new NotAuthorizedMessicRESTException(e);
		}
		
		try{
			byte[] payload = requestEntity.getBody();
			albumAPI.uploadResource(mdouser, albumCode, HtmlUtils.htmlUnescape(fileName), payload);
		}catch(IOException ioe){
			throw new IOMessicRESTException(ioe);
		}catch(Exception e){
			throw new UnknownMessicRESTException(e);
		}
		
		return new HttpEntity<HttpStatus>(HttpStatus.OK);
	}

	@ApiMethod(path = "/albums/clear?albumCode=xxxx", verb = ApiVerb.POST, description = "Clear all the temporal files that have been uploaded previously", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ApiErrors(apierrors = { 
			@ApiError(code = UnknownMessicRESTException.VALUE, description = "Unknown error"), 
			@ApiError(code = NotAuthorizedMessicRESTException.VALUE, description = "Forbidden access"),
			@ApiError(code = IOMessicRESTException.VALUE, description = "IO internal server error"),
			})
	@RequestMapping(value = "/clear", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody @ApiResponseObject
    public HttpEntity<HttpStatus> clear(
    		@ApiParam(name = "albumCode", description = "temporal code of the album we want to clear.  This code is the same the client give to the server when upload a resource for an album.  If not give this code, messic will remove all the temporal files uploaded until now.", paramType=ApiParamType.QUERY, required=false)
    		@RequestParam(value="albumCode",required=false)
    		String albumCode
    		) throws NotAuthorizedMessicRESTException, IOMessicRESTException, UnknownMessicRESTException{
		
		MDOUser mdouser=null;
		try{
			mdouser=Util.getAuthentication(userDAO);
		}catch(Exception e){
			e.printStackTrace();
			throw new NotAuthorizedMessicRESTException(e);
		}
		
		try {
			albumAPI.clearTemporal(mdouser, albumCode);
			return new HttpEntity<HttpStatus>(HttpStatus.OK);
		} catch (IOException e) {
			throw new IOMessicRESTException(e);
		} catch(Exception e){
			throw new UnknownMessicRESTException(e);
		}
		
	}


	@ApiMethod(path = "/albums/{albumCode}/wizard", verb = ApiVerb.GET, description = "Get 'magic' info from the resources that have been uploaded previously.  This resources are suposed to be of the same album, messic try to get the album info from them using several mechanisms.", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ApiErrors(apierrors = { 
			@ApiError(code = UnknownMessicRESTException.VALUE, description = "Unknown error"), 
			@ApiError(code = NotAuthorizedMessicRESTException.VALUE, description = "Forbidden access"),
			@ApiError(code = IOMessicRESTException.VALUE, description = "IO internal server error"),
			@ApiError(code = MusicTagsMessicRESTException.VALUE, description = "Song Tags error"),
			})
	@RequestMapping(value = "/{albumCode}/wizard", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody @ApiResponseObject
    public Album getWizardAlbum(
    		@ApiParam(name = "albumCode", description = "temporal code of the album we want to analyze. This code have been set previously while uploading resources. Messic will use the resources linked with these album code", paramType=ApiParamType.PATH, required=true)
    		@PathVariable  String albumCode
    		) throws NotAuthorizedMessicRESTException, UnknownMessicRESTException, IOMessicRESTException, MusicTagsMessicRESTException{

		MDOUser mdouser=null;
		try{
			mdouser=Util.getAuthentication(userDAO);
		}catch(Exception e){
			e.printStackTrace();
			throw new NotAuthorizedMessicRESTException(e);
		}

		try{
			Album album=wizardAPI.getWizardAlbum(mdouser, albumCode);
			return album;
		}catch(IOException e){
			throw new IOMessicRESTException(e);
		} catch (ReadOnlyFileException e) {
			throw new IOMessicRESTException(e);
		} catch (CannotReadException e) {
			throw new MusicTagsMessicRESTException(e);
		} catch(Exception e){
			e.printStackTrace();
			throw new UnknownMessicRESTException(e);
		}
	}


}
