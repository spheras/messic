package org.messic.server.datamodel;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "GENRE")
public class MDOGenre implements
        MDO, Serializable {
    
    private static final long serialVersionUID = 8792913920714652055L;

    @Column(name = "NAME", nullable = false)
    private String name;    
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_RESOURCES")
    @SequenceGenerator(name = "SEQ_RESOURCES", sequenceName = "SEQ_RESOURCES")
    @Column(name = "SID", nullable = false, unique = true)
    private Long sid;
    
    public MDOGenre ()
    {
        super();
    }
    
    public MDOGenre (String name)
    {
        this.name = name;        
    }

    public Long getSid() {
        return sid;
    }

    public void setSid(Long sid) {
        this.sid = sid;
    }
    
    public String getName() {
        return name;
    }

	public void setName(String name) {
		this.name = name;
	}
}