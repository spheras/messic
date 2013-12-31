package org.messic.server.datamodel;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "OTHERS")
@DiscriminatorValue(MDOAlbumResource.OTHER)
public class MDOOtherResource extends MDOAlbumResource implements
        MDO, Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -5155034982555760909L;

	public MDOOtherResource ()
    {
        super();
    }
    
    public MDOOtherResource (MDOUser user, String location)
    {
        super(user,location);
    }
    
       
}