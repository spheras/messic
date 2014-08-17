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
package org.messic.server.datamodel;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.messic.server.datamodel.datasource.MessicDataSource;

@Entity
@Table( name = "SETTINGS" )
public class MDOMessicSettings
    implements MDO, Serializable
{
    /**
	 * 
	 */
    private static final long serialVersionUID = -6585157237342912864L;

    @Id
    @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "SEQ_USERS" )
    @SequenceGenerator( name = "SEQ_USERS", sequenceName = "SEQ_USERS" )
    @Column( name = "SID", nullable = false, unique = true )
    private Long sid;

    @Column( name = "ILLEGALCHARACTERREPLACEMENT", nullable = true )
    /* string whicn will replace all those illegal characters at filename adn paths */
    private char illegalCharacterReplacement = '_';

    @Column( name = "ALLOWUSERCREATION", nullable = false )
    /* flag to know if its available the option to create new users by non messic users */
    private boolean allowUserCreation = true;

    @Column( name = "ALLOWDLNA", nullable = false )
    /* flag to know is allowed the DLNA share content */
    private boolean allowDLNA = true;

    @Column( name = "VERSION", nullable = true )
    private String version;

    /**
     * @constructor
     */
    public MDOMessicSettings()
    {
        super();
    }

    public Long getSid()
    {
        return sid;
    }

    public void setSid( Long sid )
    {
        this.sid = sid;
    }

    public String getGenericBaseStorePath()
    {
        return MessicDataSource.databasePath;
    }

    public char getIllegalCharacterReplacement()
    {
        return illegalCharacterReplacement;
    }

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
     * @return the version
     */
    public String getVersion()
    {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion( String version )
    {
        this.version = version;
    }

}