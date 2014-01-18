package org.messic.server.facade.controllers.rest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
public class UnknownMessicRESTException extends Exception{
	public static final String VALUE="500";

	/**
	 * 
	 */
	private static final long serialVersionUID = 4103892143638394373L;

	public UnknownMessicRESTException(Exception e){
		super(e.getMessage(),e.getCause());
	}
}
