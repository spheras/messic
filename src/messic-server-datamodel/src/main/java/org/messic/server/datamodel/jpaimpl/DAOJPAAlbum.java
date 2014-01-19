package org.messic.server.datamodel.jpaimpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.Query;

import org.messic.server.datamodel.MDOAlbum;
import org.messic.server.datamodel.MDOAuthor;
import org.messic.server.datamodel.dao.DAOAlbum;
import org.springframework.stereotype.Component;

@Component
public class DAOJPAAlbum
    extends DAOJPA<MDOAlbum>
    implements DAOAlbum
{

    public DAOJPAAlbum()
    {
        super( MDOAlbum.class );
    }

	@Override
	public List<MDOAlbum> getAll(String username) {
        Query query = entityManager.createQuery( "from MDOAlbum as a where (a.owner.login = :userName)" );
        query.setParameter( "userName", username);
        
        @SuppressWarnings( "unchecked" )
        List<MDOAlbum> results = query.getResultList();
        return results;
	}

	@Override
	public List<MDOAlbum> findSimilarAlbums(String albumName, String username) {
        Query query = entityManager.createQuery( "from MDOAlbum as a where (a.name LIKE :albumName) AND (a.owner.login = :userName)" );
        query.setParameter( "albumName", "%" + albumName + "%");
        query.setParameter( "userName", username);
        
        @SuppressWarnings( "unchecked" )
        List<MDOAlbum> results = query.getResultList();
        return results;
	}

	@Override
	public List<MDOAlbum> getAll(long authorSid, String username) {
        Query query = entityManager.createQuery( "from MDOAuthor as a where (a.owner.login = :userName) AND (a.sid = :authorSid)" );
        query.setParameter( "userName", username);
        query.setParameter( "authorSid", authorSid);
        
        @SuppressWarnings( "unchecked" )
        List<MDOAuthor> results = query.getResultList();
        if(results!=null && results.size()>0){
        	ArrayList<MDOAlbum> albums=new ArrayList<MDOAlbum>();
        	Set<MDOAlbum> setAlbums=results.get(0).getAlbums();
        	if(setAlbums!=null && setAlbums.size()>0){
            	Iterator<MDOAlbum> albumsit=setAlbums.iterator();
            	while(albumsit.hasNext()){
            		albums.add(albumsit.next());
            	}
        	}
        	return albums;
        }
        return null;
	}

	@Override
	public MDOAlbum getAlbum(long albumSid, String username) {
        Query query = entityManager.createQuery( "from MDOAlbum as a where (a.owner.login = :userName) AND (a.sid = :albumSid)" );
        query.setParameter( "userName", username);
        query.setParameter( "albumSid", albumSid);
        
        @SuppressWarnings( "unchecked" )
        List<MDOAlbum> results = query.getResultList();
        if(results!=null && results.size()>0){
    		return results.get(0);
        }
        return null;
	}

	@Override
	public MDOAlbum getByName(String authorName, String albumName, String username){
        Query query = entityManager.createQuery( "from MDOAlbum as a where (a.name = :albumName) AND (a.owner.login = :userName) AND (a.author.name = :authorName)" );
        query.setParameter( "albumName", "'" + albumName + "'");
        query.setParameter( "userName", username);
        query.setParameter( "authorName", authorName);
		
        @SuppressWarnings( "unchecked" )
        List<MDOAlbum> results = query.getResultList();
        if(results!=null && results.size()>0){
        	return results.get(0);
        }
        return null;
	}
	
	@Override
	public List<MDOAlbum> findSimilarAlbums(long authorSid, String albumName,
			String username) {
        Query query = entityManager.createQuery( "from MDOAlbum as a where (a.name LIKE :albumName) AND (a.owner.login = :userName) AND (a.author.sid = :authorSid)" );
        query.setParameter( "albumName", "%" + albumName + "%");
        query.setParameter( "userName", username);
        query.setParameter( "authorSid", authorSid);
        
        @SuppressWarnings( "unchecked" )
        List<MDOAlbum> results = query.getResultList();
        return results;
	}

}
