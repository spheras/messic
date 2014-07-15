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

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "USER_PERMISSIONS")
public class MDOUserPermission implements
        MDO, Serializable {
    
    private static final long serialVersionUID = 8792913920714652055L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_USER_PERMISSIONS")
    @SequenceGenerator(name = "SEQ_USER_PERMISSIONS", sequenceName = "SEQ_USER_PERMISSIONS")
    @Column(name = "SID", nullable = false, unique = true)
    private Long              sid;

    @Column(name = "PERSON", nullable = false)
    private Byte permissionValue;
    
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "USER", nullable = false)
    private MDOUser user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RESOURCE", nullable = false)
    private MDOResource resource;
    
    public MDOUserPermission()
    {
        super();
    }

    public MDOUserPermission(MDOUser user, MDOResource resource, Byte permissionValue)
    {
        this.user = user;
        this.resource = resource;
        this.permissionValue = permissionValue;
    }
    
    public Byte getPermissionValue() {
        return permissionValue;
    }

    public void setPermissionValue(Byte permissionValue) {
        this.permissionValue = permissionValue;
    }

    public MDOUser getUser() {
        return user;
    }

    public void setUser(MDOUser user) {
        this.user = user;
    }

    public MDOResource getResource() {
        return resource;
    }

    public void setResource(MDOResource resource) {
        this.resource = resource;
    }

    public Long getSid() {
        return sid;
    }

    public void setSid(Long sid) {
        this.sid = sid;
    }

        
}