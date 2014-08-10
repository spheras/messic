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
package org.messic.server.datamodel.jpaimpl;

import java.util.List;

import javax.persistence.Query;

import org.messic.server.datamodel.MDOMessicSettings;
import org.messic.server.datamodel.dao.DAOMessicSettings;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class DAOJPAMessicSettings
    extends DAOJPA<MDOMessicSettings>
    implements DAOMessicSettings
{

    public DAOJPAMessicSettings()
    {
        super( MDOMessicSettings.class );
    }

    @Override
    @Transactional
    public MDOMessicSettings getSettings()
    {
        Query query = entityManager.createQuery( "from MDOMessicSettings" );

        @SuppressWarnings( "unchecked" )
        List<MDOMessicSettings> results = query.getResultList();
        if ( results == null || results.size() == 0 )
        {
            return createBasicSettings();
        }
        else
        {
            return results.get( 0 );
        }
    }

    @Transactional
    private MDOMessicSettings createBasicSettings()
    {
        MDOMessicSettings ms = new MDOMessicSettings();
        ms.setIllegalCharacterReplacement( '_' );
        ms.setAllowUserCreation( true );
        ms.setAllowDLNA( true );
        saveSettings( ms );
        return ms;
    }

    @Override
    @Transactional
    public void saveSettings( MDOMessicSettings newSettings )
    {
        entityManager.persist( newSettings );
    }

}
