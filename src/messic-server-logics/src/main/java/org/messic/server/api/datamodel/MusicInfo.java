/*
 * Copyright (C) 2013 José Amuedo
 *
 *  This file is part of Messic.
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
