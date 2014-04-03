package org.messic.server.api.tagwizard.service;

import java.io.File;
import java.util.List;


/**
 * Plugin to obtain tag information from songs files
 */
public interface TAGWizardPlugin
{
    /**
     * The plugin must obtain tags for the files passed.  The length of the returned list must be the same as the list of files.
     * @param files {@link File}[] list of files to discover the tags
     * @return List<TagInfo/> list of tags discovered from the files
     */
    List<SongTags> getTags(File[] files);
}
