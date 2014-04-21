package org.messic.server.datamodel.jpaimpl;

import org.messic.server.datamodel.MDOPhysicalResource;
import org.messic.server.datamodel.dao.DAOPhysicalResource;
import org.springframework.stereotype.Component;

@Component
public class DAOJPAPhysicalResource
    extends DAOJPA<MDOPhysicalResource>
    implements DAOPhysicalResource
{

    public DAOJPAPhysicalResource()
    {
        super( MDOPhysicalResource.class );
    }



}
