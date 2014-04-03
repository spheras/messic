package org.messic.server.api.tagwizard.service;

import java.util.HashMap;

public class SongTags {
	public byte[] coverArt;
	public HashMap<String, TAGInfo> tags = new HashMap<String, TAGInfo>();
	public String track;
	
	public String title;
}
