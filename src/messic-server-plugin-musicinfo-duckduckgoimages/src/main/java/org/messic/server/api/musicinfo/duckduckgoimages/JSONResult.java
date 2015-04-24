package org.messic.server.api.musicinfo.duckduckgoimages;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class JSONResult
{
    public String source_name;

    public int width;

    public int height;

    public String thumbnail;

    public String url;

    public String title;

    public String image;

    /**
     * @return the source_name
     */
    public String getSource_name()
    {
        return source_name;
    }

    /**
     * @param source_name the source_name to set
     */
    public void setSource_name( String source_name )
    {
        this.source_name = source_name;
    }

    /**
     * @return the width
     */
    public int getWidth()
    {
        return width;
    }

    /**
     * @param width the width to set
     */
    public void setWidth( int width )
    {
        this.width = width;
    }

    /**
     * @return the height
     */
    public int getHeight()
    {
        return height;
    }

    /**
     * @param height the height to set
     */
    public void setHeight( int height )
    {
        this.height = height;
    }

    /**
     * @return the thumbnail
     */
    public String getThumbnail()
    {
        return thumbnail;
    }

    /**
     * @param thumbnail the thumbnail to set
     */
    public void setThumbnail( String thumbnail )
    {
        this.thumbnail = thumbnail;
    }

    /**
     * @return the url
     */
    public String getUrl()
    {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl( String url )
    {
        this.url = url;
    }

    /**
     * @return the title
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle( String title )
    {
        this.title = title;
    }

    /**
     * @return the image
     */
    public String getImage()
    {
        return image;
    }

    /**
     * @param image the image to set
     */
    public void setImage( String image )
    {
        this.image = image;
    }

}
