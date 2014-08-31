package org.messic.server.facade.controllers.rest.range;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Arrays;

/**
 * This class represents a byte range.
 */
public class Range
{
    public static final long DEFAULT_EXPIRE_TIME = 604800000L; // ..ms = 1 week.

    public static final int DEFAULT_BUFFER_SIZE = 10240; // ..bytes = 10KB.

    public static final String MULTIPART_BOUNDARY = "MULTIPART_BYTERANGES";

    public long start;

    public long end;

    public long length;

    public long total;

    /**
     * Construct a byte range.
     * 
     * @param start Start of the byte range.
     * @param end End of the byte range.
     * @param total Total length of the byte source.
     */
    public Range( long start, long end, long total )
    {
        this.start = start;
        this.end = end;
        this.length = end - start + 1;
        this.total = total;
    }

    /**
     * Returns true if the given match header matches the given value.
     * 
     * @param matchHeader The match header.
     * @param toMatch The value to be matched.
     * @return True if the given match header matches the given value.
     */
    public static boolean matches( String matchHeader, String toMatch )
    {
        String[] matchValues = matchHeader.split( "\\s*,\\s*" );
        Arrays.sort( matchValues );
        return Arrays.binarySearch( matchValues, toMatch ) > -1 || Arrays.binarySearch( matchValues, "*" ) > -1;
    }

    /**
     * Returns true if the given accept header accepts the given value.
     * 
     * @param acceptHeader The accept header.
     * @param toAccept The value to be accepted.
     * @return True if the given accept header accepts the given value.
     */
    public static boolean accepts( String acceptHeader, String toAccept )
    {
        String[] acceptValues = acceptHeader.split( "\\s*(,|;)\\s*" );
        Arrays.sort( acceptValues );
        return Arrays.binarySearch( acceptValues, toAccept ) > -1
            || Arrays.binarySearch( acceptValues, toAccept.replaceAll( "/.*$", "/*" ) ) > -1
            || Arrays.binarySearch( acceptValues, "*/*" ) > -1;
    }

    /**
     * Returns a substring of the given string value from the given begin index to the given end index as a long. If the
     * substring is empty, then -1 will be returned
     * 
     * @param value The string value to return a substring as long for.
     * @param beginIndex The begin index of the substring to be returned as long.
     * @param endIndex The end index of the substring to be returned as long.
     * @return A substring of the given string value as long or -1 if substring is empty.
     */
    public static long sublong( String value, int beginIndex, int endIndex )
    {
        String substring = value.substring( beginIndex, endIndex );
        return ( substring.length() > 0 ) ? Long.parseLong( substring ) : -1;
    }

    /**
     * Close the given resource.
     * 
     * @param resource The resource to be closed.
     */
    public static void close( Closeable resource )
    {
        if ( resource != null )
        {
            try
            {
                resource.close();
            }
            catch ( IOException ignore )
            {
                // Ignore IOException. If you want to handle this anyway, it might be useful to know
                // that this will generally only be thrown when the client aborted the request.
            }
        }
    }

    /**
     * Copy the given byte range of the given input to the given output.
     * 
     * @param input The input to copy the given range to the given output for.
     * @param output The output to copy the given range from the given input for.
     * @param start Start of the byte range.
     * @param length Length of the byte range.
     * @throws IOException If something fails at I/O level.
     */
    public static void copy( RandomAccessFile input, OutputStream output, long start, long length )
        throws IOException
    {
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        int read;

        if ( input.length() == length )
        {
            // Write full range.
            while ( ( read = input.read( buffer ) ) > 0 )
            {
                output.write( buffer, 0, read );
            }
        }
        else
        {
            // Write partial range.
            input.seek( start );
            long toRead = length;

            while ( ( read = input.read( buffer ) ) > 0 )
            {
                if ( ( toRead -= read ) > 0 )
                {
                    output.write( buffer, 0, read );
                }
                else
                {
                    output.write( buffer, 0, (int) toRead + read );
                    break;
                }
            }
        }
    }

}