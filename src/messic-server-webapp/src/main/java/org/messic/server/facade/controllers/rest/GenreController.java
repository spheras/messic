package org.messic.server.facade.controllers.rest;

import java.util.List;

import org.messic.server.api.APIGenre;
import org.messic.server.api.datamodel.Genre;
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
@RequestMapping("/genre")
public class GenreController
{
	@Autowired
	public APIGenre genreAPI;
	@Autowired
	public DAOUser userDAO;

	@RequestMapping(value="",method=RequestMethod.GET, produces="application/json")
	@ResponseBody
    protected MessicResponse find(@RequestParam(value="genreName", required=false) String genreName)
        throws Exception
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MDOUser mdouser=userDAO.getUser(auth.getName());
        
		if(genreName==null || genreName.length()==0){
			List<Genre> genres=genreAPI.getAll();
			return new MessicResponse(MessicResponse.CODE_OK, MessicResponse.MESSAGE_OK, genres);
		}else {
			List<Genre> genres=genreAPI.findSimilar(mdouser,genreName);
			return new MessicResponse(MessicResponse.CODE_OK, MessicResponse.MESSAGE_OK, genres);
		}
    }

}
