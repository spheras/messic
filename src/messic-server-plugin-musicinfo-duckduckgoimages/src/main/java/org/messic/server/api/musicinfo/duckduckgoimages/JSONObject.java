package org.messic.server.api.musicinfo.duckduckgoimages;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JSONObject
{
    public List<JSONResult> results;
}
