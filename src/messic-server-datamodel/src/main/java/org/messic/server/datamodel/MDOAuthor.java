package org.messic.server.datamodel;

import java.io.File;
import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table( name = "AUTHORS" )
@DiscriminatorValue( MDOPhysicalResource.AUTHOR )
public class MDOAuthor
    extends MDOPhysicalResource
    implements MDO, Serializable
{

    private static final long serialVersionUID = 8792913920714652055L;

    @Column( name = "NAME", nullable = false )
    private String name;

    @OneToMany( mappedBy = "author", cascade = { CascadeType.REMOVE } )
    @OrderBy( "NAME" )
    private Set<MDOAlbum> albums;

    public MDOAuthor()
    {
        super();
    }

    public MDOAuthor( MDOUser user, String location, String name )
    {
        super( user, location );
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public Set<MDOAlbum> getAlbums()
    {
        return albums;
    }

    public void setAlbums( Set<MDOAlbum> albums )
    {
        this.albums = albums;
    }

    public String calculateAbsolutePath( MDOMessicSettings settings )
    {
        String basePath = getOwner().calculateAbsolutePath( settings );
        String result = basePath + File.separatorChar + getLocation();
        return result;
    }

}