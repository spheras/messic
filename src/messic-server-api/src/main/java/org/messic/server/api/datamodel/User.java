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

import org.jaudiotagger.tag.vorbiscomment.util.Base64Coder;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;
import org.messic.server.datamodel.MDOUser;

@XmlRootElement
@ApiObject( name = "User", description = "User of Messic" )
public class User
{
    @ApiObjectField( description = "identification number of the user" )
    private Long sid = 0l;

    @ApiObjectField( description = "name of the user" )
    private String name = "";

    @ApiObjectField( description = "base 64 image for the avatar" )
    private String avatar_b64;

    @ApiObjectField( description = "email of the user" )
    private String email = "";

    @ApiObjectField( description = "username of the user" )
    private String login = "";

    @ApiObjectField( description = "password of the user" )
    private String password = "";

    @ApiObjectField( description = "flag to know if the user is an administrator" )
    private Boolean administrator = false;

    @ApiObjectField( description = "flag to know the user allow getting information for statistics purposes" )
    private Boolean allowStatistics = true; // true by default

    @ApiObjectField( description = "flag to know the user content is allowed to be shared by a DLNA protocol" )
    private Boolean allowDLNA = true; // true by default

    @ApiObjectField( description = "Path to store their songs" )
    private String storePath = "";

    /**
     * Default constructor
     */
    public User()
    {

    }

    /**
     * Update an {@link MDOUser} object. If the mdouser exist at the database, the parameter mdouser should be filled to
     * avoid detached faults... if not, the parameter can be null, this function will create a new one...
     * 
     * @param mdouser {@link MDOUser} user to udpate, null if you need a new one
     * @return {@link MDOUser} update with this user info
     */
    public MDOUser updateMDOUser( MDOUser mdouser )
    {
        if ( mdouser == null )
        {
            mdouser = new MDOUser();
        }

        if ( this.getSid() > 0 )
        {
            mdouser.setSid( getSid() );
        }
        else
        {
            mdouser.setSid( null );
            mdouser.setAdministrator( getAdministrator() );
        }
        
        mdouser.setAllowDLNA( getAllowDLNA() );
        mdouser.setAllowStatistics( getAllowStatistics() );
        mdouser.setAvatar( getAvatar() );
        mdouser.setEmail( getEmail() );
        mdouser.setLogin( getLogin() );
        mdouser.setName( getName() );
        mdouser.setPassword( getPassword() );
        mdouser.setStorePath( getStorePath() );

        return mdouser;
    }

    public static User transform( MDOUser user )
    {
        return new User( user );
    }

    /**
     * Copy constructor
     * 
     * @param mdoUser {@link MDOUser} to copy
     */
    public User( MDOUser mdoUser )
    {
        if ( mdoUser != null )
        {
            this.setAdministrator( mdoUser.getAdministrator() );

            if ( mdoUser.getAvatar() != null )
            {
                this.setAvatar_b64( new String( Base64Coder.encode( mdoUser.getAvatar() ) ) );
            }
            this.setEmail( mdoUser.getEmail() );
            this.setLogin( mdoUser.getLogin() );
            this.setName( mdoUser.getName() );
            this.setPassword( mdoUser.getPassword() );
            this.setSid( mdoUser.getSid() );
            this.setStorePath( mdoUser.getStorePath() );
            this.setAllowStatistics( mdoUser.getAllowStatistics() );
            this.setAllowDLNA( mdoUser.getAllowDLNA() );
        }
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
        return storePath;
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

    /**
     * @return the avatar_b64
     */
    public String getAvatar_b64()
    {
        return avatar_b64;
    }

    /**
     * @param avatar_b64 the avatar_b64 to set
     */
    public void setAvatar_b64( String avatar_b64 )
    {
        this.avatar_b64 = avatar_b64;
    }

    public byte[] getAvatar()
    {
        if ( this.avatar_b64 != null )
        {
            return Base64Coder.decode( this.avatar_b64 );
        }
        else
        {
            return null;
        }
    }

    /**
     * @return the allowDLNA
     */
    public Boolean getAllowDLNA()
    {
        return allowDLNA;
    }

    /**
     * @param allowDLNA the allowDLNA to set
     */
    public void setAllowDLNA( Boolean allowDLNA )
    {
        this.allowDLNA = allowDLNA;
    }

}
