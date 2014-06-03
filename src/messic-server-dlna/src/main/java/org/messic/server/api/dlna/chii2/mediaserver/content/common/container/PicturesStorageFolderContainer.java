package org.messic.server.api.dlna.chii2.mediaserver.content.common.container;

import java.util.List;

import org.fourthline.cling.support.model.DIDLObject;
import org.fourthline.cling.support.model.SortCriterion;
import org.fourthline.cling.support.model.WriteStatus;
import org.messic.server.api.dlna.chii2.mediaserver.api.content.ContentManager;
import org.messic.server.api.dlna.chii2.mediaserver.api.content.container.VisualContainer;
import org.messic.server.api.dlna.chii2.mediaserver.api.content.item.VisualPictureItem;
import org.messic.server.api.dlna.chii2.mediaserver.api.upnp.Filter;
import org.messic.server.api.dlna.chii2.mediaserver.content.common.CommonContentManager;

/**
 * Image Container
 * Represent folders in storage device, contain pictures or sub folder
 */
public class PicturesStorageFolderContainer extends VisualContainer {

    /**
     * Constructor
     * @param filter Content Filter
     * @param id Container ID
     * @param title Container Title
     */
    public PicturesStorageFolderContainer(Filter filter, String id, String title) {
        this(filter, id, CommonContentManager.PICTURES_FOLDERS_ID, title);
    }

    /**
     * Constructor
     * @param filter Content Filter
     * @param id Container ID
     * @param parentId Container Parent ID
     * @param title Container Title
     */
    public PicturesStorageFolderContainer(Filter filter, String id, String parentId, String title) {
        super();

        this.filter = filter;

        // Pictures Storage Folder Container ID
        setId(id);
        // Parent container is Root Container
        setParentID(parentId);
        // Title
        setTitle(title);
        // May used in Container Property Creator (part of UPnP protocol standard)
        setCreator("System");
        // May used in Container Property Clazz (part of UPnP protocol standard)
        setClazz(new DIDLObject.Class("object.container.storageFolder"));
        // Restricted
        setRestricted(true);
        // Searchable
        setSearchable(false);
        // Writable
        setWriteStatus(WriteStatus.NOT_WRITABLE);
    }

    @Override
    public void loadContents(long startIndex, long maxCount, SortCriterion[] orderBy, ContentManager contentManager) {
        // Read from library
        List<? extends VisualPictureItem> pictures = contentManager.getPicturesByAlbum(getTitle(), getId(), filter, startIndex, maxCount, orderBy);
        // Total count
        long count = contentManager.getPicturesCountByAlbum(getTitle());
        // Add children
        if (pictures != null && count > 0) {
            for (VisualPictureItem picture : pictures) {
                addItem(picture);
            }
            setChildCount(pictures.size());
            setTotalChildCount(count);
        } else {
            setChildCount(0);
            setTotalChildCount(0);
        }
    }
}
