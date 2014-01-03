package org.messic.server.api;

import java.io.File;

import org.messic.server.Util;
import org.messic.server.api.datamodel.Album;
import org.messic.server.datamodel.MDOUser;
import org.messic.server.datamodel.dao.DAOMessicSettings;
import org.messic.server.datamodel.dao.DAOUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class APITagWizard {
    @Autowired
    private DAOUser daoUser;
    @Autowired
    private DAOMessicSettings daoSettings;
    
    public Album getWizardAlbum(String albumCode) throws Exception{
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MDOUser mdouser=daoUser.getUser(auth.getName());
        File basePath=new File(Util.getTmpPath(mdouser, daoSettings.getSettings(), albumCode));
        File[] files=basePath.listFiles();
        TAGWizard tw=new TAGWizard();
        Album result=tw.getAlbumWizard(files);
	        
 		return result;
	}

}
