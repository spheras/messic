package org.messic.server.datamodel;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "ARTWORKS")
@DiscriminatorValue(MDOAlbumResource.ARTWORK)
public class MDOArtworkResource extends MDOAlbumResource implements
        MDO, Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 573980115145715687L;

	public MDOArtworkResource ()
    {
        super();
    }
    
    public MDOArtworkResource (MDOUser user, String location)
    {
        super(user,location);
    }
       
}