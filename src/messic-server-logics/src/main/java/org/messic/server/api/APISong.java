package org.messic.server.api;

import java.io.File;
import java.io.IOException;

import org.messic.server.Util;
import org.messic.server.datamodel.MDOSong;
import org.messic.server.datamodel.MDOUser;
import org.messic.server.datamodel.dao.DAOAuthor;
import org.messic.server.datamodel.dao.DAOMessicSettings;
import org.messic.server.datamodel.dao.DAOSong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class APISong {
    @Autowired
    private DAOMessicSettings daoSettings;
    @Autowired
    private DAOSong daoSong;
    @Autowired
    private DAOAuthor daoAuthor;
        
    public byte[] getAudioSong(MDOUser mdouser, long sid) throws IOException{
    	MDOSong song=daoSong.get(mdouser.getLogin(), sid);
    	if(song!=null){
    		String filePath=Util.getRealBaseStorePath(mdouser, daoSettings.getSettings())+File.separatorChar+song.getAbsolutePath();
    		File fsong=new File(filePath);
    		if(fsong.exists()){
    			return Util.readFile(filePath);
    		}
    	}
    	
    	return null;
    }

}
