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
package org.messic.server.api;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.imageio.IIOException;

import net.coobird.thumbnailator.Thumbnails;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;
import org.messic.server.Util;
import org.messic.server.api.datamodel.Album;
import org.messic.server.api.datamodel.AlbumConsistency;
import org.messic.server.api.datamodel.Song;
import org.messic.server.api.datamodel.User;
import org.messic.server.api.exceptions.CheckConsistencyMessicException;
import org.messic.server.api.exceptions.ExistingMessicException;
import org.messic.server.api.exceptions.ResourceNotFoundMessicException;
import org.messic.server.api.exceptions.SidNotFoundMessicException;
import org.messic.server.api.tagwizard.audiotagger.AudioTaggerTAGWizardPlugin;
import org.messic.server.datamodel.MDOAlbum;
import org.messic.server.datamodel.MDOAlbumResource;
import org.messic.server.datamodel.MDOArtwork;
import org.messic.server.datamodel.MDOAuthor;
import org.messic.server.datamodel.MDOGenre;
import org.messic.server.datamodel.MDOMessicSettings;
import org.messic.server.datamodel.MDOOtherResource;
import org.messic.server.datamodel.MDOSong;
import org.messic.server.datamodel.MDOSongStatistics;
import org.messic.server.datamodel.MDOUser;
import org.messic.server.datamodel.dao.DAOAlbum;
import org.messic.server.datamodel.dao.DAOAlbumResource;
import org.messic.server.datamodel.dao.DAOAuthor;
import org.messic.server.datamodel.dao.DAOGenre;
import org.messic.server.datamodel.dao.DAOMessicSettings;
import org.messic.server.datamodel.dao.DAOPhysicalResource;
import org.messic.server.datamodel.dao.DAOPlaylist;
import org.messic.server.datamodel.dao.DAOSong;
import org.messic.server.datamodel.dao.DAOSongStatistics;
import org.messic.server.datamodel.dao.DAOUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class APIAlbum
{

    public static final String INDEX_TMP_PROPERTIES_FILENAME = "index.tmp.properties";

    private Logger log = Logger.getLogger( APIAlbum.class );

    private Object indexTmpSemaphore = new Object();

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
    private DAOSongStatistics daoSongStatistics;

    @Autowired
    private DAOUser daoUser;

    @Autowired
    private DAOPlaylist daoPlaylists;

    @Autowired
    private DAOPhysicalResource daoPhysicalResource;

    @Autowired
    private DAOAlbumResource daoAlbumResource;

    @Transactional
    public void getAlbumZip( User user, Long albumSid, OutputStream os )
        throws IOException
    {

        MDOAlbum album = this.daoAlbum.getAlbum( albumSid, user.getLogin() );
        if ( album != null )
        {
            String basePath = album.calculateAbsolutePath( daoSettings.getSettings() );
            Util.zipFolder( basePath + File.separatorChar, os );
        }
        else
        {
            throw new IOException( "Album not found!" );
        }
    }

    @Transactional
    public boolean remove( User user, Long albumSid )
        throws IOException
    {

        MDOAlbum album = this.daoAlbum.getAlbum( albumSid, user.getLogin() );
        if ( album != null )
        {
            MDOAuthor author = album.getAuthor();
            if ( author.getAlbums().size() <= 1 )
            {
                File fpath = new File( author.calculateAbsolutePath( daoSettings.getSettings() ) );
                FileUtils.deleteDirectory( fpath );
                // after, removing the author data from database
                daoAuthor.remove( album.getAuthor() );
                return true;
            }
            else
            {
                // first, removing the album folder
                File fpath = new File( album.calculateAbsolutePath( daoSettings.getSettings() ) );
                FileUtils.deleteDirectory( fpath );
                // after, removing the album data from database
                this.daoAlbum.remove( album );
                return false;
            }
        }
        return false;
    }

    @Transactional
    public List<Album> getAll( MDOUser user, boolean authorInfo, boolean songsInfo, boolean resourcesInfo,
                               int fromResult, int maxResults, boolean orderDesc, boolean orderByAuthor )
    {
        List<MDOAlbum> albums =
            this.daoAlbum.getAll( user.getLogin(), fromResult, maxResults, orderDesc, orderByAuthor );
        return Album.transform( albums, authorInfo, songsInfo, resourcesInfo );
    }

    @Transactional
    public List<Album> getAll( MDOUser user, long authorSid, boolean authorInfo, boolean songsInfo,
                               boolean resourcesInfo, int fromResult, int maxResults, boolean orderDesc,
                               boolean orderByAuthor )
    {
        List<MDOAlbum> albums =
            daoAlbum.getAll( authorSid, user.getLogin(), fromResult, maxResults, orderDesc, orderByAuthor );
        return Album.transform( albums, authorInfo, songsInfo, resourcesInfo );
    }

    @Transactional
    public List<Album> getAllOfGenre( MDOUser user, long genreSid, boolean authorInfo, boolean songsInfo,
                                      boolean resourcesInfo, int fromResult, int maxResults, boolean orderDesc,
                                      boolean orderByAuthor )
    {
        List<MDOAlbum> albums =
            daoAlbum.getAllOfGenre( genreSid, user.getLogin(), fromResult, maxResults, orderDesc, orderByAuthor );
        return Album.transform( albums, authorInfo, songsInfo, resourcesInfo );
    }

    @Transactional
    public Album getAlbum( User user, long albumSid, boolean authorInfo, boolean songsInfo, boolean resourcesInfo )
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
                                    boolean songsInfo, boolean resourcesInfo, int fromResult, int maxResults,
                                    boolean orderDesc, boolean orderByAuthor )
    {
        List<MDOAlbum> albums =
            daoAlbum.findSimilarAlbums( authorSid, albumName, user.getLogin(), fromResult, maxResults, orderDesc,
                                        orderByAuthor );
        return Album.transform( albums, authorInfo, songsInfo, resourcesInfo );
    }

    /**
     * Reset the temporal folder. It removes all the temporal files. if albumCode exists, remove only these temporal
     * files for the code album, if not, remove everything. This is useful when the user wants to upload new songs
     * 
     * @param albumCode String code for the album to reset
     * @param exceptionFiles list of files that we don't want to remove
     * @return List<File/> the list of files that are still at the temporal folder (based on the exceptionfiles
     *         parameter)
     * @throws IOException
     */
    public List<org.messic.server.api.datamodel.File> clearTemporal( User user,
                                                                     String albumCode,
                                                                     List<org.messic.server.api.datamodel.File> exceptionFiles )
        throws IOException
    {
        MDOUser mdouser = daoUser.getUserByLogin( user.getLogin() );
        File basePath = null;
        if ( albumCode != null && albumCode.length() > 0 )
        {
            basePath = new File( mdouser.calculateTmpPath( daoSettings.getSettings(), albumCode ) );
        }
        else
        {
            basePath = new File( mdouser.calculateTmpPath( daoSettings.getSettings(), "" ) );
        }

        // first, removing everything, except the current albumcode folder (if exist)
        File ftmppath = new File( mdouser.calculateTmpPath( daoSettings.getSettings(), "" ) );
        if ( ftmppath.exists() )
        {
            File[] files = ftmppath.listFiles();
            for ( int i = 0; i < files.length; i++ )
            {
                if ( files[i].isDirectory() && !files[i].getName().equals( albumCode ) )
                {
                    FileUtils.deleteDirectory( files[i] );
                }
            }
        }

        if ( basePath.exists() && ( exceptionFiles == null || exceptionFiles.size() == 0 ) )
        {
            FileUtils.deleteDirectory( basePath );
        }
        else if ( basePath.exists() && exceptionFiles != null )
        {
            deleteFiles( basePath.getAbsolutePath(), exceptionFiles );
        }

        basePath.mkdirs();

        ArrayList<File> existingFiles = new ArrayList<File>();
        Util.listFiles( basePath.getAbsolutePath(), existingFiles );

        ArrayList<org.messic.server.api.datamodel.File> result = new ArrayList<org.messic.server.api.datamodel.File>();
        for ( int i = 0; i < existingFiles.size(); i++ )
        {
            org.messic.server.api.datamodel.File f = new org.messic.server.api.datamodel.File();
            f.setFileName( existingFiles.get( i ).getName() );
            // f.setVolume( volume ); ???
            f.setSize( existingFiles.get( i ).length() );
            result.add( f );
        }
        return result;
    }

    /**
     * Check if a certainFile is an exceptionFile, it means, a file that we shouldn't remove.
     * 
     * @param f {@link File} file to check
     * @param exceptionFiles {@link List}<File/> List of exceptionFiles
     * @return boolean true->yes, its an exception file , false->No, it isn't an exception file
     */
    private boolean isAnExceptionFile( File f, List<org.messic.server.api.datamodel.File> exceptionFiles,
                                       char replacementChar )
    {
        if ( exceptionFiles == null || exceptionFiles.size() == 0 )
        {
            return false;
        }
        for ( int i = 0; i < exceptionFiles.size(); i++ )
        {
            if ( f.getName().equals( exceptionFiles.get( i ).calculateSecureFileName( replacementChar ) ) )
            {
                if ( f.length() == exceptionFiles.get( i ).getSize() )
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Delete all the files of a certain path, and subpaths, except those files in the list of the parameter
     * exceptionFiles
     * 
     * @param basePath {@link String} basePath to start searching
     * @param exceptionFiles List<File/> Black list we don't want to remove
     */
    private void deleteFiles( String basePath, List<org.messic.server.api.datamodel.File> exceptionFiles )
    {
        File directory = new File( basePath );
        char replacementChar = daoSettings.getSettings().getIllegalCharacterReplacement();

        // get all the files from a directory
        File[] fList = directory.listFiles();
        for ( File file : fList )
        {
            if ( file.isFile() )
            {
                if ( !isAnExceptionFile( file, exceptionFiles, replacementChar )
                    && !file.getName().toUpperCase().equals( APIAlbum.INDEX_TMP_PROPERTIES_FILENAME.toUpperCase() ) )
                {
                    file.delete();
                }
            }
            else if ( file.isDirectory() )
            {
                deleteFiles( file.getAbsolutePath(), exceptionFiles );
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
    public void uploadResource( User user, String albumCode, String fileName, Integer volume, byte[] payload )
        throws IOException
    {
        MDOUser mdouser = daoUser.getUserByLogin( user.getLogin() );
        File basePath =
            new File( mdouser.calculateTmpPath( daoSettings.getSettings(), albumCode )
                + ( volume != null ? File.separatorChar + "" + volume : "" ) );
        basePath.mkdirs();

        org.messic.server.api.datamodel.File f = new org.messic.server.api.datamodel.File();
        f.setFileName( fileName );
        f.setVolume( volume );
        String secureFileName = f.calculateSecureFileName( daoSettings.getSettings().getIllegalCharacterReplacement() );
        FileOutputStream fos =
            new FileOutputStream( new File( basePath.getAbsolutePath() + File.separatorChar + secureFileName ) );
        fos.write( payload );
        fos.close();

        // Now saving the index file, basically to store the original filename associated with the safe file name
        // the original filename is important for the client, because it is the reference name of the resource
        synchronized ( indexTmpSemaphore )
        {
            Properties p = new Properties();
            File findex = new File( basePath.getAbsolutePath() + File.separatorChar + INDEX_TMP_PROPERTIES_FILENAME );
            if ( findex.exists() )
            {
                FileInputStream fisIndex = new FileInputStream( findex );
                p.load( fisIndex );
                fisIndex.close();
            }
            p.setProperty( secureFileName, fileName );
            FileOutputStream fosIndex = new FileOutputStream( findex );
            p.store( fosIndex, "index temp properties of resources for the album" );
            fosIndex.flush();
            fosIndex.close();
        }
    }

    public byte[] getAlbumResource( User mdouser, Long resourceSid )
        throws SidNotFoundMessicException, ResourceNotFoundMessicException, IOException
    {
        MDOAlbumResource resource = daoAlbumResource.get( resourceSid );
        if ( resource != null )
        {
            String resourcePath = resource.calculateAbsolutePath( daoSettings.getSettings() );
            File ftor = new File( resourcePath );
            if ( ftor.exists() )
            {
                return Util.readFile( resourcePath );
            }
            else
            {
                throw new ResourceNotFoundMessicException( resourcePath );
            }
        }
        else
        {
            throw new SidNotFoundMessicException();
        }
    }

    public void setAlbumCover( User user, Long albumSid, Long resourceSid )
    {
        MDOUser mdouser = daoUser.getUserByLogin( user.getLogin() );
        this.daoAlbum.setAlbumCover( resourceSid, albumSid, mdouser.getSid() );
    }

    public byte[] getAlbumCover( User mdouser, Long albumSid, Integer preferredWidth, Integer preferredHeight )
        throws SidNotFoundMessicException, ResourceNotFoundMessicException, IOException
    {
        MDOArtwork cover = daoAlbum.getAlbumCover( albumSid, mdouser.getLogin() );
        if ( cover != null )
        {
            String resourcePath = cover.calculateAbsolutePath( daoSettings.getSettings() );
            File ftor = new File( resourcePath );
            if ( ftor.exists() )
            {
                if ( preferredWidth != null && preferredHeight != null )
                {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    try
                    {
                        Thumbnails.of( new File( resourcePath ) ).forceSize( preferredWidth, preferredHeight ).outputFormat( "png" ).toOutputStream( baos );
                        return baos.toByteArray();
                    }
                    catch ( IIOException e )
                    {
                        // impossible to thumbnail the image?
                        // maybe it is an unsupported image type, lets just send the original image
                        log.warn( "Error creating thumbnail of album (maybe not supported):'"
                            + daoAlbum.getAlbum( albumSid, mdouser.getLogin() ).getName() + "'.  Error:"
                            + e.getMessage() );
                    }
                }
                return Util.readFile( resourcePath );
            }
            else
            {
                throw new ResourceNotFoundMessicException( resourcePath );
            }
        }
        else
        {
            throw new ResourceNotFoundMessicException( "" + albumSid );
        }
    }

    /**
     * Synchronize the Tags of the mp3 songs
     * 
     * @param mdoAlbum {@link MDOAlbum} album info
     */
    public void synchronizeTags( MDOAlbum mdoAlbum )
    {
        MDOMessicSettings settings = daoSettings.getSettings();
        AudioTaggerTAGWizardPlugin atp = new AudioTaggerTAGWizardPlugin();
        org.messic.server.api.tagwizard.service.Album salbum = new org.messic.server.api.tagwizard.service.Album();
        salbum.author = mdoAlbum.getAuthor().getName();
        salbum.name = mdoAlbum.getName();
        if ( mdoAlbum.getComments() != null )
            salbum.comments = mdoAlbum.getComments();
        if ( mdoAlbum.getGenre() != null )
            salbum.genre = mdoAlbum.getGenre().getName();
        salbum.year = mdoAlbum.getYear();

        List<MDOSong> songs = mdoAlbum.getSongs();
        for ( MDOSong mdoSong : songs )
        {
            String absPath = mdoSong.calculateAbsolutePath( settings );
            File fnew = new File( absPath );
            if ( fnew.exists() )
            {
                org.messic.server.api.tagwizard.service.Song ssong = new org.messic.server.api.tagwizard.service.Song();
                ssong.track = mdoSong.getTrack();
                ssong.name = mdoSong.getName();

                try
                {
                    atp.saveTags( salbum, ssong, fnew );
                }
                catch ( CannotReadException e )
                {
                    log.error( e );
                    // throw new IOException( e.getMessage(), e.getCause() );
                }
                catch ( TagException e )
                {
                    log.error( e );
                    // throw new IOException( e.getMessage(), e.getCause() );
                }
                catch ( ReadOnlyFileException e )
                {
                    log.error( e );
                    // throw new IOException( e.getMessage(), e.getCause() );
                }
                catch ( InvalidAudioFrameException e )
                {
                    log.error( e );
                    // throw new IOException( e.getMessage(), e.getCause() );
                }
                catch ( CannotWriteException e )
                {
                    log.error( e );
                    // throw new IOException( e.getMessage(), e.getCause() );
                }
                catch ( IOException e )
                {
                    log.error( e );
                }
            }

        }
    }

    @Transactional
    public long createOrUpdateAlbum( User user, Album album )
        throws IOException, ExistingMessicException
    {
        Long sidResult = 0l;
        MDOGenre mdoGenre = null;
        MDOAlbum mdoAlbum = null;
        MDOAuthor mdoAuthor = null;
        MDOAuthor oldAuthor = null;
        MDOUser mdouser = daoUser.getUserByLogin( user.getLogin() );
        char replacementChar = daoSettings.getSettings().getIllegalCharacterReplacement();
        MDOMessicSettings settings = daoSettings.getSettings();

        // 1st getting genre ###############################################################################
        if ( album.getGenre() != null && album.getGenre().getSid() != null )
        {
            mdoGenre = daoGenre.get( album.getGenre().getSid() );
        }
        if ( mdoGenre == null )
        {
            if ( album.getGenre() != null && album.getGenre().getName() != null
                && album.getGenre().getName().trim().length() > 0 )
            {
                mdoGenre = daoGenre.getByName( user.getLogin(), album.getGenre().getName() );
            }
        }
        if ( mdoGenre == null && album.getGenre() != null && album.getGenre().getName() != null
            && album.getGenre().getName().trim().length() > 0 )
        {
            mdoGenre = new MDOGenre( album.getGenre().getName(), mdouser );
        }

        // 2nd getting the album if exist
        // ###############################################################################
        if ( album.getSid() > 0 )
        {
            mdoAlbum = daoAlbum.getAlbum( album.getSid(), user.getLogin() );
            if ( mdoAlbum != null )
            {
                // setting the old author just to save it in the case that the author has changed.
                oldAuthor = mdoAlbum.getAuthor();
            }
        }

        // 3rd getting the author ###############################################################################
        if ( album.getAuthor().getSid() > 0 )
        {
            // trying by sid
            mdoAuthor = daoAuthor.get( user.getLogin(), album.getAuthor().getSid() );
        }
        else if ( album.getSid() > 0 && oldAuthor != null )
        {
            // if the author is not defined, but the album is defined, then we use the old Author
            mdoAuthor = oldAuthor;
        }

        if ( mdoAuthor == null )
        {
            // trying by name
            mdoAuthor = daoAuthor.getByName( album.getAuthor().getName(), user.getLogin() );
        }
        if ( mdoAuthor != null )
        {
            // an existing album from this autor??
            if ( mdoAlbum == null )
            {
                mdoAlbum = daoAlbum.getByName( mdoAuthor.getName(), album.getName(), user.getLogin() );
            }
        }
        // let's create a new author
        if ( mdoAuthor == null )
        {
            mdoAuthor = new MDOAuthor();
            mdoAuthor.setName( album.getAuthor().getName().trim() );
            mdoAuthor.setOwner( mdouser );
            mdoAuthor.setLocation( Util.replaceIllegalFilenameCharactersNew( album.getAuthor().getName(),
                                                                             replacementChar ) );
        }
        // 4th new album if none ###############################################################################
        if ( mdoAlbum == null )
        {
            mdoAlbum = new MDOAlbum();
        }

        boolean flagExistingAuthor = ( mdoAuthor.getSid() != null && mdoAuthor.getSid() > 0 );
        boolean flagAuthorNameChanged = false;
        boolean flagExistingAlbum = ( mdoAlbum.getSid() != null && mdoAlbum.getSid() > 0 );

        // checking if the user is trying to create a new album with the same name and author of another existing one.
        // the reason to forbide this is because of the filesystem. Both albums will have the same filesystem path, and
        // will merge their content!! panic!!
        List<MDOAlbum> albumsSameName = daoAlbum.findAlbum( album.getName(), user.getLogin() );
        if ( albumsSameName.size() > 0 )
        {
            for ( MDOAlbum mdoAlbumSameName : albumsSameName )
            {
                if ( mdoAlbumSameName.getSid() != mdoAlbum.getSid() )
                {
                    String authorName = mdoAlbumSameName.getAuthor().getName();
                    if ( authorName.toUpperCase().equals( album.getAuthor().getName().toUpperCase() ) )
                    {
                        throw new ExistingMessicException();
                    }
                }
            }
        }

        // old album path if the album was an existing one
        String oldAlbumPath = null;
        if ( flagExistingAlbum )
        {
            oldAlbumPath = mdoAlbum.calculateAbsolutePath( settings );
        }

        // 5th updating / creating the album
        // ###############################################################################

        // if its an existing author and the name of the author has changed...wow!! we must do more things
        if ( flagExistingAuthor
            && ( album.getAuthor() != null && album.getAuthor().getName() != null && !album.getAuthor().getName().trim().toUpperCase().equals( mdoAuthor.getName().trim().toUpperCase() ) ) )
        {
            // the name of the author has changed!!!
            flagAuthorNameChanged = true;
            mdoAuthor.setName( album.getAuthor().getName() );
            mdoAuthor.setLocation( Util.replaceIllegalFilenameCharactersNew( album.getAuthor().getName(),
                                                                             replacementChar ) );
        }

        // we save the old volumes number, maybe we have added or removed volumes!
        int oldVolumes = album.getVolumes();
        if ( flagExistingAlbum )
        {
            oldVolumes = mdoAlbum.getVolumes();
        }

        mdoAlbum.setName( album.getName() );
        mdoAlbum.setLocation( album.getYear() + "-"
            + Util.replaceIllegalFilenameCharactersNew( album.getName(), replacementChar ) );
        mdoAlbum.setAuthor( mdoAuthor );
        mdoAlbum.setComments( album.getComments() );
        mdoAlbum.setVolumes( album.getVolumes() );
        mdoAlbum.setGenre( mdoGenre );
        mdoAlbum.setOwner( mdouser );
        mdoAlbum.setYear( album.getYear() );

        // 6th saving author, genre and album
        // ###############################################################################
        daoAuthor.save( mdoAuthor );
        if ( mdoGenre != null )
        {
            daoGenre.save( mdoGenre );
        }
        daoAlbum.save( mdoAlbum );

        sidResult = mdoAlbum.getSid();
        if ( sidResult <= 0 )
        {
            mdoAlbum = daoAlbum.merge( mdoAlbum );
            sidResult = mdoAlbum.getSid();
        }

        // 7th moving album resources to definitive location
        // ###############################################################################

        String currentAlbumPath = mdoAlbum.calculateAbsolutePath( settings );
        File currentAlbumPathFile = new File( currentAlbumPath );
        String userTmpPath = mdouser.calculateTmpPath( settings, album.getCode() );

        try
        {
            // creating album path
            currentAlbumPathFile.mkdirs();

            // 7.1 - Songs resources
            if ( album.getSongs() != null && album.getSongs().size() > 0 )
            {
                List<Song> songs = album.getSongs();
                for ( Song song : songs )
                {
                    MDOSong mdoSong = new MDOSong();
                    File fnew = null;

                    if ( song.getSid() <= 0 )
                    {
                        MDOSongStatistics ss = new MDOSongStatistics();
                        ss.setTimesplayed( 0 );
                        ss.setTimesstopped( 0 );
                        daoSongStatistics.save( ss );

                        // new song
                        mdoSong.setStatistics( ss );
                        mdoSong.setTrack( song.getTrack() );
                        mdoSong.setName( song.getName() );
                        String secureExtension = song.calculateSecureExtension( replacementChar );
                        String theoricalFileName =
                            mdoSong.calculateSongTheoricalFileName( secureExtension, replacementChar );
                        mdoSong.setLocation( theoricalFileName );
                        mdoSong.setVolume( song.getVolume() );
                        mdoSong.setOwner( mdouser );
                        mdoSong.setAlbum( mdoAlbum );

                        // moving resource to the new location, note: ALL the tmp resources are under a volume folder.
                        File tmpRes =
                            new File( userTmpPath + File.separatorChar + song.getVolume() + File.separatorChar
                                + song.calculateSecureFileName( replacementChar ) );
                        fnew =
                            new File( currentAlbumPath + mdoSong.calculateVolumePath() + File.separatorChar
                                + mdoSong.getLocation() );

                        if ( fnew.exists() )
                        {
                            checkResourceLocation( mdoAlbum, mdoSong, -1 );
                            fnew =
                                new File( currentAlbumPath + mdoSong.calculateVolumePath() + File.separatorChar
                                    + mdoSong.getLocation() );
                        }

                        daoSong.save( mdoSong );

                        FileUtils.moveFile( tmpRes, fnew );
                    }
                    else
                    {
                        // existing song...
                        mdoSong = daoSong.get( user.getLogin(), song.getSid() );
                        if ( mdoSong != null )
                        {
                            mdoSong.setTrack( song.getTrack() );
                            mdoSong.setName( song.getName() );

                            String oldLocation =
                                ( flagExistingAlbum ? mdoSong.calculateAbsolutePath( oldAlbumPath, oldVolumes )
                                                : mdoSong.calculateAbsolutePath( settings ) );
                            String theoricalFileName =
                                mdoSong.calculateSongTheoricalFileName( mdoSong.getExtension(), replacementChar );
                            mdoSong.setLocation( theoricalFileName );

                            // last, we set the volume number (we need the old volume number to calculate old paths)
                            mdoSong.setVolume( song.getVolume() );
                            daoSong.save( mdoSong );

                            File fold = new File( oldLocation );
                            fnew = new File( mdoSong.calculateAbsolutePath( settings ) );
                            if ( !fold.getAbsolutePath().equals( fnew.getAbsolutePath() ) )
                            {
                                FileUtils.moveFile( fold, fnew );
                            }
                        }
                    }

                }
            }

            // 7.2 - Artwork resources
            if ( album.getArtworks() != null && album.getArtworks().size() > 0 )
            {
                List<org.messic.server.api.datamodel.File> files = album.getArtworks();
                boolean flagCovered = false;
                for ( int k = 0; k < files.size(); k++ )
                {
                    org.messic.server.api.datamodel.File file = files.get( k );
                    if ( file.getSid() <= 0 )
                    {
                        MDOArtwork mdopr = new MDOArtwork();
                        mdopr.setLocation( file.calculateSecureFileName( replacementChar ) );
                        mdopr.setOwner( mdouser );
                        mdopr.setVolume( file.getVolume() );
                        mdopr.setAlbum( mdoAlbum );

                        org.messic.server.api.datamodel.File fcover = album.getCover();
                        if ( fcover != null && file.getFileName().equals( album.getCover().getFileName() ) )
                        {
                            mdopr.setCover( true );
                            flagCovered = true;
                        }

                        // an script to set a default resource as a cover, if none set.
                        if ( k == files.size() - 1 )
                        {
                            // the last file
                            if ( !flagCovered && mdoAlbum.getCover() == null )
                            {
                                // then this is the cover by default
                                mdopr.setCover( true );
                                flagCovered = true;
                            }
                        }

                        File tmpRes =
                            new File( userTmpPath + File.separatorChar + file.getVolume() + File.separatorChar
                                + file.calculateSecureFileName( replacementChar ) );
                        File newFile =
                            new File( currentAlbumPath + mdopr.calculateVolumePath() + File.separatorChar
                                + mdopr.getLocation() );

                        if ( newFile.exists() )
                        {
                            checkResourceLocation( mdoAlbum, mdopr, -1 );
                            newFile =
                                new File( currentAlbumPath + mdopr.calculateVolumePath() + File.separatorChar
                                    + mdopr.getLocation() );
                        }

                        daoPhysicalResource.save( mdopr );
                        mdoAlbum.getArtworks().add( mdopr );

                        // moving resource to the new location
                        // FileUtils.moveFileToDirectory( tmpRes, currentAlbumPathFile, false );
                        FileUtils.moveFile( tmpRes, newFile );
                    }
                    else
                    {
                        // existing artwork...
                        MDOAlbumResource resource = daoAlbumResource.get( user.getLogin(), file.getSid() );
                        if ( resource != null )
                        {
                            String oldLocation =
                                ( flagExistingAlbum ? resource.calculateAbsolutePath( oldAlbumPath, oldVolumes )
                                                : resource.calculateAbsolutePath( settings ) );
                            resource.setLocation( file.calculateSecureFileName( replacementChar ) );

                            // last, we set the volume (we need the old volume number to find the old path)
                            resource.setVolume( file.getVolume() );
                            daoAlbumResource.save( resource );

                            File fold = new File( oldLocation );
                            File fnew = new File( resource.calculateAbsolutePath( settings ) );
                            if ( !fold.getAbsolutePath().equals( fnew.getAbsolutePath() ) )
                            {
                                FileUtils.moveFile( fold, fnew );
                            }
                        }
                    }
                }

                daoAlbum.save( mdoAlbum );
            }

            // 7.3 - Other resources
            if ( album.getOthers() != null && album.getOthers().size() > 0 )
            {
                List<org.messic.server.api.datamodel.File> files = album.getOthers();
                for ( org.messic.server.api.datamodel.File file : files )
                {
                    if ( file.getSid() <= 0 )
                    {
                        MDOOtherResource mdopr = new MDOOtherResource();
                        mdopr.setLocation( file.calculateSecureFileName( replacementChar ) );
                        mdopr.setOwner( mdouser );
                        mdopr.setVolume( file.getVolume() );
                        mdopr.setAlbum( mdoAlbum );

                        File tmpRes =
                            new File( userTmpPath + File.separatorChar + file.getVolume() + File.separatorChar
                                + file.calculateSecureFileName( replacementChar ) );
                        File newFile =
                            new File( currentAlbumPath + mdopr.calculateVolumePath() + File.separatorChar
                                + mdopr.getLocation() );

                        if ( newFile.exists() )
                        {
                            checkResourceLocation( mdoAlbum, mdopr, -1 );
                            newFile =
                                new File( currentAlbumPath + mdopr.calculateVolumePath() + File.separatorChar
                                    + mdopr.getLocation() );
                        }

                        daoPhysicalResource.save( mdopr );
                        mdoAlbum.getOthers().add( mdopr );

                        // moving resource to the new location
                        // FileUtils.moveFileToDirectory( tmpRes, currentAlbumPathFile, false );
                        FileUtils.moveFile( tmpRes, newFile );
                    }
                    else
                    {
                        // existing artwork...
                        MDOAlbumResource resource = daoAlbumResource.get( user.getLogin(), file.getSid() );
                        if ( resource != null )
                        {
                            String oldLocation =
                                ( flagExistingAlbum ? resource.calculateAbsolutePath( oldAlbumPath, oldVolumes )
                                                : resource.calculateAbsolutePath( settings ) );
                            resource.setLocation( file.calculateSecureFileName( replacementChar ) );
                            resource.setVolume( file.getVolume() );
                            daoAlbumResource.save( resource );

                            File fold = new File( oldLocation );
                            File fnew = new File( resource.calculateAbsolutePath( settings ) );
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
            if ( oldAlbumPath != null && !oldAlbumPath.equals( currentAlbumPath ) )
            {
                List<MDOAlbumResource> resources = mdoAlbum.getAllResources();
                for ( int i = 0; i < resources.size(); i++ )
                {
                    MDOAlbumResource resource = resources.get( i );
                    //warning: we need to take into account the oldVolumes number, because the old resource path could be affected by this change
                    //If the oldVolumes is different to the new volume number, then these will be moved after
                    String resourceNewPath = currentAlbumPath + resource.calculateVolumePath(oldVolumes) + File.separatorChar + resource.getLocation();
                    String resourceCurrentPath = 
                        oldAlbumPath + resource.calculateVolumePath(oldVolumes) + File.separatorChar + resource.getLocation();
                    File fnewPath = new File( resourceNewPath );
                    File foldPath = new File( resourceCurrentPath );
                    if ( foldPath.exists() )
                    {
                        FileUtils.moveFile( foldPath, fnewPath );
                    }
                }

                File fAlbumOldPath = new File( oldAlbumPath );
                FileUtils.deleteDirectory( fAlbumOldPath );
                // if the author have changed the name, we have only moved those resources from this album, but the
                // author
                // could have other albums, we need to move also.
                if ( flagAuthorNameChanged )
                {
                    File newAuthorLocation = new File( mdoAuthor.calculateAbsolutePath( settings ) );
                    // we must move all the authors albums to the new one folder
                    File oldAuthorFolder = fAlbumOldPath.getParentFile();
                    File[] oldfiles = oldAuthorFolder.listFiles();
                    for ( File file2 : oldfiles )
                    {
                        if ( file2.isDirectory() )
                        {
                            FileUtils.moveDirectoryToDirectory( file2, newAuthorLocation, true );
                        }
                    }

                    // finally we remove the old location
                    FileUtils.deleteDirectory( oldAuthorFolder );
                }
            }

            // checking if the volume number has change
            if ( flagExistingAlbum )
            {
                String albumPath = mdoAlbum.calculateAbsolutePath( settings );
                // we have decreased the volumes!
                if ( oldVolumes > mdoAlbum.getVolumes() )
                {
                    // the volume number has change, we need to remove volume folders
                    for ( int i = mdoAlbum.getVolumes() + 1; i <= oldVolumes; i++ )
                    {
                        String volumePath = albumPath + MDOAlbumResource.calculateVolumePath( oldVolumes, i );
                        File fvolumePath = new File( volumePath );
                        if ( fvolumePath.exists() )
                        {
                            FileUtils.deleteDirectory( fvolumePath );
                        }

                        this.daoAlbumResource.removeVolumeAlbumResources( user.getLogin(), mdoAlbum.getSid(), i );
                    }

                    // finally, if the new volume number is just 1, then we need to move the resources to the plain
                    // path, without a volume subfolder
                    if ( mdoAlbum.getVolumes() == 1 )
                    {
                        String firstVolumePath = albumPath + MDOAlbumResource.calculateVolumePath( oldVolumes, 1 );
                        File ffirstVolumePath = new File( firstVolumePath );
                        File[] files = ffirstVolumePath.listFiles();
                        File falbumPath = new File( albumPath );
                        if(files!=null){
                            for ( File file : files )
                            {
                                FileUtils.moveFileToDirectory( file, falbumPath, false );
                            }
                        }

                        FileUtils.deleteDirectory( ffirstVolumePath );
                        // done!
                    }
                }
                else if ( oldVolumes < mdoAlbum.getVolumes() && oldVolumes == 1 )
                {
                    // we have increased the volumes, and now we have more than 1 volume, we need to create a subfolders
                    // for the first volume
                    String newfirstVolumePath =
                        albumPath + MDOAlbumResource.calculateVolumePath( mdoAlbum.getVolumes(), 1 );

                    for ( int i = 0; i < mdoAlbum.getSongs().size(); i++ )
                    {
                        MDOAlbumResource resource = mdoAlbum.getSongs().get( i );
                        if ( resource.getVolume() <= 1 )
                        {
                            FileUtils.moveFile( new File( albumPath + File.separatorChar + resource.getLocation() ),
                                                new File( newfirstVolumePath + File.separatorChar
                                                    + resource.getLocation() ) );
                        }
                    }
                    for ( int i = 0; i < mdoAlbum.getArtworks().size(); i++ )
                    {
                        MDOAlbumResource resource = mdoAlbum.getArtworks().get( i );
                        if ( resource.getVolume() <= 1 )
                        {
                            FileUtils.moveFile( new File( albumPath + File.separatorChar + resource.getLocation() ),
                                                new File( newfirstVolumePath + File.separatorChar
                                                    + resource.getLocation() ) );
                        }
                    }
                    for ( int i = 0; i < mdoAlbum.getOthers().size(); i++ )
                    {
                        MDOAlbumResource resource = mdoAlbum.getOthers().get( i );
                        if ( resource.getVolume() <= 1 )
                        {
                            FileUtils.moveFile( new File( albumPath + File.separatorChar + resource.getLocation() ),
                                                new File( newfirstVolumePath + File.separatorChar
                                                    + resource.getLocation() ) );
                        }
                    }

                    // done!
                }
            }

            // if ( flagExistingAlbum )
            // {
            // the last thing, and very important, is to synchronize the tags of the mp3 files
            synchronizeTags( mdoAlbum );
            // }

            if ( oldAuthor != null )
            {
                daoAuthor.flush();
                // we need to check if the author is empty now... if it is empty, we should remove the author too
                if ( oldAuthor.getAlbums().size() <= 0 )
                {
                    // empty author, let's remove it
                    daoAuthor.remove( oldAuthor );
                    String oldAuthorPath = oldAuthor.calculateAbsolutePath( daoSettings.getSettings() );
                    FileUtils.deleteDirectory( new File( oldAuthorPath ) );
                }
            }
        }
        catch ( Exception e )
        {
            // something failed!!, let's try to remove if empty
            if ( !flagExistingAlbum )
            {
                String pathToRemove = mdoAlbum.calculateAbsolutePath( settings );
                FileUtils.deleteDirectory( new File( pathToRemove ) );
            }
            if ( !flagExistingAuthor )
            {
                String pathToRemove = mdoAuthor.calculateAbsolutePath( settings );
                FileUtils.deleteDirectory( new File( pathToRemove ) );
            }

            log.error( e );
            e.printStackTrace();
            throw new IOException( e.getMessage() );
        }
        catch ( Error e )
        {
            // something failed!!, let's try to remove if empty
            if ( !flagExistingAlbum )
            {
                String pathToRemove = mdoAlbum.calculateAbsolutePath( settings );
                FileUtils.deleteDirectory( new File( pathToRemove ) );
            }
            if ( !flagExistingAuthor )
            {
                String pathToRemove = mdoAuthor.calculateAbsolutePath( settings );
                FileUtils.deleteDirectory( new File( pathToRemove ) );
            }

            log.error( e );
            e.printStackTrace();
            throw e;
        }

        return sidResult;
    }

    /**
     * Check the location of the resource and give to it a valid location (searching repeated locations at the folder)
     * 
     * @param mdoAlbum {@link MDOAlbum} album of the resource
     * @param newResource {@link MDOAlbumResource} resource to be added
     * @param maxIterations int max number of iterations trying to search a valid location for the resource (<0 if
     *            infinite)
     * @return boolean -> true when the resourcenanme is valid
     */
    private boolean checkResourceLocation( MDOAlbum mdoAlbum, MDOAlbumResource newResource, int maxIterations )
    {
        if ( maxIterations == 0 )
        {
            return false;
        }

        List<MDOAlbumResource> resources = mdoAlbum.getAllResources();
        for ( MDOAlbumResource mdoAlbumResource : resources )
        {
            if ( mdoAlbumResource.getLocation().toUpperCase().equals( newResource.getLocation().toUpperCase() ) )
            {
                // we have already an existing resource with the same name!!
                try
                {
                    String filepath = newResource.calculateAbsolutePath( daoSettings.getSettings() );
                    File newFilePath = Util.getRollFileNumber( new File( filepath ) );
                    newResource.setLocation( newFilePath.getName() );
                }
                catch ( IOException e )
                {
                }
                return checkResourceLocation( mdoAlbum, newResource, maxIterations - 1 );
            }
        }
        return true;
    }

    /**
     * Check the consistency of the database and the file system resources 5
     * 
     * @param user
     * @param albumSid
     * @return
     * @throws CheckConsistencyMessicException
     */
    @Transactional
    public AlbumConsistency checkConsistency( MDOUser user, long albumSid )
        throws CheckConsistencyMessicException
    {
        AlbumConsistency ac = new AlbumConsistency();

        MDOAlbum album = daoAlbum.getAlbum( albumSid, user.getLogin() );

        if ( album != null )
        {
            ac.setName( album.getName() );
            ac.setSid( albumSid );

            // first check if the folder exist
            String albumPath = album.calculateAbsolutePath( daoSettings.getSettings() );
            if ( !new File( albumPath ).exists() )
            {
                ac.setStatus( 2 );
                ac.setMessage( "Album with name: '" + album.getName() + "' doesn't exist at the system folder!" );
                return ac;
            }

            // check if album path is the correct one
            char replacementChar = daoSettings.getSettings().getIllegalCharacterReplacement();
            String theoricalAlbumPath =
                album.getYear() + "-" + Util.replaceIllegalFilenameCharactersNew( album.getName(), replacementChar );
            if ( !theoricalAlbumPath.equals( album.getLocation() ) )
            {
                // there are differences.. maybe because previous version of messic doesn't set the year at the album
                // folder
                // or maybe the album has an old illegal filiename character set
                try
                {
                    album.setLocation( theoricalAlbumPath );
                    daoAlbum.save( album );
                    String newPath = album.calculateAbsolutePath( daoSettings.getSettings() );
                    FileUtils.moveDirectory( new File( albumPath ), new File( newPath ) );
                    ac.setStatus( 1 );
                    ac.setMessage( "Repaired. Album with wrong path. '" + album.getName() + "'" );
                }
                catch ( IOException e )
                {
                    throw new CheckConsistencyMessicException( "Error trying to set a new location to the album: '"
                        + album.getName() + "'" );
                }
            }

            List<MDOAlbumResource> resources = album.getAllResources();
            for ( MDOAlbumResource resource : resources )
            {
                String resourcePath = resource.calculateAbsolutePath( daoSettings.getSettings() );
                if ( !new File( resourcePath ).exists() )
                {
                    ac.setStatus( 2 );
                    ac.setMessage( "Resource '" + resource.getLocation() + "' of the album '" + album.getName()
                        + "' doesn't exist at the system folder!" );
                    return ac;
                }

            }

            // Now let's synchronize tags
            synchronizeTags( album );

            // check author?
            MDOAuthor author = album.getAuthor();
            String authorPath = author.calculateAbsolutePath( daoSettings.getSettings() );
            String theoricalAuthorPath = Util.replaceIllegalFilenameCharactersNew( author.getName(), replacementChar );
            if ( !theoricalAuthorPath.equals( author.getLocation() ) )
            {
                // maybe the author has an old illegal filiename character set
                try
                {
                    author.setLocation( theoricalAuthorPath );
                    daoAuthor.save( author );
                    String newPath = author.calculateAbsolutePath( daoSettings.getSettings() );
                    FileUtils.moveDirectory( new File( authorPath ), new File( newPath ) );
                    ac.setStatus( 1 );
                    ac.setMessage( "Repaired. Author with wrong path. '" + author.getName() + "'" );
                }
                catch ( IOException e )
                {
                    throw new CheckConsistencyMessicException( "Error trying to set a new location to the author: '"
                        + author.getName() + "'" );
                }
            }
        }
        else
        {
            throw new CheckConsistencyMessicException( "Album with sid " + albumSid + "doesn't exist!" );
        }
        return ac;
    }
}
