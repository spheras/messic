/*
 * Copyright (C) 2013 José Amuedo
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
package org.messic.server.datamodel.jpaimpl;

import java.util.List;

import javax.persistence.Query;

import org.messic.server.datamodel.MDOAuthor;
import org.messic.server.datamodel.dao.DAOAuthor;
import org.springframework.stereotype.Component;

@Component
public class DAOJPAAuthor
    extends DAOJPA<MDOAuthor>
    implements DAOAuthor
{

    public DAOJPAAuthor()
    {
        super( MDOAuthor.class );
    }

	@Override
    public List<MDOAuthor> getRandomAuthors(String username, int number){
    	Query query= entityManager.createQuery("from MDOAuthor as a where (1=1 AND a.owner.login= :userName) order by rand()");
        query.setParameter( "userName", username);
    	query.setMaxResults(5);
        @SuppressWarnings( "unchecked" )
    	List<MDOAuthor> results=query.getResultList();
        return results;
    }

	@Override
	public List<String> getFirstCharacters(String username){
	    //SELECT DISTINCT LEFT(name, 1) as letter FROM mydatabase ORDER BY letter
        Query query= entityManager.createQuery("select DISTINCT UPPER(SUBSTRING(name, 1,1)) as letter from MDOAuthor as a where (a.owner.login= :userName) order by letter");
        query.setParameter( "userName", username);
        @SuppressWarnings( "unchecked" )
        List<String> results=query.getResultList();
        return results;
	}
    
	@Override
	public List<MDOAuthor> getAll(String username) {
        Query query = entityManager.createQuery( "from MDOAuthor as a where (a.owner.login = :userName)  ORDER BY UPPER(a.name)" );
        query.setParameter( "userName", username);
        
        @SuppressWarnings( "unchecked" )
        List<MDOAuthor> results = query.getResultList();
        return results;
	}

   @Override
    public List<MDOAuthor> getAllDLNA() {
        Query query = entityManager.createQuery( "from MDOAuthor as a where (a.owner.allowDLNA = true)  ORDER BY UPPER(a.name)" );
        
        @SuppressWarnings( "unchecked" )
        List<MDOAuthor> results = query.getResultList();
        return results;
    }

	   
	@Override
	public MDOAuthor get(String username, long authorSid) {
        Query query = entityManager.createQuery( "from MDOAuthor as a where (a.owner.login = :userName) AND (a.sid = :authorSid)" );
        query.setParameter( "userName", username);
        query.setParameter( "authorSid", authorSid);
        
        @SuppressWarnings( "unchecked" )
        List<MDOAuthor> results = query.getResultList();
        if(results!=null && results.size()>0){
        	return results.get(0);
        }
        return null;
	}

	@Override
	public List<MDOAuthor> findSimilarAuthors(String authorName, boolean contains, String username) {
        Query query = entityManager.createQuery( "from MDOAuthor as a where (UPPER(a.name) LIKE :authorName) AND (a.owner.login = :userName)  ORDER BY UPPER(a.name)" );
        query.setParameter( "authorName", (contains?"%":"") + authorName.toUpperCase() + "%");
        query.setParameter( "userName", username);
        
        @SuppressWarnings( "unchecked" )
        List<MDOAuthor> results = query.getResultList();
        return results;
	}

	public MDOAuthor getByName(String authorName, String username){
        Query query = entityManager.createQuery( "from MDOAuthor as a where (a.name = :authorName) AND (a.owner.login = :userName)" );
        query.setParameter( "userName", username);
        query.setParameter( "authorName", authorName);
		
        @SuppressWarnings( "unchecked" )
        List<MDOAuthor> results = query.getResultList();
        if(results!=null && results.size()>0){
        	return results.get(0);
        }
        return null;
	}

}
