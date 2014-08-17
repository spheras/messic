package org.messic.server.api.datamodel;

import javax.xml.bind.annotation.XmlRootElement;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

@XmlRootElement
@ApiObject( name = "Update", description = "Update information from Messic" )
public class Update
{
    public Update()
    {

    }

    public Update( boolean needUpdate, String lastVersion, String currentVersion )
    {
        this.needUpdate = needUpdate;
        this.lastVersion = lastVersion;
        this.currentVersion = currentVersion;
    }

    @ApiObjectField( description = "flag to know if there is a new version available at the server." )
    private boolean needUpdate;

    @ApiObjectField( description = "Last version founded at the server." )
    private String lastVersion;

    @ApiObjectField( description = "Current version of messic." )
    private String currentVersion;

    /**
     * @return the needUpdate
     */
    public boolean isNeedUpdate()
    {
        return needUpdate;
    }

    /**
     * @param needUpdate the needUpdate to set
     */
    public void setNeedUpdate( boolean needUpdate )
    {
        this.needUpdate = needUpdate;
    }

    /**
     * @return the lastVersion
     */
    public String getLastVersion()
    {
        return lastVersion;
    }

    /**
     * @param lastVersion the lastVersion to set
     */
    public void setLastVersion( String lastVersion )
    {
        this.lastVersion = lastVersion;
    }

    /**
     * @return the currentVersion
     */
    public String getCurrentVersion()
    {
        return currentVersion;
    }

    /**
     * @param currentVersion the currentVersion to set
     */
    public void setCurrentVersion( String currentVersion )
    {
        this.currentVersion = currentVersion;
    }
}
