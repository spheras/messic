package org.messic.server.api.tagwizard.service;

import java.io.File;
import java.util.List;

import org.messic.server.api.plugin.MessicPlugin;


/**
 * Plugin to obtain tag information from songs files
 */
public interface TAGWizardPlugin extends MessicPlugin
{
    
    /**
     * Return a set of posibilities to the album info based on the information passed.
     * @param album {@link Album} album info
     * @param files File[] list of files
     * @return List<Album/> list of album posibilities
     */
    List<Album> getAlbumInfo(Album album, File[] files);
}
