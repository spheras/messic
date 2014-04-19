package org.messic.server.facade.controllers.pages;

import org.messic.server.Util;
import org.messic.server.datamodel.MDOAlbum;
import org.messic.server.datamodel.MDOUser;
import org.messic.server.datamodel.dao.DAOAlbum;
import org.messic.server.datamodel.dao.DAOUser;
import org.messic.server.facade.controllers.rest.exceptions.NotAuthorizedMessicRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AlbumViewController
{
    @Autowired
    public DAOAlbum daoalbum;
    @Autowired
    public DAOUser userDAO;

    @RequestMapping("/album.do")
    protected ModelAndView upload( @RequestParam(value="albumSid",required=true) Long albumSid)
        throws Exception
    {
        ModelAndView model = new ModelAndView( "album" );

        MDOUser mdouser=null;
        try{
            mdouser=Util.getAuthentication(userDAO);
        }catch(Exception e){
            e.printStackTrace();
            throw new NotAuthorizedMessicRESTException(e);
        }

        //getting the first characters of the authors, to allow listing them by start letter
        MDOAlbum result=daoalbum.getAlbum( albumSid, mdouser.getLogin());
        model.addObject( "album", result );
        
        return model;
    }

}
