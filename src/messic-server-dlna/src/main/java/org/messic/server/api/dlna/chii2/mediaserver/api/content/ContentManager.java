package org.messic.server.api.dlna.chii2.mediaserver.api.content;

import java.util.List;

import org.fourthline.cling.model.message.UpnpHeaders;
import org.fourthline.cling.support.contentdirectory.DIDLParser;
import org.fourthline.cling.support.model.DIDLObject;
import org.fourthline.cling.support.model.Res;
import org.fourthline.cling.support.model.SortCriterion;
import org.messic.server.api.dlna.chii2.mediaserver.api.content.container.VisualContainer;
import org.messic.server.api.dlna.chii2.mediaserver.api.content.item.VisualPictureItem;
import org.messic.server.api.dlna.chii2.mediaserver.api.content.item.VisualVideoItem;
import org.messic.server.api.dlna.chii2.mediaserver.api.upnp.Filter;
import org.messic.server.api.dlna.chii2.mediaserver.api.upnp.SearchCriterion;

/**
 * Content Manager used for manage media content.
 * Content Directory Service use this to provide content based on different client.
 */
public interface ContentManager {
    /**
     * Get Content Manager target Client Profile
     *
     * @return Client Profile
     */
    public String getClientProfile();

    /**
     * Get UPnP/DLNA Resource for Item
     *
     * @return Resource
     */
    public Res getResource();

    /**
     * Get DIDL Parser for current Content Manager
     *
     * @return DIDL Parser
     */
    public DIDLParser getParser();

    /**
     * Whether this Content Manager match client heads.
     * This method used for choose appropriate Content Manager.
     *
     * @param headers UPnP Headers (Http Headers)
     * @return True if client match the Content Manager
     */
    public boolean isMatch(UpnpHeaders headers);

    /**
     * Browse Object (Container or Item) based on ID
     *
     * @param objectId     Object ID
     * @param filter       Filter
     * @param startIndex   Start Index
     * @param requestCount Request (Max) Count
     * @param orderBy      Sort Method
     * @return Object (Container or Item), null if not found
     */
    public DIDLObject browseObject(String objectId, Filter filter, long startIndex, long requestCount, SortCriterion[] orderBy);

    /**
     * Search Object
     *
     * @param containerId    Container ID
     * @param searchCriteria Search Criterion
     * @param filter         Filter
     * @param startIndex     Start Index
     * @param requestCount   Request (Max) Count
     * @param orderBy        Sort Method
     * @return Objects
     */
    public List<? extends DIDLObject> searchObject(String containerId, SearchCriterion searchCriteria, Filter filter, long startIndex, long requestCount, SortCriterion[] orderBy);

    /**
     * Search Total Count
     *
     * @param containerId    Container ID
     * @param searchCriteria Search Criterion
     * @param filter         Filter
     * @param startIndex     Start Index
     * @param requestCount   Request (Max) Count
     * @param orderBy        Sort Method
     * @return Total Count
     */
    public long searchCount(String containerId, SearchCriterion searchCriteria, Filter filter, long startIndex, long requestCount, SortCriterion[] orderBy);

    /**
     * Get all Pictures Storage Folder Containers in library
     *
     * @param filter     Content Filter
     * @param startIndex Start Index
     * @param maxCount   Max Results Count
     * @param orderBy    Sort Criterion
     * @return List of Pictures Storage Folder Container
     */
    public List<? extends VisualContainer> getPicturesStorageFolders(Filter filter, long startIndex, long maxCount, SortCriterion[] orderBy);

    /**
     * Get the total Pictures Storage Folder Containers count in library
     *
     * @return Count
     */
    public long getPicturesStorageFoldersCount();

    /**
     * Get pictures by album
     *
     * @param album      Picture Album
     * @param parentId   Parent Container ID
     * @param filter     Content Filter
     * @param startIndex Start Index
     * @param maxCount   Max Results Count
     * @param orderBy    Sort Criterion
     * @return List of Photo Item
     */
    public List<? extends VisualPictureItem> getPicturesByAlbum(String album, String parentId, Filter filter, long startIndex, long maxCount, SortCriterion[] orderBy);

    /**
     * Get total pictures count by album
     *
     * @param album Album
     * @return Count
     */
    public long getPicturesCountByAlbum(String album);

    /**
     * Get movies
     *
     * @param filter     Content Filer
     * @param parentId   Parent Container ID
     * @param startIndex Start Index
     * @param maxCount   Max Results Count
     * @param orderBy    Sort Criterion
     * @return List of Movie Item
     */
    public List<? extends VisualVideoItem> getMovies(String parentId, Filter filter, long startIndex, long maxCount, SortCriterion[] orderBy);

    /**
     * Get movies count
     *
     * @return Count
     */
    public long getMoviesCount();

    /**
     * Get Container's Title from Container's ID. (By removing the container prefix)
     *
     * @param id Container ID
     * @return Container Title
     */
    public String getContainerTitle(String id);

    /**
     * Get Item Parent Container ID
     *
     * @param id Item ID
     * @return Parent Container ID
     */
    public String getItemParentId(String id);

    /**
     * Get Item Library ID, which used to query item in library
     *
     * @param id Item ID
     * @return Item Library ID
     */
    public String getItemLibraryId(String id);

    /**
     * Forge Item ID from Library ID
     *
     * @param id       Library ID
     * @param parentId Parent ID
     * @param prefix   Item Prefix
     * @return Item ID
     */
    public String forgeItemId(String id, String parentId, String prefix);

    /**
     * Forge Container ID from its Title
     *
     * @param title  Container Title
     * @param prefix Container Prefix
     * @return Container ID
     */
    public String forgeContainerId(String title, String prefix);

    /**
     * ID is Root Container
     *
     * @param id ID
     * @return True if id is Root Container
     */
    public boolean isRootContainer(String id);

    /**
     * ID is Pictures Container
     *
     * @param id ID
     * @return True if id is Pictures Container
     */
    public boolean isPicturesContainer(String id);

    /**
     * ID is Pictures Folders Container
     *
     * @param id ID
     * @return True if id is Pictures Folders Container
     */
    public boolean isPicturesFoldersContainer(String id);

    /**
     * ID is Audio Container
     *
     * @param id ID
     * @return True if id is Pictures Container
     */
    public boolean isAudioContainer(String id);

    /**
     * ID is Audio Folders Container
     *
     * @param id ID
     * @return True if id is Pictures Folders Container
     */
    public boolean isAudioFoldersContainer(String id);

    /**
     * ID is Audio Storage Folder Container
     *
     * @param id ID
     * @return True if id is Pictures Storage Folder Container
     */
    public boolean isAudioStorageFolderContainer(String id);

    /**
     * ID is Pictures Storage Folder Container
     *
     * @param id ID
     * @return True if id is Pictures Storage Folder Container
     */
    public boolean isPicturesStorageFolderContainer(String id);

    /**
     * ID is Video Container
     *
     * @param id ID
     * @return True if ID is Video Container
     */
    public boolean isVideoContainer(String id);

    /**
     * ID is Video Folders Container
     *
     * @param id ID
     * @return True if ID is Video Folders Container
     */
    public boolean isVideoFoldersContainer(String id);

    /**
     * ID is Movie Base Storage Folder Container
     *
     * @param id ID
     * @return True if ID is Movie Base Storage Folder Container
     */
    public boolean isMovieBaseStorageFolderContainer(String id);
}
