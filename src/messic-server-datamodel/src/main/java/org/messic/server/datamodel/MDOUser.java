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
package org.messic.server.datamodel;

import java.io.File;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.messic.server.Util;

@Entity
@Table( name = "USERS" )
@Inheritance( strategy = InheritanceType.JOINED )
@DiscriminatorColumn( name = "DTYPE", discriminatorType = DiscriminatorType.STRING, length = 31 )
public class MDOUser
    implements MDO, Serializable
{

    /**
     * 
     */
    private static final long serialVersionUID = -295966654597222940L;

    @Id
    @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "SEQ_USERS" )
    @SequenceGenerator( name = "SEQ_USERS", sequenceName = "SEQ_USERS" )
    @Column( name = "SID", nullable = false, unique = true )
    private Long sid;

    @Column( name = "NAME", nullable = false )
    private String name;

    @Lob
    @Column( name = "AVATAR", nullable = true )
    private byte[] avatar;

    @Column( name = "EMAIL", nullable = false )
    private String email;

    @Column(name = "LOGIN", nullable = false, unique=true)
    private String login;

    @Column( name = "PASSWORD", nullable = false )
    private String password;

    @Column( name = "ADMINISTRATOR", nullable = false )
    private Boolean administrator;

    @Column( name = "ALLOWSTATISTICS", nullable = false )
    private Boolean allowStatistics=true; //by default, true

    @Column(name = "STOREPATH" , nullable = false)
    private String storePath;

    /**
     * @constructor
     */
    public MDOUser()
    {
        super();
    }

    public MDOUser(String name, String email, byte[] avatar, String login, String password, Boolean administrator, String basePath) {
        this.name = name;
        this.email = email;
        this.avatar = avatar;
        this.login = login;
        this.password = password;
        this.administrator = administrator;
        this.storePath = basePath;
    }

    public Long getSid()
    {
        return sid;
    }

    public void setSid( Long sid )
    {
        this.sid = sid;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail( String email )
    {
        this.email = email;
    }

    public byte[] getAvatar()
    {
        return avatar;
    }

    public void setAvatar( byte[] avatar )
    {
        this.avatar = avatar;
    }

    public String getLogin()
    {
        return login;
    }

    public void setLogin( String login )
    {
        this.login = login;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword( String password )
    {
        this.password = password;
    }

    public Boolean getAdministrator()
    {
        return administrator;
    }

    public void setAdministrator( Boolean administrator )
    {
        this.administrator = administrator;
    }

    public String getStorePath()
    {
        String resultStorePath = this.storePath;
        if ( this.storePath == null || this.storePath.length() == 0
            || this.storePath.equals( Util.GENERIC_BASE_STORE_PATH_VAR ) )
        {
            resultStorePath = Util.GENERIC_BASE_STORE_PATH_VAR + File.separatorChar + getLogin();
        }

        return resultStorePath;
    }

    /**
     * calculate the absolute store path for this user.
     * 
     * @param settings {@link MDOMessicSettings} settings to get the base store path
     * @return String the absolute, full path, to store resources for this user
     */
    public String calculateAbsolutePath( MDOMessicSettings settings )
    {
        String path = getStorePath();
        path = path.replace( Util.GENERIC_BASE_STORE_PATH_VAR, settings.getGenericBaseStorePath() );
        return path;
    }

    /**
     * Obtain the path to the temporal folder for uploaded resources
     * 
     * @param settings {@link MDOMessicSettings} settings for messic
     * @param albumCode {@link String} code for the album to upload
     * @return {@link String} temporal path for uploaded
     */
    public String calculateTmpPath( MDOMessicSettings settings, String albumCode )
    {
        String basePath = calculateAbsolutePath( settings );
        basePath = basePath + File.separatorChar + Util.TEMPORAL_FOLDER + File.separatorChar + albumCode;
        return basePath;
    }

    public void setStorePath( String storePath )
    {
        this.storePath = storePath;
    }

    /**
     * @return the allowStatistics
     */
    public Boolean getAllowStatistics()
    {
        return allowStatistics;
    }

    /**
     * @param allowStatistics the allowStatistics to set
     */
    public void setAllowStatistics( Boolean allowStatistics )
    {
        this.allowStatistics = allowStatistics;
    }

}
