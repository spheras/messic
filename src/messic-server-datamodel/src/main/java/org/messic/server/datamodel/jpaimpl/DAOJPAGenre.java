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

    /**
     * @TODO limit to the valid genres for the user?
     */
	@Override
	public List<MDOGenre> findSimilarGenre(String genreName, String username) {
        Query query = entityManager.createQuery( "from MDOGenre as a where (a.name LIKE :genreName)" );
        query.setParameter( "genreName", "%" + genreName + "%");
        
        @SuppressWarnings( "unchecked" )
        List<MDOGenre> results = query.getResultList();
        return results;
	}

	
	public MDOGenre getGenre(String genreName) {
        Query query = entityManager.createQuery( "from MDOGenre as a where (a.name = :genreName)" );
        query.setParameter( "genreName", "'" + genreName + "'");
        
        @SuppressWarnings( "unchecked" )
        List<MDOGenre> results = query.getResultList();
        if(results!=null){
            return results.get(0);
        }else{
        	return null;
        }
	}

	public MDOGenre getByName(String genreName){
        Query query = entityManager.createQuery("from MDOGenre as a where (a.name = :genreName)");
        query.setParameter( "genreName", genreName);
		
        @SuppressWarnings( "unchecked" )
        List<MDOGenre> results = query.getResultList();
        if(results!=null && results.size()>0){
        	return results.get(0);
        }
        return null;
	}

}
