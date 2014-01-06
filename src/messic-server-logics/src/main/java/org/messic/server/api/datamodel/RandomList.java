package org.messic.server.api.datamodel;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RandomList extends ArrayList<Song>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 429042524274605221L;

	/**
	 * default constructor
	 */
	public RandomList(){
		super();
	}
	

}
