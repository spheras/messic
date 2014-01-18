package org.messic.server.api.datamodel;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;
import org.messic.server.datamodel.MDOGenre;

@XmlRootElement
@ApiObject(name="Genre", description="Genre of an album")
public class Genre {
	@ApiObjectField(description="identificator of a genre")
	private Long sid;
	@ApiObjectField(description="name of a genre")
	private String name;
	
	public Genre(){
		
	}
	
	/**
	 * Transform a {@link List} of {@link MDOGenre} to a {@link List} of {@link Genre}
	 * @param mdogenres {@link List}<MDOGenre/> to convert
	 * @return {@link List}<Genre/> converted
	 */
	public static List<Genre> transform(List<MDOGenre> mdogenres){
		ArrayList<Genre> genres=new ArrayList<Genre>();
		if(mdogenres!=null){
			for(int i=0;i<mdogenres.size();i++){
				genres.add(new Genre(mdogenres.get(i)));
			}
		}
		return genres;
	}

	
	public Genre(String name){
		this.name=name;
	}
	
	public Genre(MDOGenre genre){
		this.name=genre.getName();
		this.sid=genre.getSid();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getSid() {
		return sid;
	}

	public void setSid(Long sid) {
		this.sid = sid;
	}
}
