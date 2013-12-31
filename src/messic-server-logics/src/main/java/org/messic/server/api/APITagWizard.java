package org.messic.server.api;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.io.FileUtils;
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
    
    public Album getWizardAlbum() throws Exception{
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MDOUser mdouser=daoUser.getUser(auth.getName());
        File basePath=new File(Util.getWizardTmpPath(mdouser, daoSettings.getSettings()));
        File[] files=basePath.listFiles();
        TAGWizard tw=new TAGWizard();
        Album result=tw.getAlbumWizard(files);
	        
 		return result;
	}

	/**
	 * Reset the wizard temporal folder.  It removes all the temporal files. This is useful when the user wants to do a new wizard.
	 * @throws Exception
	 */
    public void resetWizard() throws Exception{
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MDOUser mdouser=daoUser.getUser(auth.getName());
        File basePath=new File(Util.getWizardTmpPath(mdouser, daoSettings.getSettings()));

        
        
        if(basePath.exists()){
            FileUtils.deleteDirectory(basePath);
        }
        
        basePath.mkdirs();
	}


	/**
	 * add a song to the temporal folder for the wizard.. This is done to wait all the songs after do a wizard over the songs and discover the tags for the album information
	 * @param payload byte[] bytes of the track
	 * @throws Exception
	 */
	public void uploadSongWizard(byte[] payload) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MDOUser mdouser=daoUser.getUser(auth.getName());
        File basePath=new File(Util.getWizardTmpPath(mdouser, daoSettings.getSettings()));
        basePath.mkdirs();
        
        FileOutputStream fos=new FileOutputStream(new File(basePath.getAbsolutePath() + File.separatorChar + "wizardTrack"+basePath.listFiles().length+".mp3"));
        fos.write(payload);
        fos.close();
	}	

}
