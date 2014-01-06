package org.messic.server.facade.controllers.rest;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.messic.server.api.APIAlbum;
import org.messic.server.api.datamodel.Album;
import org.messic.server.datamodel.MDOUser;
import org.messic.server.datamodel.dao.DAOUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;


/**
 * 	RequestMapping(value="/{albumName}",method=RequestMethod.GET, produces="application/json")
 *  RequestMapping(value="/{authorSid}/{albumName}",method=RequestMethod.GET, produces="application/json")
 *  RequestMapping(value="",method=RequestMethod.GET, produces="application/json")
 *  
 *  RequestMapping(value="/reset/{albumCode}",method=RequestMethod.POST, produces="application/json")
 *  RequestMapping(value="",method=RequestMethod.POST, produces="application/json")
 *  
 *  RequestMapping(value = "/{albumCode}/{resourceCode}/{fileName:.+}", method = RequestMethod.PUT) 
 *  
 * @author spheras
 *
 */
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
    protected MessicResponse findAlbum(@PathVariable  String albumName)
        throws Exception
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MDOUser mdouser=userDAO.getUser(auth.getName());
		List<Album> albums=albumAPI.findSimilar(mdouser, albumName);
		return new MessicResponse(MessicResponse.CODE_OK, MessicResponse.MESSAGE_OK, albums);
    }

	@RequestMapping(value="/{authorSid}/{albumName}",method=RequestMethod.GET, produces="application/json")
	@ResponseBody
    protected MessicResponse findAlbum(@PathVariable  Integer authorSid, @PathVariable  String albumName)
        throws Exception
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MDOUser mdouser=userDAO.getUser(auth.getName());
		List<Album> albums=albumAPI.findSimilar(mdouser, authorSid, albumName);
		return new MessicResponse(MessicResponse.CODE_OK, MessicResponse.MESSAGE_OK, albums);
    }

	@RequestMapping(value="",method=RequestMethod.GET, produces="application/json")
	@ResponseBody
    protected MessicResponse getAll()
        throws Exception
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MDOUser mdouser=userDAO.getUser(auth.getName());
		List<Album> albums=albumAPI.getAll(mdouser);
		return new MessicResponse(MessicResponse.CODE_OK, MessicResponse.MESSAGE_OK, albums);
    }

	@RequestMapping(value="",method=RequestMethod.POST, produces="application/json")
	@ResponseBody
    protected MessicResponse saveAlbum(@RequestBody Album album)
        throws Exception
    {
        albumAPI.saveAlbum(album);
        return new MessicResponse(200,"OK",null);
    }

	@RequestMapping(value = "/{albumCode}/{resourceCode}/{fileName:.+}", method = RequestMethod.PUT)
	@ResponseBody
	protected MessicResponse uploadResource(HttpEntity<byte[]> requestEntity,
			HttpServletResponse response, HttpSession session,@PathVariable  String albumCode, @PathVariable  String resourceCode, @PathVariable  String fileName) throws Exception {
		byte[] payload = requestEntity.getBody();
		albumAPI.uploadResource(albumCode, resourceCode, HtmlUtils.htmlUnescape(fileName), payload);
		//return new MessicResponse(MessicResponse.CODE_OK, MessicResponse.MESSAGE_OK, null); //it fails, TODO
		return null;
	}
	
	@RequestMapping(value="/reset/{albumCode}",method=RequestMethod.POST, produces="application/json")
	@ResponseBody
    protected MessicResponse reset(@PathVariable  String albumCode) throws Exception{
		albumAPI.resetUploaded(albumCode);
		return new MessicResponse(MessicResponse.CODE_OK, MessicResponse.MESSAGE_OK, null);
	}

	@RequestMapping(value="/{albumSid}/cover",method=RequestMethod.GET)
	public ResponseEntity<byte[]> getAlbumCover(@PathVariable Long albumSid) throws Exception {
	   byte[] content = albumAPI.getAlbumCover(albumSid);
	   HttpHeaders headers = new HttpHeaders();
	   headers.setContentType(MediaType.IMAGE_JPEG);
	   return new ResponseEntity<byte[]>(content, headers, HttpStatus.OK);
	}

}
