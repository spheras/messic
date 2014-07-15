/*
 * Copyright (C) 2013
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

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getProviderName()
    {
        return providerName;
    }

    public void setProviderName( String providerName )
    {
        this.providerName = providerName;
    }


}
