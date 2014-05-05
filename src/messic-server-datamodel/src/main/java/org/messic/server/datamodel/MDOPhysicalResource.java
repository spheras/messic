package org.messic.server.datamodel;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Table( name = "PHYSICAL_RESOURCES" )
@Inheritance( strategy = InheritanceType.JOINED )
@DiscriminatorColumn( name = "DTYPE", discriminatorType = DiscriminatorType.STRING, length = 31 )
@DiscriminatorValue( MDOResource.PHYSICAL_RESOURCE )
public class MDOPhysicalResource
    extends MDOResource
    implements MDO, Serializable
{

    private static final long serialVersionUID = 8792913920714652055L;

    public static final String AUTHOR = "AUTHOR";

    public static final String ALBUM = "ALBUM";

    public static final String ALBUM_RESOURCE = "ALBUM_RESOURCE";

    @Column( name = "LOCATION" )
    private String location;

    
    public MDOPhysicalResource()
    {
        super();
    }

    public MDOPhysicalResource( MDOUser user, String location )
    {
        super( user );
        this.location = location;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation( String location )
    {
        this.location = location;
    }
}