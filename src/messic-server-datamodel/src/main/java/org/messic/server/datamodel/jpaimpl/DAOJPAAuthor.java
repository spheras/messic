package org.messic.server.datamodel.jpaimpl;

import java.util.List;

import javax.persistence.Query;

import org.messic.server.datamodel.MDOAuthor;
import org.messic.server.datamodel.dao.DAOAuthor;
import org.springframework.stereotype.Component;

@Component
public class DAOJPAAuthor
    extends DAOJPA<MDOAuthor>
    implements DAOAuthor
{

    public DAOJPAAuthor()
    {
        super( MDOAuthor.class );
    }

	@Override
    public List<MDOAuthor> getRandomAuthors(int number){
    	Query query= entityManager.createQuery("from MDOAuthor as a where 1=1 order by rand()");
    	query.setMaxResults(5);
        @SuppressWarnings( "unchecked" )
    	List<MDOAuthor> results=query.getResultList();
        return results;
    }
    
	@Override
	public List<MDOAuthor> getAll(String username) {
        Query query = entityManager.createQuery( "from MDOAuthor as a where (a.owner.login = :userName)" );
        query.setParameter( "userName", username);
        
        @SuppressWarnings( "unchecked" )
        List<MDOAuthor> results = query.getResultList();
        return results;
	}

	@Override
	public List<MDOAuthor> findSimilarAuthors(String authorName, String username) {
        Query query = entityManager.createQuery( "from MDOAuthor as a where (a.name LIKE :authorName) AND (a.owner.login = :userName)" );
        query.setParameter( "authorName", "%" + authorName + "%");
        query.setParameter( "userName", username);
        
        @SuppressWarnings( "unchecked" )
        List<MDOAuthor> results = query.getResultList();
        return results;
	}

	public MDOAuthor getByName(String authorName, String username){
        Query query = entityManager.createQuery( "from MDOAuthor as a where (a.name = :authorName) AND (a.owner.login = :userName)" );
        query.setParameter( "userName", username);
        query.setParameter( "authorName", authorName);
		
        @SuppressWarnings( "unchecked" )
        List<MDOAuthor> results = query.getResultList();
        if(results!=null && results.size()>0){
        	return results.get(0);
        }
        return null;
	}

}
