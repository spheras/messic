/*
 * Copyright (C) 2013
 *
 *  This file is part of Messic.
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
		for(int i=0;i<details.length;i++){
			if(details[i].startsWith("\"")){
				String contentDetail=details[i];
				while(!contentDetail.endsWith("\"") && i<details.length){
					i++;
					contentDetail=contentDetail+" " +details[i];
				}
				rl.addDetail(contentDetail.substring(1, contentDetail.length()-1));
			}else{
				rl.addDetail(details[i]);
			}
		}
		
		List<MDOSong> songs=daoSong.genericFind(userName, rl.getDetails());
		for (MDOSong mdoSong : songs) {
			Song song=new Song(mdoSong);
			rl.addSong(song);
		}
		
		return rl;
	}

}
