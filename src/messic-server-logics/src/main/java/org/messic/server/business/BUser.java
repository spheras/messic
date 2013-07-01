package org.messic.server.business;

import org.messic.server.datamodel.dao.DAOUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class BUser {

	@Autowired
    private DAOUser userRepository;
	
}
