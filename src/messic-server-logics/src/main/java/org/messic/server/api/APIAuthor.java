package org.messic.server.api;

import java.util.List;

import org.messic.server.api.datamodel.Author;
import org.messic.server.datamodel.MDOAuthor;
import org.messic.server.datamodel.MDOUser;
import org.messic.server.datamodel.dao.DAOAlbum;
import org.messic.server.datamodel.dao.DAOAuthor;
import org.messic.server.datamodel.dao.DAOGenre;
import org.messic.server.datamodel.dao.DAOMessicSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class APIAuthor {
    @Autowired
    private DAOAuthor authorRepository;
    @Autowired
    private DAOGenre daoGenre;
    @Autowired
    private DAOAlbum daoAlbum;
    @Autowired
    private DAOMessicSettings daoSettings;

	@Transactional
    public List<Author> getAll(MDOUser user, boolean copyAlbums, boolean copySongs){
    	List<MDOAuthor> authors=authorRepository.getAll(user.getLogin());
		return Author.transform(authors,copyAlbums,copySongs);
	}

	@Transactional
    public Author getAuthor(MDOUser user, long authorSid, boolean copyAlbums, boolean copySongs){
    	MDOAuthor author=authorRepository.get(user.getLogin(), authorSid);
		return Author.transform(author,copyAlbums,copySongs);
	}

	@Transactional
    public List<Author> findSimilar(MDOUser user, String authorName, boolean copyAlbums, boolean copySongs){
    	List<MDOAuthor> authors=authorRepository.findSimilarAuthors(authorName, user.getLogin());
		return Author.transform(authors,copyAlbums, copySongs);
    }

}
