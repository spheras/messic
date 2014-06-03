package org.messic.server.api.dlna.chii2.transcoder.core.dlna.catalog;

import org.fourthline.cling.support.model.dlna.DLNAProfiles;
import org.messic.server.api.dlna.chii2.transcoder.core.dlna.codec.ImageType;
import org.messic.server.api.dlna.chii2.transcoder.core.dlna.restriction.ImageRestriction;

/**
 * PNG
 */
public class PNG extends ImageCatalog {
    public PNG() {
        // PNG_SM_ICO
        profileMap.put(DLNAProfiles.PNG_SM_ICO, new ImageRestriction[]{
                new TypeRestriction(ImageType.PNG),
                new SizeRestriction(48, 48)
        });
        // PNG_LRG_ICO
        profileMap.put(DLNAProfiles.PNG_LRG_ICO, new ImageRestriction[]{
                new TypeRestriction(ImageType.PNG),
                new SizeRestriction(120, 120)
        });
        // PNG_TN
        profileMap.put(DLNAProfiles.PNG_TN, new ImageRestriction[]{
                new TypeRestriction(ImageType.PNG),
                new SizeRestriction(160, 160)
        });
        // PNG_LRG
        profileMap.put(DLNAProfiles.PNG_LRG, new ImageRestriction[]{
                new TypeRestriction(ImageType.PNG),
                new SizeRestriction(4096, 4096)
        });
    }
}
