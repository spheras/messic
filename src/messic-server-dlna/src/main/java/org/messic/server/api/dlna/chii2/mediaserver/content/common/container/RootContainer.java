package org.messic.server.api.dlna.chii2.mediaserver.content.common.container;

import org.fourthline.cling.support.model.DIDLObject;
import org.fourthline.cling.support.model.SortCriterion;
import org.fourthline.cling.support.model.WriteStatus;
import org.messic.server.api.dlna.chii2.mediaserver.api.content.ContentManager;
import org.messic.server.api.dlna.chii2.mediaserver.api.content.container.VisualContainer;
import org.messic.server.api.dlna.chii2.mediaserver.api.upnp.Filter;
import org.messic.server.api.dlna.chii2.mediaserver.content.common.CommonContentManager;

/**
 * Root Container
 */
public class RootContainer extends VisualContainer {

    /**
     * Constructor
     *
     * @param filter Content Filter
     */
    public RootContainer(Filter filter) {
        this(filter, CommonContentManager.ROOT_ID, "-1");
    }

    /**
     * Constructor
     *
     * @param filter   Content Filter
     * @param id       Container ID
     * @param parentId Parent ID
     */
    public RootContainer(Filter filter, String id, String parentId) {
        super();

        this.filter = filter;

        // Root Container ID: 0
        setId(id);
        // There is no parent container for Root
        setParentID(parentId);
        // Title TODO: This should be I18N
        setTitle("Root");
        // May used in Container Property Creator (part of UPnP protocol standard)
        setCreator("System");
        // May used in Container Property Clazz (part of UPnP protocol standard)
        setClazz(new DIDLObject.Class("object.container"));
        // Restricted
        setRestricted(true);
        // Searchable
        setSearchable(false);
        // Writable
        setWriteStatus(WriteStatus.NOT_WRITABLE);
    }

    @Override
    public void loadContents(long startIndex, long maxCount, SortCriterion[] orderBy, ContentManager contentManager) {
        addContainer(new VideoContainer(filter));
        addContainer(new PicturesContainer(filter));
        setChildCount(2);
        setTotalChildCount(2);
    }
}
