package org.messic.server.datamodel.jpaimpl;

import org.messic.server.datamodel.MDOSong;
import org.messic.server.datamodel.dao.DAOSong;
import org.springframework.stereotype.Component;

@Component
public class DAOJPASong
    extends DAOJPA<MDOSong>
    implements DAOSong
{

    public DAOJPASong()
    {
        super( MDOSong.class );
    }


}
