package org.messic.server.api;

import org.messic.server.api.datamodel.User;
import org.messic.server.datamodel.MDOUser;
import org.messic.server.datamodel.dao.DAOUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class APIUser
{

    @Autowired
    private DAOUser userRepository;

    public User getUser( String user )
    {
        MDOUser mdouser = userRepository.getUser( user );
        if(mdouser!=null){
            User result = new User( mdouser );
            return result;
        }else{
            return null;
        }
    }

    public User createUser( User user )
    {

        boolean existAdministrator = userRepository.existUsers();
        if ( !existAdministrator )
        {
            user.setAdministrator( Boolean.TRUE );
        }

        MDOUser mdouser =
            userRepository.createUser( user.getLogin(), user.getPassword(), user.getName(), user.getEmail(),
                                       user.getAvatar(), user.getAdministrator() );
        return new User( mdouser );
    }

}
