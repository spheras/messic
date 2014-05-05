package org.messic.server.datamodel;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ARTWORKS")
@DiscriminatorValue(MDOAlbumResource.ARTWORK)
public class MDOArtwork extends MDOAlbumResource implements
        MDO, Serializable {
    
    @Column(name = "COVER", nullable = false)
    private boolean cover=false;

    /**
     * Problem with inheritance and mappedby
     * http://chriswongdevblog.blogspot.com.es/2009/10/polymorphic-one-to-many-relationships.html
     * that's the reason why it's repeated on each {@link MDOAlbumResource} subclass
     */
    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "ALBUM", nullable = false )
    private MDOAlbum album;

    /**
	 * 
	 */
	private static final long serialVersionUID = 573980115145715687L;

	public MDOArtwork ()
    {
        super();
    }
    
    public MDOArtwork (MDOUser user, String location)
    {
        super(user,location);
    }

    public final boolean isCover()
    {
        return cover;
    }

    public final void setCover( boolean cover )
    {
        this.cover = cover;
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