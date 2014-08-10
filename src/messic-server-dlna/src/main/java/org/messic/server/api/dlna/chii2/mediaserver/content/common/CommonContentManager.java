package org.messic.server.api.dlna.chii2.mediaserver.content.common;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.fourthline.cling.model.message.UpnpHeaders;
import org.fourthline.cling.support.contentdirectory.DIDLParser;
import org.fourthline.cling.support.model.DIDLObject;
import org.fourthline.cling.support.model.Protocol;
import org.fourthline.cling.support.model.ProtocolInfo;
import org.fourthline.cling.support.model.Res;
import org.fourthline.cling.support.model.SortCriterion;
import org.fourthline.cling.support.model.dlna.DLNAAttribute;
import org.fourthline.cling.support.model.dlna.DLNAConversionIndicator;
import org.fourthline.cling.support.model.dlna.DLNAConversionIndicatorAttribute;
import org.fourthline.cling.support.model.dlna.DLNAFlags;
import org.fourthline.cling.support.model.dlna.DLNAFlagsAttribute;
import org.fourthline.cling.support.model.dlna.DLNAOperations;
import org.fourthline.cling.support.model.dlna.DLNAOperationsAttribute;
import org.fourthline.cling.support.model.dlna.DLNAProfileAttribute;
import org.fourthline.cling.support.model.dlna.DLNAProfiles;
import org.fourthline.cling.support.model.dlna.DLNAProtocolInfo;
import org.messic.server.api.dlna.MusicService;
import org.messic.server.api.dlna.chii2.PictureItem;
import org.messic.server.api.dlna.chii2.medialibrary.api.core.MediaLibraryService;
import org.messic.server.api.dlna.chii2.medialibrary.api.persistence.entity.Image;
import org.messic.server.api.dlna.chii2.mediaserver.api.content.ContentManager;
import org.messic.server.api.dlna.chii2.mediaserver.api.content.container.VisualContainer;
import org.messic.server.api.dlna.chii2.mediaserver.api.content.item.VisualPictureItem;
import org.messic.server.api.dlna.chii2.mediaserver.api.content.item.VisualVideoItem;
import org.messic.server.api.dlna.chii2.mediaserver.api.http.HttpServerService;
import org.messic.server.api.dlna.chii2.mediaserver.api.upnp.Filter;
import org.messic.server.api.dlna.chii2.mediaserver.api.upnp.SearchCriterion;
import org.messic.server.api.dlna.chii2.mediaserver.content.common.container.MovieBaseStorageFolderContainer;
import org.messic.server.api.dlna.chii2.mediaserver.content.common.container.MusicContainer;
import org.messic.server.api.dlna.chii2.mediaserver.content.common.container.MusicFoldersContainer;
import org.messic.server.api.dlna.chii2.mediaserver.content.common.container.PicturesContainer;
import org.messic.server.api.dlna.chii2.mediaserver.content.common.container.PicturesFoldersContainer;
import org.messic.server.api.dlna.chii2.mediaserver.content.common.container.PicturesStorageFolderContainer;
import org.messic.server.api.dlna.chii2.mediaserver.content.common.container.RootContainer;
import org.messic.server.api.dlna.chii2.mediaserver.content.common.container.VideoContainer;
import org.messic.server.api.dlna.chii2.mediaserver.content.common.container.VideoFoldersContainer;
import org.messic.server.api.dlna.chii2.mediaserver.content.common.item.PhotoItem;
import org.messic.server.api.dlna.chii2.transcoder.api.core.TranscoderService;

/**
 * Content Manger for common clients
 */
