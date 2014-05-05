package org.messic.server.datamodel;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table( name = "OTHERS" )
@DiscriminatorValue( MDOAlbumResource.OTHER )
public class MDOOtherResource
    extends MDOAlbumResource
    implements MDO, Serializable
{

    /**
     * Problem with inheritance and mappedby
     * http://chriswongdevblog.blogspot.com.es/2009/10/polymorphic-one-to-many-relationships.html that's the reason why
     * it's repeated on each {@link MDOAlbumResource} subclass
     */
    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "ALBUM", nullable = false )
    private MDOAlbum album;

    /**
	 * 
	 */
    private static final long serialVersionUID = -5155034982555760909L;

    public MDOOtherResource()
    {
        super();
    }

    public MDOOtherResource( MDOUser user, String location )
    {
        super( user, location );
    }

    public MDOAlbum getAlbum()
    {
        return album;
    }

    public void setAlbum( MDOAlbum album )
    {
        this.album = album;
    }

    

}