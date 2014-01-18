package org.messic.server.facade.controllers.rest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.UNPROCESSABLE_ENTITY)
public class MusicTagsMessicRESTException extends Exception{
	public static final String VALUE="422";
	/**
	 * 
	 */
	private static final long serialVersionUID = 4103892143638394373L;

	public MusicTagsMessicRESTException(Exception e){
		super(e.getMessage(),e.getCause());
	}
}
