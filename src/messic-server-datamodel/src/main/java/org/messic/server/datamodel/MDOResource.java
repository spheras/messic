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
@Table( name = "RESOURCES" )
@Inheritance( strategy = InheritanceType.JOINED )
@DiscriminatorColumn( name = "DTYPE", discriminatorType = DiscriminatorType.STRING, length = 31 )
public class MDOResource
    implements MDO, Serializable
{

    /**
     * serializable
     */
    private static final long serialVersionUID = -2753653453731805880L;

    public static final String PHYSICAL_RESOURCE = "PHYSICAL_RESOURCE";

    public static final String PLAYLIST = "PLAYLIST";

    @Id
    @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "SEQ_RESOURCES" )
    @SequenceGenerator( name = "SEQ_RESOURCES", sequenceName = "SEQ_RESOURCES" )
    @Column( name = "SID", nullable = false, unique = true )
    private Long sid;

    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn( name = "OWNER", nullable = false )
    private MDOUser owner;

    public MDOResource()
    {

    }

    public MDOResource( MDOUser owner )
    {
        this.owner = owner;
    }

    public Long getSid()
    {
        return sid;
    }

    public void setSid( Long sid )
    {
        this.sid = sid;
    }

    public MDOUser getOwner()
    {
        return owner;
    }

    public void setOwner( MDOUser owner )
    {
        this.owner = owner;
    }

}