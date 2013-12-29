package org.messic.server.facade.controllers.rest;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.messic.server.api.APITagWizard;
import org.messic.server.api.datamodel.Album;
import org.messic.server.datamodel.dao.DAOMessicSettings;
import org.messic.server.datamodel.dao.DAOUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/album")
public class AlbumTagWizardController {

	@Autowired
	public DAOUser userDAO;
	@Autowired
	public DAOMessicSettings daoSettings;
	@Autowired
	public APITagWizard wizardAPI;

	@RequestMapping(value="/wizard",method=RequestMethod.GET, produces="application/json")
	@ResponseBody
    protected Album getWizardAlbum() throws Exception{
		return wizardAPI.getWizardAlbum();
	}

	@RequestMapping(value="/wizard/reset",method=RequestMethod.GET, produces="application/json")
	@ResponseBody
    protected void resetWizard() throws Exception{
		wizardAPI.resetWizard();
	}

	@RequestMapping(value = "/wizard", method = RequestMethod.PUT)
	@ResponseBody
	protected void uploadSongWizard(HttpEntity<byte[]> requestEntity,
			HttpServletResponse response, HttpSession session) throws Exception {

		byte[] payload = requestEntity.getBody();
		wizardAPI.uploadSongWizard(payload);
	}	

}
