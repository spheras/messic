package org.messic.server.api;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
     * @throws IOException
     */
    public void clearTemporal( MDOUser mdouser, String albumCode )
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

        if ( basePath.exists() )
        {
            FileUtils.deleteDirectory( basePath );
        }

        basePath.mkdirs();
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
                throw new ResourceNotFoundMessicException( basePath + File.separatorChar
                    + cover.getRelativeLocation() );
            }
        }
        else
        {
            throw new ResourceNotFoundMessicException(""+albumSid);
        }
    }

    public void createOrUpdateAlbum( MDOUser mdouser, Album album )
        throws IOException
    {

        MDOGenre mdoGenre = null;
        MDOAlbum mdoAlbum = null;
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
        MDOAuthor mdoAuthor = daoAuthor.getByName( album.getAuthor().getName(), mdouser.getLogin() );
        if ( mdoAuthor != null )
        {
            mdoAlbum = daoAlbum.getByName( mdoAuthor.getName(), album.getName(), mdouser.getLogin() );
        }
        if ( mdoAuthor == null )
        {
            mdoAuthor = new MDOAuthor();
            mdoAuthor.setName( album.getAuthor().getName() );
            mdoAuthor.setOwner( mdouser );
            mdoAuthor.setLocation( Util.getValidLocation( album.getAuthor().getName() ) );
        }
        if ( mdoAlbum == null )
        {
            mdoAlbum = new MDOAlbum();
            // TODO if the location changes, and the album exist previously, we must move the content to the new
            // location
            mdoAlbum.setLocation( Util.getValidLocation( album.getName() ) );
        }
        mdoAlbum.setName( album.getName() );
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
                MDOSong mdoSong = new MDOSong();
                mdoSong.setName( song.getName() );
                mdoSong.setLocation( Util.getValidLocation( Util.leftZeroPadding( song.getTrack(), 2 ) + "-"
                    + song.getName() ) );
                mdoSong.setOwner( mdouser );
                mdoSong.setTrack( song.getTrack() );
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
        }
        if ( album.getArtworks() != null && album.getArtworks().size() > 0 )
        {
            List<org.messic.server.api.datamodel.File> files = album.getArtworks();
            for ( org.messic.server.api.datamodel.File file : files )
            {
                MDOArtwork mdopr = new MDOArtwork();
                mdopr.setLocation( Util.getValidLocation( file.getFileName() ) );
                mdopr.setOwner( mdouser );
                mdopr.setAlbum( mdoAlbum );

                if ( file.getFileName().equals( album.getCover().getFileName() ) )
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
            daoAlbum.save( mdoAlbum );
        }
        if ( album.getOthers() != null && album.getOthers().size() > 0 )
        {
            List<org.messic.server.api.datamodel.File> files = album.getOthers();
            for ( org.messic.server.api.datamodel.File file : files )
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
                File newFile = new File( albumDir.getAbsolutePath() + File.separatorChar + file.getFileName() );
                if ( newFile.exists() )
                {
                    newFile.delete();
                }
                FileUtils.moveFileToDirectory( tmpRes, albumDir, false );
            }

            daoAlbum.save( mdoAlbum );
        }
    }

}
