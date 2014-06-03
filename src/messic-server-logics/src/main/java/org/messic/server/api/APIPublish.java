package org.messic.server.api;

import javax.annotation.PostConstruct;

import org.messic.server.datamodel.dao.DAOUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class APIPublish
{

    @Autowired
    private DAOUser userRepository;

    @PostConstruct
    public void onInit(){
        
    }

}
