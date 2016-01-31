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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.messic.server.Util;
import org.messic.server.api.datamodel.Author;
import org.messic.server.api.datamodel.FolderResourceConsistency;
import org.messic.server.api.datamodel.User;
import org.messic.server.api.exceptions.ResourceNotFoundMessicException;
import org.messic.server.api.exceptions.SidNotFoundMessicException;
import org.messic.server.datamodel.MDOAlbum;
import org.messic.server.datamodel.MDOAlbumResource;
import org.messic.server.datamodel.MDOAuthor;
import org.messic.server.datamodel.MDOUser;
import org.messic.server.datamodel.dao.DAOAlbum;
import org.messic.server.datamodel.dao.DAOAuthor;
import org.messic.server.datamodel.dao.DAOGenre;
import org.messic.server.datamodel.dao.DAOMessicSettings;
import org.messic.server.datamodel.dao.DAOUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class APIAuthor
{
    @Autowired
    private DAOAuthor daoAuthor;

    @Autowired
    private APIAlbum apiAlbum;

    @Autowired
    private DAOGenre daoGenre;

    @Autowired
    private DAOAlbum daoAlbum;

    @Autowired
    private DAOUser daoUser;

    @Autowired
    private DAOMessicSettings daoSettings;

    @Transactional
    public void remove( User user, Long authorSid )
        throws IOException
    {
        MDOAuthor author = daoAuthor.get( user.getLogin(), authorSid );
        if ( author != null )
        {
            String path = author.calculateAbsolutePath( daoSettings.getSettings() );
            daoAuthor.remove( author );
            FileUtils.deleteDirectory( new File( path ) );
        }
    }

    @Transactional
    public byte[] getAuthorCover( User mdouser, Long authorSid, Integer preferredWidth, Integer preferredHeight )
        throws SidNotFoundMessicException, ResourceNotFoundMessicException, IOException
    {

        MDOAuthor author = daoAuthor.get( mdouser.getLogin(), authorSid );
        if ( author.getAlbums().size() > 0 )
        {
            MDOAlbum album = author.getAlbums().iterator().next();
            return apiAlbum.getAlbumCover( mdouser, album.getSid(), preferredWidth, preferredHeight );
        }
        else
        {
            return null;
        }

    }

    @Transactional
    public List<Author> getAll( User user, boolean copyAlbums, boolean copySongs )
    {
        List<MDOAuthor> authors = daoAuthor.getAll( user.getLogin() );
        return Author.transform( authors, copyAlbums, copySongs );
    }

    @Transactional
    public Author getAuthor( User user, long authorSid, boolean copyAlbums, boolean copySongs )
    {
        MDOAuthor author = daoAuthor.get( user.getLogin(), authorSid );
        return Author.transform( author, copyAlbums, copySongs );
    }

    @Transactional
    public List<Author> findSimilar( User user, String authorName, boolean contains, boolean copyAlbums,
                                     boolean copySongs )
    {
        List<MDOAuthor> authors = daoAuthor.findSimilarAuthors( authorName, contains, user.getLogin() );
        return Author.transform( authors, copyAlbums, copySongs );
    }

    /**
     * Check if a concrete folder, an author folder, is valid and has all the needed (only the needed) resources
     * 
     * @param user {@link User} user scope
     * @param faufolder {@link File} file to the auth folder to check
     * @return {@link List}<{@link FolderResourceConsistency}> list of results... at least one saying that everything is
     *         OK.
     */
    @Transactional
    public List<FolderResourceConsistency> checkConsistencyFolder( MDOUser user, File faufolder )
    {
        List<FolderResourceConsistency> result = new ArrayList<FolderResourceConsistency>();

        if ( faufolder.isDirectory() )
        {
            // An author?
            MDOAuthor author = daoAuthor.getAuthorByLocation( faufolder.getName(), user.getLogin() );

            if ( author != null )
            {
                File[] alfolders = faufolder.listFiles();
                for ( File falfolder : alfolders )
                {
                    // An album?
                    MDOAlbum album = null;
                    Iterator<MDOAlbum> albums = author.getAlbums().iterator();
                    while ( albums.hasNext() )
                    {
                        album = albums.next();

                        String supposedAlbumPath = album.calculateAbsolutePath( daoSettings.getSettings() );
                        if ( supposedAlbumPath.equals( falfolder.getAbsolutePath() ) )
                        {
                            // this is the album!
                            break;
                        }
                        album = null;
                    }

                    if ( album != null )
                    {
                        if ( album.getVolumes() == null || album.getVolumes() <= 1 )
                        {
                            // just 1 volume (no volumes)
                            File[] rfiles = falfolder.listFiles();
                            for ( File rfile : rfiles )
                            {
                                MDOAlbumResource resource = null;
                                List<MDOAlbumResource> resources = album.getAllResources();
                                for ( int k = 0; k < resources.size(); k++ )
                                {
                                    resource = resources.get( k );
                                    String supposedResourcePath =
                                        resource.calculateAbsolutePath( daoSettings.getSettings() );
                                    if ( supposedResourcePath.equals( rfile.getAbsolutePath() ) )
                                    {
                                        // This is the resource
                                        break;
                                    }
                                    resource = null;
                                }

                                if ( resource != null )
                                {
                                    // perfect, nothing to say
                                }
                                else
                                {
                                    // no resource found for this file!!!
                                    FolderResourceConsistency frc = new FolderResourceConsistency();
                                    frc.setStatus( 1 );
                                    frc.setAbsoluteLocation( rfile.getAbsolutePath() );
                                    frc.setRelativeLocation( faufolder.getName() + File.separatorChar
                                        + falfolder.getName() + File.separatorChar + rfile.getName() );
                                    frc.setAlbumResource( true );
                                    frc.setMessage( "No resource found for the file:" + faufolder.getName()
                                        + File.separatorChar + falfolder.getName() + File.separatorChar
                                        + rfile.getName() );
                                    result.add( frc );
                                }
                            }
                        }
                        else
                        {
                            // we need to take into account volumes
                            File[] ffiles = falfolder.listFiles();
                            List<File> rfiles = new ArrayList<File>();
                            for ( File ffile : ffiles )
                            {
                                rfiles.add( ffile );
                            }
                            Collections.sort( rfiles );

                            int cvol = 1;
                            for ( File rfile : rfiles )
                            {
                                if ( !rfile.isDirectory() )
                                {
                                    // not a directory in the root folder?? (we only expect volume folders there)
                                    // no resource found for this file!!!
                                    FolderResourceConsistency frc = new FolderResourceConsistency();
                                    frc.setStatus( 1 );
                                    frc.setAbsoluteLocation( rfile.getAbsolutePath() );
                                    frc.setRelativeLocation( faufolder.getName() + File.separatorChar
                                        + falfolder.getName() + File.separatorChar + rfile.getName() );
                                    frc.setAlbumResource( true );
                                    frc.setMessage( "File at root folder? Volume albums shouldnt have files at root folder:"
                                        + faufolder.getName()
                                        + File.separatorChar
                                        + falfolder.getName()
                                        + File.separatorChar + rfile.getName() );
                                    result.add( frc );

                                }
                                else
                                {

                                    String expectedVol =
                                        MDOAlbumResource.VOLUME_FOLDER_PRENAME + Util.leftZeroPadding( cvol, 2 );
                                    if ( rfile.getName().toUpperCase().equals( expectedVol.toUpperCase() ) )
                                    {

                                        // just 1 volume (no volumes)
                                        File[] rvolfiles = rfile.listFiles();
                                        for ( File rvolfile : rvolfiles )
                                        {
                                            MDOAlbumResource resource = null;
                                            List<MDOAlbumResource> resources = album.getAllResources();
                                            for ( int k = 0; k < resources.size(); k++ )
                                            {
                                                resource = resources.get( k );
                                                String supposedResourcePath =
                                                    resource.calculateAbsolutePath( daoSettings.getSettings() );
                                                if ( supposedResourcePath.equals( rvolfile.getAbsolutePath() ) )
                                                {
                                                    // This is the resource
                                                    break;
                                                }
                                                resource = null;
                                            }

                                            if ( resource != null )
                                            {
                                                // perfect, nothing to say
                                            }
                                            else
                                            {
                                                // no resource found for this file!!!
                                                FolderResourceConsistency frc = new FolderResourceConsistency();
                                                frc.setStatus( 1 );
                                                frc.setAbsoluteLocation( rvolfile.getAbsolutePath() );
                                                frc.setRelativeLocation( faufolder.getName() + File.separatorChar
                                                    + falfolder.getName() + File.separatorChar + rvolfile.getName() );
                                                frc.setAlbumResource( true );
                                                frc.setMessage( "No resource found for the file:" + faufolder.getName()
                                                    + File.separatorChar + falfolder.getName() + File.separatorChar
                                                    + rvolfile.getName() );
                                                result.add( frc );
                                            }
                                        }

                                        cvol++;
                                    }
                                    else
                                    {
                                        // folder name different from the vol number folder expected!
                                        FolderResourceConsistency frc = new FolderResourceConsistency();
                                        frc.setStatus( 1 );
                                        frc.setAbsoluteLocation( rfile.getAbsolutePath() );
                                        frc.setRelativeLocation( faufolder.getName() + File.separatorChar
                                            + falfolder.getName() + File.separatorChar + rfile.getName() );
                                        frc.setAlbumResource( true );
                                        frc.setMessage( "No resource found for the folder:" + faufolder.getName()
                                            + File.separatorChar + falfolder.getName() + File.separatorChar
                                            + rfile.getName() );
                                        result.add( frc );
                                    }
                                }
                            }

                        }
                    }
                    else
                    {
                        // no album found for this folder!!!
                        FolderResourceConsistency frc = new FolderResourceConsistency();
                        frc.setStatus( 1 );
                        frc.setAbsoluteLocation( falfolder.getAbsolutePath() );
                        frc.setRelativeLocation( faufolder.getName() + File.separatorChar + falfolder.getName() );
                        frc.setAlbumFolder( true );
                        frc.setMessage( "No album found for the folder:" + faufolder.getName() + File.separatorChar
                            + falfolder.getName() );
                        result.add( frc );
                    }
                }
            }
            else
            {
                if ( !faufolder.getName().equalsIgnoreCase( ".tmp" ) )
                {
                    // no author found for this folder!!!
                    FolderResourceConsistency frc = new FolderResourceConsistency();
                    frc.setStatus( 1 );
                    frc.setAbsoluteLocation( faufolder.getAbsolutePath() );
                    frc.setRelativeLocation( faufolder.getName() );
                    frc.setAuthorFolder( true );
                    frc.setMessage( "No Author found for the folder:" + faufolder.getName() );
                    result.add( frc );
                }
            }

        }

        if ( result.isEmpty() )
        {
            FolderResourceConsistency frc = new FolderResourceConsistency();
            result.add( frc );
        }
        return result;
    }

    /**
     * Check if the folders are just the needed folders..
     * 
     * @param albumSid
     * @return
     */
    @Transactional
    public List<FolderResourceConsistency> checkConsistency( MDOUser user )
    {
        List<FolderResourceConsistency> result = new ArrayList<FolderResourceConsistency>();
        String userPath = user.calculateAbsolutePath( daoSettings.getSettings() );
        File fuserPath = new File( userPath );
        File[] authorFolders = fuserPath.listFiles();
        for ( File faufolder : authorFolders )
        {
            List<FolderResourceConsistency> pr = checkConsistencyFolder( user, faufolder );
            result.addAll( pr );
        }

        return result;
    }

    /**
     * Return the list of folders at the author level file system (at the messic music folder of the user scope)
     * 
     * @param user {@link User} scope
     * @return {@link List}<{@link String}/> List of folders at the messic music folder
     */
    public List<String> getAuthorFolders( MDOUser user )
    {
        String userPath = user.calculateAbsolutePath( daoSettings.getSettings() );
        File fuserPath = new File( userPath );
        if ( fuserPath.exists() )
        {
            String[] list = fuserPath.list();
            List<String> rlist = new ArrayList<String>();
            for ( String string : list )
            {
                rlist.add( string );
            }
            return rlist;
        }
        else
        {
            return new ArrayList<String>();
        }

    }
}
