package org.messic.server.datamodel.datasource;

import org.junit.Assert;
import org.junit.Test;

public class MessicDataSourceTest
{

    @Test
    public void testSetUrl()
    {
        MessicDataSource mds = new MessicDataSource();
        String urlTest = "jdbc:h2:MESSIC_PATH/db;DB_CLOSE_DELAY=-1";
        mds.setUrl( urlTest );
        Assert.assertTrue( !mds.getUrl().equals( urlTest ) );
    }
}
