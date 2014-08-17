package org.messic.server.api.dlna.chii2.mediaserver.content.common.container.messic;

import org.fourthline.cling.support.model.DIDLObject;
import org.fourthline.cling.support.model.SortCriterion;
import org.fourthline.cling.support.model.WriteStatus;
import org.messic.server.api.dlna.chii2.mediaserver.api.content.ContentManager;
import org.messic.server.api.dlna.chii2.mediaserver.api.content.container.VisualContainer;
import org.messic.server.api.dlna.chii2.mediaserver.api.upnp.Filter;
import org.messic.server.api.dlna.chii2.mediaserver.content.common.CommonContentManager;

public class EmptyVisualContainer
    extends VisualContainer
{
    /**
     * Constructor
     * 
     * @param filter Content Filter
     */
    public EmptyVisualContainer( Filter filter, String id, String label )
    {
        this( filter, id, CommonContentManager.ROOT_ID, label );
    }

    /**
     * Constructor
     * 
     * @param filter Content Filter
     * @param id Container ID
     * @param parentId Parent ID
     */
    public EmptyVisualContainer( Filter filter, String id, String parentId, String label )
    {
        super();

        // Pictures Container ID: 3
        setId( id );
        // Parent container is Root Container
        setParentID( parentId );
        // Title TODO: This should be I18N
        setTitle( label );
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
        // TODO Auto-generated method stub

    }

}
