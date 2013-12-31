package org.messic.server.api.datamodel;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MessicResponse {
	public Integer code;
	public String message;
	public Object content;
	
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
