package org.messic.server.facade.controllers.pages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SettingsController
{
	
	@RequestMapping("/settings.do")
	/**
	 * This controller launches the creation user window. 
	 * @param arg0
	 * @param arg1
	 * @return
	 * @throws Exception
	 */
    protected ModelAndView showSettings( HttpServletRequest arg0, HttpServletResponse arg1 )
        throws Exception
    {
        ModelAndView model = new ModelAndView("user");

        return model;
    }
		
}
