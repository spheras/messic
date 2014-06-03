package org.messic.server.api.dlna.chii2.medialibrary.api.persistence.entity;

import java.util.Date;

/**
 * Represent a image file on the disk
 */
public interface ImageFile {

    /**
     * Get the id
     *
     * @return id
     */
    public String getId();

    /**
     * Set the id
     *
     * @param id ID
     */
    public void setId(String id);

    /**
     * Get Image File Name
     *
     * @return File Name
     */
    public String getFileName();

    /**
     * Set Image File Name
     *
     * @param fileName File Name
     */
    public void setFileName(String fileName);

    /**
     * Get Image File Path (Parent Directory)
     *
     * @return File Path (Parent Directory)
     */
    public String getFilePath();

    /**
     * Set Image File Path (Parent Directory)
     *
     * @param filePath File Path (Parent Directory)
     */
    public void setFilePath(String filePath);

    /**
     * Get Image Absolute File Path
     *
     * @return Absolute File Path
     */
    public String getAbsolutePath();

    /**
     * Set Image Absolute File Path
     *
     * @param absolutePath Absolute File Path
     */
    public void setAbsolutePath(String absolutePath);

    /**
     * Get Image File Extension
     *
     * @return File Extension
     */
    public String getFileExtension();

    /**
     * Set Image File Extension
     *
     * @param fileExtension Movie File Extension
     */
    public void setFileExtension(String fileExtension);

    /**
     * Get Image Type
     *
     * @return Type
     */
    public String getType();

    /**
     * Set Image Type
     *
     * @param type Type
     */
    public void setType(String type);

    /**
     * Get Image Size
     *
     * @return Size
     */
    public long getSize();

    /**
     * Set Image Size
     *
     * @param size Size
     */
    public void setSize(long size);

    /**
     * Get Image Width
     *
     * @return Width
     */
    public int getWidth();

    /**
     * Set Image Width
     *
     * @param width Width
     */
    public void setWidth(int width);

    /**
     * Get Image Height
     *
     * @return Height
     */
    public int getHeight();

    /**
     * Set Image Height
     *
     * @param height Height
     */
    public void setHeight(int height);

    /**
     * Get Image Color Depth
     *
     * @return Color Depth
     */
    public int getColorDepth();

    /**
     * Set Image Color Depth
     *
     * @param colorDepth Color Depth
     */
    public void setColorDepth(int colorDepth);

    /**
     * Get Image Color Type
     *
     * @return Color Type
     */
    public String getColorType();

    /**
     * Set Image Color Type
     *
     * @param colorType Color Type
     */
    public void setColorType(String colorType);

    /**
     * Get Image Camera Model
     *
     * @return Camera Model
     */
    public String getCameraModel();

    /**
     * Set Image Camera Model
     *
     * @param model Camera Model
     */
    public void setCameraModel(String model);

    /**
     * Get Image taken Date
     *
     * @return Date
     */
    public Date getDateTaken();

    /**
     * Set Image taken Date
     *
     * @param date Date
     */
    public void setDateTaken(Date date);

    /**
     * Get Image Exposure Time (sec)
     *
     * @return Exposure Time
     */
    public String getExposureTime();

    /**
     * Set Image Exposure Time (sec)
     *
     * @param exposureTime Exposure Time
     */
    public void setExposureTime(String exposureTime);

    /**
     * Get Image ISO
     *
     * @return ISO
     */
    public int getISO();

    /**
     * Set Image ISO
     *
     * @param iso ISO
     */
    public void setISO(int iso);

    /**
     * Get Image Focal Length (mm)
     *
     * @return Focal Length
     */
    public String getFocalLength();

    /**
     * Set Image Focal Length (mm)
     *
     * @param focalLength Focal Length
     */
    public void setFocalLength(String focalLength);

    /**
     * Get Image User Comment
     *
     * @return User Comment
     */
    public String getUserComment();

    /**
     * Set Image User Comment
     *
     * @param userComment User Comment
     */
    public void setUserComment(String userComment);

    /**
     * Get Image Width DPI
     *
     * @return Width DPI
     */
    public int getWidthDPI();

    /**
     * Set Image Width DPI
     *
     * @param widthDPI Width DPI
     */
    public void setWidthDPI(int widthDPI);

    /**
     * Get Image Height DPI
     *
     * @return Height DPI
     */
    public int getHeightDPI();

    /**
     * Set Image Height DPI
     *
     * @param heightDPI Height DPI
     */
    public void setHeightDPI(int heightDPI);

    /**
     * Get Image MIME Type
     *
     * @return MIME Type
     */
    public String getMimeType();

    /**
     * Set Image MIME Type
     *
     * @param mimeType MIME Type
     */
    public void setMimeType(String mimeType);

    /**
     * Get Image Camera Company
     *
     * @return Camera Company
     */
    public String getCameraMaker();

    /**
     * Set Image Camera Company
     *
     * @param cameraMaker Camera Company
     */
    public void setCameraMaker(String cameraMaker);

    /**
     * Get Image F Number
     *
     * @return F Number
     */
    public String getFNumber();

    /**
     * Set Image F Number
     *
     * @param fNumber F Number
     */
    public void setFNumber(String fNumber);

    /**
     * Get Image Shutter Speed
     *
     * @return Shutter Speed
     */
    public String getShutterSpeed();

    /**
     * Set Image Shutter Speed
     *
     * @param shutterSpeed Shutter Speed
     */
    public void setShutterSpeed(String shutterSpeed);

    /**
     * Get Image Flash
     *
     * @return Flash
     */
    public int getFlash();

    /**
     * Set Image Flash
     *
     * @param flash Flash
     */
    public void setFlash(int flash);

    /**
     * Get Image Reference
     *
     * @return Image
     */
    public Image getImage();

    /**
     * Set Image Reference
     *
     * @param image Image
     */
    public void setImage(Image image);
}
