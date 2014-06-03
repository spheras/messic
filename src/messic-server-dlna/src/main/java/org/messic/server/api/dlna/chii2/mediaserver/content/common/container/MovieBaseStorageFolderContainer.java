package org.messic.server.api.dlna.chii2.mediaserver.content.common.container;

import java.util.List;

import org.fourthline.cling.support.model.DIDLObject;
import org.fourthline.cling.support.model.SortCriterion;
import org.fourthline.cling.support.model.WriteStatus;
import org.messic.server.api.dlna.chii2.mediaserver.api.content.ContentManager;
import org.messic.server.api.dlna.chii2.mediaserver.api.content.container.VisualContainer;
import org.messic.server.api.dlna.chii2.mediaserver.api.content.item.VisualVideoItem;
import org.messic.server.api.dlna.chii2.mediaserver.api.upnp.Filter;
import org.messic.server.api.dlna.chii2.mediaserver.content.common.CommonContentManager;

/**
 * Movie Storage Container
 * This is a fixed dynamic container for video storage folders. UPnP/DLNA doesn't tell difference between Movie/TV Show/Etc but we do.
 * Contains all containers and movies represent folders in storage
 */
public class MovieBaseStorageFolderContainer extends VisualContainer {

     /**
     * Constructor
     *
     * @param filter Content Filter
     */
    public MovieBaseStorageFolderContainer(Filter filter) {
        this(filter, CommonContentManager.MOVIE_BASE_STORAGE_FOLDER_ID, CommonContentManager.VIDEO_FOLDERS_ID);
    }

    /**
     * Constructor
     *
     * @param filter   Content Filter
     * @param id       Container ID
     * @param parentId Parent ID
     */
    public MovieBaseStorageFolderContainer(Filter filter, String id, String parentId) {
        super();

        this.filter = filter;

        // Movie Storage Folders Container ID: 1501
        setId(id);
        // Parent container is Video Folders Container
        setParentID(parentId);
        // Title TODO: This should be I18N
        setTitle("Movies");
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
        List<? extends VisualVideoItem> movies = contentManager.getMovies(getId(), filter, startIndex, maxCount, orderBy);
        // Total count
        long count = contentManager.getMoviesCount();
        // Add children
        if (movies != null && count > 0) {
            for (VisualVideoItem movie : movies) {
                addItem(movie);
            }
            setChildCount(movies.size());
            setTotalChildCount(count);
        } else {
            setChildCount(0);
            setTotalChildCount(0);
        }
    }
}
