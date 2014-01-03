package org.messic.server;

import java.io.File;

import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.messic.server.datamodel.MDOMessicSettings;
import org.messic.server.datamodel.MDOUser;

public class Util {
	
	/** variable to define that the store path is the generic one (from settings) */
	public static final String GENERIC_BASE_STORE_PATH_VAR="$(generic)";
	public static final String TEMPORAL_FOLDER=".tmp"+File.separatorChar+"resources";

	/**
	 * Fill at the left with zeros... example: leftZeroPadding("5",3)  will return "005"
	 * @param number int an integer that will be filled with zeros until complete the desired characters
	 * @param howManyChar int how many chars will have the returned value
	 * @return String filled
	 */
	public static String leftZeroPadding(int number,int howManyChar){
		return String.format("%0"+howManyChar+"d", number);		
	}
	
	/**
	 * Return a valid location to a file path
	 * @param filename {@link String} location to convert
	 * @return a valid location
	 */
	public static String getValidLocation(String location){
		return location; //TODO
	}
	
	/**
	 * Obtain  the path to the temporal folder for uploaded resources
	 * @param user {@link MDOUser} user scope
	 * @param settings {@link MDOMessicSettings} settings
	 * @param albumCode {@link String} code for the album to upload
	 * @return {@link String} temporal path for uploaded
	 */
	public static String getTmpPath(MDOUser user, MDOMessicSettings settings, String albumCode){
	        String path=Util.getRealBaseStorePath(user, settings);
	        path=path+File.separator+TEMPORAL_FOLDER+File.separatorChar+albumCode;
	        return path;
	}
	
	/**
	 * Get the real ubication for the songs of the user
	 * @param user {@link MDOUser} user for which we are asking
	 * @param settings {@link MDOMessicSettings} current settings
	 * @return {@link String} the real base store path for this user
	 */
	public static String getRealBaseStorePath(MDOUser user, MDOMessicSettings settings){
		String userBaseStorePath=user.getStorePath();
		
		if(userBaseStorePath==null || userBaseStorePath.length()==0 || userBaseStorePath.equals(GENERIC_BASE_STORE_PATH_VAR)){
			userBaseStorePath= GENERIC_BASE_STORE_PATH_VAR + File.separatorChar + user.getLogin();
		}
		
		String definitive=userBaseStorePath.replace(GENERIC_BASE_STORE_PATH_VAR, settings.getGenericBaseStorePath());
		return definitive;

	}
	
	/**
	 * Detach and initialize proxies of an hibernate entity. Doing this, the entity doesn't need any session to get the lazy fields.
	 * (it doesn't see  the internal lazy fields) -> TODO
	 * @param entity
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T initializeAndUnproxy(T entity) {
		if (entity == null) {
			throw new NullPointerException(
					"Entity passed for initialization is null");
		}

		Hibernate.initialize(entity);
		if (entity instanceof HibernateProxy) {
			entity = (T) ((HibernateProxy) entity)
					.getHibernateLazyInitializer().getImplementation();
		}
		return entity;
	}

}
