package org.messic.server.datamodel.jpaimpl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.messic.server.datamodel.MDO;
import org.messic.server.datamodel.dao.DAO;
import org.springframework.transaction.annotation.Transactional;

public class DAOJPA<T extends MDO>
    implements DAO<T>
{
    private Class<T> type;

    protected EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager( EntityManager entityManager )
    {
        this.entityManager = entityManager;
    }

    public DAOJPA( Class<T> type )
    {
        super();
        this.type = type;
    }

    @Transactional( readOnly = true )
    public T get( Long id )
    {
        if ( id == null )
        {
            return null;
        }
        else
        {
            return entityManager.find( type, id );
        }
    }

    @SuppressWarnings( "unchecked" )
    @Transactional( readOnly = true )
    public List<T> getAll()
    {
        return entityManager.createQuery( "select o from " + type.getName() + "o" ).getResultList();
    }

    public void save( T object )
    {
        entityManager.persist( object );
    }

    public void remove( T object )
    {
        entityManager.remove( object );
    }
}
