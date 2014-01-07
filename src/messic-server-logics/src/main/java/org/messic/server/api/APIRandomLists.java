package org.messic.server.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.messic.server.api.datamodel.RandomList;
import org.messic.server.api.datamodel.Song;
import org.messic.server.datamodel.MDOAlbum;
import org.messic.server.datamodel.MDOAuthor;
import org.messic.server.datamodel.MDOSong;
import org.messic.server.datamodel.dao.DAOAuthor;
import org.messic.server.datamodel.dao.DAOSong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class APIRandomLists {
    @Autowired
    private DAOSong daoSong;
    @Autowired
    private DAOAuthor daoAuthor;
    
	@Transactional
    public List<RandomList> getAllLists(){
		ArrayList<RandomList> result=new ArrayList<RandomList>();
		
	    	List<MDOSong> songs=daoSong.getAll();
	    	if(songs.size()>0){
		    	RandomList rl=new RandomList();
		    	long seed = System.nanoTime();
		    	Collections.shuffle(songs, new Random(seed));
		
		    	for (MDOSong mdoSong : songs) {
					Song song=new Song(mdoSong);
					rl.add(song);
				}
		    	result.add(rl);
	    	}
	    	
	    	List<MDOAuthor> randomAuthorList=daoAuthor.getRandomAuthors(1);
	    	if(randomAuthorList!=null && randomAuthorList.size()>0){
		    	RandomList rl=new RandomList();
		    	Iterator<MDOAlbum> albumsit=randomAuthorList.get(0).getAlbums().iterator();
		    	while(albumsit.hasNext()){
		    		MDOAlbum album=albumsit.next();
			    	for (MDOSong mdoSong : album.getSongs()) {
						Song song=new Song(mdoSong);
						rl.add(song);
					}
		    	}
		    	long seed = System.nanoTime();
		    	Collections.shuffle(rl,new Random(seed));
		    	result.add(rl);
	    	}
    	
		return result;
	}

}
