package org.messic.server.api;

import java.io.File;
import java.io.IOException;

import org.messic.server.Util;
import org.messic.server.api.datamodel.Song;
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
        
    @Transactional
    public void remove(MDOUser user, Long songSid) throws IOException{
        MDOSong song=this.daoSong.get( user.getLogin() ,songSid);
        if(song!=null){
            	//first, removing the song file
            	String path=Util.getRealBaseStorePath(user, daoSettings.getSettings());
            	path=path+File.separatorChar+song.getAbsolutePath();
            	File fpath=new File(path);
            	fpath.delete();
            	//after, removing the album data from database
                this.daoSong.remove( song );
        }
    }

    
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
    
    /**
     * Try to get the song name and track from a file name
     * @param filePath {@link String} the filename to investigate
     * @return {@link Song} song info, only the track number and track name
     */
    public Song getSongInfoFromFileName(String filePath){
        File tmpf=new File(filePath);
        String fileName=tmpf.getName();
        
        fileName=fileName.replaceAll( "_", " " );
        
        int where=Util.areThereNumbers(fileName);
        if(where>=0){
            String[] parts=fileName.split( "-" );
            for(int i=0;i<parts.length-1;i++){
                if(Util.areThereNumbers(parts[i])>=0){
                    try{
                        int trackn=Integer.valueOf( parts[i].trim() );
                        String trackName=Util.addPartsFromArray( parts, i+1, parts.length-1,"-" );
                        int wheredot=trackName.indexOf( "." );
                        if(wheredot>=0){
                            trackName=trackName.substring( 0,wheredot );
                        }
                        return new Song(0,trackn,trackName.trim()); 
                    }catch(Exception e){
                        //not a track?
                    }
                }
            }
        }

        String trackName=fileName;
        int wheredot=trackName.indexOf( "." );
        if(wheredot>=0){
            trackName=trackName.substring( 0,wheredot );
        }
        return new Song(-1,-1,trackName);
    }


}
