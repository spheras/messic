package org.messic.server.datamodel.update;

import org.messic.server.datamodel.MDOMessicSettings;
import org.messic.server.datamodel.dao.DAOMessicSettings;
import org.messic.server.datamodel.dao.DAOUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class MessicDBUpdate
{

    @Autowired
    private DAOMessicSettings daoSettings;

    @Autowired
    private DAOUser daoUser;

    /**
     * update the current database to the new version
     * 
     * @param sversion {@link String} version like "1.0.0-snapshot"
     * @param version int the vesrion number (first number)
     * @param revision int the revision number (second number)
     * @param compile int the compilation number (third number)
     * @param semantic String the semantic number (snapshot). This could not be present
     */
    @Transactional
    public void update( String sversion, int version, int revision, int compile, String semantic )
    {
        if ( isDifferentVersion( sversion ) )
        {
            updateToVersion( version, revision, compile, semantic );
            MDOMessicSettings mms = daoSettings.getSettings();
            mms.setVersion( sversion );
            daoSettings.saveSettings( mms );

            daoUser.usersNotifyMessicUpdate();
        }
    }

    /**
     * Update the database to a new version
     * 
     * @param version int the vesrion number (first number)
     * @param revision int the revision number (second number)
     * @param compile int the compilation number (third number)
     * @param semantic String the semantic number (snapshot). This could not be present
     */
    @Transactional
    private void updateToVersion( int version, int revision, int compile, String semantic )
    {
        // by the moment nothing to update at the database
    }

    /**
     * Check if we have a different version at database. Receive the current version of messic
     * 
     * @param sversion {@link String} current version of messic
     * @return boolean true->is different
     */
    private boolean isDifferentVersion( String sversion )
    {
        String dbVersion = daoSettings.getSettings().getVersion();
        if ( dbVersion != null && dbVersion.equals( sversion ) )
        {
            return false;
        }
        else
        {
            return true;
        }
    }
}
