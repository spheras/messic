package org.messic.server.api.tagwizard.service;
public class TAGInfo {
    
    public static final String ARTIST = "ARTIST";
    public static final String ALBUM = "ALBUM";
    public static final String YEAR = "YEAR";
    public static final String COMMENT = "COMMENT";
    public static final String GENRE = "GENRE";

    
	public String value;
	public int puntuation;

	public TAGInfo(String value) {
		this.value = value;
	}
}
