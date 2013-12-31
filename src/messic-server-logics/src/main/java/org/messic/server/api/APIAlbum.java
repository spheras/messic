package org.messic.server.api;

import java.util.List;

import org.messic.server.api.datamodel.Album;
import org.messic.server.api.datamodel.Song;
import org.messic.server.datamodel.MDOAlbum;
import org.messic.server.datamodel.MDOAlbumResource;
import org.messic.server.datamodel.MDOAuthor;
import org.messic.server.datamodel.MDOGenre;
import org.messic.server.datamodel.MDOSong;
import org.messic.server.datamodel.MDOUser;
import org.messic.server.datamodel.dao.DAOAlbum;
import org.messic.server.datamodel.dao.DAOAuthor;
import org.messic.server.datamodel.dao.DAOGenre;
import org.messic.server.datamodel.dao.DAOPhysicalResource;
import org.messic.server.datamodel.dao.DAOSong;
import org.messic.server.datamodel.dao.DAOUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class APIAlbum {
    @Autowired
    private DAOAlbum daoAlbum;
    @Autowired
    private DAOAuthor daoAuthor;
    @Autowired
    private DAOGenre daoGenre;
    @Autowired
    private DAOUser daoUser;
    @Autowired
    private DAOSong daoSong;
    @Autowired
    private DAOPhysicalResource daoPhysicalResource;
    
    
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

	public void saveAlbum(Album album) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MDOUser mdouser=daoUser.getUser(auth.getName());

        MDOGenre mdoGenre=null;
        MDOAlbum mdoAlbum=null;
        if(album.getGenre()!=null && album.getGenre().getSid()!=null){
        	mdoGenre=daoGenre.get(album.getGenre().getSid());
        }
        if(mdoGenre==null){
        	if(album.getGenre()!=null && album.getGenre().getName()!=null){
            	mdoGenre=daoGenre.getByName(album.getGenre().getName());
        	}
        }
        if(mdoGenre==null && album.getGenre()!=null && album.getGenre().getName()!=null){
        	mdoGenre=new MDOGenre(album.getGenre().getName());
        }
        MDOAuthor mdoAuthor=daoAuthor.getByName(album.getAuthor().getName(), mdouser.getLogin());
        if(mdoAuthor!=null){
        	mdoAlbum=daoAlbum.getByName(mdoAuthor.getName(), album.getName(), mdouser.getLogin());
        }
        if(mdoAuthor==null){
        	mdoAuthor=new MDOAuthor();
        	mdoAuthor.setName(album.getAuthor().getName());
        	mdoAuthor.setOwner(mdouser);
        	mdoAuthor.setLocation(""); //TODO
        }
        if(mdoAlbum==null){
        	mdoAlbum=new MDOAlbum();
        }
        mdoAlbum.setName(album.getName());
    	mdoAlbum.setAuthor(mdoAuthor);
    	mdoAlbum.setComments(album.getComments());
    	mdoAlbum.setGenre(mdoGenre);
    	mdoAlbum.setOwner(mdouser);
    	mdoAlbum.setYear(album.getYear());

    	daoAuthor.save(mdoAuthor);
    	daoGenre.save(mdoGenre);
    	daoAlbum.save(mdoAlbum);
        
        if(album.getSongs()!=null && album.getSongs().size()>0){
        	List<Song> songs=album.getSongs();
        	for (Song song : songs) {
				MDOSong mdoSong=new MDOSong();
				mdoSong.setName(song.getName());
				mdoSong.setLocation("");//TODO
				mdoSong.setOwner(mdouser);
				mdoSong.setTrack(song.getTrack());
				mdoSong.setAlbum(mdoAlbum);
				daoSong.save(mdoSong);
			}
        }
        if(album.getArtworks()!=null && album.getArtworks().size()>0){
        	List<org.messic.server.api.datamodel.File> files=album.getArtworks();
        	for (org.messic.server.api.datamodel.File file : files) {
				MDOAlbumResource mdopr=new MDOAlbumResource();
				mdopr.setLocation("");//TODO
				mdopr.setOwner(mdouser);
				mdopr.setAlbum(mdoAlbum);
				daoPhysicalResource.save(mdopr);
				mdoAlbum.getArtworks().add(mdopr);
			}
        	daoAlbum.save(mdoAlbum);
        }
        if(album.getOthers()!=null && album.getOthers().size()>0){
        	List<org.messic.server.api.datamodel.File> files=album.getOthers();
        	for (org.messic.server.api.datamodel.File file : files) {
        		MDOAlbumResource mdopr=new MDOAlbumResource();
				mdopr.setLocation("");//TODO
				mdopr.setOwner(mdouser);
				mdopr.setAlbum(mdoAlbum);
				daoPhysicalResource.save(mdopr);
				mdoAlbum.getOthers().add(mdopr);
			}
        	
        	daoAlbum.save(mdoAlbum);
        }

        
	}

}
