package org.messic.server.business;

import org.messic.server.business.vo.UserVO;
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
	
	public MDOUser createUser(UserVO uservo) {
		
		boolean existAdministrator = userRepository.existUsers();
		if(!existAdministrator)
		{
			uservo.setAdministrator(Boolean.TRUE);
		}
		
		// TODO CREAR EL REPOSITORIO DEL USUARIO
		return userRepository.createUser(uservo.getLogin(), uservo.getPassword(), uservo.getName(), uservo.getEmail(), uservo.getAvatar(), uservo.getAdministrator());
	}
		
}