public class CommonContentManager
    implements ContentManager
{

    Logger log = Logger.getLogger( CommonContentManager.class );

    // Root Container ID
    public final static String ROOT_ID = "0";

    // Audio Container
    public final static String AUDIO_ID = "1";

    // Video Container
    public final static String VIDEO_ID = "2";

    // Pictures Container
    public final static String PICTURES_ID = "3";

    // Audio Folders Container
    public final static String AUDIO_FOLDERS_ID = "14";

    // Video Folders Container
    public final static String VIDEO_FOLDERS_ID = "15";

    // Pictures Folders Container
    public final static String PICTURES_FOLDERS_ID = "16";

    // Movie Storage Folders Container as a Base Container
    public final static String MOVIE_BASE_STORAGE_FOLDER_ID = "1501";

    // Pictures Storage Folder Container ID Prefix
    public final static String PICTURES_STORAGE_FOLDER_PREFIX = "PSFC-";

    // Picture Item ID Prefix
    public final static String PICTURE_ITEM_PREFIX = "PICI-";

    // Photo Item ID Prefix
    public final static String PHOTO_ITEM_PREFIX = "PI-";

    // Movie Item ID Prefix
    public final static String MOVIE_ITEM_PREFIX = "MI-";

    // Media Library
    protected MediaLibraryService mediaLibrary;

    // HTTP Server
    protected HttpServerService httpServer;

    // Transcoder
    protected TranscoderService transcoder;

    // Logger
    // protected Logger logger = LoggerFactory.getLogger("org.chii2.mediaserver.content");
    // UUID Length
    private int uuidLength = UUID.randomUUID().toString().length();

    protected MusicService musicService = new MusicService();

    /**
     * Constructor
     * 
     * @param mediaLibrary Media Library
     * @param httpServer Http Server
     * @param transcoder Transcoder
     */
    public CommonContentManager( MediaLibraryService mediaLibrary, HttpServerService httpServer,
                                 TranscoderService transcoder, MusicService musicService )
    {
        this.mediaLibrary = mediaLibrary;
        this.httpServer = httpServer;
        this.transcoder = transcoder;
        this.musicService = musicService;
    }

    @Override
    public String getClientProfile()
    {
        return TranscoderService.PROFILE_COMMON;
    }

    @Override
    public Res getResource()
    {
        return new Res();
    }

    @Override
    public DIDLParser getParser()
    {
        return new DIDLParser();
    }

    @Override
    public boolean isMatch( UpnpHeaders headers )
    {
        // Always return true
        return true;
    }

    @Override
    public DIDLObject browseObject( String objectId, Filter filter, long startIndex, long requestCount,
                                    SortCriterion[] orderBy )
    {
        // Root Container
        if ( isRootContainer( objectId ) )
        {
            VisualContainer container = new RootContainer( filter );
            container.loadContents( startIndex, requestCount, orderBy, this );
            return container;
        }

        /** ------------------------- Audio ------------------------- **/
        // Pictures Container
        else if ( isAudioContainer( objectId ) )
        {
            VisualContainer container = new MusicContainer( filter );
            container.loadContents( startIndex, requestCount, orderBy, this );
            return container;
        }
        // Pictures Folders Container
        else if ( isAudioFoldersContainer( objectId ) )
        {
            VisualContainer container = new MusicFoldersContainer( filter );
            container.loadContents( startIndex, requestCount, orderBy, this );
            return container;
        }

        /** ------------------------- Pictures ------------------------- **/
        // Pictures Container
        else if ( isPicturesContainer( objectId ) )
        {
            VisualContainer container = new PicturesContainer( filter );
            container.loadContents( startIndex, requestCount, orderBy, this );
            return container;
        }
        // Pictures Folders Container
        else if ( isPicturesFoldersContainer( objectId ) )
        {
            VisualContainer container = new PicturesFoldersContainer( filter );
            container.loadContents( startIndex, requestCount, orderBy, this );
            return container;
        }
        // Pictures Storage Folder Container (Dynamic)
        else if ( isPicturesStorageFolderContainer( objectId ) )
        {
            VisualContainer container =
                new PicturesStorageFolderContainer( filter, objectId, getContainerTitle( objectId ) );
            container.loadContents( startIndex, requestCount, orderBy, this );
            return container;
        }

        /** ------------------------- Video ------------------------- **/
        // Video Container
        else if ( isVideoContainer( objectId ) )
        {
            VisualContainer container = new VideoContainer( filter );
            container.loadContents( startIndex, requestCount, orderBy, this );
            return container;
        }
        // Video Folders Container
        else if ( isVideoFoldersContainer( objectId ) )
        {
            VisualContainer container = new VideoFoldersContainer( filter );
            container.loadContents( startIndex, requestCount, orderBy, this );
            return container;
        }
        // Movie Base Storage Folder Container
        else if ( isMovieBaseStorageFolderContainer( objectId ) )
        {
            VisualContainer container = new MovieBaseStorageFolderContainer( filter );
            container.loadContents( startIndex, requestCount, orderBy, this );
            return container;
        }

        // Invalid
        else
        {
            // TODO Maybe should throw a NO_SUCH_OBJECT exception, instead of null result
            return null;
        }
    }

    @Override
    public List<? extends DIDLObject> searchObject( String containerId, SearchCriterion searchCriteria, Filter filter,
                                                    long startIndex, long requestCount, SortCriterion[] orderBy )
    {
        List<? extends DIDLObject> results = null;
        switch ( searchCriteria.getSearchType() )
        {
            case SEARCH_IMAGE:
                results = this.searchImage( containerId, searchCriteria, filter, startIndex, requestCount, orderBy );
                break;
            case SEARCH_AUDIO_ALBUMS:
                log.info( "searching audio albums!" );
                return musicService.getAlbums( containerId, startIndex, requestCount, null );
            case SEARCH_AUDIO_AUTHORS:
                log.info( "searching audio authors!" );
                return musicService.getAuthors( containerId, startIndex, requestCount, null );
            case SEARCH_AUDIO_GENRE:
                log.info( "searching audio genre!" );
                break;
            case SEARCH_AUDIO:
                log.info( "searching audio songs!" );
                return musicService.getSongs( containerId, startIndex, requestCount, null );
            case SEARCH_PLAYLIST:
                log.info( "Searching playlists!" );
                return musicService.getPlaylists( containerId, startIndex, requestCount, null );
        }
        return results;
    }

    @Override
    public long searchCount( String containerId, SearchCriterion searchCriteria, Filter filter, long startIndex,
                             long requestCount, SortCriterion[] orderBy )
    {
        long result = 1;
        switch ( searchCriteria.getSearchType() )
        {
            case SEARCH_IMAGE:
                result = this.searchImageCount( containerId, searchCriteria, filter, startIndex, requestCount, orderBy );
                break;
        }
        return result;
    }

    // Search Image
    private List<? extends DIDLObject> searchImage( String containerId, SearchCriterion searchCriteria, Filter filter,
                                                    long startIndex, long maxCount, SortCriterion[] orderBy )
    {
        // Forge sort
        Map<String, String> sorts = new HashMap<String, String>();
        for ( SortCriterion sort : orderBy )
        {
            String field = null;
            if ( "dc:title".equalsIgnoreCase( sort.getPropertyName() ) )
            {
                field = "title";
            }
            else if ( "dc:date".equalsIgnoreCase( sort.getPropertyName() ) )
            {
                field = "file.date_taken";
            }
            if ( field != null )
            {
                if ( sort.isAscending() )
                {
                    sorts.put( field, "asc" );
                }
                else
                {
                    sorts.put( field, "desc" );
                }
            }
        }
        // Get images from library
        List<? extends Image> images;

        int start = -1;
        int max = -1;
        try
        {
            start = (int) startIndex;
            max = (int) maxCount;
        }
        catch ( Exception ignore )
        {
        }
        images = this.mediaLibrary.getImages( start, max, sorts );

        // Results
        List<VisualPictureItem> pictures = new ArrayList<VisualPictureItem>();
        // Create picture item and add to results
        for ( Image image : images )
        {
            // Item
            VisualPictureItem pictureItem;
            // Parent ID
            // TODO: We use PICTURES_STORAGE_FOLDER_PREFIX here, maybe not correct
            String parentId = this.forgeContainerId( image.getAlbum(), PICTURES_STORAGE_FOLDER_PREFIX );
            // ID from library
            String libraryId = image.getId();
            // ID
            String id;
            // Title
            String title = image.getTitle();
            // Photo?
            if ( image.isPhoto() )
            {
                id = forgeItemId( libraryId, parentId, PHOTO_ITEM_PREFIX );
                pictureItem = new PhotoItem( filter, id, parentId, title, image.getAlbum() );
            }
            else
            {
                id = forgeItemId( libraryId, parentId, PICTURE_ITEM_PREFIX );
                pictureItem = new PictureItem( filter, id, parentId, title, image.getAlbum() );
            }

            // Picture Date
            Date date = image.getDateTaken();
            if ( filter.contains( "dc:date" ) && date != null )
            {
                pictureItem.setDate( new SimpleDateFormat( "yyyy-MM-dd" ).format( date ) );
            }
            String comment = image.getUserComment();
            // Description
            if ( filter.contains( "dc:description" ) && StringUtils.isNotBlank( comment ) )
            {
                pictureItem.setDescription( comment );
            }
            // Long Description
            if ( filter.contains( "upnp:longDescription" ) && StringUtils.isNotBlank( comment ) )
            {
                pictureItem.setLongDescription( comment );
            }
            // Rating
            float rating = image.getRating();
            if ( filter.contains( "upnp:rating" ) && rating > 0 )
            {
                pictureItem.setRating( Float.toString( rating ) );
            }

            // Resource
            if ( this.transcoder.isValidImage( this.getClientProfile(), image.getType(), image.getWidth(),
                                               image.getHeight() ) )
            {
                // Resource
                Res originalResource = this.getResource();
                // URL
                URI originalUri = this.httpServer.forgeUrl( "image", getClientProfile(), false, libraryId );
                originalResource.setValue( originalUri.toString() );
                // Profile
                DLNAProfiles originalProfile =
                    this.transcoder.getImageTranscodedProfile( this.getClientProfile(), image.getType(),
                                                               image.getWidth(), image.getHeight() );
                // MIME
                String mime =
                    this.transcoder.getImageTranscodedMime( this.getClientProfile(), image.getType(), image.getWidth(),
                                                            image.getHeight() );
                // This should not happens
                if ( StringUtils.isBlank( mime ) )
                {
                    mime = image.getMimeType();
                    // logger.warn("Can't determine image MIME type, use {} from file information.", mime);
                }
                // DLNA Attribute
                EnumMap<DLNAAttribute.Type, DLNAAttribute> dlnaAttributes =
                    new EnumMap<DLNAAttribute.Type, DLNAAttribute>( DLNAAttribute.Type.class );
                if ( originalProfile != null && originalProfile != DLNAProfiles.NONE )
                {
                    dlnaAttributes.put( DLNAAttribute.Type.DLNA_ORG_PN, new DLNAProfileAttribute( originalProfile ) );
                }
                dlnaAttributes.put( DLNAAttribute.Type.DLNA_ORG_OP, new DLNAOperationsAttribute( DLNAOperations.RANGE ) );
                dlnaAttributes.put( DLNAAttribute.Type.DLNA_ORG_FLAGS,
                                    new DLNAFlagsAttribute( DLNAFlags.STREAMING_TRANSFER_MODE,
                                                            DLNAFlags.BACKGROUND_TRANSFERT_MODE, DLNAFlags.DLNA_V15 ) );
                originalResource.setProtocolInfo( new DLNAProtocolInfo( Protocol.HTTP_GET, ProtocolInfo.WILDCARD, mime,
                                                                        dlnaAttributes ) );
                // Resolution
                if ( filter.contains( "res@resolution" ) )
                {
                    originalResource.setResolution( image.getWidth(), image.getHeight() );
                }
                // Color Depth
                if ( filter.contains( "res@colorDepth" ) )
                {
                    originalResource.setColorDepth( (long) image.getColorDepth() );
                }
                // Size
                if ( filter.contains( "res@size" ) )
                {
                    originalResource.setSize( image.getSize() );
                }
                // Add Resource to item
                pictureItem.addResource( originalResource );
            }
            else
            {
                // TODO: Transcoded Image Handling
            }
            // Add to results
            pictures.add( pictureItem );
        }
        return pictures;
    }

    // Search Image Count
    public long searchImageCount( String containerId, SearchCriterion searchCriteria, Filter filter, long startIndex,
                                  long requestCount, SortCriterion[] orderBy )
    {
        return this.mediaLibrary.getImagesCount();
    }

    @Override
    public List<PicturesStorageFolderContainer> getPicturesStorageFolders( Filter filter, long startIndex,
                                                                           long maxCount, SortCriterion[] orderBy )
    {
        // Forge sort
        Map<String, String> sorts = new HashMap<String, String>();
        for ( SortCriterion sort : orderBy )
        {
            String field = null;
            if ( "dc:title".equalsIgnoreCase( sort.getPropertyName() ) )
            {
                field = "album";
            }
            if ( field != null )
            {
                if ( sort.isAscending() )
                {
                    sorts.put( field, "asc" );
                }
                else
                {
                    sorts.put( field, "desc" );
                }
            }
        }
        // Result
        List<PicturesStorageFolderContainer> containers = new ArrayList<PicturesStorageFolderContainer>();
        // Get image albums from Chii2 Media Library
        List<String> albums;
        try
        {
            albums = this.mediaLibrary.getImageAlbums( (int) startIndex, (int) maxCount, sorts );
        }
        catch ( IllegalArgumentException e )
        {
            albums = this.mediaLibrary.getImageAlbums( -1, -1, null );
        }

        // Add to result
        if ( albums != null )
        {
            for ( String album : albums )
            {
                if ( StringUtils.isNotEmpty( album ) )
                {
                    String id = this.forgeContainerId( album, PICTURES_STORAGE_FOLDER_PREFIX );
                    containers.add( new PicturesStorageFolderContainer( filter, id, album ) );
                }
            }
            return containers;
        }
        else
        {
            return null;
        }
    }

    @Override
    public long getPicturesStorageFoldersCount()
    {
        return this.mediaLibrary.getImageAlbumsCount();
    }

    @Override
    public List<VisualPictureItem> getPicturesByAlbum( String album, String parentId, Filter filter, long startIndex,
                                                       long maxCount, SortCriterion[] orderBy )
    {
        // Forge sort
        Map<String, String> sorts = new HashMap<String, String>();
        for ( SortCriterion sort : orderBy )
        {
            String field = null;
            if ( "dc:title".equalsIgnoreCase( sort.getPropertyName() ) )
            {
                field = "title";
            }
            else if ( "dc:date".equalsIgnoreCase( sort.getPropertyName() ) )
            {
                field = "file.date_taken";
            }
            if ( field != null )
            {
                if ( sort.isAscending() )
                {
                    sorts.put( field, "asc" );
                }
                else
                {
                    sorts.put( field, "desc" );
                }
            }
        }
        // Get images from library
        List<? extends Image> images;

        int start = -1;
        int max = -1;
        try
        {
            start = (int) startIndex;
            max = (int) maxCount;
        }
        catch ( Exception ignore )
        {
        }
        images = this.mediaLibrary.getImagesByField( "album", album, true, start, max, sorts );

        // Results
        List<VisualPictureItem> pictures = new ArrayList<VisualPictureItem>();
        // Create picture item and add to results
        for ( Image image : images )
        {
            // Item
            VisualPictureItem pictureItem;
            // ID from library
            String libraryId = image.getId();
            // ID
            String id;
            // Title
            String title = image.getTitle();
            // Photo?
            if ( image.isPhoto() )
            {
                id = forgeItemId( libraryId, parentId, PHOTO_ITEM_PREFIX );
                pictureItem = new PhotoItem( filter, id, parentId, title, image.getAlbum() );
            }
            else
            {
                id = forgeItemId( libraryId, parentId, PICTURE_ITEM_PREFIX );
                pictureItem = new PictureItem( filter, id, parentId, title, image.getAlbum() );
            }

            // Picture Date
            Date date = image.getDateTaken();
            if ( filter.contains( "dc:date" ) && date != null )
            {
                pictureItem.setDate( new SimpleDateFormat( "yyyy-MM-dd" ).format( date ) );
            }
            String comment = image.getUserComment();
            // Description
            if ( filter.contains( "dc:description" ) && StringUtils.isNotBlank( comment ) )
            {
                pictureItem.setDescription( comment );
            }
            // Long Description
            if ( filter.contains( "upnp:longDescription" ) && StringUtils.isNotBlank( comment ) )
            {
                pictureItem.setLongDescription( comment );
            }
            // Rating
            float rating = image.getRating();
            if ( filter.contains( "upnp:rating" ) && rating > 0 )
            {
                pictureItem.setRating( Float.toString( rating ) );
            }

            // Resource
            if ( this.transcoder.isValidImage( this.getClientProfile(), image.getType(), image.getWidth(),
                                               image.getHeight() ) )
            {
                // Resource
                Res originalResource = this.getResource();
                // URL
                URI originalUri = this.httpServer.forgeUrl( "image", getClientProfile(), false, libraryId );
                originalResource.setValue( originalUri.toString() );
                // Profile
                DLNAProfiles originalProfile =
                    this.transcoder.getImageTranscodedProfile( this.getClientProfile(), image.getType(),
                                                               image.getWidth(), image.getHeight() );
                // MIME
                String mime =
                    this.transcoder.getImageTranscodedMime( this.getClientProfile(), image.getType(), image.getWidth(),
                                                            image.getHeight() );
                // This should not happens
                if ( StringUtils.isBlank( mime ) )
                {
                    mime = image.getMimeType();
                    // logger.warn("Can't determine image MIME type, use {} from file information.", mime);
                }
                // DLNA Attribute
                EnumMap<DLNAAttribute.Type, DLNAAttribute> dlnaAttributes =
                    new EnumMap<DLNAAttribute.Type, DLNAAttribute>( DLNAAttribute.Type.class );
                if ( originalProfile != null && originalProfile != DLNAProfiles.NONE )
                {
                    dlnaAttributes.put( DLNAAttribute.Type.DLNA_ORG_PN, new DLNAProfileAttribute( originalProfile ) );
                }
                dlnaAttributes.put( DLNAAttribute.Type.DLNA_ORG_OP, new DLNAOperationsAttribute( DLNAOperations.NONE ) );
                dlnaAttributes.put( DLNAAttribute.Type.DLNA_ORG_FLAGS,
                                    new DLNAFlagsAttribute( DLNAFlags.STREAMING_TRANSFER_MODE,
                                                            DLNAFlags.BACKGROUND_TRANSFERT_MODE, DLNAFlags.DLNA_V15 ) );
                originalResource.setProtocolInfo( new DLNAProtocolInfo( Protocol.HTTP_GET, ProtocolInfo.WILDCARD, mime,
                                                                        dlnaAttributes ) );
                // Resolution
                if ( filter.contains( "res@resolution" ) )
                {
                    originalResource.setResolution( image.getWidth(), image.getHeight() );
                }
                // Color Depth
                if ( filter.contains( "res@colorDepth" ) )
                {
                    originalResource.setColorDepth( (long) image.getColorDepth() );
                }
                // Size
                if ( filter.contains( "res@size" ) )
                {
                    originalResource.setSize( image.getSize() );
                }
                // Add Resource to item
                pictureItem.addResource( originalResource );
            }
            else
            {
                // Resource
                Res transcodedResource = this.getResource();
                // URL
                URI transcodedUri = this.httpServer.forgeUrl( "image", getClientProfile(), true, libraryId );
                transcodedResource.setValue( transcodedUri.toString() );
                // Profile
                DLNAProfiles transcodedProfile =
                    this.transcoder.getImageTranscodedProfile( this.getClientProfile(), image.getType(),
                                                               image.getWidth(), image.getHeight() );
                // MIME
                String mime =
                    this.transcoder.getImageTranscodedMime( this.getClientProfile(), image.getType(), image.getWidth(),
                                                            image.getHeight() );
                // This should not happens
                if ( StringUtils.isBlank( mime ) )
                {
                    mime = image.getMimeType();
                    // logger.warn("Can't determine image MIME type, use {} from file information.", mime);
                }
                // DLNA Attribute
                EnumMap<DLNAAttribute.Type, DLNAAttribute> dlnaAttributes =
                    new EnumMap<DLNAAttribute.Type, DLNAAttribute>( DLNAAttribute.Type.class );
                if ( transcodedProfile != null && transcodedProfile != DLNAProfiles.NONE )
                {
                    dlnaAttributes.put( DLNAAttribute.Type.DLNA_ORG_PN, new DLNAProfileAttribute( transcodedProfile ) );
                }
                dlnaAttributes.put( DLNAAttribute.Type.DLNA_ORG_CI,
                                    new DLNAConversionIndicatorAttribute( DLNAConversionIndicator.TRANSCODED ) );
                dlnaAttributes.put( DLNAAttribute.Type.DLNA_ORG_OP, new DLNAOperationsAttribute( DLNAOperations.NONE ) );
                dlnaAttributes.put( DLNAAttribute.Type.DLNA_ORG_FLAGS,
                                    new DLNAFlagsAttribute( DLNAFlags.STREAMING_TRANSFER_MODE,
                                                            DLNAFlags.BACKGROUND_TRANSFERT_MODE, DLNAFlags.DLNA_V15 ) );
                transcodedResource.setProtocolInfo( new DLNAProtocolInfo( Protocol.HTTP_GET, ProtocolInfo.WILDCARD,
                                                                          mime, dlnaAttributes ) );
                // Resolution
                if ( filter.contains( "res@resolution" ) )
                {
                    transcodedResource.setResolution( image.getWidth(), image.getHeight() );
                }
                // Color Depth
                if ( filter.contains( "res@colorDepth" ) )
                {
                    transcodedResource.setColorDepth( (long) image.getColorDepth() );
                }
                // Add Resource to item
                pictureItem.addResource( transcodedResource );
            }
            // Add to results
            pictures.add( pictureItem );
        }
        return pictures;
    }

    @Override
    public long getPicturesCountByAlbum( String album )
    {
        return mediaLibrary.getImagesCountByAlbum( album );
    }

    @Override
    public List<? extends VisualVideoItem> getMovies( String parentId, Filter filter, long startIndex, long maxCount,
                                                      SortCriterion[] orderBy )
    {
        // TODO: finished this later
        return null;
    }

    @Override
    public long getMoviesCount()
    {
        return mediaLibrary.getMoviesCount();
    }

    @Override
    public String getContainerTitle( String id )
    {
        if ( id != null && id.indexOf( '-' ) > 0 )
        {
            return id.substring( id.indexOf( '-' ) + 1 );
        }
        else
        {
            return id;
        }
    }

    @Override
    public String getItemParentId( String id )
    {
        if ( id != null && id.indexOf( '-' ) > 0 )
        {
            String subId = id.substring( id.indexOf( '-' ) + 1 );
            if ( subId.length() > uuidLength + 1 )
            {
                return subId.substring( 0, subId.length() - uuidLength - 1 );
            }
        }

        return null;
    }

    @Override
    public String getItemLibraryId( String id )
    {
        if ( id.length() > uuidLength + 1 )
        {
            return id.substring( id.length() - uuidLength );
        }

        return null;
    }

    @Override
    public String forgeItemId( String libraryId, String parentId, String prefix )
    {
        if ( libraryId != null && libraryId.length() == uuidLength )
        {
            return prefix + parentId + "-" + libraryId;
        }
        else
        {
            return null;
        }
    }

    @Override
    public String forgeContainerId( String title, String prefix )
    {
        if ( StringUtils.isNotBlank( title ) )
        {
            return prefix + title;
        }
        else
        {
            return null;
        }
    }

    @Override
    public boolean isRootContainer( String id )
    {
        return ROOT_ID.equalsIgnoreCase( id );
    }

    @Override
    public boolean isPicturesContainer( String id )
    {
        return PICTURES_ID.equalsIgnoreCase( id );
    }

    @Override
    public boolean isPicturesFoldersContainer( String id )
    {
        return PICTURES_FOLDERS_ID.equalsIgnoreCase( id );
    }

    @Override
    public boolean isPicturesStorageFolderContainer( String id )
    {
        return id != null && id.length() > 5 && id.substring( 0, 5 ).equalsIgnoreCase( PICTURES_STORAGE_FOLDER_PREFIX );
    }

    @Override
    public boolean isAudioContainer( String id )
    {
        return AUDIO_ID.equalsIgnoreCase( id );
    }

    @Override
    public boolean isAudioFoldersContainer( String id )
    {
        return AUDIO_FOLDERS_ID.equalsIgnoreCase( id );
    }

    @Override
    public boolean isAudioStorageFolderContainer( String id )
    {
        // TODO?
        return false;
    }

    @Override
    public boolean isVideoContainer( String id )
    {
        return VIDEO_ID.equalsIgnoreCase( id );
    }

    @Override
    public boolean isVideoFoldersContainer( String id )
    {
        return VIDEO_FOLDERS_ID.equalsIgnoreCase( id );
    }

    @Override
    public boolean isMovieBaseStorageFolderContainer( String id )
    {
        return MOVIE_BASE_STORAGE_FOLDER_ID.equalsIgnoreCase( id );
    }
}
