package org.messic.server.facade.controllers.rest;

import org.messic.server.api.APISong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * @author spheras
 *
 */
@Controller
@RequestMapping("/song")
public class SongController
{
	@Autowired
	public APISong songAPI;
	

	@RequestMapping(value="/{songSid}/audio",method=RequestMethod.GET)
	public ResponseEntity<byte[]> getSong(@PathVariable Long songSid) throws Exception {
	   byte[] content = songAPI.getAudioSong(songSid);
	   HttpHeaders headers = new HttpHeaders();
	   headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
	   return new ResponseEntity<byte[]>(content, headers, HttpStatus.OK);
	}

}
