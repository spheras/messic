package org.messic.server.datamodel.jpaimpl;

import org.messic.server.datamodel.MDOResource;
import org.messic.server.datamodel.dao.DAOPhysicalResource;
import org.springframework.stereotype.Component;

@Component
public class DAOJPAPhysicalResource
    extends DAOJPA<MDOResource>
    implements DAOPhysicalResource
{

    public DAOJPAPhysicalResource()
    {
        super( MDOResource.class );
    }


}
