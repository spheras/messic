/*
 * Copyright (c) 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.messic.server.api.musicinfo.youtube;

import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;

/**
 * Print a list of videos matching a search term.
 *
 * @author Jeremy Walker
 */
public class YoutubeSearch
{

    /**
     * Define a global variable that identifies the name of a file that contains the developer's API key.
     */
    private static final String PROPERTIES_FILENAME = "youtube.properties";

    private static final long NUMBER_OF_VIDEOS_RETURNED = 50;

    /**
     * Define a global instance of the HTTP transport.
     */
    public HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    /**
     * Define a global instance of the JSON factory.
     */
    public static final JsonFactory JSON_FACTORY = new JacksonFactory();

    /**
     * Define a global instance of a Youtube object, which will be used to make YouTube Data API requests.
     */
    private static YouTube youtube;

    public static void main( String[] args )
        throws IOException
    {
        new YoutubeSearch().search( "radiohead", null );
    }

    /**
     * Search videos at youtube by a keyword
     * 
     * @param query String keyword/s to search
     * @throws IOException
     */
    public List<YoutubeInfo> search( String query, Proxy proxy )
        throws IOException
    {
        if ( proxy != null )
        {
            HTTP_TRANSPORT = new NetHttpTransport.Builder().setProxy( proxy ).build();
        }

        // Read the developer key from the properties file.
        Properties properties = new Properties();
        try
        {
            InputStream in =
                YoutubeSearch.class.getResourceAsStream( "/org/messic/server/api/musicinfo/youtube/"
                    + PROPERTIES_FILENAME );
            properties.load( in );

        }
        catch ( IOException e )
        {
            System.err.println( "There was an error reading " + PROPERTIES_FILENAME + ": " + e.getCause() + " : "
                + e.getMessage() );
            System.exit( 1 );
        }

        // This object is used to make YouTube Data API requests. The last
        // argument is required, but since we don't need anything
        // initialized when the HttpRequest is initialized, we override
        // the interface and provide a no-op function.
        youtube = new YouTube.Builder( HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer()
        {

            @Override
            public void initialize( HttpRequest request )
                throws IOException
            {
                // TODO Auto-generated method stub

            }
        } ).setApplicationName( "messic-server-plugin-musicinfo-youtube" ).build();

        // Prompt the user to enter a query term.
        String queryTerm = query;

        // Define the API request for retrieving search results.
        YouTube.Search.List search = youtube.search().list( "id,snippet" );

        // Set your developer key from the Google Developers Console for
        // non-authenticated requests. See:
        // https://console.developers.google.com/
        String apiKey = properties.getProperty( "youtube.apikey" );
        search.setKey( apiKey );
        search.setQ( queryTerm );

        // Restrict the search results to only include videos. See:
        // https://developers.google.com/youtube/v3/docs/search/list#type
        search.setType( "video" );

        // To increase efficiency, only retrieve the fields that the
        // application uses.
        search.setFields( "items(id/kind,id/videoId,snippet/title,snippet/description,snippet/thumbnails/default/url)" );
        search.setMaxResults( NUMBER_OF_VIDEOS_RETURNED );

        // Call the API and print results.
        SearchListResponse searchResponse = search.execute();
        List<SearchResult> searchResultList = searchResponse.getItems();
        // if ( searchResultList != null )
        // {
        // prettyPrint( searchResultList.iterator(), queryTerm );
        // }
        return convertTo( searchResultList.iterator() );
    }

    private List<YoutubeInfo> convertTo( Iterator<SearchResult> iteratorSearchResults )
    {
        List<YoutubeInfo> results = new ArrayList<YoutubeInfo>();

        if ( !iteratorSearchResults.hasNext() )
        {
            return results;
        }

        while ( iteratorSearchResults.hasNext() )
        {

            SearchResult singleVideo = iteratorSearchResults.next();
            ResourceId rId = singleVideo.getId();

            // Confirm that the result represents a video. Otherwise, the
            // item will not contain a video ID.
            if ( rId.getKind().equals( "youtube#video" ) )
            {
                YoutubeInfo yi = new YoutubeInfo();
                yi.thumbnail = singleVideo.getSnippet().getThumbnails().getDefault().getUrl();
                yi.id = rId.getVideoId();
                yi.title = singleVideo.getSnippet().getTitle();
                yi.description = singleVideo.getSnippet().getDescription();
                results.add( yi );
            }
        }

        return results;

    }

    /*
     * Prints out all results in the Iterator. For each result, print the title, video ID, and thumbnail.
     * @param iteratorSearchResults Iterator of SearchResults to print
     * @param query Search query (String)
     */
    private static void prettyPrint( Iterator<SearchResult> iteratorSearchResults, String query )
    {

        System.out.println( "\n=============================================================" );
        System.out.println( "   First " + NUMBER_OF_VIDEOS_RETURNED + " videos for search on \"" + query + "\"." );
        System.out.println( "=============================================================\n" );

        if ( !iteratorSearchResults.hasNext() )
        {
            System.out.println( " There aren't any results for your query." );
        }

        while ( iteratorSearchResults.hasNext() )
        {

            SearchResult singleVideo = iteratorSearchResults.next();
            ResourceId rId = singleVideo.getId();

            // Confirm that the result represents a video. Otherwise, the
            // item will not contain a video ID.
            if ( rId.getKind().equals( "youtube#video" ) )
            {
                Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getDefault();

                System.out.println( " Video Id" + rId.getVideoId() );
                System.out.println( " Title: " + singleVideo.getSnippet().getTitle() );
                System.out.println( " Description: " + singleVideo.getSnippet().getDescription() );
                System.out.println( " Thumbnail: " + thumbnail.getUrl() );
                System.out.println( "\n-------------------------------------------------------------\n" );
            }
        }
    }
}