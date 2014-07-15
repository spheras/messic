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
    @ApiObjectField( description = "generic base store path, default music store path" )
    private String genericBaseStorePath = "";

    @ApiObjectField( description = "Illegal character replacement for paths and filenames" )
    private char illegalCharacterReplacement = '_';

    @ApiObjectField( description = "indicates if messic allows the auto user creation" )
    private boolean allowUserCreation = true;

    @ApiObjectField( description = "indicates if messic allows the settings of an especific folder for the users (different from the default messic music store)" )
    private boolean allowUserSpecificFolder = false;

    @ApiObjectField( description = "indicates if messic allows the dlna share content" )
    private boolean allowDLNA = true;

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
        mdoSettings.setAllowUserSpecificFolder( this.isAllowUserSpecificFolder() );
        mdoSettings.setGenericBaseStorePath( this.getGenericBaseStorePath() );
        mdoSettings.setIllegalCharacterReplacement( this.getIllegalCharacterReplacement() );
        return mdoSettings;
    }

    /**
     * Constructor copy
     * 
     * @param mdoSettings
     */
    public MessicSettings( MDOMessicSettings mdoSettings )
    {
        this.genericBaseStorePath = mdoSettings.getGenericBaseStorePath();
        this.illegalCharacterReplacement = mdoSettings.getIllegalCharacterReplacement();
        this.allowUserCreation = mdoSettings.isAllowUserCreation();
        this.allowUserSpecificFolder = mdoSettings.isAllowUserSpecificFolder();
        this.allowDLNA = mdoSettings.isAllowDLNA();
    }

    /**
     * @return the genericBaseStorePath
     */
    public String getGenericBaseStorePath()
    {
        return genericBaseStorePath;
    }

    /**
     * @param genericBaseStorePath the genericBaseStorePath to set
     */
    public void setGenericBaseStorePath( String genericBaseStorePath )
    {
        this.genericBaseStorePath = genericBaseStorePath;
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
     * @return the allowUserSpecificFolder
     */
    public boolean isAllowUserSpecificFolder()
    {
        return allowUserSpecificFolder;
    }

    /**
     * @param allowUserSpecificFolder the allowUserSpecificFolder to set
     */
    public void setAllowUserSpecificFolder( boolean allowUserSpecificFolder )
    {
        this.allowUserSpecificFolder = allowUserSpecificFolder;
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
}
