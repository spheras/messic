package org.messic.server.facade.controllers.rest;

import java.util.List;

import org.messic.server.api.APIGenre;
import org.messic.server.api.datamodel.Genre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/genre")
public class GenreController
{
	@Autowired
	public APIGenre genreAPI;
	
	@RequestMapping(value="",method=RequestMethod.GET, produces="application/json")
	@ResponseBody
    protected List<Genre> getAll()
        throws Exception
    {
		List<Genre> genres=genreAPI.getAll();
		return genres;
    }

}
