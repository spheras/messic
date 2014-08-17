package org.messic.server.api.dlna.chii2.mediaserver.content.common.item;

import org.messic.server.api.dlna.chii2.mediaserver.api.content.item.VisualPhotoItem;
import org.messic.server.api.dlna.chii2.mediaserver.api.upnp.Filter;

/**
 * Photo Item
 */
public class PhotoItem extends VisualPhotoItem {
    /**
     * Constructor
     *
     * @param filter   Content Filter
     * @param id       Item ID
     * @param parentId Item Parent ID
     * @param title    Item Title
     * @param album    Album
     */
    public PhotoItem(Filter filter, String id, String parentId, String title, String album) {
        super();

        this.filter = filter;

        // Item ID
        setId(id);
        // Item Parent ID
        setParentID(parentId);
        // Item Title
        setTitle(title);
        // Photo Album
        if (filter.contains("upnp:album")) {
            setAlbum(album);
        }
        //  Creator (part of UPnP protocol standard)
        setCreator("System");
    }
}
