package org.messic.starter;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Proxy;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.swing.JOptionPane;

import org.hibernate.Query;
import org.hibernate.Session;
import org.messic.configuration.MessicConfig;

import sun.tools.jps.JpsOut;

import com.mkyong.core.OSValidator;

public class Util
{
    public enum MusicFolderState
    {
        NOT_EXIST, // doesn't exist the folder, so... its ok (messic will create it)
        EXIST_EMPTY, // exist the folder, but it's empty, so... OK
        EXIST_WITH_DATABASE, // exist the folder, and there is a messic database... it should be OK, but a warning must
                             // be shown to the user
        EXIST_WITHOUT_DATABASE // exist the folder, isn't empty, and there is not a messic database... is not good
                               // place... but the user is the master.
    }

    public static void main( String[] args )
        throws Exception
    {
        String internal = getMessicInternalURL();
        System.out.println( internal );
    }

    /**
     * test if the music folder (specified at param folder) is empty, or not.. if not, if it has a messic database, ...
     * 
     * @param folder {@link String} folder to test
     * @return {@link MusicFolderState} the state of the folder
     */
    public static MusicFolderState testIfMusicFolderIsEmpty( String folder )
    {
        File f = new File( folder );
        if ( !f.exists() )
        {
            return MusicFolderState.NOT_EXIST;
        }

        File[] files = f.listFiles();
        if ( files.length > 0 )
        {
            for ( File file : files )
            {
                if ( file.isDirectory() && file.getName().trim().toUpperCase().equals( ".DATABASE" ) )
                {
                    return MusicFolderState.EXIST_WITH_DATABASE;
                }
            }
            return MusicFolderState.EXIST_WITHOUT_DATABASE;
        }
        else
        {
            return MusicFolderState.EXIST_EMPTY;
        }
    }

