package org.messic.server.api.dlna.chii2.mediaserver.api.upnp;

import org.apache.commons.lang.StringUtils;


/**
 * Search Criterion
 */
public class SearchCriterion {
    // Search Type
    private SearchType searchType;

    public SearchType getSearchType() {
        return searchType;
    }

    public void setSearchType(SearchType searchType) {
        this.searchType = searchType;
    }

    /**
     * Parse Search Criterion from String
     * TODO: Not Completed
     *
     * @param searchCriterionString Search Criterion String
     * @return Search Criterion
     */
    public static SearchCriterion parseSearchCriterion(String searchCriterionString) {
        SearchCriterion searchCriterion = new SearchCriterion();
        if (searchCriterionString != null) {
            String[] factors = searchCriterionString.split("(and|or)");
            for (String factor : factors) {
                factor = StringUtils.trimToEmpty(factor);
                if(factor.charAt( 0 )=='('){
                    factor=factor.substring( 1,factor.length()-1 );
                }
                String[] subFactors = factor.split("\\s", 3);
                if (subFactors != null && subFactors.length == 3) {
                    if ("upnp:class".equalsIgnoreCase(subFactors[0]) && ("=".equalsIgnoreCase(subFactors[1]) || "derivedfrom".equalsIgnoreCase(subFactors[1]))) {
                        if ("\"object.item.imageItem\"".equalsIgnoreCase(subFactors[2]) || "\"object.item.imageItem.photo\"".equalsIgnoreCase(subFactors[2])) {
                            searchCriterion.setSearchType(SearchCriterion.SearchType.SEARCH_IMAGE);
                        } else if ("\"object.item.videoItem\"".equalsIgnoreCase(subFactors[2])) {
                            searchCriterion.setSearchType(SearchCriterion.SearchType.SEARCH_VIDEO);
                        } else if ("\"object.container.playlistContainer\"".equalsIgnoreCase(subFactors[2])) {
                            searchCriterion.setSearchType(SearchCriterion.SearchType.SEARCH_PLAYLIST);
                        } else if ("\"object.item.audioItem\"".equalsIgnoreCase(subFactors[2])) {
                            searchCriterion.setSearchType(SearchCriterion.SearchType.SEARCH_AUDIO);
                        } else if ("\"object.container.album.musicAlbum\"".equalsIgnoreCase(subFactors[2])) {
                            searchCriterion.setSearchType(SearchCriterion.SearchType.SEARCH_AUDIO_ALBUMS);
                        } else if ("\"object.container.person.musicArtist\"".equalsIgnoreCase(subFactors[2])) {
                            searchCriterion.setSearchType(SearchCriterion.SearchType.SEARCH_AUDIO_AUTHORS);
                        } else if ("\"object.container.genre.musicGenre\"".equalsIgnoreCase(subFactors[2])) {
                            searchCriterion.setSearchType(SearchCriterion.SearchType.SEARCH_AUDIO_GENRE);
                        } else {
                            searchCriterion.setSearchType(SearchCriterion.SearchType.SEARCH_UNKNOWN);
                        }
                    }
                }
            }
        }
        return searchCriterion;
    }

    /**
     * Search Type
     * TODO: Not Completed
     */
    public enum SearchType {
        SEARCH_IMAGE,
        SEARCH_VIDEO,
        SEARCH_PLAYLIST,
        SEARCH_AUDIO_AUTHORS,
        SEARCH_AUDIO_ALBUMS,
        SEARCH_AUDIO_GENRE,
        SEARCH_AUDIO,
        SEARCH_UNKNOWN
    }
}
