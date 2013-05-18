package org.messic.server.datamodel.dao;

import java.util.List;

import org.messic.server.datamodel.MDO;

/**
 * DAO Generic Interface
 * @param <T> extends {@link MDO}
 */
public interface DAO<T extends MDO>
{
    T get( Long id );

    List<T> getAll();

    void save( T object );

    void remove( T object );
}