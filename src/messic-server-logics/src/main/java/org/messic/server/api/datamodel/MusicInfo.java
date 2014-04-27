package org.messic.server.api.datamodel;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

@XmlRootElement
@ApiObject( name = "MusicInfo", description = "Music Info obtained by a 3rd party" )
public class MusicInfo
{
    @ApiObjectField( description = "html content obtained" )
    private String htmlContent;

    @ApiObjectField( description = "available providers to obtain music info" )
    private List<MusicInfoPlugin> providers;

    /**
     * Constructor
     * 
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

    public final List<MusicInfoPlugin> getProviders()
    {
        return providers;
    }

    public final void setProviders( List<MusicInfoPlugin> providers )
    {
        this.providers = providers;
    }

    public final void addProvider( MusicInfoPlugin provider )
    {
        if ( this.providers == null )
        {
            this.providers = new ArrayList<MusicInfoPlugin>();
        }
        this.providers.add( provider );
    }

}
