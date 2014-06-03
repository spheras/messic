package org.messic.server.api.dlna.chii2.transcoder.core.dlna.catalog;

import java.util.LinkedHashMap;
import java.util.Map;

import org.fourthline.cling.support.model.dlna.DLNAProfiles;
import org.messic.server.api.dlna.chii2.transcoder.core.dlna.codec.ImageType;
import org.messic.server.api.dlna.chii2.transcoder.core.dlna.restriction.ImageRestriction;

/**
 * Image Catalog
 */
public class ImageCatalog {
    // Profile - Restrictions Map
    protected LinkedHashMap<DLNAProfiles, ImageRestriction[]> profileMap = new LinkedHashMap<DLNAProfiles, ImageRestriction[]>();

    /**
     * Resolve DLNA Prpfile
     * @param imageType   Image Type
     * @param imageWidth  Image Width
     * @param imageHeight Image Height
     * @return DLNA Profile
     */
    public DLNAProfiles resolve(String imageType, int imageWidth, int imageHeight) {
        loop_profile:
        for (Map.Entry<DLNAProfiles, ImageRestriction[]> entry : profileMap.entrySet()) {
            for (ImageRestriction imageRestriction : entry.getValue()) {
                //noinspection unchecked
                if (!imageRestriction.pass(imageRestriction.value(imageType, imageWidth, imageHeight)))
                    continue loop_profile;
            }
            return entry.getKey();
        }
        return DLNAProfiles.NONE;
    }

    /**
     * Type Restriction
     */
    public class TypeRestriction extends ImageRestriction<String> {
        // Target Type
        private ImageType targetType;

        public TypeRestriction(ImageType targetType) {
            this.targetType = targetType;
        }

        @Override
        public String value(String imageType, int imageWidth, int imageHeight) {
            return imageType;
        }

        @Override
        public boolean pass(String value) {
            return ImageType.match(value, targetType);
        }
    }

    /**
     * Size Restriction
     */
    public class SizeRestriction extends ImageRestriction<int[]> {
        // Target Width
        private int targetWidth;
        // Target Height
        private int targetHeight;

        public SizeRestriction(int targetWidth, int targetHeight) {
            this.targetWidth = targetWidth;
            this.targetHeight = targetHeight;
        }

        @Override
        public int[] value(String imageType, int imageWidth, int imageHeight) {
            return new int[]{imageWidth, imageHeight};
        }

        @Override
        public boolean pass(int[] value) {
            return value[0] == targetWidth && value[1] == targetHeight;
        }
    }
}
