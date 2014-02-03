package org.messic.server.api;

import org.messic.server.api.datamodel.User;
import org.messic.server.datamodel.MDOUser;
import org.messic.server.datamodel.dao.DAOUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class APIUser {

	@Autowired
    private DAOUser userRepository;
	
	public APIUser(){
		System.out.println("pozi");
	}
	
	public MDOUser getUser(String user)	{
		return userRepository.getUser(user);
	}
	
	public MDOUser createUser(User user) {
		
		boolean existAdministrator = userRepository.existUsers();
		if(!existAdministrator)
		{
			user.setAdministrator(Boolean.TRUE);
		}
		
		// TODO CREAR EL REPOSITORIO DEL USUARIO
		return userRepository.createUser(user.getLogin(), user.getPassword(), user.getName(), user.getEmail(), user.getAvatar(), user.getAdministrator());
	}
		
}
