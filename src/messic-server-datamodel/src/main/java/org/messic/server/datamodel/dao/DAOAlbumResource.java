package org.messic.server.datamodel.dao;

import org.messic.server.datamodel.MDOAlbumResource;



/**
 * DAO for PhysicalResource table
 */
public interface DAOAlbumResource extends DAO<MDOAlbumResource>
{
    /**
     * Obtain an albumresource with the sid, only search in the scope of the username
     * @param username {@link String} user scope
     * @param sid long sid of the resource
     * @return {@link MDOAlbumResource}
     */
    MDOAlbumResource get(String username, long sid);	
}
