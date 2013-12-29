package org.messic.server.api;

import java.util.List;

import org.messic.server.api.datamodel.Author;
import org.messic.server.datamodel.MDOAlbum;
import org.messic.server.datamodel.MDOAuthor;
import org.messic.server.datamodel.MDOGenre;
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
    public List<Author> getAll(MDOUser user){
    	List<MDOAuthor> authors=authorRepository.getAll(user.getLogin());
		return Author.transform(authors,false,false);
	}

	@Transactional
    public List<Author> findSimilar(MDOUser user, String authorName){
		//@TODO remove
		if(authorName.equalsIgnoreCase("create")){
			daoSettings.getSettings();

			MDOAuthor author1=new MDOAuthor(user,"/home/spheras/desarrollo/tmp/radiohead","radio head");
			authorRepository.save(author1);
			MDOAuthor author2=new MDOAuthor(user,"/home/spheras/desarrollo/tmp/acdc","AC/DC");
			authorRepository.save(author2);
			MDOAuthor author3=new MDOAuthor(user,"/home/spheras/desarrollo/tmp/kubelik","Kubelik");
			authorRepository.save(author3);
			
			MDOGenre genre1=new MDOGenre("Rock");
			daoGenre.save(genre1);
			MDOGenre genre2=new MDOGenre("Rap");
			daoGenre.save(genre2);
			MDOGenre genre3=new MDOGenre("Pop");
			daoGenre.save(genre3);
			MDOGenre genre4=new MDOGenre("Jazz");
			daoGenre.save(genre4);
			MDOGenre genre5=new MDOGenre("Soul");
			daoGenre.save(genre5);
			MDOGenre genre6=new MDOGenre("Classical");
			daoGenre.save(genre6);
			
			try{
				MDOAlbum album11=new MDOAlbum(user, "/home/spheras/desarrollo/tmp/radiohead/1", "uno", 1999, author1, genre1,"");
				daoAlbum.save(album11);
				MDOAlbum album12=new MDOAlbum(user, "/home/spheras/desarrollo/tmp/radiohead/2", "dos", 1999, author1, genre2,"");
				daoAlbum.save(album12);
				MDOAlbum album13=new MDOAlbum(user, "/home/spheras/desarrollo/tmp/radiohead/3", "tres", 1999, author1, genre3,"");
				daoAlbum.save(album13);

				MDOAlbum album21=new MDOAlbum(user, "/home/spheras/desarrollo/tmp/acdc/1", "la una de la tarde", 1960, author2, genre4,"");
				daoAlbum.save(album21);
				MDOAlbum album22=new MDOAlbum(user, "/home/spheras/desarrollo/tmp/acdc/2", "el jodio en medio", 1970, author2, genre5,"");
				daoAlbum.save(album22);
				MDOAlbum album23=new MDOAlbum(user, "/home/spheras/desarrollo/tmp/acdc/3", "esta prueba la gano yo", 1980, author2, genre6,"");
				daoAlbum.save(album23);
				
				MDOAlbum album31=new MDOAlbum(user, "/home/spheras/desarrollo/tmp/kubelik/1", "la teoría del dos y medio", 2013, author3, genre1,"");
				daoAlbum.save(album31);
				MDOAlbum album32=new MDOAlbum(user, "/home/spheras/desarrollo/tmp/kubelik/2", "maquetas varias", 2010, author3, genre3,"");
				daoAlbum.save(album32);
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}
    	
    	List<MDOAuthor> authors=authorRepository.findSimilarAuthors(authorName, user.getLogin());
		return Author.transform(authors,false, false);
    }

}
