/*
 * Copyright (C) 2013
 *
 *  This file is part of Messic.
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public 
 *  License as published by
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
package org.messic.server.api.dlna;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.fourthline.cling.support.model.DescMeta;
import org.fourthline.cling.support.model.PersonWithRole;
import org.fourthline.cling.support.model.Protocol;
import org.fourthline.cling.support.model.ProtocolInfo;
import org.fourthline.cling.support.model.Res;
import org.fourthline.cling.support.model.container.MusicAlbum;
import org.fourthline.cling.support.model.container.MusicArtist;
import org.fourthline.cling.support.model.dlna.DLNAAttribute;
import org.fourthline.cling.support.model.dlna.DLNAConversionIndicator;
import org.fourthline.cling.support.model.dlna.DLNAConversionIndicatorAttribute;
import org.fourthline.cling.support.model.dlna.DLNAFlags;
import org.fourthline.cling.support.model.dlna.DLNAFlagsAttribute;
import org.fourthline.cling.support.model.dlna.DLNAOperations;
import org.fourthline.cling.support.model.dlna.DLNAOperationsAttribute;
import org.fourthline.cling.support.model.dlna.DLNAProfileAttribute;
import org.fourthline.cling.support.model.dlna.DLNAProfiles;
import org.fourthline.cling.support.model.dlna.DLNAProtocolInfo;
import org.fourthline.cling.support.model.item.MusicTrack;
import org.fourthline.cling.support.model.item.PlaylistItem;
import org.messic.server.Util;
import org.messic.server.api.dlna.chii2.mediaserver.api.content.container.VisualContainer;
import org.messic.server.api.dlna.chii2.mediaserver.content.common.container.messic.MessicContainer;
import org.messic.server.datamodel.MDOAlbum;
import org.messic.server.datamodel.MDOAuthor;
import org.messic.server.datamodel.MDOPlaylist;
import org.messic.server.datamodel.MDOSong;
import org.messic.server.datamodel.MDOUser;
import org.messic.server.datamodel.dao.DAOAlbum;
import org.messic.server.datamodel.dao.DAOAuthor;
import org.messic.server.datamodel.dao.DAOMessicSettings;
import org.messic.server.datamodel.dao.DAOPlaylist;
import org.messic.server.datamodel.dao.DAOSong;
import org.messic.server.datamodel.dao.DAOUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class MusicService
{
    private static Logger log = Logger.getLogger( MusicService.class );

    @Autowired
    private DAOSong daosong;

    @Autowired
    private DAOPlaylist daoPlaylist;

    @Autowired
    private DAOAlbum daoalbum;

    @Autowired
    private DAOAuthor daoauthor;

    @Autowired
    private DAOUser daoUsers;

    @Autowired
    private DAOMessicSettings daoSettings;

    @Autowired
    private AuthenticationDLNAMusicService authenticationSessionManager;

    public static HashMap<String, String> tokens = new HashMap<String, String>();

    /**
     * Return the messic server name
     * @return
     */
    public String getMessicServerName()
    {
        return daoSettings.getSettings().getMessicServerName();
    }

    /**
     * Check if DLNA service is allowed by Messic configuration
     * 
     * @return boolean
     */
    public boolean isGenericAllowed()
    {
        return this.daoSettings.getSettings().isAllowDLNA();
    }

    /**
     * Check if DLNA service is allowed by any users.
     * 
     * @return true->yes, its allowed at least by one user
     */
    public boolean isAnyUserAllowed()
    {
        return ( this.daoUsers.countAllowedDLNAUsers() > 0 );
    }

    /**
     * Return the list of names of users that allow DLNA share content
     * 
     * @return
     */
    public List<MDOUser> getDLNAUsers()
    {
        List<MDOUser> users = this.daoUsers.getDLNAUsers();
        return users;
    }

    @Transactional
    public List<MusicArtist> getAuthors( String containerId, long startIndex, long maxCount, VisualContainer vc )
    {
        List<MusicArtist> result = new ArrayList<MusicArtist>();
        List<MDOAuthor> authors = null;

        Long sid = Long.valueOf( containerId );

        if ( sid > 0 && vc != null )
        {
            authors = new ArrayList<MDOAuthor>();
            String username = this.daoUsers.getUserById( sid ).getLogin();
            authors = this.daoauthor.getAll( username );
        }
        else
        {
            authors = this.daoauthor.getAllDLNA();
        }

        int iadded = 0;
        for ( long i = startIndex; i < startIndex + maxCount && i < authors.size(); i++ )
        {
            MDOAuthor author = authors.get( (int) i );

            MusicArtist ma = new MusicArtist();
            ma.setId( author.getOwner().getSid() + MessicContainer.SEPARATOR + author.getSid() );
            ma.setParentID( author.getOwner().getSid() + "" );
            ma.setTitle( author.getName() );

            if ( vc != null )
            {
                vc.addContainer( ma );
            }

            iadded++;
            result.add( ma );
        }

        if ( vc != null )
        {
            vc.setChildCount( iadded );
            vc.setTotalChildCount( authors.size() );
        }

        return result;
    }

    @Transactional
    public MDOAuthor getAuthor( Long authorSid )
    {
        MDOAuthor author = this.daoauthor.get( authorSid );
        if ( author.getOwner().getAllowDLNA() )
        {
            return author;
        }
        else
        {
            return null;
        }
    }

    @Transactional
    public MDOAlbum getAlbum( Long albumSid )
    {
        MDOAlbum album = this.daoalbum.get( albumSid );
        if ( album.getOwner().getAllowDLNA() )
        {
            return album;
        }
        else
        {
            return null;
        }
    }

    @Transactional
    public MDOUser getUser( Long userSid )
    {
        return this.daoUsers.getUserById( userSid );
    }

    @Transactional
    public List<MDOAuthor> getUserAuthors( Long userSid )
    {
        MDOUser user = this.daoUsers.getUserById( userSid );
        List<MDOAuthor> authors = new ArrayList<MDOAuthor>();
        if ( user != null )
        {
            authors = this.daoauthor.getAll( user.getLogin() );
        }
        return authors;

    }

    @Transactional
    public List<MusicAlbum> getAlbums( String containerId, long startIndex, long maxCount, VisualContainer vc )
    {
        String[] parts = containerId.split( MessicContainer.SEPARATOR );

        List<MusicAlbum> result = new ArrayList<MusicAlbum>();
        List<MDOAlbum> albums = null;
        if ( parts.length == 2 )
        {
            Long sid = Long.valueOf( parts[1] );
            albums = new ArrayList<MDOAlbum>();
            Set<MDOAlbum> setalbums = this.daoauthor.get( sid ).getAlbums();
            for ( MDOAlbum mdoAlbum : setalbums )
            {
                albums.add( mdoAlbum );
            }
        }
        else
        {
            albums = this.daoalbum.getAllDLNA();
        }

        for ( long i = startIndex; i < startIndex + maxCount && i < albums.size(); i++ )
        {
            MDOAlbum album = albums.get( (int) i );
            MusicAlbum ma = new MusicAlbum();
            ma.setArtists( new PersonWithRole[] { new PersonWithRole( album.getAuthor().getName() ) } );
            ma.setDate( "" + album.getYear() );
            ma.setGenres( new String[] { album.getGenre().getName() } );
            ma.setTitle( album.getName() );

            ma.setParentID( album.getOwner().getSid() + MessicContainer.SEPARATOR + album.getAuthor().getSid() );
            ma.setId( album.getOwner().getSid() + MessicContainer.SEPARATOR + album.getAuthor().getSid()
                + MessicContainer.SEPARATOR + album.getSid() );

            result.add( ma );
            if ( vc != null )
            {
                vc.addContainer( ma );
            }
        }

        if ( vc != null )
        {
            vc.setChildCount( result.size() );
            vc.setTotalChildCount( albums.size() );
        }

        return result;
    }

    /**
     * Function to login the DLNA petittion. Return the token logged
     * 
     * @param login Sting username to login
     * @return String token loged
     */
    public String loginDLNA( String login, String hashPassword )
    {
        String token = tokens.get( login );
        if ( token == null )
        {
            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            authorities.add( new SimpleGrantedAuthority( "ROLE_USER" ) );

            UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken( login, hashPassword, authorities );
            token = this.authenticationSessionManager.successfulAuthenticationDLNA( authentication );
            tokens.put( login, token );
        }
        return token;
    }

    /**
     * Return the current port where is messic published
     * 
     * @return String the port number
     */
    public int getCurrentPort()
    {
        return this.authenticationSessionManager.getCurrentPort();
    }

    /**
     * return if messic is secured by https
     * 
     * @return boolean
     */
    public boolean isSecured()
    {
        return this.authenticationSessionManager.isSecured();
    }

    @Transactional
    public List<PlaylistItem> getPlaylists( String containerId, long startIndex, long maxCount, VisualContainer vc )
    {
        List<PlaylistItem> result = new ArrayList<PlaylistItem>();
        List<MDOPlaylist> playlists = this.daoPlaylist.getAllDLNA();

        for ( long i = startIndex; i < startIndex + maxCount && i < playlists.size(); i++ )
        {
            MDOPlaylist mdop = playlists.get( (int) i );

            HashMap<MDOAuthor, String> authors = new HashMap<MDOAuthor, String>();
            List<MDOSong> songs = mdop.getSongs();
            for ( int j = 0; j < songs.size(); j++ )
            {
                MDOSong song = songs.get( j );
                authors.put( song.getAlbum().getAuthor(), "" );
            }

            PlaylistItem pli = new PlaylistItem();
            pli.setTitle( mdop.getName() );
            pli.setDescription( mdop.getName() );
            pli.setLongDescription( mdop.getName() );
            pli.setDate( "24/01/2013" );
            MDOAuthor[] mdoauthors = new MDOAuthor[authors.size()];
            authors.keySet().toArray( mdoauthors );
            PersonWithRole[] persons = new PersonWithRole[mdoauthors.length];
            for ( int k = 0; k < mdoauthors.length; k++ )
            {
                MDOAuthor mdoa = mdoauthors[k];
                PersonWithRole pwr = new PersonWithRole( mdoa.getName() );
                persons[k] = pwr;
            }
            pli.setArtists( persons );

            pli.setParentID( mdop.getOwner().getSid() + "" );
            pli.setId( mdop.getOwner().getSid() + MessicContainer.SEPARATOR );

            for ( int l = 0; l < mdop.getSongs().size(); l++ )
            {
                MDOSong song = mdop.getSongs().get( l );

                MDOUser user = song.getOwner();
                String token = loginDLNA( user.getLogin(), user.getPassword() );

                Res resource = new Res();
                EnumMap<DLNAAttribute.Type, DLNAAttribute> dlnaAttributes =
                    new EnumMap<DLNAAttribute.Type, DLNAAttribute>( DLNAAttribute.Type.class );

                URI originalUri = null;
                try
                {
                    originalUri =
                        new URI( ( isSecured() ? "https" : "http" ) + "://" + Util.getInternalIp() + ":"
                            + getCurrentPort() + "/messic/services/songs/" + song.getSid() + "/dlna?messic_token="
                            + token );
                }
                catch ( URISyntaxException e )
                {
                    log.error( "failed!", e );
                }
                catch ( Exception e )
                {
                    log.error( "failed!", e );
                }
                resource.setValue( originalUri.toString() );
                DLNAProfiles originalProfile = DLNAProfiles.MP3;
                dlnaAttributes.put( DLNAAttribute.Type.DLNA_ORG_PN, new DLNAProfileAttribute( originalProfile ) );
                dlnaAttributes.put( DLNAAttribute.Type.DLNA_ORG_OP, new DLNAOperationsAttribute( DLNAOperations.RANGE ) );
                dlnaAttributes.put( DLNAAttribute.Type.DLNA_ORG_CI,
                                    new DLNAConversionIndicatorAttribute( DLNAConversionIndicator.NONE ) );
                dlnaAttributes.put( DLNAAttribute.Type.DLNA_ORG_FLAGS,
                                    new DLNAFlagsAttribute( DLNAFlags.STREAMING_TRANSFER_MODE,
                                                            DLNAFlags.BACKGROUND_TRANSFERT_MODE, DLNAFlags.DLNA_V15 ) );

                resource.setProtocolInfo( new DLNAProtocolInfo( Protocol.HTTP_GET, ProtocolInfo.WILDCARD, "audio/mp3",
                                                                dlnaAttributes ) );

                pli.addResource( resource );

            }

            result.add( pli );
        }
        return result;
    }

    @Transactional
    public List<MusicTrack> getSongs( String containerId, long startIndex, long maxCount, VisualContainer vc )
    {
        String[] parts = containerId.split( MessicContainer.SEPARATOR );

        List<MusicTrack> result = new ArrayList<MusicTrack>();
        List<MDOSong> songs = null;
        if ( parts.length == 3 )
        {
            Long sid = Long.valueOf( parts[2] );
            songs = new ArrayList<MDOSong>();
            songs = this.daoalbum.get( sid ).getSongs();
        }
        else
        {
            songs = this.daosong.getAllDLNA();
        }

        int iadded = 0;

        for ( long i = startIndex; i < startIndex + maxCount && i < songs.size(); i++ )
        {
            MDOSong song = songs.get( (int) i );
            MDOUser user = song.getOwner();
            String token = loginDLNA( user.getLogin(), user.getPassword() );

            MusicTrack ai = new MusicTrack();
            ai.setAlbum( song.getAlbum().getName() );
            ai.setArtists( new PersonWithRole[] { new PersonWithRole( song.getAlbum().getAuthor().getName() ) } );
            // ai.setCreator( "CreatorRefree" );
            ai.setDate( "" + song.getAlbum().getYear() );
            List<DescMeta> listdm = new ArrayList<DescMeta>();
            ai.setDescMetadata( listdm );
            // ai.setDescription( "description" );
            // ai.setLanguage( "es_ES" );
            // ai.setLongDescription( "long description" );
            // ai.setPublishers( new Person[]{new Person( "namepublisher" )} );
            ai.setRefID( "" + song.getSid() );
            ai.setRights( new String[] {} );
            ai.setCreator( song.getAlbum().getAuthor().getName() );
            ai.setGenres( new String[] { song.getAlbum().getGenre().getName() } );
            ai.setParentID( song.getOwner().getSid() + MessicContainer.SEPARATOR + song.getAlbum().getAuthor().getSid()
                + MessicContainer.SEPARATOR + song.getAlbum().getSid() );
            ai.setId( song.getOwner().getSid() + MessicContainer.SEPARATOR + song.getAlbum().getAuthor().getSid()
                + MessicContainer.SEPARATOR + song.getAlbum().getSid() + MessicContainer.SEPARATOR + song.getSid() );
            ai.setTitle( song.getName() );
            ai.setOriginalTrackNumber( song.getTrack() );

            URI originalUri = null;
            try
            {
                originalUri =
                    new URI( ( isSecured() ? "https" : "http" ) + "://" + Util.getInternalIp() + ":" + getCurrentPort()
                        + "/messic/services/songs/" + song.getSid() + "/dlna?messic_token=" + token );
            }
            catch ( URISyntaxException e )
            {
                log.error( "failed!", e );
            }
            catch ( Exception e )
            {
                log.error( "failed!", e );
            }
            Res resource = new Res();
            EnumMap<DLNAAttribute.Type, DLNAAttribute> dlnaAttributes =
                new EnumMap<DLNAAttribute.Type, DLNAAttribute>( DLNAAttribute.Type.class );

            resource.setValue( originalUri.toString() );
            DLNAProfiles originalProfile = DLNAProfiles.MP3;
            dlnaAttributes.put( DLNAAttribute.Type.DLNA_ORG_PN, new DLNAProfileAttribute( originalProfile ) );
            dlnaAttributes.put( DLNAAttribute.Type.DLNA_ORG_OP, new DLNAOperationsAttribute( DLNAOperations.RANGE ) );
            dlnaAttributes.put( DLNAAttribute.Type.DLNA_ORG_CI,
                                new DLNAConversionIndicatorAttribute( DLNAConversionIndicator.NONE ) );
            dlnaAttributes.put( DLNAAttribute.Type.DLNA_ORG_FLAGS,
                                new DLNAFlagsAttribute( DLNAFlags.STREAMING_TRANSFER_MODE,
                                                        DLNAFlags.BACKGROUND_TRANSFERT_MODE, DLNAFlags.DLNA_V15 ) );

            resource.setProtocolInfo( new DLNAProtocolInfo( Protocol.HTTP_GET, ProtocolInfo.WILDCARD, "audio/mp3",
                                                            dlnaAttributes ) );

            ai.addResource( resource );
            result.add( ai );

            if ( vc != null )
            {
                vc.addItem( ai );
            }

            iadded++;
        }

        if ( vc != null )
        {
            vc.setChildCount( iadded );
            vc.setTotalChildCount( songs.size() );
        }

        return result;
    }

}
