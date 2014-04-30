package org.messic.server.api;

import java.io.File;
import java.io.IOException;

import org.messic.server.Util;
import org.messic.server.datamodel.MDOAlbumResource;
import org.messic.server.datamodel.MDOUser;
import org.messic.server.datamodel.dao.DAOAlbumResource;
import org.messic.server.datamodel.dao.DAOMessicSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class APIAlbumResource {
    @Autowired
    private DAOMessicSettings daoSettings;
    @Autowired
    private DAOAlbumResource daoAlbumResource;
        
    @Transactional
    public void remove(MDOUser user, Long resourceSid) throws IOException{
        MDOAlbumResource resource=this.daoAlbumResource.get( user.getLogin() , resourceSid);
        if(resource!=null){
            	//first, removing the resource file
            	String path=Util.getRealBaseStorePath(user, daoSettings.getSettings());
            	path=path+File.separatorChar+resource.getAbsolutePath();
            	File fpath=new File(path);
            	fpath.delete();
            	//after, removing the album data from database
                this.daoAlbumResource.remove( resource);
        }
    }


}
