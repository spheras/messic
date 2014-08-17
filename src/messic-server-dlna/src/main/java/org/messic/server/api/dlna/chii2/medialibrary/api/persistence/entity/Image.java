package org.messic.server.api.dlna.chii2.medialibrary.api.persistence.entity;

import java.util.Date;

/**
 * Represent a Image (which may contains a image file)
 */
public interface Image {

    /**
     * Get the id
     *
     * @return id Id
     */
    public String getId();

    /**
     * Set the id
     *
     * @param id Id
     */
    public void setId(String id);

    /**
     * Get Image Title
     *
     * @return Title
     */
    public String getTitle();

    /**
     * Set Image Title
     *
     * @param title Title
     */
    public void setTitle(String title);

    /**
     * Get Image Album
     *
     * @return Album
     */
    public String getAlbum();

    /**
     * Set Image Album
     *
     * @param album Album
     */
    public void setAlbum(String album);

    /**
     * Get Image Rating
     *
     * @return Rating
     */
    public float getRating();

    /**
     * Set Image Rating
     *
     * @param rating Rating
     */
    public void setRating(float rating);

    /**
     * Get the Image File
     *
     * @return Image File
     */
    public ImageFile getOriginalFile();

    /**
     * Set the Image File
     *
     * @param imageFile Image File
     */
    public void setOriginalFile(ImageFile imageFile);

    /**
     * Image is photo
     *
     * @return True if image is photo
     */
    public boolean isPhoto();

    /**
     * Get Image File Name
     *
     * @return File Name
     */
    public String getFileName();

    /**
     * Get Image File Path (Directory)
     *
     * @return File Path (Directory)
     */
    public String getFilePath();

    /**
     * Get Image Absolute File Name (Including Path)
     *
     * @return Absolute File Name (Including Path)
     */
    public String getAbsolutePath();

    /**
     * Get Image File Extension
     *
     * @return Movie File Extension
     */
    public String getFileExtension();

    /**
     * Get Image Type
     *
     * @return Type
     */
    public String getType();

    /**
     * Get Image Size
     *
     * @return Size
     */
    public long getSize();

    /**
     * Get Image Width
     *
     * @return Width
     */
    public int getWidth();

    /**
     * Get Image Height
     *
     * @return Height
     */
    public int getHeight();

    /**
     * Get Image Color Depth
     *
     * @return Color Depth
     */
    public int getColorDepth();

    /**
     * Get Image Color Type
     *
     * @return Color Type
     */
    public String getColorType();

    /**
     * Get Image Camera Model
     *
     * @return Camera Model
     */
    public String getCameraModel();

    /**
     * Get Image taken Date
     *
     * @return Date
     */
    public Date getDateTaken();

    /**
     * Get Image Exposure Time (sec)
     *
     * @return Exposure Time
     */
    public String getExposureTime();

    /**
     * Get Image ISO
     *
     * @return ISO
     */
    public int getISO();

    /**
     * Get Image Focal Length (mm)
     *
     * @return Focal Length
     */
    public String getFocalLength();

    /**
     * Get Image User Comment
     *
     * @return User Comment
     */
    public String getUserComment();

    /**
     * Get Image Width DPI
     *
     * @return Width DPI
     */
    public int getWidthDPI();

    /**
     * Get Image Height DPI
     *
     * @return Height DPI
     */
    public int getHeightDPI();

    /**
     * Get Image MIME Type
     *
     * @return MIME Type
     */
    public String getMimeType();

    /**
     * Get Image Camera Company
     *
     * @return Camera Company
     */
    public String getCameraMaker();

    /**
     * Get Image F Number
     *
     * @return F Number
     */
    public String getFNumber();

    /**
     * Get Image Shutter Speed
     *
     * @return Shutter Speed
     */
    public String getShutterSpeed();

    /**
     * Get Image Flash
     *
     * @return Flash
     */
    public int getFlash();
}
