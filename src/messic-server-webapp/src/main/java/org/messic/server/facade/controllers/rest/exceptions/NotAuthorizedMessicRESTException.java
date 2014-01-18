package org.messic.server.facade.controllers.rest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.FORBIDDEN)
public class NotAuthorizedMessicRESTException extends Exception{
	public static final String VALUE="403";

	/**
	 * 
	 */
	private static final long serialVersionUID = 4103892143638394373L;

	public NotAuthorizedMessicRESTException(Exception e){
		super(e.getMessage(),e.getCause());
	}
}
