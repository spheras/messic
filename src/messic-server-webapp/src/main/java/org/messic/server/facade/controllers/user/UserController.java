package org.messic.server.facade.controllers.user;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.messic.server.api.APIUser;
import org.messic.server.api.datamodel.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

@Controller
@RequestMapping("/users")
public class UserController
{

	@Autowired
	private APIUser userAPI;
			
	@RequestMapping(value="/{userLogin}",method=RequestMethod.GET)
	@ResponseBody
	/**
	 * This controller return user by userLogin
	 * @param userLogin
	 * @param arg0
	 * @param arg1
	 * @return
	 * @throws Exception
	 */
    protected User getUser(@PathVariable String userLogin, HttpServletRequest arg0, HttpServletResponse arg1 )
        throws Exception
    {        
		User user = userAPI.getUserByLogin(userLogin);		
		return user;		
    }		
	
	
	@RequestMapping(value="",method=RequestMethod.POST)
	@ResponseBody
	/**
	 * This controller processes the user creation request
	 * @param user
	 * @param arg0
	 * @param arg1
	 * @return
	 * @throws Exception
	 */
    protected User createOrUpdate(User user, HttpServletRequest arg0, HttpServletResponse arg1 )
        throws Exception
    {        
		User new_user;
		if(user.getSid()==null)
		{
			new_user = userAPI.createUser(user);
		}
		else
		{
			new_user = userAPI.updateUser(user);
		}
		
		return new_user;		
    }	
	
	@InitBinder
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
        throws ServletException {
		binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
    }
	
}
