package org.messic.server.datamodel.dao;

import org.messic.server.datamodel.MDOUser;

/**
 * DAO for User table
 */
public interface DAOUser extends DAO<MDOUser>
{
    MDOUser getUser(String user);

}
