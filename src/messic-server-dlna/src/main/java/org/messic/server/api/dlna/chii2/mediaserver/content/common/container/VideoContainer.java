package org.messic.server.api.dlna.chii2.mediaserver.content.common.container;

import org.fourthline.cling.support.model.DIDLObject;
import org.fourthline.cling.support.model.SortCriterion;
import org.fourthline.cling.support.model.WriteStatus;
import org.messic.server.api.dlna.chii2.mediaserver.api.content.ContentManager;
import org.messic.server.api.dlna.chii2.mediaserver.api.content.container.VisualContainer;
import org.messic.server.api.dlna.chii2.mediaserver.api.upnp.Filter;
import org.messic.server.api.dlna.chii2.mediaserver.content.common.CommonContentManager;

/**
 * Video Container
 * Contains all containers and items relating to video
 */
public class VideoContainer extends VisualContainer {

    /**
     * Constructor
     *
     * @param filter Content Filter
     */
    public VideoContainer(Filter filter) {
        this(filter, CommonContentManager.VIDEO_ID, CommonContentManager.ROOT_ID);
    }

    /**
     * Constructor
     *
     * @param filter   Content Filter
     * @param id       Container ID
     * @param parentId Parent ID
     */
    public VideoContainer(Filter filter, String id, String parentId) {
        super();

        this.filter = filter;

        // Video Container ID: 2
        setId(id);
        // Parent container is Root Container
        setParentID(parentId);
        // Title TODO: This should be I18N
        setTitle("Video");
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
        addContainer(new VideoFoldersContainer(filter));
        setChildCount(1);
        setTotalChildCount(1);
    }
}
