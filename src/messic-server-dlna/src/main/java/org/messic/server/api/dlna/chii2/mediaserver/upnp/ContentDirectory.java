package org.messic.server.api.dlna.chii2.mediaserver.upnp;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.fourthline.cling.model.message.StreamRequestMessage;
import org.fourthline.cling.model.message.UpnpHeaders;
import org.fourthline.cling.support.contentdirectory.AbstractContentDirectoryService;
import org.fourthline.cling.support.contentdirectory.ContentDirectoryErrorCode;
import org.fourthline.cling.support.contentdirectory.ContentDirectoryException;
import org.fourthline.cling.support.contentdirectory.DIDLParser;
import org.fourthline.cling.support.model.BrowseFlag;
import org.fourthline.cling.support.model.BrowseResult;
import org.fourthline.cling.support.model.DIDLContent;
import org.fourthline.cling.support.model.DIDLObject;
import org.fourthline.cling.support.model.SortCriterion;
import org.fourthline.cling.support.model.container.Container;
import org.fourthline.cling.support.model.item.Item;
import org.messic.server.api.dlna.MessicProtocolFactoryImpl;
import org.messic.server.api.dlna.MessicUpnpServiceImpl;
import org.messic.server.api.dlna.MusicService;
import org.messic.server.api.dlna.chii2.medialibrary.api.core.MediaLibraryService;
import org.messic.server.api.dlna.chii2.mediaserver.api.content.ContentManager;
import org.messic.server.api.dlna.chii2.mediaserver.api.content.container.VisualContainer;
import org.messic.server.api.dlna.chii2.mediaserver.api.http.HttpServerService;
import org.messic.server.api.dlna.chii2.mediaserver.api.upnp.Filter;
import org.messic.server.api.dlna.chii2.mediaserver.api.upnp.SearchCriterion;
import org.messic.server.api.dlna.chii2.mediaserver.content.common.CommonContentManager;
import org.messic.server.api.dlna.chii2.mediaserver.content.common.MessicCommonContentManager;
import org.messic.server.api.dlna.chii2.mediaserver.content.wmp.WMPContentManager;
import org.messic.server.api.dlna.chii2.mediaserver.content.xbox.XBoxContentManager;
import org.messic.server.api.dlna.chii2.transcoder.api.core.TranscoderService;

/**
 * ContentDirectory Service for UPnP AV/DLNA Media Server
 */
