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
@Table(name = "SETTINGS")
public class MDOMessicSettings implements MDO,Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6585157237342912864L;

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_USERS")
    @SequenceGenerator(name = "SEQ_USERS", sequenceName = "SEQ_USERS")
    @Column(name = "SID", nullable = false, unique = true)
    private Long sid;
    
    @Column(name = "GENERICBASESTOREPATH", nullable = true)
    private String genericBaseStorePath;

    /**
     * @constructor
     */
    public MDOMessicSettings() {
        super();
    }
    
    public MDOMessicSettings(String genericBaseStorePath) {
    	setGenericBaseStorePath(genericBaseStorePath);
    }

    public Long getSid() {
        return sid;
    }

    public void setSid(Long sid) {
        this.sid = sid;
    }

	public String getGenericBaseStorePath() {
		return genericBaseStorePath;
	}

	public void setGenericBaseStorePath(String genericBaseStorePath) {
		this.genericBaseStorePath = genericBaseStorePath;
	}

    
}