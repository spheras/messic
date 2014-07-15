/*
 * Copyright (C) 2013
 *
 *  This file is part of Messic.
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.messic.server.api.dlna;

import java.util.List;
import java.util.Map;

import org.fourthline.cling.support.model.item.Movie;
import org.messic.server.api.dlna.chii2.medialibrary.api.core.MediaLibraryService;
import org.messic.server.api.dlna.chii2.medialibrary.api.persistence.entity.Image;
import org.messic.server.api.dlna.chii2.medialibrary.api.persistence.entity.ImageFile;

public class MessicMediaLibraryService implements MediaLibraryService
{

    @Override
    public void scanAll()
    {
     System.out.println("MESSIC LIBRARY SCAN ALL!");   
    }

    @Override
    public void scanMovies()
    {
        System.out.println("MESSIC LIBRARY SCAN MOVIES!");   
        
    }

    @Override
    public void scanImages()
    {
        System.out.println("MESSIC LIBRARY SCAN IMAGES!");   
        
    }

    @Override
    public List<? extends Movie> getMovies()
    {
        System.out.println("MESSIC LIBRARY GET MOVIES!");   
        return null;
    }

    @Override
    public List<? extends Movie> getMovies( int firstResult, int maxResults )
    {
        System.out.println("MESSIC LIBRARY GET MOVIES2!");   
        return null;
    }

    @Override
    public List<? extends Movie> getMovies( int firstResult, int maxResults, Map<String, String> sorts )
    {
        System.out.println("MESSIC LIBRARY GET MOVIES3!");   
        return null;
    }

    @Override
    public Movie getMovieById( String id )
    {
        System.out.println("MESSIC LIBRARY GET MOVIE BY ID!");   
        return null;
    }

    @Override
    public List<? extends Movie> getMoviesByName( String movieName )
    {
        System.out.println("MESSIC LIBRARY GET MOVIE BY NAME!");   
        return null;
    }

    @Override
    public List<? extends Movie> getMoviesByName( String movieName, int firstResult, int maxResults )
    {
        System.out.println("MESSIC LIBRARY GET MOVIE BY NAME2!");   
        return null;
    }

    @Override
    public List<? extends Movie> getMoviesByName( String movieName, int firstResult, int maxResults,
                                                  Map<String, String> sorts )
    {
        System.out.println("MESSIC LIBRARY GET MOVIE BY NAME3!");   
        return null;
    }

    @Override
    public byte[] getMovieThumbnailById( String movieId )
    {
        System.out.println("MESSIC LIBRARY GET MOVIE THUMBNAIL BY ID!");   
       return null;
    }

    @Override
    public long getMoviesCount()
    {
        System.out.println("MESSIC LIBRARY GET MOVIES COUNT!");   
        return 0;
    }

    @Override
    public long getMovieFilesCount()
    {
        System.out.println("MESSIC LIBRARY GET MOVIES FILES COUNT!");   
        return 0;
    }

    @Override
    public List<? extends Image> getImages()
    {
        System.out.println("MESSIC LIBRARY GET IMAGES!");   
        return null;
    }

    @Override
    public List<? extends Image> getImages( int firstResult, int maxResults )
    {
        System.out.println("MESSIC LIBRARY GET IMAGES2!");   
        return null;
    }

    @Override
    public List<? extends Image> getImages( int firstResult, int maxResults, Map<String, String> sorts )
    {
        System.out.println("MESSIC LIBRARY GET IMAGES3!");   
        return null;
    }

    @Override
    public Image getImageById( String id )
    {
        System.out.println("MESSIC LIBRARY GET IMAGES2¡ BY ID!");   
        return null;
    }

    @Override
    public List<? extends Image> getImagesByField( String fieldName, String fieldValue, boolean strict )
    {
        System.out.println("MESSIC LIBRARY GET IMAGES BY FIELD!");   
        return null;
    }

    @Override
    public List<? extends Image> getImagesByField( String fieldName, String fieldValue, boolean strict,
                                                   int firstResult, int maxResults )
    {
        System.out.println("MESSIC LIBRARY GET IMAGES BY FIELD2!");   
        return null;
    }

    @Override
    public List<? extends Image> getImagesByField( String fieldName, String fieldValue, boolean strict,
                                                   int firstResult, int maxResults, Map<String, String> sorts )
    {
        System.out.println("MESSIC LIBRARY GET IMAGES BY FIELD3!");   
        return null;
    }

    @Override
    public List<String> getImageAlbums()
    {
        System.out.println("MESSIC LIBRARY GET IMAGES ALBUMS!");   
        return null;
    }

    @Override
    public List<String> getImageAlbums( int firstResult, int maxResults )
    {
        System.out.println("MESSIC LIBRARY GET IMAGE ALBUMS2!");   
        return null;
    }

    @Override
    public List<String> getImageAlbums( int firstResult, int maxResults, Map<String, String> sorts )
    {
        System.out.println("MESSIC LIBRARY GET IMAGE ALBUMS3!");   
        return null;
    }

    @Override
    public long getImagesCount()
    {
        System.out.println("MESSIC LIBRARY GET IMAGES COUNT!");   
        return 0;
    }

    @Override
    public long getImageAlbumsCount()
    {
        System.out.println("MESSIC LIBRARY GET IMAGES ALBUMS COUNT!");   
        return 0;
    }

    @Override
    public long getImagesCountByAlbum( String album )
    {
        System.out.println("MESSIC LIBRARY GET IMAGES COUNT BY ALBUM!");   
        return 0;
    }

    @Override
    public ImageFile getImageFileById( String id )
    {
        System.out.println("MESSIC LIBRARY GET IMAGE FILE BY ID!");   
        return null;
    }

}
