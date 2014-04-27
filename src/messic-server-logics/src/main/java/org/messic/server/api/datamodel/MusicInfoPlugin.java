package org.messic.server.api.datamodel;

import javax.xml.bind.annotation.XmlRootElement;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

@XmlRootElement
@ApiObject( name = "MusicInfoPlugin", description = "Music Info Plugin which is able to obtain music info from a 3rd provider" )
public class MusicInfoPlugin
{
    @ApiObjectField( description = "name of the plugin info" )
    private String name;
    @ApiObjectField( description = "name of the provider which is used to get the info" )
    private String providerName;

    /**
     * Constructor
     * @param htmlContent
     */
    public MusicInfoPlugin( String name, String providerName )
    {
        this.name=name;
        this.providerName=providerName;
    }


}
