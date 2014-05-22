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
		return createSettings(System.getProperty("user.home")+File.separatorChar+"messic-data");
	}

	@Override
	public MDOMessicSettings setSettings(Long sid, String genericBaseStorePath) {
		MDOMessicSettings settings = entityManager.getReference(MDOMessicSettings.class, sid);
		settings.setGenericBaseStorePath(genericBaseStorePath);	
		entityManager.persist(settings);
        return settings;
	}

	@Override
	public MDOMessicSettings createSettings(String genericBaseStorePath) {
		MDOMessicSettings newSettings = new MDOMessicSettings(genericBaseStorePath);
		entityManager.persist(newSettings);
		return newSettings;
	}


}
