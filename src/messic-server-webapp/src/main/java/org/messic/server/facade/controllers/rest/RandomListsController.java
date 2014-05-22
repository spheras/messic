package org.messic.server.facade.controllers.rest;

import java.util.List;

import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiError;
import org.jsondoc.core.annotation.ApiErrors;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiResponseObject;
import org.jsondoc.core.pojo.ApiVerb;
import org.messic.server.Util;
import org.messic.server.api.APIRandomLists;
import org.messic.server.api.datamodel.RandomList;
import org.messic.server.api.datamodel.User;
import org.messic.server.datamodel.dao.DAOUser;
import org.messic.server.facade.controllers.rest.exceptions.NotAuthorizedMessicRESTException;
import org.messic.server.facade.controllers.rest.exceptions.UnknownMessicRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/randomlists")
@Api(name = "RandomList services", description = "Methods for managing random lists")
public class RandomListsController
{
	@Autowired
	public APIRandomLists randomListsAPI;
	@Autowired
	public DAOUser userDAO;

	@ApiMethod(path = "/randomlists", verb = ApiVerb.GET, description = "Get random lists of music", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ApiErrors(apierrors = { @ApiError(code = UnknownMessicRESTException.VALUE, description = "Unknown error"), @ApiError(code = NotAuthorizedMessicRESTException.VALUE, description = "Forbidden access")})
	@RequestMapping(value="",method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody @ApiResponseObject
    public List<RandomList> getAll() throws UnknownMessicRESTException, NotAuthorizedMessicRESTException{
		
		User user=null;
		try{
			user=Util.getAuthentication(userDAO);
		}catch(Exception e){
			e.printStackTrace();
			throw new NotAuthorizedMessicRESTException(e);
		}

		try{
			List<RandomList> lists=randomListsAPI.getAllLists(user);
			return lists;
		}catch(Exception e){
			throw new UnknownMessicRESTException(e);
		}
    }

}
