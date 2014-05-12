package org.messic.server;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.messic.server.datamodel.MDOMessicSettings;
import org.messic.server.datamodel.MDOSong;
import org.messic.server.datamodel.MDOUser;
import org.messic.server.datamodel.dao.DAOUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class Util
{

    /** variable to define that the store path is the generic one (from settings) */
    public static final String GENERIC_BASE_STORE_PATH_VAR = "$(generic)";

    public static final String TEMPORAL_FOLDER = ".tmp" + File.separatorChar + "resources";

    /**
     * Temporal method to DEBUG without login TODO remove!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     * 
     * @param userDAO
     * @return
     */
    public static MDOUser getAuthentication( DAOUser userDAO )
    {
        MDOUser mdouser = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        mdouser = userDAO.getUser( ( auth != null ? auth.getName() : "" ) );
        return mdouser;
    }

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
     * Return a valid location to a file path
     * 
     * @param filename {@link String} location to convert
     * @return a valid location
     */
    public static String getValidLocation( String location )
    {
        return location; // TODO
    }

    /**
     * Obtain the path to the temporal folder for uploaded resources
     * 
     * @param user {@link MDOUser} user scope
     * @param settings {@link MDOMessicSettings} settings
     * @param albumCode {@link String} code for the album to upload
     * @return {@link String} temporal path for uploaded
     */
    public static String getTmpPath( MDOUser user, MDOMessicSettings settings, String albumCode )
    {
        String path = Util.getRealBaseStorePath( user, settings );
        path = path + File.separator + TEMPORAL_FOLDER + File.separatorChar + albumCode;
        return path;
    }

    /**
     * Get the real ubication for the songs of the user
     * 
     * @param user {@link MDOUser} user for which we are asking
     * @param settings {@link MDOMessicSettings} current settings
     * @return {@link String} the real base store path for this user
     */
    public static String getRealBaseStorePath( MDOUser user, MDOMessicSettings settings )
    {
        String userBaseStorePath = user.getStorePath();

        if ( userBaseStorePath == null || userBaseStorePath.length() == 0
            || userBaseStorePath.equals( GENERIC_BASE_STORE_PATH_VAR ) )
        {
            userBaseStorePath = GENERIC_BASE_STORE_PATH_VAR + File.separatorChar + user.getLogin();
        }

        String definitive = userBaseStorePath.replace( GENERIC_BASE_STORE_PATH_VAR, settings.getGenericBaseStorePath() );
        return definitive;

    }

    /**
     * Detach and initialize proxies of an hibernate entity. Doing this, the entity doesn't need any session to get the
     * lazy fields. (it doesn't see the internal lazy fields) -> TODO
     * 
     * @param entity
     * @return
     */
    @SuppressWarnings( "unchecked" )
    public static <T> T initializeAndUnproxy( T entity )
    {
        if ( entity == null )
        {
            throw new NullPointerException( "Entity passed for initialization is null" );
        }

        Hibernate.initialize( entity );
        if ( entity instanceof HibernateProxy )
        {
            entity = (T) ( (HibernateProxy) entity ).getHibernateLazyInitializer().getImplementation();
        }
        return entity;
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
     * Obtain the theorical filename of a song, i mean, the location that sould be, based on the track number and song
     * name (the location is the filename for songs)
     * 
     * @return {@link String} the theorical location
     */
    public static final String getSongTheoricalFileName( MDOSong song )
    {
        return Util.getValidLocation( Util.leftZeroPadding( song.getTrack(), 2 ) + "-" + song.getName() );
    }

    /**
     * Zip a set of files. The results is sent to an {@link OutputStream}
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
    
    public static void zipFolder(String folderpath, OutputStream os) throws IOException{
        String sourceFolderName =  folderpath;
 
        ZipOutputStream zos = new ZipOutputStream(os);
        //level - the compression level (0-9)
        zos.setLevel(9);
 
        zipFolder2(zos, sourceFolderName, sourceFolderName);
 
        zos.close();        
    }
    
    public static void zipFolder2(ZipOutputStream zos,String folderName,String baseFolderName) throws IOException{
        File f = new File(folderName);
        if(f.exists()){
 
            if(f.isDirectory()){
                //Thank to peter
                //For pointing out missing entry for empty folder
                if(!folderName.equalsIgnoreCase(baseFolderName)){
                    String entryName = folderName.substring(baseFolderName.length()+1,folderName.length()) + File.separatorChar;
                    System.out.println("Adding folder entry " + entryName);
                    ZipEntry ze= new ZipEntry(entryName);
                    zos.putNextEntry(ze);    
                }
                File f2[] = f.listFiles();
                for(int i=0;i<f2.length;i++){
                    zipFolder2(zos,f2[i].getAbsolutePath(),baseFolderName);    
                }
            }else{
                //add file
                //extract the relative name for entry purpose
                String entryName = folderName.substring(baseFolderName.length()+1,folderName.length());
                System.out.print("Adding file entry " + entryName + "...");
                ZipEntry ze= new ZipEntry(entryName);
                zos.putNextEntry(ze);
                FileInputStream in = new FileInputStream(folderName);
                int len;
                byte buffer[] = new byte[1024];
                while ((len = in.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }
                in.close();
                zos.closeEntry();
                System.out.println("OK!");
 
            }
        }else{
            throw new IOException("File or directory not found " + folderName);
        }
 
    }
}
