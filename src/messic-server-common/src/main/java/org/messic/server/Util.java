/*
 * 
 * Copyright (C) 2013
 * 
 * This file is part of Messic.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.messic.server;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FilenameUtils;

public class Util
{

    /** variable to define that the store path is the generic one (from settings) */
    public static final String GENERIC_BASE_STORE_PATH_VAR = "$(generic)";

    /** default extension for files */
    public static final String DEFAULT_EXTENSION = "unk";

    /** temporal folder */
    public static final String TEMPORAL_FOLDER = ".tmp" + File.separatorChar + "resources";

    /** All the illegal characters for a filename or a path */
    public static final String ILLEGAL_FILENAME_CHARS_OLD = "[\\]\\[!\"#$%&()*+,/:;<=>?@\\^`{|}~]+";

    public static final String ILLEGAL_FILENAME_CHARS_NEW = "[\"%*\\/:<>?\\\\^`{|}]+";

    public static final String ILLEGAL_FILENAME_OPENENCAPSULATE_CHARS_NEW = "[<{]+";

    public static final String ILLEGAL_FILENAME_CLOSEENCAPSULATE_CHARS_NEW = "[>}]+";

    public static final String ILLEGAL_FILENAME_SEPARATOR_CHARS_NEW = "[/\\\\|:;]+";

    public static final Pattern ILLEGAL_FILENAME_CHARS_PATTERN_OLD = Pattern.compile( ILLEGAL_FILENAME_CHARS_OLD );

    public static final Pattern ILLEGAL_FILENAME_CHARS_PATTERN_NEW = Pattern.compile( ILLEGAL_FILENAME_CHARS_NEW );

    public static final Pattern ILLEGAL_FILENAME_OPENENCAPSULATE_CHARS_PATTERN_NEW =
        Pattern.compile( ILLEGAL_FILENAME_OPENENCAPSULATE_CHARS_NEW );

    public static final Pattern ILLEGAL_FILENAME_CLOSEENCAPSULATE_CHARS_PATTERN_NEW =
        Pattern.compile( ILLEGAL_FILENAME_CLOSEENCAPSULATE_CHARS_NEW );

    /**
     * Check if the string is an integer
     * 
     * @param str {@link String}
     * @return boolean true->is an integer
     */
    public static boolean isInteger( String str )
    {
        try
        {
            Integer.parseInt( str );
            return true;
        }
        catch ( NumberFormatException nfe )
        {
        }
        return false;
    }

    /**
     * Read an inputstream and return a byte[] with the whole content of the readed at the inputstream
     * 
     * @param is {@link InputStream}
     * @return byte[] content
     * @throws IOException
     */
    public static byte[] readInputStream( InputStream is )
        throws IOException
    {
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int cant = is.read( buffer );
        while ( cant > 0 )
        {
            baos.write( buffer, 0, cant );
            cant = is.read( buffer );
        }

        return baos.toByteArray();
    }

    /**
     * Read a binary file in streaming, writing directly to the outputstream
     * 
     * @param filePath
     * @param os {@link OutputStream} outputstream where the file will be writed
     * @return
     * @throws IOException
     */
    public static void streamFile( String filePath, OutputStream os )
        throws IOException
    {
        FileInputStream fis = new FileInputStream( new File( filePath ) );
        byte[] buffer = new byte[1024];
        int cant = fis.read( buffer );
        while ( cant > 0 )
        {
            os.write( buffer, 0, cant );
            cant = fis.read( buffer );
        }
        fis.close();
    }

    /**
     * Read a binary file
     * 
     * @param filePath
     * @return
     * @throws IOException
     */
    public static byte[] readFile( String filePath )
        throws IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileInputStream fis = new FileInputStream( new File( filePath ) );
        byte[] buffer = new byte[1024];
        int cant = fis.read( buffer );
        while ( cant > 0 )
        {
            baos.write( buffer, 0, cant );
            cant = fis.read( buffer );
        }
        fis.close();
        return baos.toByteArray();

    }

    /**
     * Fill at the left with zeros... example: leftZeroPadding("5",3) will return "005"
     * 
     * @param number int an integer that will be filled with zeros until complete the desired characters
     * @param howManyChar int how many chars will have the returned value
     * @return String filled
     */
    public static String leftZeroPadding( int number, int howManyChar )
    {
        return String.format( "%0" + howManyChar + "d", number );
    }

    /**
     * Return a valid location to a file path !!This is the old version which was very restrictive
     * 
     * @param filename {@link String} location to convert
     * @param replacementCharacter char char that will replace all those illegal characters
     * @return a valid location
     */
    public static String replaceIllegalFilenameCharactersOld( String filename, char replacementCharacter )
    {
        Matcher matcher = ILLEGAL_FILENAME_CHARS_PATTERN_OLD.matcher( filename );
        String result = matcher.replaceAll( replacementCharacter + "" );
        return result;
    }

    /**
     * Return if the filename or path have illegal characters !!This is the old version which was very restrictive
     * 
     * @param filename {@link String} filename or path
     * @return boolean true->it have illegal characters
     */
    public static boolean haveFilenameIllegalCharactersOld( String filename )
    {
        Matcher matcher = ILLEGAL_FILENAME_CHARS_PATTERN_OLD.matcher( filename );
        return matcher.find();
    }

    /**
     * Return a valid location to a file path
     * 
     * @param filename {@link String} location to convert
     * @param replacementCharacter char char that will replace all those illegal characters
     * @return a valid location
     */
    public static String replaceIllegalFilenameCharactersNew( String filename, char replacementCharacter )
    {
        String result = filename.replaceAll( ILLEGAL_FILENAME_OPENENCAPSULATE_CHARS_NEW, "[" );
        result = result.replaceAll( ILLEGAL_FILENAME_CLOSEENCAPSULATE_CHARS_NEW, "]" );
        result = result.replaceAll( ILLEGAL_FILENAME_SEPARATOR_CHARS_NEW, "-" );

        Matcher matcher = ILLEGAL_FILENAME_CHARS_PATTERN_NEW.matcher( result );
        result = matcher.replaceAll( replacementCharacter + "" );
        return result;
    }

    /**
     * Return if the filename or path have illegal characters
     * 
     * @param filename {@link String} filename or path
     * @return boolean true->it have illegal characters
     */
    public static boolean haveFilenameIllegalCharactersNew( String filename )
    {
        Matcher matcher = ILLEGAL_FILENAME_CHARS_PATTERN_NEW.matcher( filename );
        return matcher.find();
    }

    /**
     * Indicates if there is any number on the string... return the position of the number, -1 if none
     * 
     * @param str {@link String} to investigate
     * @return int the position of the number
     */
    public static int areThereNumbers( String str )
    {
        char[] chars = str.toCharArray();
        for ( int i = 0; i < chars.length; i++ )
        {
            char c = chars[i];
            boolean isDigit = ( c >= '0' && c <= '9' );
            if ( isDigit )
            {
                return i;
            }
        }
        return -1;
    }

    /**
     * Create an String joining all the parts from the array str, but only the elements from from, to to (both included)
     * The returned string will join the strings with a defined separator
     * 
     * @param str {@link String} string to join
     * @param from int from this element (included)
     * @param to int to this element (included)
     * @param separator {@link String} separator used
     * @return
     */
    public static String addPartsFromArray( String[] str, int from, int to, String separator )
    {
        StringBuffer result = new StringBuffer();
        for ( int i = from; i <= to; i++ )
        {
            result.append( str[i] );
            if ( i < to && separator != null )
            {
                result.append( separator );
            }
        }

        return result.toString();
    }

    /**
     * Zip a set of files. The results is sent to an {@link OutputStream}
     * 
     * @param files {@link List}<File/> list of files to be zipped
     * @param os {@link OutputStream} outputstream where the zip is created
     * @throws IOException
     */
    public static void zipFiles( List<File> files, OutputStream os )
        throws IOException
    {
        FileInputStream in = null;
        ZipOutputStream zos = new ZipOutputStream( os );

        for ( File file : files )
        {
            ZipEntry ze = new ZipEntry( file.getName() );
            zos.putNextEntry( ze );
            try
            {
                byte[] buffer = new byte[1024];
                in = new FileInputStream( file.getAbsolutePath() );
                int len;
                while ( ( len = in.read( buffer ) ) > 0 )
                {
                    zos.write( buffer, 0, len );
                }
            }
            finally
            {
                in.close();
            }
        }

    }

    /**
     * Zip folder. You can specify the baseSubfolder where everything will be compressed inside the zip. Null if you
     * want those files directly from the zip, without subfolder
     * 
     * @param folderpath folder to zip
     * @param baseSubfolder Null if you want those files directly from the zip, without subfolder
     * @param os
     * @throws IOException
     */
    public static void zipFolder( String folderpath, String baseSubfolder, OutputStream os )
        throws IOException
    {
        String sourceFolderName = folderpath;

        ZipOutputStream zos = new ZipOutputStream( os, Charset.forName( "UTF-8" ) );
        // level - the compression level (0-9)
        zos.setLevel( 9 );

        zipFolder2( zos, sourceFolderName, sourceFolderName, baseSubfolder );

        zos.close();
    }

    private static void zipFolder2( ZipOutputStream zos, String folderName, String baseFolderName,
                                    String headFolderName )
        throws IOException
    {
        File f = new File( folderName );
        if ( f.exists() )
        {

            if ( f.isDirectory() )
            {
                // Thank to peter
                // For pointing out missing entry for empty folder
                if ( !folderName.equalsIgnoreCase( baseFolderName ) )
                {
                    String entryName =
                        folderName.substring( baseFolderName.length(), folderName.length() ) + File.separatorChar;
                    ZipEntry ze = new ZipEntry( entryName );
                    zos.putNextEntry( ze );
                }
                File f2[] = f.listFiles();
                for ( int i = 0; i < f2.length; i++ )
                {
                    zipFolder2( zos, f2[i].getAbsolutePath(), baseFolderName, headFolderName );
                }
            }
            else
            {
                // add file
                // extract the relative name for entry purpose
                String entryName = folderName.substring( baseFolderName.length(), folderName.length() );
                ZipEntry ze = new ZipEntry( ( headFolderName != null ? headFolderName + "/" : "" ) + entryName );
                zos.putNextEntry( ze );
                FileInputStream in = new FileInputStream( folderName );
                int len;
                byte buffer[] = new byte[1024];
                while ( ( len = in.read( buffer ) ) > 0 )
                {
                    zos.write( buffer, 0, len );
                }
                in.close();
                zos.closeEntry();

            }
        }
        else
        {
            throw new IOException( "File or directory not found " + folderName );
        }

    }

    /**
     * List all the files that exist in a certain path (and subfolders)
     * 
     * @param basePath {@link String} absolute path to a directory to start searching
     * @param files {@link List}<File/> a list that will be filled with the existing files in the path
     */
    public static final void listFiles( String basePath, List<File> files )
    {
        File directory = new File( basePath );

        // get all the files from a directory
        File[] fList = directory.listFiles();
        if ( fList != null )
        {
            for ( File file : fList )
            {
                if ( file.isFile() )
                {
                    files.add( file );
                }
                else if ( file.isDirectory() )
                {
                    listFiles( file.getAbsolutePath(), files );
                }
            }
        }
    }

    /**
     * Make a copy of a buffered image
     * 
     * @param bi {@link BufferedImage} image to copy
     * @return {@link BufferedImage} copied
     */
    public static BufferedImage ImagedeepCopy( BufferedImage bi )
    {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData( null );
        return new BufferedImage( cm, raster, isAlphaPremultiplied, null );
    }

    /**
     * Get the internal IP of the machine where is launched Messic
     * 
     * @return
     * @throws Exception
     */
    public static String getInternalIp()
        throws Exception
    {
        InetAddress thisIp = getLocalHostLANAddress();
        return thisIp.getHostAddress().toString();
    }

    /**
     * Returns an <code>InetAddress</code> object encapsulating what is most likely the machine's LAN IP address.
     * <p/>
     * This method is intended for use as a replacement of JDK method <code>InetAddress.getLocalHost</code>, because
     * that method is ambiguous on Linux systems. Linux systems enumerate the loopback network interface the same way as
     * regular LAN network interfaces, but the JDK <code>InetAddress.getLocalHost</code> method does not specify the
     * algorithm used to select the address returned under such circumstances, and will often return the loopback
     * address, which is not valid for network communication. Details
     * <a href="http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4665037">here</a>.
     * <p/>
     * This method will scan all IP addresses on all network interfaces on the host machine to determine the IP address
     * most likely to be the machine's LAN address. If the machine has multiple IP addresses, this method will prefer a
     * site-local IP address (e.g. 192.168.x.x or 10.10.x.x, usually IPv4) if the machine has one (and will return the
     * first site-local address if the machine has more than one), but if the machine does not hold a site-local
     * address, this method will return simply the first non-loopback address found (IPv4 or IPv6).
     * <p/>
     * If this method cannot find a non-loopback address using this selection algorithm, it will fall back to calling
     * and returning the result of JDK method <code>InetAddress.getLocalHost</code>.
     * <p/>
     * 
     * @throws UnknownHostException If the LAN address of the machine cannot be found.
     */
    private static InetAddress getLocalHostLANAddress()
        throws UnknownHostException
    {
        try
        {
            InetAddress candidateAddress = null;
            // Iterate all NICs (network interface cards)...
            for ( Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements(); )
            {
                NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
                // Iterate all IP addresses assigned to each card...
                for ( Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); )
                {
                    InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
                    if ( !inetAddr.isLoopbackAddress() )
                    {

                        if ( inetAddr.isSiteLocalAddress() )
                        {
                            // Found non-loopback site-local address. Return it immediately...
                            return inetAddr;
                        }
                        else if ( candidateAddress == null )
                        {
                            // Found non-loopback address, but not necessarily site-local.
                            // Store it as a candidate to be returned if site-local address is not subsequently found...
                            candidateAddress = inetAddr;
                            // Note that we don't repeatedly assign non-loopback non-site-local addresses as candidates,
                            // only the first. For subsequent iterations, candidate will be non-null.
                        }
                    }
                }
            }
            if ( candidateAddress != null )
            {
                // We did not find a site-local address, but we found some other non-loopback address.
                // Server might have a non-site-local address assigned to its NIC (or it might be running
                // IPv6 which deprecates the "site-local" concept).
                // Return this non-loopback candidate address...
                return candidateAddress;
            }
            // At this point, we did not find a non-loopback address.
            // Fall back to returning whatever InetAddress.getLocalHost() returns...
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            if ( jdkSuppliedAddress == null )
            {
                throw new UnknownHostException( "The JDK InetAddress.getLocalHost() method unexpectedly returned null." );
            }
            return jdkSuppliedAddress;
        }
        catch ( Exception e )
        {
            UnknownHostException unknownHostException =
                new UnknownHostException( "Failed to determine LAN address: " + e );
            unknownHostException.initCause( e );
            throw unknownHostException;
        }
    }

    /**
     * http://stackoverflow.com/questions/363681/generating-random-integers-in-a-range-with-java Returns a pseudo-random
     * number between min and max, inclusive. The difference between min and max can be at most
     * <code>Integer.MAX_VALUE - 1</code>.
     * 
     * @param min Minimum value
     * @param max Maximum value. Must be greater than min.
     * @return Integer between min and max, inclusive.
     * @see java.util.Random#nextInt(int)
     */
    public static int randInt( int min, int max )
    {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt( ( max - min ) + 1 ) + min;

        return randomNum;
    }

    /**
     * Generate a new file if necessary with the filename changed in a roll way.. the new file (if it exist) will be the
     * same filename, but adding "_"+number The number will be increased each time a new file is created.
     * 
     * @param file
     * @return
     */
    public static File getRollFileNumber( File file )
    {
        File fresult = file;
        if ( !file.isDirectory() && file.exists() )
        {
            file.getName();
            String name = FilenameUtils.getBaseName( file.getName() );
            String ext = FilenameUtils.getExtension( file.getName() );
            String parent = file.getParent();
            int indexOf_ = name.lastIndexOf( "_" );
            if ( indexOf_ > 0 )
            {
                String first = name.substring( 0, indexOf_ );
                String last = name.substring( indexOf_ + 1 );
                if ( isInteger( last ) )
                {
                    int number = Integer.valueOf( last );
                    number++;
                    fresult = new File( parent + File.separatorChar + first + "_" + number + "." + ext );
                }
                else
                {
                    fresult = new File( parent + File.separatorChar + first + "_" + last + "_1." + ext );
                }
            }
            else
            {
                fresult = new File( parent + File.separatorChar + name + "_1." + ext );
            }
        }

        return fresult;
    }
}
