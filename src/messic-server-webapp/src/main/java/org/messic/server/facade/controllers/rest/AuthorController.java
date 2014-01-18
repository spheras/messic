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
import org.messic.server.Util;
import org.messic.server.api.APIAuthor;
import org.messic.server.api.datamodel.Author;
import org.messic.server.datamodel.MDOUser;
import org.messic.server.datamodel.dao.DAOUser;
import org.messic.server.facade.controllers.rest.exceptions.NotAuthorizedMessicRESTException;
import org.messic.server.facade.controllers.rest.exceptions.UnknownMessicRESTException;
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
@RequestMapping("/authors")
@Api(name = "Author services", description = "Methods for managing authors")
public class AuthorController
{
	@Autowired
	public APIAuthor authorAPI;
	@Autowired
	public DAOUser userDAO;

	@ApiMethod(path = "/authors", verb = ApiVerb.GET, description = "Get all authors", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ApiErrors(apierrors = { @ApiError(code = UnknownMessicRESTException.VALUE, description = "Unknown error"), @ApiError(code = NotAuthorizedMessicRESTException.VALUE, description = "Forbidden access")})
	@RequestMapping(value="",method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody @ApiResponseObject
    public List<Author> getAll(
    		@RequestParam(value="filterName",required=false) 
    		@ApiParam(name = "filterName", description = "partial name of the author to search", paramType=ApiParamType.QUERY, required=false)
    		String filterName) throws UnknownMessicRESTException, NotAuthorizedMessicRESTException{

		MDOUser mdouser=null;
		try{
			mdouser=Util.getAuthentication(userDAO);
		}catch(Exception e){
			e.printStackTrace();
			throw new NotAuthorizedMessicRESTException(e);
		}

		try{
			List<Author> authors=null;
			if(filterName==null){
				authors=authorAPI.getAll(mdouser);
			}else{
				authors=authorAPI.findSimilar(mdouser,filterName);
			}
			
			return authors;
		}catch(Exception e){
			e.printStackTrace();
			throw new UnknownMessicRESTException(e);
		}
	}



}
