package org.messic.server.api.datamodel;

import javax.xml.bind.annotation.XmlRootElement;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

@XmlRootElement
@ApiObject( name = "MusicInfo", description = "Music Info obtained by a 3rd party" )
public class MusicInfo
{
    @ApiObjectField( description = "html content obtained" )
    private String htmlContent;

    /**
     * Constructor
     * @param htmlContent
     */
    public MusicInfo( String htmlContent )
    {
        this.htmlContent = htmlContent;
    }

    public final String getHtmlContent()
    {
        return htmlContent;
    }

    public final void setHtmlContent( String htmlContent )
    {
        this.htmlContent = htmlContent;
    }

}
