package org.messic.server.api;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.messic.server.Util;
import org.messic.server.api.datamodel.Album;
import org.messic.server.api.datamodel.Song;
import org.messic.server.api.exceptions.ResourceNotFoundMessicException;
import org.messic.server.api.exceptions.SidNotFoundMessicException;
import org.messic.server.datamodel.MDOAlbum;
import org.messic.server.datamodel.MDOAlbumResource;
import org.messic.server.datamodel.MDOArtwork;
import org.messic.server.datamodel.MDOAuthor;
import org.messic.server.datamodel.MDOGenre;
import org.messic.server.datamodel.MDOOtherResource;
import org.messic.server.datamodel.MDOSong;
import org.messic.server.datamodel.MDOUser;
import org.messic.server.datamodel.dao.DAOAlbum;
import org.messic.server.datamodel.dao.DAOAlbumResource;
import org.messic.server.datamodel.dao.DAOAuthor;
import org.messic.server.datamodel.dao.DAOGenre;
import org.messic.server.datamodel.dao.DAOMessicSettings;
import org.messic.server.datamodel.dao.DAOPhysicalResource;
import org.messic.server.datamodel.dao.DAOSong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class APIAlbum
{
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

    @Autowired
    private DAOAlbumResource daoAlbumResource;

    @Transactional
    public void getAlbumZip( MDOUser user, Long albumSid, OutputStream os )
        throws IOException
    {

        MDOAlbum album = this.daoAlbum.getAlbum( albumSid, user.getLogin() );
        if ( album != null )
        {
            String basePath = Util.getRealBaseStorePath( user, daoSettings.getSettings() );

            Util.zipFolder( basePath + File.separatorChar + album.getAbsolutePath(), os );

            // List<MDOSong> songs=album.getSongs();
            // List<MDOArtwork> artworks=album.getArtworks();
            // List<MDOOtherResource> others=album.getOthers();
            //
            // ArrayList<File> files=new ArrayList<File>();
            //
            // for(int i=0;i<songs.size();i++){
            // MDOSong song=songs.get( i );
            // String filePath=basePath+File.separatorChar+song.getAbsolutePath();
            // File f=new File(filePath);
            // if(f.exists()){
            // files.add(f);
            // }
            // }
            // for(int i=0;i<artworks.size();i++){
            // MDOArtwork artwork=artworks.get( i );
            // String filePath=basePath+File.separatorChar+artwork.getAbsolutePath();
            // File f=new File(filePath);
            // if(f.exists()){
            // files.add(f);
            // }
            // }
            // for(int i=0;i<others.size();i++){
            // MDOOtherResource other=others.get( i );
            // String filePath=basePath+File.separatorChar+other.getAbsolutePath();
            // File f=new File(filePath);
            // if(f.exists()){
            // files.add(f);
            // }
            // }
            //
            // Util.zipFiles( files, os );
        }
        else
        {
            throw new IOException( "Album not found!" );
        }
    }

    @Transactional
    public void remove( MDOUser user, Long albumSid )
        throws IOException
    {
        MDOAlbum album = this.daoAlbum.getAlbum( albumSid, user.getLogin() );
        if ( album != null )
        {
            if ( album.getAuthor().getAlbums().size() <= 1 )
            {
                // first, removing the author folder
                MDOAuthor author = album.getAuthor();
                String path = Util.getRealBaseStorePath( user, daoSettings.getSettings() );
                path = path + File.separatorChar + author.getLocation();
                File fpath = new File( path );
                FileUtils.deleteDirectory( fpath );
                // after, removing the author data from database
                daoAuthor.remove( album.getAuthor() );
            }
            else
            {
                // first, removing the album folder
                MDOAuthor author = album.getAuthor();
                String path = Util.getRealBaseStorePath( user, daoSettings.getSettings() );
                path = path + File.separatorChar + author.getLocation() + File.separatorChar + album.getLocation();
                File fpath = new File( path );
                FileUtils.deleteDirectory( fpath );
                // after, removing the album data from database
                this.daoAlbum.remove( album );
            }
        }
    }

    @Transactional
    public List<Album> getAll( MDOUser user, boolean authorInfo, boolean songsInfo, boolean resourcesInfo )
    {
        List<MDOAlbum> albums = this.daoAlbum.getAll( user.getLogin() );
        return Album.transform( albums, authorInfo, songsInfo, resourcesInfo );
    }

    @Transactional
    public List<Album> getAll( MDOUser user, long authorSid, boolean authorInfo, boolean songsInfo,
                               boolean resourcesInfo )
    {
        List<MDOAlbum> albums = daoAlbum.getAll( authorSid, user.getLogin() );
        return Album.transform( albums, authorInfo, songsInfo, resourcesInfo );
    }

    @Transactional
    public Album getAlbum( MDOUser user, long albumSid, boolean authorInfo, boolean songsInfo, boolean resourcesInfo )
    {
        MDOAlbum album = daoAlbum.getAlbum( albumSid, user.getLogin() );
        return Album.transform( album, authorInfo, songsInfo, resourcesInfo );
    }

    @Transactional
    public List<Album> findSimilar( MDOUser user, String albumName, boolean authorInfo, boolean songsInfo,
                                    boolean resourcesInfo )
    {
        List<MDOAlbum> albums = daoAlbum.findSimilarAlbums( albumName, user.getLogin() );
        return Album.transform( albums, authorInfo, songsInfo, resourcesInfo );
    }

    @Transactional
    public List<Album> findSimilar( MDOUser user, int authorSid, String albumName, boolean authorInfo,
                                    boolean songsInfo, boolean resourcesInfo )
    {
        List<MDOAlbum> albums = daoAlbum.findSimilarAlbums( authorSid, albumName, user.getLogin() );
        return Album.transform( albums, authorInfo, songsInfo, resourcesInfo );
    }

    /**
     * Reset the temporal folder. It removes all the temporal files. if albumCode exists, remove only these temporal
     * files for the code album, if not, remove everything. This is useful when the user wants to upload new songs
     * 
     * @param albumCode String code for the album to reset
     * @param exceptionFiles list of files that we don't want to remove
     * @return List<File/> the list of files that are still at the temporal folder (based on the exceptionfiles parameter)
     * @throws IOException
     */
    public List<org.messic.server.api.datamodel.File> clearTemporal( MDOUser mdouser, String albumCode, List<org.messic.server.api.datamodel.File> exceptionFiles)
        throws IOException
    {
        File basePath = null;
        if ( albumCode != null && albumCode.length() > 0 )
        {
            basePath = new File( Util.getTmpPath( mdouser, daoSettings.getSettings(), albumCode ) );
        }
        else
        {
            basePath = new File( Util.getTmpPath( mdouser, daoSettings.getSettings(), "" ) );
        }

        if ( basePath.exists() && (exceptionFiles==null || exceptionFiles.size()==0))
        {
            FileUtils.deleteDirectory( basePath );
        }else if(basePath.exists() && exceptionFiles!=null){
            deleteFiles( basePath.getAbsolutePath(), exceptionFiles );
        }

        basePath.mkdirs();
        
        ArrayList<File> existingFiles=new ArrayList<File>();
        Util.listFiles( basePath.getAbsolutePath(), existingFiles );
 
        ArrayList<org.messic.server.api.datamodel.File> result=new ArrayList<org.messic.server.api.datamodel.File>();
        for (int i=0;i<existingFiles.size();i++){
            org.messic.server.api.datamodel.File f=new org.messic.server.api.datamodel.File(  );
            f.setFileName( existingFiles.get( i ).getName());
            f.setSize( existingFiles.get( i ).length() );
            result.add(f);
        }
        return result;
    }
    

    /**
     * Check if a certainFile is an exceptionFile, it means, a file that we shouldn't remove.
     * @param f {@link File} file to check
     * @param exceptionFiles {@link List}<File/> List of exceptionFiles
     * @return boolean true->yes, its an exception file  ,  false->No, it isn't an exception file
     */
    private boolean isAnExceptionFile(File f, List<org.messic.server.api.datamodel.File> exceptionFiles){
        if(exceptionFiles==null || exceptionFiles.size()==0){
            return false;
        }
        for(int i=0;i<exceptionFiles.size();i++){
            if(f.getName().equals( exceptionFiles.get( i ).getFileName() )){
                if(f.length()==exceptionFiles.get( i ).getSize()){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Delete all the files of a certain path, and subpaths, except those files in the list of the parameter exceptionFiles 
     * @param basePath {@link String} basePath to start searching
     * @param exceptionFiles List<File/> Black list we don't want to remove
     */
    private void deleteFiles(String basePath, List<org.messic.server.api.datamodel.File> exceptionFiles){
        File directory = new File(basePath);

        // get all the files from a directory
        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isFile()) {
                if(!isAnExceptionFile( file, exceptionFiles )){
                    file.delete();
                }
            } else if (file.isDirectory()) {
                deleteFiles(file.getAbsolutePath(), exceptionFiles);
            }
        }        
    }
    
    /**
     * Add a resource to the temporal folder. This is necessary to do after things like, wizard, or create album, ..
     * 
     * @param albumCode {@link String} album code for the resources to upload
     * @param fileName String file name uploaded
     * @param payload byte[] bytes of the track
     * @throws IOException
     * @throws Exception
     */
    public void uploadResource( MDOUser mdouser, String albumCode, String fileName, byte[] payload )
        throws IOException
    {
        File basePath = new File( Util.getTmpPath( mdouser, daoSettings.getSettings(), albumCode ) );
        basePath.mkdirs();

        FileOutputStream fos =
            new FileOutputStream( new File( basePath.getAbsolutePath() + File.separatorChar + fileName ) );
        fos.write( payload );
        fos.close();
    }

    public byte[] getAlbumResource( MDOUser mdouser, Long resourceSid )
        throws SidNotFoundMessicException, ResourceNotFoundMessicException, IOException
    {
        MDOAlbumResource resource = daoAlbumResource.get( resourceSid );
        if ( resource != null )
        {
            String basePath = Util.getRealBaseStorePath( mdouser, daoSettings.getSettings() );
            File ftor = new File( basePath + File.separatorChar + resource.getRelativeLocation() );
            if ( ftor.exists() )
            {
                return Util.readFile( basePath + File.separatorChar + resource.getRelativeLocation() );
            }
            else
            {
                throw new ResourceNotFoundMessicException( basePath + File.separatorChar
                    + resource.getRelativeLocation() );
            }
        }
        else
        {
            throw new SidNotFoundMessicException();
        }
    }

    public byte[] getAlbumCover( MDOUser mdouser, Long albumSid )
        throws SidNotFoundMessicException, ResourceNotFoundMessicException, IOException
    {
        MDOAlbumResource cover = daoAlbum.getAlbumCover( albumSid, mdouser.getLogin() );
        if ( cover != null )
        {
            String basePath = Util.getRealBaseStorePath( mdouser, daoSettings.getSettings() );
            File ftor = new File( basePath + File.separatorChar + cover.getRelativeLocation() );
            if ( ftor.exists() )
            {
                return Util.readFile( basePath + File.separatorChar + cover.getRelativeLocation() );
            }
            else
            {
                throw new ResourceNotFoundMessicException( basePath + File.separatorChar + cover.getRelativeLocation() );
            }
        }
        else
        {
            throw new ResourceNotFoundMessicException( "" + albumSid );
        }
    }

    public void createOrUpdateAlbum( MDOUser mdouser, Album album )
        throws IOException
    {
        MDOGenre mdoGenre = null;
        MDOAlbum mdoAlbum = null;
        MDOAuthor mdoAuthor = null;

        // 1st getting genre
        if ( album.getGenre() != null && album.getGenre().getSid() != null )
        {
            mdoGenre = daoGenre.get( album.getGenre().getSid() );
        }
        if ( mdoGenre == null )
        {
            if ( album.getGenre() != null && album.getGenre().getName() != null )
            {
                mdoGenre = daoGenre.getByName( album.getGenre().getName() );
            }
        }
        if ( mdoGenre == null && album.getGenre() != null && album.getGenre().getName() != null )
        {
            mdoGenre = new MDOGenre( album.getGenre().getName() );
        }

        // 2nd getting the album if exist
        if ( album.getSid() > 0 )
        {
            mdoAlbum = daoAlbum.get( album.getSid() );
        }

        // 3rd getting the author
        if ( album.getAuthor().getSid() > 0 )
        {
            // trying by sid
            mdoAuthor = daoAuthor.get( mdouser.getLogin(), album.getAuthor().getSid() );
        }
        if ( mdoAuthor == null )
        {
            // trying by name
            mdoAuthor = daoAuthor.getByName( album.getAuthor().getName(), mdouser.getLogin() );
        }
        if ( mdoAuthor != null )
        {
            // an existing album from this autor??
            if ( mdoAlbum == null )
            {
                mdoAlbum = daoAlbum.getByName( mdoAuthor.getName(), album.getName(), mdouser.getLogin() );
            }
        }
        // let's create a new author
        if ( mdoAuthor == null )
        {
            mdoAuthor = new MDOAuthor();
            mdoAuthor.setName( album.getAuthor().getName() );
            mdoAuthor.setOwner( mdouser );
            mdoAuthor.setLocation( Util.getValidLocation( album.getAuthor().getName() ) );
        }
        // 4th new album if none
        if ( mdoAlbum == null )
        {
            mdoAlbum = new MDOAlbum();
        }

        boolean flagExistingAlbum = ( mdoAlbum.getSid() != null && mdoAlbum.getSid() > 0 );
        String oldPath = null;
        if ( flagExistingAlbum )
        {
            oldPath = mdoAlbum.getAbsolutePath();
        }

        // updating / creating the album
        mdoAlbum.setName( album.getName() );
        mdoAlbum.setLocation( Util.getValidLocation( album.getName() ) );
        mdoAlbum.setAuthor( mdoAuthor );
        mdoAlbum.setComments( album.getComments() );
        mdoAlbum.setGenre( mdoGenre );
        mdoAlbum.setOwner( mdouser );
        mdoAlbum.setYear( album.getYear() );

        daoAuthor.save( mdoAuthor );
        daoGenre.save( mdoGenre );
        daoAlbum.save( mdoAlbum );

        // let's create the disc space
        String basePath = Util.getRealBaseStorePath( mdouser, daoSettings.getSettings() );
        String authorBasePath = basePath + File.separatorChar + mdoAuthor.getLocation();
        String albumBasePath = authorBasePath + File.separatorChar + mdoAlbum.getLocation();
        File albumDir = new File( albumBasePath );
        albumDir.mkdirs();

        if ( album.getSongs() != null && album.getSongs().size() > 0 )
        {
            List<Song> songs = album.getSongs();
            for ( Song song : songs )
            {
                if ( song.getSid() <= 0 )
                {
                    MDOSong mdoSong = new MDOSong();
                    mdoSong.setTrack( song.getTrack() );
                    mdoSong.setName( song.getName() );
                    mdoSong.setLocation( Util.getSongTheoricalFileName( mdoSong ) );
                    mdoSong.setOwner( mdouser );
                    mdoSong.setAlbum( mdoAlbum );
                    daoSong.save( mdoSong );

                    // moving resource to the new location
                    String tmpPath = Util.getTmpPath( mdouser, daoSettings.getSettings(), album.getCode() );
                    File tmpRes = new File( tmpPath + File.separatorChar + song.getFileName() );
                    File newFile = new File( albumBasePath + File.separatorChar + mdoSong.getLocation() );
                    if ( newFile.exists() )
                    {
                        newFile.delete();
                    }
                    FileUtils.moveFile( tmpRes, newFile );
                }
                else
                {
                    // existing song...
                    MDOSong mdoSong = daoSong.get( mdouser.getLogin(), song.getSid() );
                    if ( mdoSong != null )
                    {
                        mdoSong.setTrack( song.getTrack() );
                        mdoSong.setName( song.getName() );
                        String oldLocation = mdoSong.getLocation();
                        mdoSong.setLocation( Util.getSongTheoricalFileName( mdoSong ) );
                        daoSong.save( mdoSong );

                        File fold = new File( albumBasePath + File.separatorChar + oldLocation );
                        File fnew = new File( albumBasePath + File.separatorChar + mdoSong.getLocation() );
                        if ( !fold.getAbsolutePath().equals( fnew.getAbsolutePath() ) )
                        {
                            FileUtils.moveFile( fold, fnew );
                        }

                        // TODO change tags
                    }
                }
            }
        }
        if ( album.getArtworks() != null && album.getArtworks().size() > 0 )
        {
            List<org.messic.server.api.datamodel.File> files = album.getArtworks();
            for ( org.messic.server.api.datamodel.File file : files )
            {
                if ( file.getSid() <= 0 )
                {
                    MDOArtwork mdopr = new MDOArtwork();
                    mdopr.setLocation( Util.getValidLocation( file.getFileName() ) );
                    mdopr.setOwner( mdouser );
                    mdopr.setAlbum( mdoAlbum );

                    org.messic.server.api.datamodel.File fcover = album.getCover();
                    if ( fcover != null && file.getFileName().equals( album.getCover().getFileName() ) )
                    {
                        mdopr.setCover( true );
                    }

                    daoPhysicalResource.save( mdopr );
                    mdoAlbum.getArtworks().add( mdopr );

                    // moving resource to the new location
                    String tmpPath = Util.getTmpPath( mdouser, daoSettings.getSettings(), album.getCode() );
                    File tmpRes = new File( tmpPath + File.separatorChar + file.getFileName() );
                    File newFile = new File( albumDir.getAbsolutePath() + File.separatorChar + file.getFileName() );
                    if ( newFile.exists() )
                    {
                        newFile.delete();
                    }
                    FileUtils.moveFileToDirectory( tmpRes, albumDir, false );
                }
                else
                {
                    // existing artwork...
                    MDOAlbumResource resource = daoAlbumResource.get( mdouser.getLogin(), file.getSid() );
                    if ( resource != null )
                    {
                        String oldLocation = resource.getLocation();
                        resource.setLocation( file.getFileName() );
                        daoAlbumResource.save( resource );

                        File fold = new File( albumBasePath + File.separatorChar + oldLocation );
                        File fnew = new File( albumBasePath + File.separatorChar + resource.getLocation() );
                        if ( !fold.getAbsolutePath().equals( fnew.getAbsolutePath() ) )
                        {
                            FileUtils.moveFile( fold, fnew );
                        }
                    }
                }
            }
            daoAlbum.save( mdoAlbum );
        }
        if ( album.getOthers() != null && album.getOthers().size() > 0 )
        {
            List<org.messic.server.api.datamodel.File> files = album.getOthers();
            for ( org.messic.server.api.datamodel.File file : files )
            {
                if ( file.getSid() <= 0 )
                {
                    MDOOtherResource mdopr = new MDOOtherResource();
                    mdopr.setLocation( Util.getValidLocation( file.getFileName() ) );
                    mdopr.setOwner( mdouser );
                    mdopr.setAlbum( mdoAlbum );
                    daoPhysicalResource.save( mdopr );
                    mdoAlbum.getOthers().add( mdopr );

                    // moving resource to the new location
                    String tmpPath = Util.getTmpPath( mdouser, daoSettings.getSettings(), album.getCode() );
                    File tmpRes = new File( tmpPath + File.separatorChar + file.getFileName() );
                    File newFile =
                        new File( albumDir.getAbsolutePath() + File.separatorChar
                            + Util.getValidLocation( file.getFileName() ) );
                    if ( newFile.exists() )
                    {
                        newFile.delete();
                    }
                    FileUtils.moveFileToDirectory( tmpRes, albumDir, false );
                }
                else
                {
                    // existing artwork...
                    MDOAlbumResource resource = daoAlbumResource.get( mdouser.getLogin(), file.getSid() );
                    if ( resource != null )
                    {
                        String oldLocation = resource.getLocation();
                        resource.setLocation( file.getFileName() );
                        daoAlbumResource.save( resource );

                        File fold = new File( albumBasePath + File.separatorChar + oldLocation );
                        File fnew = new File( albumBasePath + File.separatorChar + resource.getLocation() );
                        if ( !fold.getAbsolutePath().equals( fnew.getAbsolutePath() ) )
                        {
                            FileUtils.moveFile( fold, fnew );
                        }
                    }
                }
            }

            daoAlbum.save( mdoAlbum );
        }

        // let's see if the album was an existing one... then it should moved to the new location
        if ( oldPath != null && !oldPath.equals( mdoAlbum.getAbsolutePath() ) )
        {
            List<MDOAlbumResource> resources = mdoAlbum.getAllResources();
            for ( int i = 0; i < resources.size(); i++ )
            {
                MDOAlbumResource resource = resources.get( i );
                String resourceNewPath =
                    basePath + File.separatorChar + mdoAlbum.getAbsolutePath() + File.separatorChar
                        + resource.getLocation();
                String resourceCurrentPath =
                    basePath + File.separatorChar + oldPath + File.separatorChar + resource.getLocation();
                File fnewPath = new File( resourceNewPath );
                File foldPath = new File( resourceCurrentPath );
                if ( foldPath.exists() )
                {
                    FileUtils.moveFile( foldPath, fnewPath );
                }
            }

            File fAlbumOldPath = new File( basePath + File.separatorChar + oldPath );
            FileUtils.deleteDirectory( fAlbumOldPath );
        }
    }

}
