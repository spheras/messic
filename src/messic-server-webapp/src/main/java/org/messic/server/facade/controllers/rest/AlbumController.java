package org.messic.server.facade.controllers.rest;

import java.util.List;

import org.messic.server.api.APIAlbum;
import org.messic.server.api.datamodel.Album;
import org.messic.server.api.datamodel.MessicResponse;
import org.messic.server.datamodel.MDOUser;
import org.messic.server.datamodel.dao.DAOUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/album")
public class AlbumController
{
	@Autowired
	public APIAlbum albumAPI;
	@Autowired
	public DAOUser userDAO;
	
	@RequestMapping(value="/{albumName}",method=RequestMethod.GET, produces="application/json")
	@ResponseBody
    protected List<Album> findAuthor(@PathVariable  String albumName)
        throws Exception
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MDOUser mdouser=userDAO.getUser(auth.getName());
		List<Album> albums=albumAPI.findSimilar(mdouser, albumName);
		return albums;
    }

	@RequestMapping(value="/{authorSid}/{albumName}",method=RequestMethod.GET, produces="application/json")
	@ResponseBody
    protected List<Album> findAuthor(@PathVariable  Integer authorSid, @PathVariable  String albumName)
        throws Exception
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MDOUser mdouser=userDAO.getUser(auth.getName());
		List<Album> albums=albumAPI.findSimilar(mdouser, authorSid, albumName);
		return albums;
    }

	@RequestMapping(value="",method=RequestMethod.GET, produces="application/json")
	@ResponseBody
    protected List<Album> getAll()
        throws Exception
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MDOUser mdouser=userDAO.getUser(auth.getName());
		List<Album> albums=albumAPI.getAll(mdouser);
		return albums;
    }

	@RequestMapping(value="",method=RequestMethod.POST, produces="application/json")
	@ResponseBody
    protected MessicResponse saveAlbum(@RequestBody Album album)
        throws Exception
    {
        albumAPI.saveAlbum(album);
        return new MessicResponse(200,"OK",null);
    }

}
