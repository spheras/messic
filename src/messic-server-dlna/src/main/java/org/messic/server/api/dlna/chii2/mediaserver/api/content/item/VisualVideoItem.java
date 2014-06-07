package org.messic.server.api.dlna.chii2.mediaserver.api.content.item;

import org.fourthline.cling.support.model.item.VideoItem;
import org.messic.server.api.dlna.chii2.mediaserver.api.upnp.Filter;

/**
 * Visual Video Item
 */
public abstract class VisualVideoItem extends VideoItem {
    // Filter
    protected Filter filter;
}
