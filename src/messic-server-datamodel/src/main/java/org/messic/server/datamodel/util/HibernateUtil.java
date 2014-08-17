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
