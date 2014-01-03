package org.messic.server.api;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.messic.server.Util;
import org.messic.server.api.datamodel.Album;
import org.messic.server.api.datamodel.Song;
import org.messic.server.datamodel.MDOAlbum;
import org.messic.server.datamodel.MDOAlbumResource;
import org.messic.server.datamodel.MDOAuthor;
import org.messic.server.datamodel.MDOGenre;
import org.messic.server.datamodel.MDOSong;
import org.messic.server.datamodel.MDOUser;
import org.messic.server.datamodel.dao.DAOAlbum;
import org.messic.server.datamodel.dao.DAOAuthor;
import org.messic.server.datamodel.dao.DAOGenre;
import org.messic.server.datamodel.dao.DAOMessicSettings;
import org.messic.server.datamodel.dao.DAOPhysicalResource;
import org.messic.server.datamodel.dao.DAOSong;
import org.messic.server.datamodel.dao.DAOUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class APIAlbum {
    @Autowired
    private DAOMessicSettings daoSettings;
    @Autowired
    private DAOAlbum daoAlbum;
    @Autowired
    private DAOAuthor daoAuthor;
    @Autowired
    private DAOGenre daoGenre;
    @Autowired
    private DAOUser daoUser;
    @Autowired
    private DAOSong daoSong;
    @Autowired
    private DAOPhysicalResource daoPhysicalResource;
    
    
	@Transactional
    public List<Album> getAll(MDOUser user){
    	List<MDOAlbum> albums=daoAlbum.getAll(user.getLogin());
		return Album.transform(albums,false);
	}

	@Transactional
    public List<Album> getAll(MDOUser user, int authorSid){
    	List<MDOAlbum> albums=daoAlbum.getAll(authorSid, user.getLogin());
		return Album.transform(albums,false);
	}

	@Transactional
    public List<Album> findSimilar(MDOUser user, String albumName){
    	List<MDOAlbum> albums=daoAlbum.findSimilarAlbums(albumName, user.getLogin());
		return Album.transform(albums,false);
    }

	@Transactional
    public List<Album> findSimilar(MDOUser user, int authorSid, String albumName){
    	List<MDOAlbum> albums=daoAlbum.findSimilarAlbums(authorSid, albumName, user.getLogin());
		return Album.transform(albums,false);
    }
	
	/**
	 * Reset the temporal folder.  It removes all the temporal files. 
	 * This is useful when the user wants to upload new songs
	 * @param albumCode String code for the album to reset
	 * @throws Exception
	 */
    public void resetUploaded(String albumCode) throws Exception{
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MDOUser mdouser=daoUser.getUser(auth.getName());
        File basePath=new File(Util.getTmpPath(mdouser, daoSettings.getSettings(),albumCode));
        
        if(basePath.exists()){
            FileUtils.deleteDirectory(basePath);
        }
        
        basePath.mkdirs();
	}


	/**
	 * Add a resource to the temporal folder.  This is necessary to do after things like, wizard, or create album, .. 
	 * @param albumCode {@link String} album code for the resources to upload
	 * @param resourceCode String code for the filename, the way it will be referenced in the future
	 * @param fileName String file name uploaded
	 * @param payload byte[] bytes of the track
	 * @throws Exception
	 */
	public void uploadResource(String albumCode, String resourceCode, String fileName, byte[] payload) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MDOUser mdouser=daoUser.getUser(auth.getName());
        File basePath=new File(Util.getTmpPath(mdouser, daoSettings.getSettings(), albumCode));
        basePath.mkdirs();

        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(new File(basePath.getAbsolutePath() + File.separatorChar + ".index"), true)));
        	out.println(resourceCode+":[#]:"+fileName);
        out.close();
        
        FileOutputStream fos=new FileOutputStream(new File(basePath.getAbsolutePath() + File.separatorChar + fileName));
        fos.write(payload);
        fos.close();
	}	


	public void saveAlbum(Album album) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MDOUser mdouser=daoUser.getUser(auth.getName());

        MDOGenre mdoGenre=null;
        MDOAlbum mdoAlbum=null;
        if(album.getGenre()!=null && album.getGenre().getSid()!=null){
        	mdoGenre=daoGenre.get(album.getGenre().getSid());
        }
        if(mdoGenre==null){
        	if(album.getGenre()!=null && album.getGenre().getName()!=null){
            	mdoGenre=daoGenre.getByName(album.getGenre().getName());
        	}
        }
        if(mdoGenre==null && album.getGenre()!=null && album.getGenre().getName()!=null){
        	mdoGenre=new MDOGenre(album.getGenre().getName());
        }
        MDOAuthor mdoAuthor=daoAuthor.getByName(album.getAuthor().getName(), mdouser.getLogin());
        if(mdoAuthor!=null){
        	mdoAlbum=daoAlbum.getByName(mdoAuthor.getName(), album.getName(), mdouser.getLogin());
        }
        if(mdoAuthor==null){
        	mdoAuthor=new MDOAuthor();
        	mdoAuthor.setName(album.getAuthor().getName());
        	mdoAuthor.setOwner(mdouser);
        	mdoAuthor.setLocation(Util.getValidLocation(album.getAuthor().getName())); //TODO
        }
        if(mdoAlbum==null){
        	mdoAlbum=new MDOAlbum();
        	//TODO if the location changes, and the album exist previously, we must move the content to the new location
        	mdoAlbum.setLocation(Util.getValidLocation(album.getName()));
        }
        mdoAlbum.setName(album.getName());
    	mdoAlbum.setAuthor(mdoAuthor);
    	mdoAlbum.setComments(album.getComments());
    	mdoAlbum.setGenre(mdoGenre);
    	mdoAlbum.setOwner(mdouser);
    	mdoAlbum.setYear(album.getYear());

    	daoAuthor.save(mdoAuthor);
    	daoGenre.save(mdoGenre);
    	daoAlbum.save(mdoAlbum);
    	
    	//let's create the disc space
    	String basePath=Util.getRealBaseStorePath(mdouser, daoSettings.getSettings());
    	String authorBasePath=basePath+File.separatorChar+mdoAuthor.getLocation();
    	String albumBasePath=authorBasePath+File.separatorChar+mdoAlbum.getLocation();
    	File albumDir=new File(albumBasePath);
    	albumDir.mkdirs();
    	
        if(album.getSongs()!=null && album.getSongs().size()>0){
        	List<Song> songs=album.getSongs();
        	for (Song song : songs) {
				MDOSong mdoSong=new MDOSong();
				mdoSong.setName(song.getName());
				mdoSong.setLocation(Util.getValidLocation(song.getFileName()));
				mdoSong.setOwner(mdouser);
				mdoSong.setTrack(song.getTrack());
				mdoSong.setAlbum(mdoAlbum);
				daoSong.save(mdoSong);
				
				//moving resource to the new location
				String tmpPath=Util.getTmpPath(mdouser, daoSettings.getSettings(), album.getCode());
				File tmpRes=new File(tmpPath+File.separatorChar+song.getFileName());
				File newFile=new File(albumBasePath+File.separatorChar+Util.getValidLocation(Util.leftZeroPadding(song.getTrack(),2)+"-"+song.getName()));
				FileUtils.moveFile(tmpRes, newFile);
			}
        }
        if(album.getArtworks()!=null && album.getArtworks().size()>0){
        	List<org.messic.server.api.datamodel.File> files=album.getArtworks();
        	for (org.messic.server.api.datamodel.File file : files) {
				MDOAlbumResource mdopr=new MDOAlbumResource();
				mdopr.setLocation(Util.getValidLocation(file.getFileName()));
				mdopr.setOwner(mdouser);
				mdopr.setAlbum(mdoAlbum);
				daoPhysicalResource.save(mdopr);
				mdoAlbum.getArtworks().add(mdopr);
				
				//moving resource to the new location
				String tmpPath=Util.getTmpPath(mdouser, daoSettings.getSettings(), album.getCode());
				File tmpRes=new File(tmpPath+File.separatorChar+file.getFileName());
				FileUtils.moveFileToDirectory(tmpRes, albumDir , false);
			}
        	daoAlbum.save(mdoAlbum);
        }
        if(album.getOthers()!=null && album.getOthers().size()>0){
        	List<org.messic.server.api.datamodel.File> files=album.getOthers();
        	for (org.messic.server.api.datamodel.File file : files) {
        		MDOAlbumResource mdopr=new MDOAlbumResource();
				mdopr.setLocation(Util.getValidLocation(file.getFileName()));
				mdopr.setOwner(mdouser);
				mdopr.setAlbum(mdoAlbum);
				daoPhysicalResource.save(mdopr);
				mdoAlbum.getOthers().add(mdopr);
				
				//moving resource to the new location
				String tmpPath=Util.getTmpPath(mdouser, daoSettings.getSettings(), album.getCode());
				File tmpRes=new File(tmpPath+File.separatorChar+file.getFileName());
				FileUtils.moveFileToDirectory(tmpRes, albumDir , false);
			}
        	
        	daoAlbum.save(mdoAlbum);
        }

        
	}

}
