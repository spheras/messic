package org.messic.server.api;

import java.util.List;

import org.messic.server.api.datamodel.Genre;
import org.messic.server.api.datamodel.User;
import org.messic.server.datamodel.MDOGenre;
import org.messic.server.datamodel.dao.DAOGenre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class APIGenre {
    @Autowired
    private DAOGenre daoGenre;
    
	@Transactional
    public List<Genre> getAll(){
    	List<MDOGenre> genres=daoGenre.getAll();
		return Genre.transform(genres);
	}

	@Transactional
    public List<Genre> findSimilar(User user, String genreName){
    	List<MDOGenre> genres=daoGenre.findSimilarGenre(genreName, user.getLogin());
		return Genre.transform(genres);
    }

}
