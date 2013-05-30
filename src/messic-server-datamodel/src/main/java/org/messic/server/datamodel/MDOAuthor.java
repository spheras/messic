package org.messic.server.datamodel;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "AUTHORS")
@DiscriminatorValue(MDOPhysicalResource.AUTHOR)

public class MDOAuthor extends MDOPhysicalResource implements
        MDO, Serializable {
    
    private static final long serialVersionUID = 8792913920714652055L;

    @Column(name = "NAME", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PHYSICAL_RESOURCE", nullable = false)
    private MDOPhysicalResource physicalResource; 
    
    @OneToMany(mappedBy = "author")
    private Set<MDOAlbum> albums;
    
    public MDOAuthor ()
    {
        super();
    }
    
    public MDOAuthor (MDOUser user, String location, String name)
    {
        super(user,location);
        this.name = name;        
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<MDOAlbum> getAlbums() {
        return albums;
    }

    public void setAlbums(Set<MDOAlbum> albums) {
        this.albums = albums;
    }

    public MDOPhysicalResource getPhysicalResource() {
        return physicalResource;
    }

    public void setPhysicalResource(MDOPhysicalResource physicalResource) {
        this.physicalResource = physicalResource;
    }
        
}