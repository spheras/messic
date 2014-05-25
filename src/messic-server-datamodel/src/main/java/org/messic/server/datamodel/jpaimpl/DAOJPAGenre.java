package org.messic.server.datamodel.jpaimpl;

import java.util.List;

import javax.persistence.Query;

import org.messic.server.datamodel.MDOGenre;
import org.messic.server.datamodel.dao.DAOGenre;
import org.springframework.stereotype.Component;

@Component
public class DAOJPAGenre
    extends DAOJPA<MDOGenre>
    implements DAOGenre
{

    public DAOJPAGenre()
    {
        super( MDOGenre.class );
    }

    @Override
    public List<MDOGenre> getRandomGenre(String username, int number){
        Query query= entityManager.createQuery("from MDOGenre as a where (1=1 AND a.owner.login= :userName) order by rand()");
        query.setParameter( "userName", username);
        query.setMaxResults(5);
        @SuppressWarnings( "unchecked" )
        List<MDOGenre> results=query.getResultList();
        return results;
    }

    /**
     * @TODO limit to the valid genres for the user?
     */
	@Override
	public List<MDOGenre> findSimilarGenre(String genreName, String username) {
        Query query = entityManager.createQuery( "from MDOGenre as a where (a.owner.login = :userName) AND (a.name LIKE :genreName)" );
        query.setParameter( "genreName", "%" + genreName + "%");
        query.setParameter( "userName", username);
        
        @SuppressWarnings( "unchecked" )
        List<MDOGenre> results = query.getResultList();
        return results;
	}

	
	public MDOGenre getGenre(String username, String genreName) {
        Query query = entityManager.createQuery( "from MDOGenre as a where (a.owner.login = :userName) AND (a.name = :genreName)" );
        query.setParameter( "genreName", "'" + genreName + "'");
        query.setParameter( "userName", username);
        
        @SuppressWarnings( "unchecked" )
        List<MDOGenre> results = query.getResultList();
        if(results!=null){
            return results.get(0);
        }else{
        	return null;
        }
	}

	public MDOGenre getByName(String username, String genreName){
        Query query = entityManager.createQuery("from MDOGenre as a where (a.owner.login = :userName) AND (a.name = :genreName)");
        query.setParameter( "genreName", genreName);
        query.setParameter( "userName", username);
		
        @SuppressWarnings( "unchecked" )
        List<MDOGenre> results = query.getResultList();
        if(results!=null && results.size()>0){
        	return results.get(0);
        }
        return null;
	}

}
