package org.messic.server.facade.controllers.user;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.messic.server.api.APIUser;
import org.messic.server.api.datamodel.User;
import org.messic.server.datamodel.MDOUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/user")
public class UserController
{

	final String avatar_session_id = "avatar";
	
	@Autowired
	private APIUser userAPI;
	
	@RequestMapping("/show/create.do")
	/**
	 * This controller launches the creation user window. 
	 * @param arg0
	 * @param arg1
	 * @return
	 * @throws Exception
	 */
    protected ModelAndView showCreate( HttpServletRequest arg0, HttpServletResponse arg1 )
        throws Exception
    {
        ModelAndView model = new ModelAndView("user");

        return model;
    }
	
	@RequestMapping("/show/update.do")
	/**
	 * This controller launches the modification user window
	 * @param arg0
	 * @param arg1
	 * @return
	 * @throws Exception
	 */
    protected ModelAndView showUpdate( HttpServletRequest arg0, HttpServletResponse arg1 )
        throws Exception
    {
        ModelAndView model = new ModelAndView("update");

        return model;
    }
	
	@RequestMapping("/show/delete.do")
	/**
	 * This controller launches the deletion user window
	 * @param arg0
	 * @param arg1
	 * @return
	 * @throws Exception
	 */
    protected ModelAndView showDelete( HttpServletRequest arg0, HttpServletResponse arg1 )
        throws Exception
    {
        ModelAndView model = new ModelAndView("delete");

        return model;
    }
	
	@RequestMapping("/show.do")
	/**
	 * This controller launches the user window.
	 * @param arg0
	 * @param arg1
	 * @return
	 * @throws Exception
	 */
    protected ModelAndView show( HttpServletRequest arg0, HttpServletResponse arg1 )
        throws Exception
    {
        ModelAndView model = new ModelAndView("user");

        return model;
    }
	
	@RequestMapping(value="/create.do",method=RequestMethod.POST)
	@ResponseBody
	/**
	 * This controller processes the user creation request
	 * @param user
	 * @param session
	 * @param arg0
	 * @param arg1
	 * @return
	 * @throws Exception
	 */
    protected MDOUser create(User user, HttpSession session, HttpServletRequest arg0, HttpServletResponse arg1 )
        throws Exception
    {
        
		if(session.getAttribute(avatar_session_id)!=null)
		{
			user.setAvatar((byte[])session.getAttribute(avatar_session_id));
		}
		else
		{
			//TODO recuperar el avatar por defecto
		}
		
		MDOUser mdoUser = userAPI.createUser(user);
		
		return mdoUser;
		
    }
	
	@RequestMapping("/update.do")
	/**
	 * Controller to process the modification user request.
	 * @param arg0
	 * @param arg1
	 * @return
	 * @throws Exception
	 */
    protected ModelAndView update( HttpServletRequest arg0, HttpServletResponse arg1 )
        throws Exception
    {
        ModelAndView model = new ModelAndView("update");

        return model;
    }
	
	@RequestMapping("/delete.do")
	/**
	 * Controller to process the deletion user request.
	 * @param arg0
	 * @param arg1
	 * @return
	 * @throws Exception
	 */
    protected ModelAndView delete( HttpServletRequest arg0, HttpServletResponse arg1 )
        throws Exception
    {
        ModelAndView model = new ModelAndView("delete");

        return model;
    }
	
	@RequestMapping("/avatar/create.do")
	@ResponseBody
	/**
	 * Controller to process avatar file in server.
	 * @param session
	 * @param arg0
	 * @param arg1
	 * @return
	 * @throws Exception
	 */
    protected Map<String, Object> avatarCreate( HttpSession session, HttpServletRequest arg0, HttpServletResponse arg1 )
        throws Exception
    {
        
		MultipartHttpServletRequest request = (MultipartHttpServletRequest)arg0;
		MultipartFile multipartFile = request.getFile("avatar"); 
		
		byte[] avatarBytes = multipartFile.getBytes();
		
		session.setAttribute(avatar_session_id, avatarBytes);
		
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("success", true);
		
		return response;
		
    }
	
	@RequestMapping("/avatar/show.do")
	@ResponseBody
	/**
	 * Controler to show the avatar file saved in the user session.
	 * @param session
	 * @param arg0
	 * @param arg1
	 * @return
	 * @throws Exception
	 */
	protected ResponseEntity <byte []> avatarShow( HttpSession session, HttpServletRequest arg0, HttpServletResponse arg1 )
        throws Exception
    {
		
    	byte[] avatar = (byte[])session.getAttribute(avatar_session_id);    	
    	return new ResponseEntity<byte[]>(avatar,HttpStatus.OK);
        
    }

}
