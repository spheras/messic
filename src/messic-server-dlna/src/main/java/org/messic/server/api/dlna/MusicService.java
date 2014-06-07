/*
 * Copyright (C) 2013 José Amuedo
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
package org.messic.server.api.dlna;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

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
import org.messic.server.datamodel.MDOAlbum;
import org.messic.server.datamodel.MDOAuthor;
import org.messic.server.datamodel.MDOSong;
import org.messic.server.datamodel.MDOUser;
import org.messic.server.datamodel.dao.DAOAlbum;
import org.messic.server.datamodel.dao.DAOAuthor;
import org.messic.server.datamodel.dao.DAOSong;
import org.messic.server.facade.security.AuthenticationSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class MusicService
{
    @Autowired
    private DAOSong daosong;

    @Autowired
    private DAOAlbum daoalbum;

    @Autowired
    private DAOAuthor daoauthor;

    private static HashMap<String, String> tokens = new HashMap<String, String>();

    @Transactional
    public List<MusicArtist> getAuthors( long containerId )
    {
        List<MusicArtist> result = new ArrayList<MusicArtist>();
        List<MDOAuthor> authors=this.daoauthor.getAll();
        for ( MDOAuthor author : authors )
        {
            MusicArtist ma=new MusicArtist();
            ma.setId( ""+ author.getSid());
            ma.setParentID( author.getOwner().getSid()+"" );
            ma.setTitle( author.getName() );
            result.add(ma);
        }
        return result;
    }

    @Transactional
    public List<MusicAlbum> getAlbums( long containerId )
    {
        List<MusicAlbum> result = new ArrayList<MusicAlbum>();
        List<MDOAlbum> albums = null;
        if ( containerId > 7 )
        {
            albums = new ArrayList<MDOAlbum>();
            Set<MDOAlbum> setalbums = this.daoauthor.get( containerId ).getAlbums();
            for ( MDOAlbum mdoAlbum : setalbums )
            {
                albums.add( mdoAlbum );
            }
        }
        else
        {
            albums = this.daoalbum.getAll();
        }
        for ( MDOAlbum album : albums )
        {
            MusicAlbum ma = new MusicAlbum();
            ma.setArtists( new PersonWithRole[] { new PersonWithRole( album.getAuthor().getName() ) } );
            ma.setDate( "" + album.getYear() );
            ma.setGenres( new String[] { album.getGenre().getName() } );
            ma.setParentID( "" + album.getAuthor().getSid() );
            ma.setTitle( album.getName() );
            ma.setId( "" + album.getSid() );
            result.add( ma );
        }
        return result;
    }

    @Transactional
    public List<MusicTrack> getSongs( long containerId )
    {
        List<MusicTrack> result = new ArrayList<MusicTrack>();
        List<MDOSong> songs = null;
        if ( containerId > 4 )
        {
            MDOAlbum album = this.daoalbum.get( containerId );
            songs = album.getSongs();
        }
        else
        {
            songs = this.daosong.getAll();
        }

        for ( MDOSong song : songs )
        {
            MDOUser user = song.getOwner();
            String token = tokens.get( user.getLogin() );
            if ( token == null )
            {
                UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken( user.getLogin(), "12345" );
                token = AuthenticationSessionManager.successfulAuthentication( authentication );
                tokens.put( user.getLogin(), token );
            }

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
            ai.setParentID( "" + song.getAlbum().getSid() );
            // ai.setPublishers( new Person[]{new Person( "namepublisher" )} );
            ai.setRefID( "" + song.getSid() );
            ai.setRights( new String[] {} );
            ai.setCreator( song.getAlbum().getAuthor().getName() );
            ai.setGenres( new String[] { song.getAlbum().getGenre().getName() } );
            ai.setId( "" + song.getSid() );
            ai.setTitle( song.getName() );
            ai.setOriginalTrackNumber( song.getTrack() );

            URI originalUri = null;
            try
            {
                originalUri =
                    new URI( "http://192.168.0.103:8181/messic/services/songs/" + song.getSid()
                        + "/audio?messic_token=" + token );
            }
            catch ( URISyntaxException e )
            {
                e.printStackTrace();
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
        }

        return result;
    }

}
