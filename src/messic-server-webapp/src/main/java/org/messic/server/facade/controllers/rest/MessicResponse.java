package org.messic.server.facade.controllers.rest;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MessicResponse {
	public static final Integer CODE_OK=200;
	public static final String MESSAGE_OK="OK";
	public Integer code;
	public String message;
	public Object content;

	public MessicResponse(){
		//Default constructor
	}
	
	/**
	 * Constructor
	 * @param code int code of the response
	 * @param message {@link String} message of the response
	 * @param content {@link Object} content for the message
	 */
	public MessicResponse(int code, String message, Object content){
		this.code=code;
		this.message=message;
		this.content=content;
	}
}
