package org.messic.server.api.dlna.chii2.transcoder.core.dlna.catalog;

import org.fourthline.cling.support.model.dlna.DLNAProfiles;
import org.messic.server.api.dlna.chii2.transcoder.core.dlna.codec.ImageType;
import org.messic.server.api.dlna.chii2.transcoder.core.dlna.restriction.ImageRestriction;

/**
 * JPEG
 */
public class JPEG extends ImageCatalog {
    public JPEG() {
        // JPEG_SM_ICO
        profileMap.put(DLNAProfiles.JPEG_SM_ICO, new ImageRestriction[]{
                new TypeRestriction(ImageType.JPEG),
                new SizeRestriction(48, 48)
        });
        // JPEG_LRG_ICO
        profileMap.put(DLNAProfiles.JPEG_LRG_ICO, new ImageRestriction[]{
                new TypeRestriction(ImageType.JPEG),
                new SizeRestriction(120, 120)
        });
        // JPEG_TN
        profileMap.put(DLNAProfiles.JPEG_TN, new ImageRestriction[]{
                new TypeRestriction(ImageType.JPEG),
                new SizeRestriction(160, 160)
        });
        // JPEG_SM
        profileMap.put(DLNAProfiles.JPEG_SM, new ImageRestriction[]{
                new TypeRestriction(ImageType.JPEG),
                new SizeRestriction(640, 480)
        });
        // JPEG_MED
        profileMap.put(DLNAProfiles.JPEG_MED, new ImageRestriction[]{
                new TypeRestriction(ImageType.JPEG),
                new SizeRestriction(1024, 768)
        });
        // JPEG_LRG
        profileMap.put(DLNAProfiles.JPEG_LRG, new ImageRestriction[]{
                new TypeRestriction(ImageType.JPEG),
                new SizeRestriction(4096, 4096)
        });
    }
}
