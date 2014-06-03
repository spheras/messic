package org.messic.server.api.dlna.chii2.mediaserver.content.common.item;

import org.messic.server.api.dlna.chii2.mediaserver.api.content.item.VisualVideoItem;

import java.util.List;

/**
 * Movie Item (Video Item)
 * Especially represent a movie
 */
public class MovieItem extends VisualVideoItem {
    /**
     * Constructor
     *
     * @param id       Item ID
     * @param parentId Item Parent ID
     * @param title    Item Title
     */
    public MovieItem(String id, String parentId, String title) {
        super();

        // Item ID
        setId(id);
        // Item Parent ID
        setParentID(parentId);
        // Item Title
        setTitle(title);
        //  Creator (part of UPnP protocol standard)
        setCreator("System");
    }
}
