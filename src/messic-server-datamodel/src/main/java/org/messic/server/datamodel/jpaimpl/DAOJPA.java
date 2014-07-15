/*
 * Copyright (C) 2013
 *
 *  This file is part of Messic.
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.messic.server.datamodel.jpaimpl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
        return entityManager.createQuery( "from " + type.getName() ).getResultList();
    }

    @Transactional( readOnly = true )
    public long getCount()
    {
        Query q = entityManager.createQuery( "SELECT COUNT(e) FROM " + type.getName() + " e" );
        return (Long) q.getSingleResult();
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
