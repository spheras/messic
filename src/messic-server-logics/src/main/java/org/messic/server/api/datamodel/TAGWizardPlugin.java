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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

@XmlRootElement
@ApiObject(name="TAGWizardPlugin", description="TAGWizardPlugin is an entity that represents TAGWizard plugins to obtain information from songs files.")
public class TAGWizardPlugin
{
    @ApiObjectField( description = "name of the plugin" )
    private String name;
    
    @ApiObjectField( description = "album discovered by the plugin" )
    private List<Album> albums;

    public TAGWizardPlugin(String name, Album album){
        this.name=name;
        this.albums=new ArrayList<Album>();
        if(album!=null){
            this.albums.add(album);
        }
    }

    public TAGWizardPlugin(String name, List<Album> albums){
        this.name=name;
        this.albums=albums;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public List<Album> getAlbums()
    {
        return albums;
    }

    public void setAlbums( List<Album> albums )
    {
        this.albums = albums;
    }
    
    public void addAlbum(Album album){
        if(this.albums==null){
            this.albums=new ArrayList<Album>();
        }
        if(album!=null){
            this.albums.add(album);
        }
    }

    
}
