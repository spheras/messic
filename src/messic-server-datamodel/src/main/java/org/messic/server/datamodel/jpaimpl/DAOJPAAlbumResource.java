package org.messic.server.datamodel.jpaimpl;

import java.util.List;

import javax.persistence.Query;

import org.messic.server.datamodel.MDOAlbumResource;
import org.messic.server.datamodel.dao.DAOAlbumResource;
import org.springframework.stereotype.Component;

@Component
public class DAOJPAAlbumResource
    extends DAOJPA<MDOAlbumResource>
    implements DAOAlbumResource
{

    public DAOJPAAlbumResource()
    {
        super( MDOAlbumResource.class );
    }


    @Override
    public MDOAlbumResource get(String username, long sid) {
        Query query = entityManager.createQuery( "from MDOAlbumResource as a where (a.owner.login = :userName AND a.sid = :sid)" );
        query.setParameter( "userName", username);
        query.setParameter( "sid",sid);
        
        @SuppressWarnings( "unchecked" )
        List<MDOAlbumResource> results = query.getResultList();
        if(results!=null && results.size()>0){
            return results.get(0);
        }
        return null;
    }

}
