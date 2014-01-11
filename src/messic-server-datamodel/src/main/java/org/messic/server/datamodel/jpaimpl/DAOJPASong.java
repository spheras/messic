package org.messic.server.datamodel.jpaimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Query;

import org.messic.server.datamodel.MDOSong;
import org.messic.server.datamodel.dao.DAOSong;
import org.springframework.stereotype.Component;

@Component
public class DAOJPASong
    extends DAOJPA<MDOSong>
    implements DAOSong
{

    public DAOJPASong()
    {
        super( MDOSong.class );
    }

    @Override
    public List<MDOSong> genericFind(String username, List<String> searches){
    	HashMap<Long, MDOSong> finalResult=new HashMap<Long, MDOSong>();
    	for(int i=0;i<searches.size();i++){
    		String content=searches.get(i);

    		 String sql="from MDOSong as a WHERE (a.owner.login = :userName) AND (";
        		sql=sql+"(UPPER(a.name) LIKE :what) OR ";
        		sql=sql+"(UPPER(a.album.name) LIKE :what) OR ";
        		sql=sql+"(UPPER(a.album.genre.name) LIKE :what) OR ";
                try {
                    Integer.parseInt(content);
            		sql=sql+"(a.album.year LIKE :what) OR ";
                } catch (NumberFormatException nfe) {}
        		sql=sql+"(UPPER(a.album.comments) LIKE :what) OR ";
        		sql=sql+"(UPPER(a.album.author.name) LIKE :what)";
        		sql=sql+")";

        		
                Query query = entityManager.createQuery( sql );
                query.setParameter( "userName", username);
                query.setParameter("what","%"+content.toUpperCase()+"%");
                
                @SuppressWarnings( "unchecked" )
                List<MDOSong> results = query.getResultList();
        		for (MDOSong mdoSong : results) {
    				finalResult.put(mdoSong.getSid(), mdoSong);
    			}
    		}
    	
    	ArrayList<MDOSong> result=new ArrayList<MDOSong>();
    	Iterator<MDOSong> songsit=finalResult.values().iterator();
    	while(songsit.hasNext()){
    		result.add(songsit.next());
    	}
    	return result;
    }
}
