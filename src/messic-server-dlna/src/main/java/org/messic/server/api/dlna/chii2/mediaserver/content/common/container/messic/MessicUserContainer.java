package org.messic.server.api.dlna.chii2.mediaserver.content.common.container.messic;

import java.util.List;

import org.fourthline.cling.support.model.DIDLObject;
import org.fourthline.cling.support.model.SortCriterion;
import org.fourthline.cling.support.model.WriteStatus;
import org.fourthline.cling.support.model.container.MusicArtist;
import org.messic.server.api.dlna.MusicService;
import org.messic.server.api.dlna.chii2.mediaserver.api.content.ContentManager;
import org.messic.server.api.dlna.chii2.mediaserver.api.content.container.VisualContainer;
import org.messic.server.api.dlna.chii2.mediaserver.api.upnp.Filter;
import org.messic.server.api.dlna.chii2.mediaserver.content.common.CommonContentManager;
import org.messic.server.datamodel.MDOAuthor;
import org.messic.server.datamodel.MDOUser;

public class MessicUserContainer
    extends VisualContainer
{
    public MusicService ms;

    /**
     * Constructor
     * 
     * @param filter Content Filter
     */
    public MessicUserContainer( Filter filter, Long userSid, String userName, MusicService ms )
    {
        this( filter, "" + userSid, CommonContentManager.ROOT_ID, userName, ms );
    }

    /**
     * Constructor
     * 
     * @param filter Content Filter
     * @param id Container ID
     * @param parentId Parent ID
     */
    public MessicUserContainer( Filter filter, String id, String parentId, String username, MusicService ms )
    {
        super();

        this.ms = ms;

        // Pictures Container ID: 3
        setId( id );
        // Parent container is Root Container
        setParentID( parentId );
        // Title TODO: This should be I18N
        setTitle( username );
        // May used in Container Property Creator (part of UPnP protocol standard)
        setCreator( "System" );
        // May used in Container Property Clazz (part of UPnP protocol standard)
        setClazz( new DIDLObject.Class( "object.container" ) );
        // Restricted
        setRestricted( true );
        // Searchable
        setSearchable( false );
        // Writable
        setWriteStatus( WriteStatus.NOT_WRITABLE );
    }

    @Override
    public void loadContents( long startIndex, long maxCount, SortCriterion[] orderBy, ContentManager contentManager )
    {
        this.ms.getAuthors( getId(), startIndex, maxCount, this );
    }

}
