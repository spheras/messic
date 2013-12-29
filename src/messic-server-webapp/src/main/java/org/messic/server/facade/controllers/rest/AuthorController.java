package org.messic.server.facade.controllers.rest;

import java.util.List;

import org.messic.server.api.APIAuthor;
import org.messic.server.api.datamodel.Author;
import org.messic.server.datamodel.MDOUser;
import org.messic.server.datamodel.dao.DAOUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/author")
public class AuthorController
{
	@Autowired
	public APIAuthor authorAPI;
	@Autowired
	public DAOUser userDAO;
	
	@RequestMapping(value="/{authorName}",method=RequestMethod.GET, produces="application/json")
	@ResponseBody
    protected List<Author> findAuthor(@PathVariable  String authorName)
        throws Exception
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MDOUser mdouser=userDAO.getUser(auth.getName());
		List<Author> authors=authorAPI.findSimilar(mdouser, authorName);
		return authors;
    }

	@RequestMapping(value="",method=RequestMethod.GET, produces="application/json")
	@ResponseBody
    protected List<Author> getAll()
        throws Exception
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MDOUser mdouser=userDAO.getUser(auth.getName());
		List<Author> authors=authorAPI.getAll(mdouser);
		return authors;
    }

}
