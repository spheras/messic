package org.messic.server.datamodel.dao;

import org.messic.server.datamodel.MDOUser;


/**
 * DAO for User table
 */
public interface DAOUser extends DAO<MDOUser>
{
    public MDOUser getUserByLogin(String user);
    
    public MDOUser getUserById(Long userSid);
    
    public MDOUser createUser(String login, String password, String name, String email, byte[] avatar, boolean administrator, String storePath);
    
    public MDOUser updatePassword(Long userSid, String newPassword);
    
    public MDOUser updateUserData(Long userSid, String name, String email, byte[] avatar, String storePath);
    
    public void removeUser(Long userSid);
    
    public boolean existUser(String user);
    
    public boolean existUsers();

    public MDOUser authenticate(String user, String password);
   

}
