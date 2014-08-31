package org.messic.configuration;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class MessicVersionTest
{
    @Test
    public void testConstruction()
    {
        MessicVersion mv = new MessicVersion( "1.0.3" );
        Assert.assertTrue( mv.version == 1 );
        Assert.assertTrue( mv.revision == 0 );
        Assert.assertTrue( mv.patch == 3 );
        Assert.assertTrue( mv.semantic.equals( "" ) );
        Assert.assertTrue( mv.semanticPatch == 0 );

        mv = new MessicVersion( "5.3.1" );
        Assert.assertTrue( mv.version == 5 );
        Assert.assertTrue( mv.revision == 3 );
        Assert.assertTrue( mv.patch == 1 );
        Assert.assertTrue( mv.semantic.equals( "" ) );
        Assert.assertTrue( mv.semanticPatch == 0 );

        mv = new MessicVersion( "111.3.0-alpha" );
        Assert.assertTrue( mv.version == 111 );
        Assert.assertTrue( mv.revision == 3 );
        Assert.assertTrue( mv.patch == 0 );
        Assert.assertTrue( mv.semantic.equals( "alpha" ) );
        Assert.assertTrue( mv.semanticPatch == 0 );

        mv = new MessicVersion( "0.0.0-beta" );
        Assert.assertTrue( mv.version == 0 );
        Assert.assertTrue( mv.revision == 0 );
        Assert.assertTrue( mv.patch == 0 );
        Assert.assertTrue( mv.semantic.equals( "beta" ) );
        Assert.assertTrue( mv.semanticPatch == 0 );

        mv = new MessicVersion( "3.1.0-rc" );
        Assert.assertTrue( mv.version == 3 );
        Assert.assertTrue( mv.revision == 1 );
        Assert.assertTrue( mv.patch == 0 );
        Assert.assertTrue( mv.semantic.equals( "rc" ) );
        Assert.assertTrue( mv.semanticPatch == 0 );

        mv = new MessicVersion( "111.3.0-other" );
        Assert.assertTrue( mv.version == 111 );
        Assert.assertTrue( mv.revision == 3 );
        Assert.assertTrue( mv.patch == 0 );
        Assert.assertTrue( mv.semantic.equals( "other" ) );
        Assert.assertTrue( mv.semanticPatch == 0 );

        mv = new MessicVersion( "111.3.0-beta.42" );
        Assert.assertTrue( mv.version == 111 );
        Assert.assertTrue( mv.revision == 3 );
        Assert.assertTrue( mv.patch == 0 );
        Assert.assertTrue( mv.semantic.equals( "beta" ) );
        Assert.assertTrue( mv.semanticPatch == 42 );

        mv = new MessicVersion( "1.1.0-alpha.122" );
        Assert.assertTrue( mv.version == 1 );
        Assert.assertTrue( mv.revision == 1 );
        Assert.assertTrue( mv.patch == 0 );
        Assert.assertTrue( mv.semantic.equals( "alpha" ) );
        Assert.assertTrue( mv.semanticPatch == 122 );
    }

    @Test
    public void testCompare()
    {
        MessicVersion mv01 = new MessicVersion( "1.0.0-alpha" );
        MessicVersion mv02 = new MessicVersion( "1.0.0-alpha.5" );
        MessicVersion mv03 = new MessicVersion( "1.0.0-beta.4" );
        MessicVersion mv04 = new MessicVersion( "1.0.0-beta.51" );
        MessicVersion mv05 = new MessicVersion( "1.0.0-rc" );
        MessicVersion mv06 = new MessicVersion( "1.0.0-rc.2" );
        MessicVersion mv07 = new MessicVersion( "1.0.0" );
        MessicVersion mv08 = new MessicVersion( "1.0.1-alpha" );
        MessicVersion mv09 = new MessicVersion( "1.0.1-alpha.5" );
        MessicVersion mv10 = new MessicVersion( "1.0.1-beta" );
        MessicVersion mv11 = new MessicVersion( "1.0.1-beta.5" );
        MessicVersion mv12 = new MessicVersion( "1.0.1-beta.12" );
        MessicVersion mv13 = new MessicVersion( "1.0.1-rc" );
        MessicVersion mv14 = new MessicVersion( "1.0.1-rc.2" );
        MessicVersion mv15 = new MessicVersion( "1.5.0" );
        MessicVersion mv16 = new MessicVersion( "2.0.0-alpha.2" );
        MessicVersion mv17 = new MessicVersion( "2.0.0" );

        List<MessicVersion> list = new ArrayList<MessicVersion>();
        list.add( mv01 );
        list.add( mv02 );
        list.add( mv03 );
        list.add( mv04 );
        list.add( mv05 );
        list.add( mv06 );
        list.add( mv07 );
        list.add( mv08 );
        list.add( mv09 );
        list.add( mv10 );
        list.add( mv11 );
        list.add( mv12 );
        list.add( mv13 );
        list.add( mv14 );
        list.add( mv15 );
        list.add( mv16 );
        list.add( mv17 );

        for ( int i = 0; i < list.size(); i++ )
        {
            MessicVersion thismv = list.get( i );
            for ( int j = 0; j < list.size(); j++ )
            {
                MessicVersion othermv = list.get( j );
                if ( i == j )
                {
                    Assert.assertTrue( thismv.equals( othermv ) );
                    Assert.assertTrue( thismv.compareTo( othermv ) == 0 );
                }
                else if ( i < j )
                {
                    Assert.assertTrue( !thismv.equals( othermv ) );
                    Assert.assertTrue( thismv.compareTo( othermv ) < 0 );
                }
                else
                {
                    Assert.assertTrue( !thismv.equals( othermv ) );
                    Assert.assertTrue( thismv.compareTo( othermv ) > 0 );
                }
            }
        }

    }
}
