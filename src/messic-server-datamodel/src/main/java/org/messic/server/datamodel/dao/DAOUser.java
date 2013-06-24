package org.messic.server.datamodel.dao;

import org.messic.server.datamodel.MDOUser;


/**
 * DAO for User table
 */
public interface DAOUser extends DAO<MDOUser>
{
    public MDOUser getUser(String user);
    
    public MDOUser createUser(String login, String password, String name, boolean administrator);
    
    public MDOUser updatePassword(Long userSid, String newPassword);
    
    public MDOUser updateUserData(Long userSid, String name);
    
    public void removeUser(Long userSid);
    
    public boolean existUser(String login);

    public MDOUser authenticate(String login, String password);
   

}
