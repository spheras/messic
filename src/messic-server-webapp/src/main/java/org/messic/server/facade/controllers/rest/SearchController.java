package org.messic.server.facade.controllers.rest;

import org.messic.server.api.APISearch;
import org.messic.server.api.datamodel.RandomList;
import org.messic.server.datamodel.MDOUser;
import org.messic.server.datamodel.dao.DAOUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/search")
public class SearchController
{
	@Autowired
	public APISearch searchAPI;
	@Autowired
	public DAOUser userDAO;
	
	@RequestMapping(value="",method=RequestMethod.GET, produces="application/json")
	@ResponseBody
    protected MessicResponse find(@RequestParam(value="content", required=true) String content)
        throws Exception
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MDOUser mdouser=userDAO.getUser(auth.getName());
		RandomList list=searchAPI.search(mdouser.getLogin(),content);
		return new MessicResponse(MessicResponse.CODE_OK, MessicResponse.MESSAGE_OK, list);
    }

}
