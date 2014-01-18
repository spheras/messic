package org.messic.server.facade.controllers.rest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND)
public class NotFoundMessicRESTException extends Exception{
	public static final String VALUE="404";
	/**
	 * 
	 */
	private static final long serialVersionUID = 4103892143638394373L;

	public NotFoundMessicRESTException(Exception e){
		super(e.getMessage(),e.getCause());
	}
}
