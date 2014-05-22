package org.messic.server.api.datamodel;

import org.messic.server.datamodel.MDOMessicSettings;


public class MessicSettings {
	
	private Long sid;
    private String genericBaseStorePath;

    /**
     * @constructor
     */
    public MessicSettings() {
        super();
    }
    
    public MessicSettings(String genericBaseStorePath) {
    	setGenericBaseStorePath(genericBaseStorePath);
    }
    
    public static MessicSettings transform(MDOMessicSettings settings){
		return new MessicSettings(settings.getGenericBaseStorePath());
	}

	public String getGenericBaseStorePath() {
		return genericBaseStorePath;
	}

	public void setGenericBaseStorePath(String genericBaseStorePath) {
		this.genericBaseStorePath = genericBaseStorePath;
	}

	public Long getSid() {
		return sid;
	}

	public void setSid(Long sid) {
		this.sid = sid;
	}

    
}