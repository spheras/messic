package org.messic.server.api;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.messic.server.Util;
import org.messic.server.api.datamodel.Album;
import org.messic.server.api.datamodel.Song;
import org.messic.server.api.datamodel.User;
import org.messic.server.api.exceptions.ResourceNotFoundMessicException;
import org.messic.server.api.exceptions.SidNotFoundMessicException;
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
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class APIAlbum {
	@Autowired
	private DAOUser daoUsers;
    @Autowired
    private DAOMessicSettings daoSettings;
    @Autowired
    private DAOAlbum daoAlbum;
    @Autowired
    private DAOAuthor daoAuthor;
    @Autowired
    private DAOGenre daoGenre;
    @Autowired
    private DAOSong daoSong;
    @Autowired
    private DAOPhysicalResource daoPhysicalResource;
    
    
	@Transactional
    public List<Album> getAll(User user, boolean authorInfo, boolean songsInfo){
    	List<MDOAlbum> albums=daoAlbum.getAll(user.getLogin());
		return Album.transform(albums,authorInfo,songsInfo);
	}

	@Transactional
    public List<Album> getAll(User user, long authorSid, boolean authorInfo, boolean songsInfo){
    	List<MDOAlbum> albums=daoAlbum.getAll(authorSid, user.getLogin());
		return Album.transform(albums,authorInfo,songsInfo);
	}

	@Transactional
    public Album getAlbum(User user, long albumSid, boolean authorInfo, boolean songsInfo){
    	MDOAlbum album=daoAlbum.getAlbum(albumSid, user.getLogin());
		return Album.transform(album,authorInfo,songsInfo);
	}

	@Transactional
    public List<Album> findSimilar(User user, String albumName, boolean authorInfo,boolean songsInfo){
    	List<MDOAlbum> albums=daoAlbum.findSimilarAlbums(albumName, user.getLogin());
		return Album.transform(albums,authorInfo,songsInfo);
    }

	@Transactional
    public List<Album> findSimilar(User user, int authorSid, String albumName, boolean authorInfo, boolean songsInfo){
    	List<MDOAlbum> albums=daoAlbum.findSimilarAlbums(authorSid, albumName, user.getLogin());
		return Album.transform(albums,authorInfo,songsInfo);
    }
	
	/**
	 * Reset the temporal folder.  It removes all the temporal files. if albumCode exists, remove only these temporal files for the code album, if not, remove everything.
	 * This is useful when the user wants to upload new songs
	 * @param albumCode String code for the album to reset
	 * @throws IOException 
	 */
    public void clearTemporal(User user, String albumCode) throws IOException{
    	File basePath=null;
    	if(albumCode!=null && albumCode.length()>0){
            basePath=new File(Util.getTmpPath(user, daoSettings.getSettings(),albumCode));
    	}else{
            basePath=new File(Util.getTmpPath(user, daoSettings.getSettings(),""));
    	}
            
        if(basePath.exists()){
            FileUtils.deleteDirectory(basePath);
        }
        
        basePath.mkdirs();
	}


	/**
	 * Add a resource to the temporal folder.  This is necessary to do after things like, wizard, or create album, .. 
	 * @param albumCode {@link String} album code for the resources to upload
	 * @param fileName String file name uploaded
	 * @param payload byte[] bytes of the track
	 * @throws IOException 
	 * @throws Exception
	 */
	public void uploadResource(User user, String albumCode, String fileName, byte[] payload) throws IOException {
		File basePath=new File(Util.getTmpPath(user, daoSettings.getSettings(), albumCode));
        basePath.mkdirs();

        /*
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(new File(basePath.getAbsolutePath() + File.separatorChar + ".index"), true)));
        	out.println(resourceCode+":[#]:"+fileName);
        out.close();
        */
        
        FileOutputStream fos=new FileOutputStream(new File(basePath.getAbsolutePath() + File.separatorChar + fileName));
        fos.write(payload);
        fos.close();
	}	

	public byte[] getAlbumCover(User user, Long albumSid) throws SidNotFoundMessicException, ResourceNotFoundMessicException, IOException{
		MDOAlbum album=daoAlbum.get(albumSid);
		if(album!=null){
			MDOAlbumResource resource=album.getCover();
			if(resource!=null){
				String basePath=Util.getRealBaseStorePath(user, daoSettings.getSettings());
				File ftor=new File(basePath+File.separatorChar+resource.getRelativeLocation());
				if(ftor.exists()){
					return Util.readFile(basePath+File.separatorChar+resource.getRelativeLocation());
				}else{
					throw new ResourceNotFoundMessicException(basePath+File.separatorChar+resource.getRelativeLocation());
				}
			}
		}else{
			throw new SidNotFoundMessicException(); 
		}
		
		return null;
	}

	public void createOrUpdateAlbum(User user, Album album) throws IOException {

        MDOGenre mdoGenre=null;
        MDOAlbum mdoAlbum=null;
        MDOUser mdouser = daoUsers.get(user.getSid());
    	
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
        	mdoAuthor.setLocation(Util.getValidLocation(album.getAuthor().getName()));
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
    	String basePath=Util.getRealBaseStorePath(user, daoSettings.getSettings());
    	String authorBasePath=basePath+File.separatorChar+mdoAuthor.getLocation();
    	String albumBasePath=authorBasePath+File.separatorChar+mdoAlbum.getLocation();
    	File albumDir=new File(albumBasePath);
    	albumDir.mkdirs();
    	
        if(album.getSongs()!=null && album.getSongs().size()>0){
        	List<Song> songs=album.getSongs();
        	for (Song song : songs) {
				MDOSong mdoSong=new MDOSong();
				mdoSong.setName(song.getName());
				mdoSong.setLocation(Util.getValidLocation(Util.leftZeroPadding(song.getTrack(),2)+"-"+song.getName()));
				mdoSong.setOwner(mdouser);
				mdoSong.setTrack(song.getTrack());
				mdoSong.setAlbum(mdoAlbum);
				daoSong.save(mdoSong);
				
				//moving resource to the new location
				String tmpPath=Util.getTmpPath(user, daoSettings.getSettings(), album.getCode());
				File tmpRes=new File(tmpPath+File.separatorChar+song.getFileName());
				File newFile=new File(albumBasePath+File.separatorChar+mdoSong.getLocation());
				if(newFile.exists()){
					newFile.delete();
				}
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
				
				if(file.getFileName().equals(album.getCover().getFileName())){
					MDOAlbumResource mdopr2=new MDOAlbumResource();
					mdopr2.setLocation(Util.getValidLocation(file.getFileName()));
					mdopr2.setOwner(mdouser);
					mdopr2.setAlbum(mdoAlbum);
					daoPhysicalResource.save(mdopr2);
					mdoAlbum.setCover(mdopr2);
				}
				
				//moving resource to the new location
				String tmpPath=Util.getTmpPath(user, daoSettings.getSettings(), album.getCode());
				File tmpRes=new File(tmpPath+File.separatorChar+file.getFileName());
				File newFile=new File(albumDir.getAbsolutePath()+File.separatorChar+file.getFileName());
				if(newFile.exists()){
					newFile.delete();
				}
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
				String tmpPath=Util.getTmpPath(user, daoSettings.getSettings(), album.getCode());
				File tmpRes=new File(tmpPath+File.separatorChar+file.getFileName());
				File newFile=new File(albumDir.getAbsolutePath()+File.separatorChar+file.getFileName());
				if(newFile.exists()){
					newFile.delete();
				}
				FileUtils.moveFileToDirectory(tmpRes, albumDir , false);
			}
        	
        	daoAlbum.save(mdoAlbum);
        }
	}

}
