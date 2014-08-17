package org.messic.server.api.dlna.chii2.mediaserver.content.common.container.messic;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.fourthline.cling.support.model.DIDLObject;
import org.fourthline.cling.support.model.PersonWithRole;
import org.fourthline.cling.support.model.SortCriterion;
import org.fourthline.cling.support.model.WriteStatus;
import org.fourthline.cling.support.model.container.MusicAlbum;
import org.messic.server.api.dlna.MusicService;
import org.messic.server.api.dlna.chii2.mediaserver.api.content.ContentManager;
import org.messic.server.api.dlna.chii2.mediaserver.api.content.container.VisualContainer;
import org.messic.server.api.dlna.chii2.mediaserver.api.upnp.Filter;
import org.messic.server.api.dlna.chii2.mediaserver.content.common.CommonContentManager;
import org.messic.server.datamodel.MDOAlbum;
import org.messic.server.datamodel.MDOAuthor;

public class MessicUserAuthorContainer
    extends VisualContainer
{
    private MusicService ms;

    /**
     * Constructor
     * 
     * @param filter Content Filter
     */
    public MessicUserAuthorContainer( Filter filter, String id, String authorName, MusicService ms )
    {
        this( filter, id, CommonContentManager.ROOT_ID, authorName, ms );
    }

    /**
     * Constructor
     * 
     * @param filter Content Filter
     * @param id Container ID
     * @param parentId Parent ID
     */
    public MessicUserAuthorContainer( Filter filter, String id, String parentId, String authorName, MusicService ms )
    {
        super();

        this.ms = ms;

        // Pictures Container ID: 3
        setId( id );
        // Parent container is Root Container
        setParentID( parentId );
        // Title TODO: This should be I18N
        setTitle( authorName );
        // May used in Container Property Creator (part of UPnP protocol standard)
        setCreator( "System" );
        // May used in Container Property Clazz (part of UPnP protocol standard)
        setClazz( new DIDLObject.Class( "object.container" ) );
        // May used in Container Property Clazz (part of UPnP protocol standard)
        setClazz( new DIDLObject.Class( "object.container.storageFolder" ) );

        // Restricted
        setRestricted( true );
        // Searchable
        setSearchable( false );
        // Writable
        setWriteStatus( WriteStatus.NOT_WRITABLE );
    }

    public MessicUserAuthorContainer( MusicService ms )
    {
        this.ms = ms;
    }

    @Override
    public void loadContents( long startIndex, long maxCount, SortCriterion[] orderBy, ContentManager contentManager )
    {
        this.ms.getAlbums( getId(), startIndex, maxCount, this );
    }

}
