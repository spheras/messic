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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table( name = "GENERIC_TAG" )
public class MDOGenericTAG
    implements MDO, Serializable
{

    /**
     * 
     */
    private static final long serialVersionUID = 188923638402900613L;

    @Id
    @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "SEQ_TAGS" )
    @SequenceGenerator( name = "SEQ_TAGS", sequenceName = "SEQ_TAGS" )
    @Column( name = "SID", nullable = false, unique = true )
    private Long sid;

    @Column( name = "NAME", nullable = false )
    private String name;

    @Column( name = "VALUE", nullable = false )
    private String value;

    public MDOGenericTAG()
    {
        super();
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName( String name )
    {
        this.name = name;
    }

    /**
     * @return the sid
     */
    public Long getSid()
    {
        return sid;
    }

    /**
     * @param sid the sid to set
     */
    public void setSid( Long sid )
    {
        this.sid = sid;
    }

    /**
     * @return the value
     */
    public String getValue()
    {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue( String value )
    {
        this.value = value;
    }

}