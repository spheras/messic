package org.messic.server.facade.controllers.rest;

import java.util.List;

import org.messic.server.api.APIRandomLists;
import org.messic.server.api.datamodel.RandomList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/randomlists")
public class RandomListsController
{
	@Autowired
	public APIRandomLists randomListsAPI;
	
	@RequestMapping(value="",method=RequestMethod.GET, produces="application/json")
	@ResponseBody
    protected MessicResponse getAll()
        throws Exception
    {
		List<RandomList> lists=randomListsAPI.getAllLists();
		return new MessicResponse(MessicResponse.CODE_OK, MessicResponse.MESSAGE_OK, lists);
    }

}
