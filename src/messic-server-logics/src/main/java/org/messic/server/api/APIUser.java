package org.messic.server.api;

import org.messic.server.api.datamodel.User;
import org.messic.server.datamodel.MDOUser;
import org.messic.server.datamodel.dao.DAOMessicSettings;
import org.messic.server.datamodel.dao.DAOUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class APIUser {

	@Autowired
    private DAOUser userRepository;
	@Autowired
    private DAOMessicSettings settingsRepository;
	
	public User getUserByLogin(String user)	{
		MDOUser mdoUser = userRepository.getUserByLogin(user);
		return new User(mdoUser);
	}
	
	public User getUserById(Long userId)	{
		MDOUser mdoUser = userRepository.getUserById(userId);
		return new User(mdoUser);
	}
	
	public User createUser(User user) {
		
		boolean existAdministrator = userRepository.existUsers();
		if(!existAdministrator)
		{
			user.setAdministrator(Boolean.TRUE);
		}
		else
		{
			user.setAdministrator(Boolean.FALSE);
		}
		
		MDOUser mdoUser = userRepository.createUser(user.getLogin(), user.getPassword(), user.getName(), user.getEmail(), user.getAvatar(), user.getAdministrator(), user.getStorePath());
		
		return new User(mdoUser);
	}
	
	public User updateUser(User user) {
		
		boolean existAdministrator = userRepository.existUsers();
		if(!existAdministrator)
		{
			user.setAdministrator(Boolean.TRUE);
		}
		
		MDOUser mdoUser = userRepository.updateUserData(user.getSid(), user.getName(), user.getEmail(), user.getAvatar(), user.getStorePath());
		
		if(!user.getPassword().equals(mdoUser.getPassword()))
		{
			userRepository.updatePassword(mdoUser.getSid(), user.getPassword());
		}
		
		return new User(mdoUser);
	}
		
}
