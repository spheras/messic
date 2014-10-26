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
import org.messic.server.datamodel.MDOMessicSettings;
import org.messic.server.datamodel.dao.DAOMessicSettings;

@XmlRootElement
@ApiObject( name = "MessicSettings", description = "Messic Settings" )
public class MessicSettings
{
    @ApiObjectField( description = "Illegal character replacement for paths and filenames" )
    private char illegalCharacterReplacement = '_';

    @ApiObjectField( description = "indicates if messic allows the auto user creation" )
    private boolean allowUserCreation = true;

    @ApiObjectField( description = "indicates if messic allows the dlna share content" )
    private boolean allowDLNA = true;

    @ApiObjectField( description = "indicates if messic allows the discovering the messic service over udp broadcast (this is different from dlna)" )
    private boolean allowDiscovering = true;

    @ApiObjectField( description = "This is the name of the messic server. By the default it takes the host name" )
    private String messicServerName = "";

    public MessicSettings()
    {

    }

    /**
     * Obtain the MDOSettings corresponding with the settings of this object
     * 
     * @param daoSettings
     * @return
     */
    public MDOMessicSettings getMDOSettings( DAOMessicSettings daoSettings )
    {
        MDOMessicSettings mdoSettings = daoSettings.getSettings();
        mdoSettings.setAllowDLNA( this.isAllowDLNA() );
        mdoSettings.setAllowUserCreation( this.isAllowUserCreation() );
        mdoSettings.setIllegalCharacterReplacement( this.getIllegalCharacterReplacement() );
        mdoSettings.setAllowMessicDiscovering( this.isAllowDiscovering() );
        mdoSettings.setMessicServerName( this.getMessicServerName() );
        return mdoSettings;
    }

    /**
     * Constructor copy
     * 
     * @param mdoSettings
     */
    public MessicSettings( MDOMessicSettings mdoSettings )
    {
        this.illegalCharacterReplacement = mdoSettings.getIllegalCharacterReplacement();
        this.allowUserCreation = mdoSettings.isAllowUserCreation();
        this.allowDLNA = mdoSettings.isAllowDLNA();
        this.allowDiscovering = mdoSettings.isAllowMessicDiscovering();
        this.messicServerName = mdoSettings.getMessicServerName();
    }

    /**
     * @return the illegalCharacterReplacement
     */
    public char getIllegalCharacterReplacement()
    {
        return illegalCharacterReplacement;
    }

    /**
     * @param illegalCharacterReplacement the illegalCharacterReplacement to set
     */
    public void setIllegalCharacterReplacement( char illegalCharacterReplacement )
    {
        this.illegalCharacterReplacement = illegalCharacterReplacement;
    }

    /**
     * @return the allowUserCreation
     */
    public boolean isAllowUserCreation()
    {
        return allowUserCreation;
    }

    /**
     * @param allowUserCreation the allowUserCreation to set
     */
    public void setAllowUserCreation( boolean allowUserCreation )
    {
        this.allowUserCreation = allowUserCreation;
    }

    /**
     * @return the allowDLNA
     */
    public boolean isAllowDLNA()
    {
        return allowDLNA;
    }

    /**
     * @param allowDLNA the allowDLNA to set
     */
    public void setAllowDLNA( boolean allowDLNA )
    {
        this.allowDLNA = allowDLNA;
    }

    /**
     * @return the allowDiscovering
     */
    public boolean isAllowDiscovering()
    {
        return allowDiscovering;
    }

    /**
     * @param allowDiscovering the allowDiscovering to set
     */
    public void setAllowDiscovering( boolean allowDiscovering )
    {
        this.allowDiscovering = allowDiscovering;
    }

    /**
     * @return the messicServerName
     */
    public String getMessicServerName()
    {
        return messicServerName;
    }

    /**
     * @param messicServerName the messicServerName to set
     */
    public void setMessicServerName( String messicServerName )
    {
        this.messicServerName = messicServerName;
    }
}
