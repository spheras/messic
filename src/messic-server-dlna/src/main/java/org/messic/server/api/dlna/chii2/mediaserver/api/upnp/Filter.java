package org.messic.server.api.dlna.chii2.mediaserver.api.upnp;

import java.util.ArrayList;
import java.util.List;

/**
 * Filter
 */
public class Filter {
    // Match all the filed
    private boolean isMatchAll = false;
    // Filed Map
    private List<String> fieldList = new ArrayList<String>();

    /**
     * Constructor
     *
     * @param filter Filter String
     */
    public Filter(String filter) {
        if (filter != null) {
            filter = filter.trim();
            if ("*".equalsIgnoreCase(filter)) {
                this.isMatchAll = true;
            } else {
                String[] fields = filter.split(",");
                if (fields != null) {
                    for (String field : fields) {
                        this.fieldList.add(field);
                    }
                }
            }
        }
    }

    /**
     * Field is contained in Filter
     *
     * @param field UPnP Field
     * @return True if field in the Filter
     */
    public boolean contains(String field) {
        return this.isMatchAll || this.fieldList.contains(field);
    }
}
