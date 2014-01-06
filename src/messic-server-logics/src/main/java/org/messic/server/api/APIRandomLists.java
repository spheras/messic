package org.messic.server.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.messic.server.api.datamodel.RandomList;
import org.messic.server.api.datamodel.Song;
import org.messic.server.datamodel.MDOSong;
import org.messic.server.datamodel.dao.DAOSong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class APIRandomLists {
    @Autowired
    private DAOSong daoSong;
    
	@Transactional
    public List<RandomList> getAllLists(){
		ArrayList<RandomList> result=new ArrayList<RandomList>();
		
	    	RandomList rl=new RandomList();
	    	List<MDOSong> songs=daoSong.getAll();
	    	if(songs.size()>0){
		    	long seed = System.nanoTime();
		    	Collections.shuffle(songs, new Random(seed));
		
		    	for (MDOSong mdoSong : songs) {
					Song song=new Song(mdoSong);
					rl.add(song);
				}
		    	result.add(rl);
	    	}
    	
		return result;
	}

}
