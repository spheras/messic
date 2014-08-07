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
package org.messic.server.api;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.messic.server.api.datamodel.MessicSettings;
import org.messic.server.api.datamodel.User;
import org.messic.server.api.dlna.DLNAServer;
import org.messic.server.api.exceptions.NotAllowedMessicException;
import org.messic.server.datamodel.MDOUser;
import org.messic.server.datamodel.dao.DAOMessicSettings;
import org.messic.server.datamodel.dao.DAOUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class APIUser
{

    @Autowired
    private DAOUser daoUser;

    @Autowired
    private DAOMessicSettings daoSettings;

    @Autowired
    private DLNAServer dlnaServer;

    public void resetPassword( Long sid )
    {
        MDOUser mdoUserToReset = daoUser.getUserById( sid );
        mdoUserToReset.setPassword( "123456" );
        daoUser.saveUser( mdoUserToReset, true, mdoUserToReset.getPassword() );
    }

    public void removeUser( Long sid, boolean removeMusicContent )
        throws Exception
    {
        MDOUser mdoUserToRemove = daoUser.getUserById( sid );
        if ( mdoUserToRemove != null )
        {
            if ( removeMusicContent )
            {
                String absolutePath = mdoUserToRemove.calculateAbsolutePath( daoSettings.getSettings() );
                File fabsolutePath = new File( absolutePath );
                if ( fabsolutePath.exists() )
                {
                    FileUtils.deleteDirectory( new File( absolutePath ) );
                }
            }

            daoUser.remove( mdoUserToRemove );
        }
        else
        {
            throw new Exception( "User doesn't exist!" );
        }
    }

    public User getUserByLogin( String user )
    {
        MDOUser mdoUser = daoUser.getUserByLogin( user );
        if ( mdoUser != null )
        {
            return new User( mdoUser );
        }
        else
        {
            return null;
        }
    }

    public User getUserById( Long userId )
    {
        MDOUser mdoUser = daoUser.getUserById( userId );
        return new User( mdoUser );
    }

    /**
     * Check if the userName is valid to create a new user... it must be unique, so the username must not exist
     * 
     * @param userName {@link String} username to check
     * @return boolean true->is valid, false->not valid!
     */
    public boolean validateNewUserName( String userName )
    {
        MDOUser mdoUser = daoUser.getUserByLogin( userName );
        if ( mdoUser == null )
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public User createUser( User user )
    {
        boolean existAdministrator = daoUser.existUsers();
        if ( !existAdministrator )
        {
            user.setAdministrator( Boolean.TRUE );
        }
        else
        {
            user.setAdministrator( Boolean.FALSE );
        }

        MDOUser newUser = user.updateMDOUser( null );
        daoUser.saveUser( newUser, true, user.getPassword() );

        MDOUser userCreated = daoUser.getUserByLogin( user.getLogin() );

        this.dlnaServer.refreshServer();

        return new User( userCreated );

    }

    /**
     * Save settings
     * 
     * @param settings {@link MessicSettings} settings to be saved
     * @throws NotAllowedMessicException
     */
    public void saveSettings( User user, MessicSettings settings )
        throws NotAllowedMessicException
    {
        boolean allowed = false;

        if ( !daoUser.existUsers() )
        {
            // there is no users at the database... this should be the creation of the admin, maybe
            allowed = true;
        }
        else
        {
            if ( user != null )
            {
                MDOUser mdouser = daoUser.getUserByLogin( user.getLogin() );
                if ( mdouser != null )
                {
                    if ( mdouser.getAdministrator() )
                    {
                        allowed = true;
                    }
                    else
                    {
                        allowed = false;
                    }
                }
                else
                {
                    // no user logged... but not the first,, this is not an admnistrator!!
                    allowed = false;
                }
            }
            else
            {
                // no user logged... but not the first,, this is not an admnistrator!!
                allowed = false;
            }
        }

        if ( allowed )
        {
            daoSettings.saveSettings( settings.getMDOSettings( this.daoSettings ) );

            // after save settings we need to update dlna services
            this.dlnaServer.refreshServer();
        }
        else
        {
            throw new NotAllowedMessicException( "This user is not allowed to change messic settings!" );
        }
    }

    public User updateUser( User user )
    {

        boolean existAdministrator = daoUser.existUsers();
        if ( !existAdministrator )
        {
            user.setAdministrator( Boolean.TRUE );
        }

        MDOUser mdoUser = this.daoUser.getUserByLogin( user.getLogin() );

        mdoUser = user.updateMDOUser( mdoUser );

        this.daoUser.saveUser( mdoUser, !user.getPassword().equals( mdoUser.getPassword()), user.getPassword() );

        this.dlnaServer.refreshServer();

        return user;

    }

}
