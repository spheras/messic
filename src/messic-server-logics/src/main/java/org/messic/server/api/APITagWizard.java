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
import java.util.ArrayList;
import java.util.List;

import org.messic.server.api.datamodel.Album;
import org.messic.server.api.datamodel.User;
import org.messic.server.api.tagwizard.TAGWizard;
import org.messic.server.api.tagwizard.audiotagger.AudioTaggerTAGWizardPlugin;
import org.messic.server.api.tagwizard.service.TAGWizardPlugin;
import org.messic.server.datamodel.MDOUser;
import org.messic.server.datamodel.dao.DAOMessicSettings;
import org.messic.server.datamodel.dao.DAOUser;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class APITagWizard
{
    @Autowired
    private DAOMessicSettings daoSettings;

    @Autowired
    private DAOUser daoUser;

    /**
     * Obtain a tagwizard plugin with the name
     * 
     * @return TAGWizardPlugin plugin with that name
     */
    private List<TAGWizardPlugin> getTAGWizardPlugins()
    {

        BundleContext context = FrameworkUtil.getBundle( TAGWizardPlugin.class ).getBundleContext();
        ArrayList<TAGWizardPlugin> result = new ArrayList<TAGWizardPlugin>();

        try
        {
            // Query for all service references matching any TAGWizard plugin
            ServiceReference<?>[] refs = null;
            refs = context.getServiceReferences( TAGWizardPlugin.class.getName(), "(TAGWizard=*)" );
            if ( refs != null )
            {
                for ( int i = 0; i < refs.length; i++ )
                {
                    TAGWizardPlugin twp = (TAGWizardPlugin) context.getService( refs[i] );
                    if ( !twp.getName().equals( AudioTaggerTAGWizardPlugin.NAME ) )
                    {
                        result.add( twp );
                    }
                }
            }
        }
        catch ( Exception e )
        {
            // TODO log
        }
        if ( result.size() > 0 )
        {
            return result;
        }
        else
        {
            return null;
        }
    }

    /**
     * Obtain all available tagwizard plugins, only the names without results. Also return the basic wizard info
     * obtained from the uploaded files, at position 0.
     * 
     * @param mdouser {@link MDOUser} scope
     * @return List<TAGWizardPlugin/> plugins available
     * @throws IOException
     */
    public List<org.messic.server.api.datamodel.TAGWizardPlugin> getWizards( User user, String albumCode )
        throws IOException
    {
        MDOUser mdouser = daoUser.getUserByLogin( user.getLogin() );
        List<TAGWizardPlugin> twp = getTAGWizardPlugins();
        ArrayList<org.messic.server.api.datamodel.TAGWizardPlugin> result =
            new ArrayList<org.messic.server.api.datamodel.TAGWizardPlugin>();
        if ( twp != null )
        {
            for ( int i = 0; i < twp.size(); i++ )
            {
                org.messic.server.api.datamodel.TAGWizardPlugin dmtwp =
                    new org.messic.server.api.datamodel.TAGWizardPlugin( twp.get( i ).getName(), (Album) null );
                result.add( dmtwp );
            }
        }

        File tmpPath = new File( mdouser.calculateTmpPath( daoSettings.getSettings(), albumCode ) );
        File[] files = tmpPath.listFiles();
        TAGWizard tw = new TAGWizard();
        org.messic.server.api.datamodel.TAGWizardPlugin basicPlugin = tw.getAlbumWizard( user, null, files );
        result.add( 0, basicPlugin );

        return result;
    }

    public org.messic.server.api.datamodel.TAGWizardPlugin getWizardAlbum( User user, String pluginName,
                                                                           Album albumHelpInfo, String albumCode )
        throws IOException
    {
        MDOUser mdouser = daoUser.getUserByLogin( user.getLogin() );
        File basePath = new File( mdouser.calculateTmpPath( daoSettings.getSettings(), albumCode ) );
        File[] files = basePath.listFiles();
        TAGWizard tw = new TAGWizard();
        org.messic.server.api.datamodel.TAGWizardPlugin result = tw.getAlbumWizard( albumHelpInfo, files, pluginName );

        return result;
    }

}
