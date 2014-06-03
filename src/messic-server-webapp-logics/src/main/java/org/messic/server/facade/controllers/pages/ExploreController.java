package org.messic.server.facade.controllers.pages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.messic.server.datamodel.dao.DAOAuthor;
import org.messic.server.datamodel.dao.DAOUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ExploreController
{
    @Autowired
    public DAOAuthor daoauthor;
    @Autowired
    public DAOUser userDAO;

    @RequestMapping("/explore.do")
    protected ModelAndView upload( HttpServletRequest arg0, HttpServletResponse arg1 )
        throws Exception
    {
        ModelAndView model = new ModelAndView( "explore" );

//        MDOUser mdouser=null;
//        try{
//            mdouser=Util.getAuthentication(userDAO);
//        }catch(Exception e){
//            e.printStackTrace();
//            throw new NotAuthorizedMessicRESTException(e);
//        }
//
//        //getting the first characters of the authors, to allow listing them by start letter
//        List<String> results=daoauthor.getFirstCharacters( mdouser.getLogin() );
//        model.addObject( "letters", results );
        
        return model;
    }

}
