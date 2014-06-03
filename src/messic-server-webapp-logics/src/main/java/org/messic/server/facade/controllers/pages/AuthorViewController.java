package org.messic.server.facade.controllers.pages;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.messic.server.api.APIAuthor;
import org.messic.server.api.APIMusicInfo;
import org.messic.server.api.datamodel.Author;
import org.messic.server.api.datamodel.User;
import org.messic.server.api.musicinfo.service.MusicInfoPlugin;
import org.messic.server.datamodel.dao.DAOUser;
import org.messic.server.facade.security.SecurityUtil;
import org.messic.server.facade.security.TokenManagementFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AuthorViewController
{
    @Autowired
    public APIAuthor apiauthor;

    @Autowired
    public APIMusicInfo apimi;

    @Autowired
    public TokenManagementFilter tmf;

    @Autowired
    public DAOUser userDAO;

    @RequestMapping( "/author.do" )
    protected ModelAndView view( @RequestParam( value = "authorSid", required = true )
    Long authorSid, HttpServletRequest req )
        throws Exception
    {
        ModelAndView model = new ModelAndView( "author" );

        User user = SecurityUtil.getCurrentUser();

        // getting the first characters of the authors, to allow listing them by start letter
        Author result = apiauthor.getAuthor( user, authorSid, true, true );
        model.addObject( "author", result );
        model.addObject( "token", this.tmf.obtainToken( req, tmf.getTokenParameter() ) );

        List<MusicInfoPlugin> plugins = apimi.getMusicInfoPlugins( "*" );
        model.addObject( "plugins", plugins );

        return model;
    }

}
