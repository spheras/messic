package org.messic.server.api.dlna.chii2;

import org.messic.server.api.dlna.chii2.mediaserver.api.content.item.VisualPictureItem;
import org.messic.server.api.dlna.chii2.mediaserver.api.upnp.Filter;

/**
 * Picture Item
 */
public class PictureItem extends VisualPictureItem {
    /**
     * Constructor
     *
     * @param filter   Content Filter
     * @param id       Item ID
     * @param parentId Item Parent ID
     * @param title    Item Title
     * @param album Album
     */
    public PictureItem(Filter filter, String id, String parentId, String title, String album) {
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
