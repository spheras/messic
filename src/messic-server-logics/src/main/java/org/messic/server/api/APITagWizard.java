package org.messic.server.api;

import java.io.File;
import java.io.IOException;

import org.messic.server.Util;
import org.messic.server.api.datamodel.Album;
import org.messic.server.api.tagwizard.TAGWizard;
import org.messic.server.datamodel.MDOUser;
import org.messic.server.datamodel.dao.DAOMessicSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class APITagWizard {
    @Autowired
    private DAOMessicSettings daoSettings;
    
    public Album getWizardAlbum(MDOUser mdouser, String albumCode) throws IOException{
        File basePath=new File(Util.getTmpPath(mdouser, daoSettings.getSettings(), albumCode));
        File[] files=basePath.listFiles();
        TAGWizard tw=new TAGWizard();
        Album result=tw.getAlbumWizard(files);
	        
 		return result;
	}

}
