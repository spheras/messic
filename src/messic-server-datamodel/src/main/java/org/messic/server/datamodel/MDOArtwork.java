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