package org.messic.server.api;

import java.util.List;

import org.messic.server.api.datamodel.Album;
import org.messic.server.datamodel.MDOAlbum;
import org.messic.server.datamodel.MDOUser;
import org.messic.server.datamodel.dao.DAOAlbum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class APIAlbum {
    @Autowired
    private DAOAlbum daoAlbum;
    
	@Transactional
    public List<Album> getAll(MDOUser user){
    	List<MDOAlbum> albums=daoAlbum.getAll(user.getLogin());
		return Album.transform(albums,false);
	}

	@Transactional
    public List<Album> getAll(MDOUser user, int authorSid){
    	List<MDOAlbum> albums=daoAlbum.getAll(authorSid, user.getLogin());
		return Album.transform(albums,false);
	}

	@Transactional
    public List<Album> findSimilar(MDOUser user, String albumName){
    	List<MDOAlbum> albums=daoAlbum.findSimilarAlbums(albumName, user.getLogin());
		return Album.transform(albums,false);
    }

	@Transactional
    public List<Album> findSimilar(MDOUser user, int authorSid, String albumName){
    	List<MDOAlbum> albums=daoAlbum.findSimilarAlbums(authorSid, albumName, user.getLogin());
		return Album.transform(albums,false);
    }

}
