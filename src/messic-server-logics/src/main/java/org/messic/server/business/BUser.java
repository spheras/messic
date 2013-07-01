package org.messic.server.business;

import org.messic.server.datamodel.MDOUser;
import org.messic.server.datamodel.dao.DAOUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class BUser {

	@Autowired
    private DAOUser userRepository;
	
	public MDOUser getUser(String user)	{
		return userRepository.getUser(user);
	}
	
	public MDOUser createUser(String login, String password, String name,
			boolean administrator) {
		return userRepository.createUser(login, password, name, administrator);
	}
	
}
