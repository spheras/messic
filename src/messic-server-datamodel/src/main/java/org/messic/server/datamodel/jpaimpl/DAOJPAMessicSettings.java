package org.messic.server.datamodel.jpaimpl;

import java.io.File;
import java.util.List;

import javax.persistence.Query;

import org.messic.server.datamodel.MDOMessicSettings;
import org.messic.server.datamodel.dao.DAOMessicSettings;
import org.springframework.stereotype.Component;

@Component
public class DAOJPAMessicSettings
    extends DAOJPA<MDOMessicSettings>
    implements DAOMessicSettings
{

    public DAOJPAMessicSettings()
    {
        super( MDOMessicSettings.class );
    }

	@Override
	public MDOMessicSettings getSettings() {
        Query query = entityManager.createQuery( "from MDOMessicSettings" );
        
        @SuppressWarnings( "unchecked" )
        List<MDOMessicSettings> results = query.getResultList();
        if(results==null || results.size()==0){
        	return createBasicSettings();
        }else{
        	return results.get(0);
        }
	}
	
	private MDOMessicSettings createBasicSettings(){
		MDOMessicSettings ms=new MDOMessicSettings();
		ms.setGenericBaseStorePath(System.getProperty("user.home")+File.separatorChar+"messic-data");
		return setSettings(ms);
	}

	@Override
	public MDOMessicSettings setSettings(MDOMessicSettings newSettings) {
		newSettings.setSid(0l);
		//save(newSettings);
		
		entityManager.merge(newSettings);
        Query query = entityManager.createQuery( "from MDOMessicSettings" );
        @SuppressWarnings( "unchecked" )
        List<MDOMessicSettings> results = query.getResultList();
    	return results.get(0);
	}


}
