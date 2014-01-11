package org.messic.server.api;

import java.util.List;

import org.messic.server.api.datamodel.RandomList;
import org.messic.server.api.datamodel.Song;
import org.messic.server.datamodel.MDOSong;
import org.messic.server.datamodel.dao.DAOAuthor;
import org.messic.server.datamodel.dao.DAOSong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class APISearch {
    @Autowired
    private DAOSong daoSong;
    @Autowired
    private DAOAuthor daoAuthor;
    
	@Transactional
    public RandomList search(String userName, String content){
    	RandomList rl=new RandomList("RandomListName-Search","RandomListTitle-Search");
		String[] details=content.split(" ");
		for (String detail : details) {
			rl.addDetail(detail);
		}
		
		List<MDOSong> songs=daoSong.genericFind(userName, content);
		for (MDOSong mdoSong : songs) {
			Song song=new Song(mdoSong);
			rl.addSong(song);
		}
		
		return rl;
	}

}
