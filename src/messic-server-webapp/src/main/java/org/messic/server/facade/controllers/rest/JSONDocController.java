package org.messic.server.facade.controllers.rest;

import java.util.ArrayList;

import javax.servlet.ServletContext;

import org.jsondoc.core.pojo.JSONDoc;
import org.jsondoc.core.util.JSONDocUtils;
import org.messic.server.api.datamodel.Album;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/jsondoc")
public class JSONDocController {
	@Autowired
	private ServletContext servletContext;
	private String version;
	private String basePath;
	
	public JSONDocController(){
		setVersion("1.0");
		setBasePath("http://localhost:8080/messic");
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	JSONDoc getApi() {
		ArrayList<String> packagesList=new ArrayList<String>();
		packagesList.add(this.getClass().getPackage().getName());
		packagesList.add(Album.class.getPackage().getName());
		return JSONDocUtils.getApiDoc(version, basePath,packagesList);
	}

}
