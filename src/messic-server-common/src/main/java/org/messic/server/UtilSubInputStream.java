package org.messic.server;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * This class is derived on the apache SubInputStream
 * https://svn.apache.org/repos/asf/xmlgraphics/commons/trunk/src/java
 * /org/apache/xmlgraphics/util/io/SubInputStream.java
 */
public class UtilSubInputStream
    extends FilterInputStream
{

    public UtilSubInputStream( InputStream in, long from, long to )
        throws IOException
    {
        super( in );
        if(from>0){
            in.skip( from );
        }
        this.bytesToRead = to - from;
    }

    /** Indicates the number of bytes remaining to be read from the underlying InputStream. */
    private long bytesToRead;

    /** {@inheritDoc} */
    public int read()
        throws IOException
    {
        if ( bytesToRead > 0 )
        {
            int result = super.read();
            if ( result >= 0 )
            {
                bytesToRead--;
                return result;
            }
            else
            {
                return -1;
            }
        }
        else
        {
            return -1;
        }
    }

    /** {@inheritDoc} */
    public int read( byte[] b, int off, int len )
        throws IOException
    {
        if ( bytesToRead == 0 )
        {
            return -1;
        }
        int effRead = (int) Math.min( bytesToRead, len );
        // cast to int is safe because len can never be bigger than Integer.MAX_VALUE

        int result = super.read( b, off, effRead );
        if ( result >= 0 )
        {
            bytesToRead -= result;
        }
        return result;
    }

    /** {@inheritDoc} */
    public long skip( long n )
        throws IOException
    {
        long effRead = Math.min( bytesToRead, n );
        long result = super.skip( effRead );
        bytesToRead -= result;
        return result;
    }

    /** {@inheritDoc} */
    public void close()
        throws IOException
    {
        this.bytesToRead = 0;
        super.close();
    }

}
