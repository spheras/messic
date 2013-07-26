package org.messic.server.facade.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

public class LoginController
    extends AbstractController
{

    @Override
    protected ModelAndView handleRequestInternal( HttpServletRequest arg0, HttpServletResponse arg1 )
        throws Exception
    {
        ModelAndView model = new ModelAndView( "login" );
        model.addObject( "msg", "hello world2" );

        return model;
    }

}