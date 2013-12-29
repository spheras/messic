package org.messic.server.datamodel;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "RESOURCES")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "DTYPE", discriminatorType = DiscriminatorType.STRING, length = 31)
public class MDOResource implements MDO, Serializable {

    /**
     * serializable
     */
    private static final long serialVersionUID = -2753653453731805880L;

    public static final String PHYSICAL_RESOURCE = "PHYSICAL_RESOURCE";
    public static final String PLAYLIST = "PLAYLIST";
 
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_RESOURCES")
    @SequenceGenerator(name = "SEQ_RESOURCES", sequenceName = "SEQ_RESOURCES")
    @Column(name = "SID", nullable = false, unique = true)
    private Long sid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OWNER", nullable = false)
    private MDOUser owner;
    
    public MDOResource()
    {
    	
    }

    public MDOResource(MDOUser owner)
    {
        this.owner = owner;
    }

    public Long getSid() {
        return sid;
    }

    public void setSid(Long sid) {
        this.sid = sid;
    }

    public MDOUser getOwner() {
        return owner;
    }

    public void setOwner(MDOUser owner) {
        this.owner = owner;
    }


}