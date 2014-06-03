package org.messic.server.datamodel.util;

import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

public class HibernateUtil
{
    /**
     * Detach and initialize proxies of an hibernate entity. Doing this, the entity doesn't need any session to get the
     * lazy fields. (it doesn't see the internal lazy fields) -> TODO
     * 
     * @param entity
     * @return
     */
    @SuppressWarnings( "unchecked" )
    public static <T> T initializeAndUnproxy( T entity )
    {
        if ( entity == null )
        {
            throw new NullPointerException( "Entity passed for initialization is null" );
        }

        Hibernate.initialize( entity );
        if ( entity instanceof HibernateProxy )
        {
            entity = (T) ( (HibernateProxy) entity ).getHibernateLazyInitializer().getImplementation();
        }
        return entity;
    }

}