    public static String getMessicInternalURL()
        throws Exception
    {
        String port = getCurrentPort();
        String internalIP = getInternalIp();
        if ( internalIP.length() > "255.255.255.255".length() )
        {
            // ipv6??? TODO
            internalIP = "127.0.0.1";
        }
        return ( isSecured() ? "https" : "http" ) + "://" + internalIP + ":" + port + "/messic";
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

    public static String getCurrentPort()
    {
        File f = new File( "./currentport" );
        if ( f.exists() )
        {
            try
            {
                Properties pcurrentport = new Properties();
                pcurrentport.load( new FileInputStream( f ) );
                String currentPort = pcurrentport.getProperty( "currentPort" );
                return currentPort;
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }
            return "";
        }
        else
        {
            return "";
        }
    }

    public static boolean isSecured()
    {
        File f = new File( "./currentport" );
        if ( f.exists() )
        {
            Properties pcurrentport = new Properties();
            try
            {
                pcurrentport.load( new FileInputStream( f ) );
                String ssecured = pcurrentport.getProperty( "secured" );
                try
                {
                    Boolean secured = Boolean.valueOf( ssecured );
                    return secured;
                }
                catch ( Exception e )
                {
                    return false;
                }
            }
            catch ( FileNotFoundException e1 )
            {
                e1.printStackTrace();
                return false;
            }
            catch ( IOException e1 )
            {
                e1.printStackTrace();
                return false;
            }
        }
        else
        {
            return false;
        }
    }

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
     * address, which is not valid for network communication. Details <a
     * href="http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4665037">here</a>.
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

    public static String getExternalIp( Proxy p )
        throws Exception
    {
        URL whatismyip = new URL( "http://checkip.amazonaws.com" );
        BufferedReader in = null;
        InputStream is = null;
        try
        {
            if ( p != null )
            {
                is = whatismyip.openConnection( p ).getInputStream();
            }
            else
            {
                is = whatismyip.openConnection().getInputStream();
            }

            in = new BufferedReader( new InputStreamReader( is ) );
            String ip = in.readLine();
            return ip;
        }
        finally
        {
            if ( in != null )
            {
                try
                {
                    in.close();
                }
                catch ( IOException e )
                {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Check if messic java process is running returns the process number, -1 if not running
     * 
     * @return long
     * @throws IOException
     */
    public static boolean isMessicRunning()
        throws IOException
    {
        return ( getMessicProcess() >= 0l );
    }

    /**
     * return the messic process. -1 if non process found
     * 
     * @return long
     * @throws IOException
     */
    public static long getMessicProcess()
        throws IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream( baos );
        JpsOut.main( new String[] {}, ps );

        baos.close();
        String str = new String( baos.toByteArray() );
        if ( str.indexOf( "MessicMain" ) >= 0 )
        {
            String[] lines = str.split( "\n" );
            for ( int i = 0; i < lines.length; i++ )
            {
                if ( lines[i].indexOf( "MessicMain" ) >= 0 )
                {
                    return Long.valueOf( lines[i].split( " " )[0] );
                }
            }
            return -1;
        }
        else
        {
            return -1;
        }
    }

    public static void stopMessicService()
        throws IOException
    {
        if ( OSValidator.isWindows() )
        {
            Long messicProcessNumber = Util.getMessicProcess();
            Runtime.getRuntime().exec( new String[] { "taskkill", "/PID", "" + messicProcessNumber, "/T", "/F" } );
            try
            {
                Thread.sleep( 5000 );
            }
            catch ( InterruptedException e )
            {
                e.printStackTrace();
            }

        }
        else if ( OSValidator.isMac() || OSValidator.isUnix() )
        {
            Long messicProcessNumber = Util.getMessicProcess();
            Runtime.getRuntime().exec( new String[] { "kill", "" + messicProcessNumber } );
            try
            {
                Thread.sleep( 5000 );
            }
            catch ( InterruptedException e )
            {
                e.printStackTrace();
            }
        }

    }

    private static String getHashPassword( String newPassword )
    {
        String pass;
        MessageDigest md;
        try
        {

            byte[] hashPassword = newPassword.getBytes( "UTF-8" );

            md = MessageDigest.getInstance( "MD5" );
            byte[] encpass = md.digest( hashPassword );

            // converting byte array to Hexadecimal String
            StringBuilder sb = new StringBuilder( 2 * encpass.length );
            for ( byte b : encpass )
            {
                sb.append( String.format( "%02x", b & 0xff ) );
            }
            pass = sb.toString();

        }
        catch ( Exception e )
        {
            e.printStackTrace();
            pass = newPassword;
        }

        return pass;
    }

    public static void resetAdminPassword( Properties ml )
    {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();

        try
        {
            session.beginTransaction();

            Query query =
                session.createSQLQuery( "UPDATE USERS a set a.PASSWORD='" + getHashPassword( "12345" )
                    + "' WHERE a.ADMINISTRATOR = true" );
            query.executeUpdate();

            query = session.createSQLQuery( "SELECT LOGIN FROM USERS a WHERE a.ADMINISTRATOR = true" );
            List results = query.list();

            JOptionPane.showMessageDialog( null, ml.getProperty( "messic-password-reseted1" ) + " '" + results.get( 0 )
                + "' " + ml.getProperty( "messic-password-reseted2" ) + " '12345'" );

            JOptionPane.showMessageDialog( null, ml.getProperty( "messic-shutdown" ) );

            session.flush();
            session.getTransaction().commit();
            System.exit( 0 );
        }
        catch ( IndexOutOfBoundsException iobe )
        {
            iobe.printStackTrace();
            JOptionPane.showMessageDialog( null, ml.getProperty( "messic-password-reset-noaccount" ) );
            session.getTransaction().rollback();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog( null, ml.getProperty( "messic-password-reseted-error" ) );
            session.getTransaction().rollback();
        }
        finally
        {
            HibernateUtil.closeSessionFactory();
        }
    }

    public static Properties getMultilanguage()
    {
        Locale locale = Locale.getDefault();
        String language = locale.getLanguage();
        // language="ca"; testing catalan language
        InputStream is = Util.class.getResourceAsStream( "/language-" + language + ".properties" );
        if ( is == null )
        {
            is = Util.class.getResourceAsStream( "/language.properties" );
        }
        Properties prop = new Properties();
        try
        {
            prop.load( is );
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
        return prop;
    }

    /**
     * Start messic service
     * 
     * @throws IOException
     * @throws InterruptedException
     */
    public static void launchMessicService( final MessicLaunchedObserver observer, String javaOption, boolean debug )
        throws IOException, InterruptedException
    {
        if ( OSValidator.isWindows() || OSValidator.isMac() || OSValidator.isUnix() )
        {
            MessicConfig mc = new MessicConfig();

            File f = new File( "." );
            System.out.println( "launching from: " + f.getAbsolutePath() );

            String classpath = getClasspath( OSValidator.isWindows() );

            String[] params = null;
            String paramJava = "./bin/jre1.8.0_45/bin/java";
            if ( javaOption != null )
            {
                if ( javaOption.trim().toUpperCase().equals( "NATIVE" ) )
                {
                    paramJava = "java";
                }
                else
                {
                    paramJava = javaOption;
                }
            }
            if ( OSValidator.isUnix() )
            {
                File ftest1 = new File( "./bin/jre1.8.0_45-x64" );
                String arch = System.getProperty( "os.arch" );
                if ( arch.indexOf( "64" ) >= 0 && ftest1.exists() )
                {
                    // maybe we are using x64
                    paramJava = "./bin/jre1.8.0_45-x64/bin/java";
                }
            }
            else if ( OSValidator.isMac() )
            {
                paramJava = "./bin/jre1.8.0_45.jre/Contents/Home/bin/java";
            }

            String paramCP = "-cp";
            String paramMainClass = "org.messic.service.MessicMain";
            String paramMusicFolder = mc.getMusicFolder();
            if ( OSValidator.isWindows() )
            {
                // params =
                // new String[] { "cmd", "/c", "start", "\"MessicService\"", paramJava, paramCP, classpath,
                // paramMainClass };
                // params = new String[] { paramJava, paramCP, classpath, paramMainClass };

                params = new String[] { "wscript", ".\\bin\\messicservice.vbs", classpath, paramMusicFolder };
            }
            else
            {
                String sparams =
                    paramJava + " " + paramCP + " " + classpath + " " + paramMainClass + " \"" + paramMusicFolder
                        + "\"";
                params = new String[] { "bash", "-c", "nohup " + sparams + " &" };
            }

            final File fFlagStarted = new File( "./conf/messicStarted" );
            if ( fFlagStarted.exists() )
            {
                fFlagStarted.delete();
            }

            for ( int i = 0; i < params.length; i++ )
            {
                System.out.print( params[i] + " " );
            }
            System.out.println( "" );

            final Process p = Runtime.getRuntime().exec( params );
            // final Process p = Runtime.getRuntime().exec( params );

            if ( debug )
            {
                System.out.println( Arrays.toString( params ) );
            }
            // finally, the service process is completely detached in windows, mac and linux. So we cann't get the
            // terminal output
            // if ( OSValidator.isWindows() || true)
            // {
            Thread tWait = new Thread()
            {
                public void run()
                {
                    try
                    {
                        while ( !fFlagStarted.exists() )
                        {
                            Thread.sleep( 500 );
                        }
                        if ( observer != null )
                        {
                            observer.messicLaunched();
                        }
                        Thread.sleep( 1000 );
                    }
                    catch ( InterruptedException e )
                    {
                        e.printStackTrace();
                        if ( observer != null )
                        {
                            observer.messicLaunched();
                        }
                    }
                };
            };
            tWait.start();
            // }
            // else
            // {
            // Thread tErrorStream = new Thread()
            // {
            // public void run()
            // {
            // InputStream is = p.getErrorStream();
            //
            // StringBuffer sb = new StringBuffer();
            // byte[] buffer = new byte[1024];
            // int cant = 0;
            // try
            // {
            // cant = is.read( buffer );
            // }
            // catch ( IOException e )
            // {
            // // TODO Auto-generated catch block
            // e.printStackTrace();
            // }
            // boolean ended = false;
            // while ( cant > 0 && ended == false )
            // {
            // String news = new String( buffer, 0, cant );
            // sb.append( news );
            // System.out.print( news );
            // try
            // {
            // cant = is.read( buffer );
            // }
            // catch ( IOException e )
            // {
            // // TODO Auto-generated catch block
            // e.printStackTrace();
            // }
            // }
            // };
            // };
            // tErrorStream.start();
            //
            // Thread tInputStream = new Thread()
            // {
            // public void run()
            // {
            // InputStream is = p.getInputStream();
            //
            // StringBuffer sb = new StringBuffer();
            // byte[] buffer = new byte[1024];
            // int cant = 0;
            // try
            // {
            // cant = is.read( buffer );
            // }
            // catch ( IOException e )
            // {
            // // TODO Auto-generated catch block
            // e.printStackTrace();
            // }
            // boolean ended = false;
            // boolean launched = false;
            // while ( cant > 0 && ended == false )
            // {
            // String news = new String( buffer, 0, cant );
            // sb.append( news );
            // if ( sb.indexOf( "[MESSIC] Service started" ) >= 0 )
            // {
            // if ( observer != null & !launched )
            // {
            // observer.messicLaunched();
            // launched = true;
            // }
            // }
            // System.out.print( news );
            // try
            // {
            // cant = is.read( buffer );
            // }
            // catch ( IOException e )
            // {
            // // TODO Auto-generated catch block
            // e.printStackTrace();
            // }
            // }
            // };
            //
            // };
            // tInputStream.start();
            //
            // }

        }
        else
        {
            System.out.println( "OS not compatible with messic :(" );
        }
    }

    private static String getClasspath( boolean windows )
    {
        String result = "";
        File classpathfolder = new File( "./classpath" );
        File[] files = classpathfolder.listFiles();
        for ( int i = 0; i < files.length; i++ )
        {
            if ( files[i].getName().endsWith( ".jar" ) )
            {
                result = result + "./classpath/" + files[i].getName() + ( windows ? ";" : ":" );
            }
        }
        result = result.substring( 0, result.length() - 1 );
        return result;
    }
}
