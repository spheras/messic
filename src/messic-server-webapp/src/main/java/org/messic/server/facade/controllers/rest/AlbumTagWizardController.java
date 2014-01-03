package org.messic.server.facade.controllers.rest;

import org.messic.server.api.APITagWizard;
import org.messic.server.datamodel.dao.DAOMessicSettings;
import org.messic.server.datamodel.dao.DAOUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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

	@RequestMapping(value="/wizard/{albumCode}",method=RequestMethod.GET, produces="application/json")
	@ResponseBody
    protected MessicResponse getWizardAlbum(@PathVariable  String albumCode) throws Exception{
		return new MessicResponse(MessicResponse.CODE_OK, MessicResponse.MESSAGE_OK,wizardAPI.getWizardAlbum(albumCode));
	}

}
