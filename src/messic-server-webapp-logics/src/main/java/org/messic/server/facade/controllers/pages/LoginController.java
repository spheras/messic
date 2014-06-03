package org.messic.server.facade.controllers.pages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController
{

	@RequestMapping("/login.do")
    protected ModelAndView login( HttpServletRequest arg0, HttpServletResponse arg1 )
        throws Exception
    {
        ModelAndView model = new ModelAndView( "login" );
        model.addObject( "msg", "hello world2" );

        return model;
    }

}
