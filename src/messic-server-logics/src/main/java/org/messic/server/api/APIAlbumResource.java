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
package org.messic.server.api;

import java.io.File;
import java.io.IOException;

import org.messic.server.api.datamodel.User;
import org.messic.server.datamodel.MDOAlbumResource;
import org.messic.server.datamodel.dao.DAOAlbumResource;
import org.messic.server.datamodel.dao.DAOMessicSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class APIAlbumResource
{
    @Autowired
    private DAOMessicSettings daoSettings;

    @Autowired
    private DAOAlbumResource daoAlbumResource;

    @Transactional
    public void remove( User user, Long resourceSid )
        throws IOException
    {
        MDOAlbumResource resource = this.daoAlbumResource.get( user.getLogin(), resourceSid );
        if ( resource != null )
        {
            // first, removing the resource file
            String path = resource.calculateAbsolutePath( daoSettings.getSettings() );
            File fpath = new File( path );
            fpath.delete();
            // after, removing the album data from database
            this.daoAlbumResource.remove( resource );
        }
    }

}
