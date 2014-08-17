package org.messic.server.api.dlna.chii2.mediaserver.api.content.container;

import org.fourthline.cling.support.model.SortCriterion;
import org.fourthline.cling.support.model.container.Container;
import org.messic.server.api.dlna.chii2.mediaserver.api.content.ContentManager;
import org.messic.server.api.dlna.chii2.mediaserver.api.upnp.Filter;

/**
 * UPnP / DLNA Visual Container
 */
public abstract class VisualContainer extends Container {

    // Filter
    protected Filter filter;
    // Total Child Count
    protected long totalChildCount;

    /**
     * Get total child count (matches in database)
     *
     * @return Total Child Count
     */
    public long getTotalChildCount() {
        return this.totalChildCount;
    }

    /**
     * Set total child count (matches in database)
     *
     * @param totalChildCount Total Child Count
     */
    public void setTotalChildCount(long totalChildCount) {
        this.totalChildCount = totalChildCount;
    }

    /**
     * Load contents into container, this should be called before use/access container's item(or sub-container)
     *
     * @param startIndex     Start Index
     * @param maxCount       Max Results Count
     * @param orderBy        Sort Criterion
     * @param contentManager Content Manager
     */
    public abstract void loadContents(long startIndex, long maxCount, SortCriterion[] orderBy, ContentManager contentManager);
}
