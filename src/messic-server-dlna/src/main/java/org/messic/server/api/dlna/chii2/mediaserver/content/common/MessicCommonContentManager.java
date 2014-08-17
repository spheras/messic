package org.messic.server.api.dlna.chii2.mediaserver.content.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.fourthline.cling.support.model.DIDLObject;
import org.fourthline.cling.support.model.PersonWithRole;
import org.fourthline.cling.support.model.SortCriterion;
import org.fourthline.cling.support.model.container.MusicAlbum;
import org.fourthline.cling.support.model.container.MusicArtist;
import org.messic.server.api.dlna.MusicService;
import org.messic.server.api.dlna.chii2.medialibrary.api.core.MediaLibraryService;
import org.messic.server.api.dlna.chii2.mediaserver.api.content.ContentManager;
import org.messic.server.api.dlna.chii2.mediaserver.api.content.container.VisualContainer;
import org.messic.server.api.dlna.chii2.mediaserver.api.http.HttpServerService;
import org.messic.server.api.dlna.chii2.mediaserver.api.upnp.Filter;
import org.messic.server.api.dlna.chii2.mediaserver.content.common.container.MovieBaseStorageFolderContainer;
import org.messic.server.api.dlna.chii2.mediaserver.content.common.container.MusicContainer;
import org.messic.server.api.dlna.chii2.mediaserver.content.common.container.MusicFoldersContainer;
import org.messic.server.api.dlna.chii2.mediaserver.content.common.container.PicturesContainer;
import org.messic.server.api.dlna.chii2.mediaserver.content.common.container.PicturesFoldersContainer;
import org.messic.server.api.dlna.chii2.mediaserver.content.common.container.PicturesStorageFolderContainer;
import org.messic.server.api.dlna.chii2.mediaserver.content.common.container.RootContainer;
import org.messic.server.api.dlna.chii2.mediaserver.content.common.container.VideoContainer;
import org.messic.server.api.dlna.chii2.mediaserver.content.common.container.VideoFoldersContainer;
import org.messic.server.api.dlna.chii2.mediaserver.content.common.container.messic.EmptyVisualContainer;
import org.messic.server.api.dlna.chii2.mediaserver.content.common.container.messic.MessicContainer;
import org.messic.server.api.dlna.chii2.mediaserver.content.common.container.messic.MessicUserAuthorAlbumContainer;
import org.messic.server.api.dlna.chii2.mediaserver.content.common.container.messic.MessicUserAuthorContainer;
import org.messic.server.api.dlna.chii2.mediaserver.content.common.container.messic.MessicUserContainer;
import org.messic.server.api.dlna.chii2.transcoder.api.core.TranscoderService;
import org.messic.server.datamodel.MDOAlbum;
import org.messic.server.datamodel.MDOAuthor;
import org.messic.server.datamodel.MDOUser;
import org.springframework.transaction.annotation.Transactional;

public class MessicCommonContentManager
    extends CommonContentManager

{

    public MessicCommonContentManager( MediaLibraryService mediaLibrary, HttpServerService httpServer,
                                       TranscoderService transcoder, MusicService musicService )
    {
        super( mediaLibrary, httpServer, transcoder, musicService );
    }

    public static final String MESSIC_ID = "1";

    public boolean isMessicContainer( String id )
    {
        return MESSIC_ID.equalsIgnoreCase( id );
    }

    public boolean isMessicUserContainer( String id )
    {
        String[] parts = id.split( MessicContainer.SEPARATOR );
        return parts.length == 1;
    }

    public boolean isMessicAuthorContainer( String id )
    {
        String[] parts = id.split( MessicContainer.SEPARATOR );
        return parts.length == 2;
    }

    public boolean isMessicAuthorAlbumContainer( String id )
    {
        String[] parts = id.split( MessicContainer.SEPARATOR );
        return parts.length == 3;
    }

    @Override
    @Transactional
    public DIDLObject browseObject( String objectId, Filter filter, long startIndex, long requestCount,
                                    SortCriterion[] orderBy )
    {
        // Root Container
        if ( isRootContainer( objectId ) )
        {
            RootContainer container = new RootContainer( filter );
            container.ms = this.musicService;
            container.loadContents( startIndex, requestCount, orderBy, this );
            return container;
        }
        // Pictures Container
        else if ( isMessicContainer( objectId ) )
        {
            MessicContainer container = new MessicContainer( filter );
            container.ms = this.musicService;
            container.loadContents( startIndex, requestCount, orderBy, this );
            return container;
        }
        else if ( isMessicUserContainer( objectId ) )
        {
            MDOUser user = this.musicService.getUser( Long.valueOf( objectId ) );
            if ( user.getAllowDLNA() )
            {
                MessicUserContainer container =
                    new MessicUserContainer( filter, Long.valueOf( objectId ), user.getName(), this.musicService );
                container.ms = this.musicService;
                container.loadContents( startIndex, requestCount, orderBy, this );
                return container;
            }
            else
            {
                return null;
            }
        }
        else if ( isMessicAuthorContainer( objectId ) )
        {
            String[] parts = objectId.split( MessicContainer.SEPARATOR );
            MDOAuthor author = this.musicService.getAuthor( Long.valueOf( parts[1] ) );
            if ( author != null )
            {
                MessicUserAuthorContainer container =
                    new MessicUserAuthorContainer( filter, objectId, author.getName(), this.musicService );
                container.loadContents( startIndex, requestCount, orderBy, this );
                return container;
            }
            else
            {
                return null;
            }
        }
        else if ( isMessicAuthorAlbumContainer( objectId ) )
        {
            String[] parts = objectId.split( MessicContainer.SEPARATOR );
            MDOAlbum album = this.musicService.getAlbum( Long.valueOf( parts[2] ) );
            if ( album!=null )
            {
                MessicUserAuthorAlbumContainer container =
                    new MessicUserAuthorAlbumContainer( filter, objectId, album.getName(), this.musicService );
                container.loadContents( startIndex, requestCount, orderBy, this );
                return container;
            }
            else
            {
                return null;
            }
        }
        // Invalid
        else
        {
            // TODO Maybe should throw a NO_SUCH_OBJECT exception, instead of null result
            return null;
        }

    }
}
