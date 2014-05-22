package org.messic.server.api;

import java.io.File;
import java.io.IOException;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.messic.server.Util;
import org.messic.server.api.datamodel.Album;
import org.messic.server.api.datamodel.User;
import org.messic.server.datamodel.dao.DAOMessicSettings;
import org.messic.server.datamodel.dao.DAOUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class APITagWizard {
    @Autowired
    private DAOMessicSettings daoSettings;
    @Autowired
    private DAOUser daoUsers;
    
    public Album getWizardAlbum(User user, String albumCode) throws CannotReadException, IOException, ReadOnlyFileException{
        File basePath=new File(Util.getTmpPath(user, daoSettings.getSettings(), albumCode));
        File[] files=basePath.listFiles();
        TAGWizard tw=new TAGWizard();
        Album result=tw.getAlbumWizard(files);
	        
 		return result;
	}

}