public class ContentDirectory
    extends AbstractContentDirectoryService
{
    // Media Library
    private MediaLibraryService mediaLibrary;

    // HTTP Server
    private HttpServerService httpServer;

    // Transcoder
    private TranscoderService transcoder;

    // Music service
    private MusicService musicService;

    private MessicUpnpServiceImpl musi;

    // Content Manger List
    private LinkedList<ContentManager> contentManagers;

    public ContentDirectory( MessicUpnpServiceImpl musi, MediaLibraryService ms, HttpServerService httpServer,
                             TranscoderService transcoder, MusicService musicService )
    {
        super();
        this.contentManagers = new LinkedList<ContentManager>();
        this.mediaLibrary = ms;
        this.httpServer = httpServer;
        this.transcoder = transcoder;
        this.musicService = musicService;
        this.musi = musi;
        contentManagers.add( new XBoxContentManager( this.mediaLibrary, this.httpServer, this.transcoder, musicService ) );
        contentManagers.add( new WMPContentManager( this.mediaLibrary, this.httpServer, this.transcoder, musicService ) );
        contentManagers.add( new MessicCommonContentManager( this.mediaLibrary, this.httpServer, this.transcoder,
                                                             this.musicService ) );
    }

    @Override
    public BrowseResult browse( String objectID, BrowseFlag browseFlag, String filterString, long firstResult,
                                long maxResults, SortCriterion[] orderby )
        throws ContentDirectoryException
    {
        // Client Headers
        MessicProtocolFactoryImpl mpfi = (MessicProtocolFactoryImpl) this.musi.getProtocolFactory();
        StreamRequestMessage srm = mpfi.getMessage();
        UpnpHeaders headers = srm.getHeaders();

        // Filter
        Filter filter = new Filter( filterString );
        // Content Manager based on client
        ContentManager contentManager = getContentManager( headers );
        // DIDL Parser
        DIDLParser parser = contentManager.getParser();
        // Result
        DIDLContent didlContent = new DIDLContent();
        // Search and get the object from the given id
        DIDLObject didlObject = contentManager.browseObject( objectID, filter, firstResult, maxResults, orderby );
        // Not found, return empty result
        if ( didlObject == null )
        {
            // logger.info("Object not found with ObjectID:<{}>", objectID);
            // TODO I'm not sure this is correct, maybe should throw a NO_SUCH_OBJECT exception, instead of empty result
            try
            {
                return new BrowseResult( parser.generate( didlContent ), 0, 0 );
            }
            catch ( Exception e )
            {
                throw new ContentDirectoryException( ContentDirectoryErrorCode.CANNOT_PROCESS,
                                                     ExceptionUtils.getMessage( e ) );
            }
        }

        // Number of count returned
        long numReturned = 0;
        // Total matches
        long totalMatches = 0;

        // Browse metadata
        if ( browseFlag.equals( BrowseFlag.METADATA ) )
        {
            if ( didlObject instanceof Container )
            {
                // logger.info("Browsing metadata of container:<{}>" + didlObject.getId());
                didlContent.addContainer( (Container) didlObject );
                numReturned = totalMatches = 1;
            }
            else if ( didlObject instanceof Item )
            {
                // logger.info("Browsing metadata of item:<{}>", didlObject.getId());
                didlContent.addItem( (Item) didlObject );
                numReturned = totalMatches = 1;
            }
        }
        // Browse children
        else if ( browseFlag.equals( BrowseFlag.DIRECT_CHILDREN ) )
        {
            if ( didlObject instanceof Container )
            {
                // logger.info("Browsing children of container:<{}>", didlObject.getId());
                VisualContainer container = (VisualContainer) didlObject;
                if ( container.getChildCount() <= maxResults )
                {
                    for ( Container subContainer : container.getContainers() )
                    {
                        didlContent.addContainer( subContainer );
                    }
                    for ( Item item : container.getItems() )
                    {
                        didlContent.addItem( item );
                    }
                    numReturned = container.getChildCount();
                    totalMatches = container.getTotalChildCount();
                }
                else
                {
                    // TODO Maybe should cut the extra results
                    throw new ContentDirectoryException( ContentDirectoryErrorCode.CANNOT_PROCESS,
                                                         "Returned results count exceeds max request count limit." );
                }
            }
        }

        // Return result
        // logger.info("Browsing result numReturned: <{}> and total matches: <{}>", numReturned, totalMatches);
        try
        {
            return new BrowseResult( parser.generate( didlContent ), numReturned, totalMatches );
        }
        catch ( Exception e )
        {
            throw new ContentDirectoryException( ContentDirectoryErrorCode.CANNOT_PROCESS,
                                                 ExceptionUtils.getMessage( e ) );
        }
    }

    /**
     * Get suitable Content Manager ofr client
     * 
     * @param headers Client UPnP (Http) Headers
     * @return Content Manager
     */
    protected ContentManager getContentManager( UpnpHeaders headers )
    {
        for ( ContentManager contentManager : this.contentManagers )
        {
            if ( contentManager.isMatch( headers ) )
            {
                return contentManager;
            }
        }
        return new CommonContentManager( this.mediaLibrary, this.httpServer, this.transcoder, this.musicService );
    }

    @Override
    public BrowseResult search( String containerId, String searchCriteria, String filterString, long firstResult,
                                long maxResults, SortCriterion[] orderBy )
        throws ContentDirectoryException
    {
        // logger.debug( String.format(
        // "ContentDirectory receive search request with ContainerID:%s, SearchCriteria:%s, Filter:%s, FirstResult:%s, MaxResults:%s, SortCriterion:%s.",
        // containerId, searchCriteria, filterString, firstResult, maxResults,
        // getSortCriterionString( orderBy ) ) );
        // Client Headers
        MessicProtocolFactoryImpl mpfi = (MessicProtocolFactoryImpl) this.musi.getProtocolFactory();
        StreamRequestMessage srm = mpfi.getMessage();
        UpnpHeaders headers = srm.getHeaders();
        // Filter
        Filter filter = new Filter( filterString );
        // Content Manager based on client
        ContentManager contentManager = getContentManager( headers );
        // Search Criterion
        SearchCriterion searchCriterion = SearchCriterion.parseSearchCriterion( searchCriteria );
        // DIDL Parser
        DIDLParser parser = contentManager.getParser();
        // Result
        DIDLContent didlContent = new DIDLContent();
        // Search
        List<? extends DIDLObject> didlObjects =
            contentManager.searchObject( containerId, searchCriterion, filter, firstResult, maxResults, orderBy );

        // Number of count returned
        long numReturned = 0;
        // Total matches
        long totalMatches = 0;

        // Add to results
        if ( didlObjects != null && didlObjects.size() > 0 )
        {
            for ( DIDLObject didlObject : didlObjects )
            {
                if ( didlObject instanceof Container )
                {
                    didlContent.addContainer( (Container) didlObject );
                }
                else if ( didlObject instanceof Item )
                {
                    didlContent.addItem( (Item) didlObject );
                }
            }

            numReturned = didlObjects.size();
            totalMatches = numReturned;
            // messic TODO why this???
            // contentManager.searchCount( containerId, searchCriterion, filter, firstResult, maxResults, orderBy );
        }

        // Return result
        // logger.info("Search result numReturned: <{}> and total matches: <{}>", numReturned, totalMatches);
        try
        {
            return new BrowseResult( parser.generate( didlContent ), numReturned, totalMatches );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            throw new ContentDirectoryException( ContentDirectoryErrorCode.CANNOT_PROCESS,
                                                 ExceptionUtils.getMessage( e ) );
        }
    }

}