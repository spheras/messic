/*
 * Copyright (C) 2013
 *
 *  This file is part of Messic.
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.messic.service;

import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ServiceLoader;

import org.apache.commons.io.FileUtils;
import org.messic.configuration.MessicConfig;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;

public class MessicMain
{
    /** This is important to maintain because some others wait to this message to know that messic have been started */
    private static String END_LOG = "[MESSIC] Service started";

    /** messic config */
    private static MessicConfig mc = new MessicConfig();

    public static void main( String[] args )
        throws BundleException, InterruptedException, IOException
    {

        File f = new File( "." );
        String absolutePath = f.getAbsolutePath();
        System.setProperty( "messic.app.folder", absolutePath.substring( 0, absolutePath.length() - 2 ) );

        String musicFolder = "";
        if ( args.length > 0 )
        {
            musicFolder = args[0];
            File fMusicFolder = new File( musicFolder );
            if ( !fMusicFolder.exists() )
            {
                fMusicFolder.mkdirs();
            }
        }
        System.setProperty( "messic.musicfolder", musicFolder );

        File flagStarted = new File( "./conf/messicStarted" );
        if ( flagStarted.exists() )
        {
            flagStarted.delete();
        }

        AnimatedGifSplashScreen agss = null;
        System.out.println( "isHeadless?" + GraphicsEnvironment.isHeadless() );
        if ( !GraphicsEnvironment.isHeadless())
        {
            //agss = new AnimatedGifSplashScreen();
        }

        deleteFelixCache();
        setJettyConfig();
        final Framework framework = createFramework();
        installBundles( framework );

        Runtime.getRuntime().addShutdownHook( new Thread()
        {
            @Override
            public void run()
            {
                System.out.println( "[MESSIC] stopping service" );
                closingEvent( framework );
                System.out.println( "[MESSIC] service stopped" );
            }
        } );

        if ( agss != null )
        {
            agss.dispose();
        }

        System.out.println( END_LOG );

        // just a file to know that it has been started
        FileOutputStream fos = new FileOutputStream( flagStarted );
        fos.write( END_LOG.getBytes() );
        fos.close();
    }

    /**
     * Closing Osgi Framework Event. This the place to do things when the framework is closing.
     * 
     * @param framework {@link Framework}
     */
    private static void closingEvent( Framework framework )
    {
        stopFramework( framework );
        deleteFelixCache();
    }

    /**
     * Delete the Felix Cache folder
     */
    private static void deleteFelixCache()
    {
        File felixCache = new File( "./felix-cache" );
        if ( felixCache.exists() && felixCache.isDirectory() )
        {
            try
            {
                FileUtils.deleteDirectory( felixCache );
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * Stop the Osgi framework
     * 
     * @param framework {@link Framework} to stop
     */
    private static void stopFramework( Framework framework )
    {
        try
        {
            framework.stop();
            framework.waitForStop( 30000 );
            System.out.println( "OSGI Framework Stopped!" );
        }
        catch ( InterruptedException ie )
        {
            ie.printStackTrace();
        }
        catch ( BundleException be )
        {
            be.printStackTrace();
        }
    }

    /**
     * Create an osgi framework
     * 
     * @return {@link Framework} osgi framework created
     * @throws BundleException
     */
    private static Framework createFramework()
        throws BundleException
    {
        FrameworkFactory frameworkFactory = ServiceLoader.load( FrameworkFactory.class ).iterator().next();
        Framework framework = frameworkFactory.newFramework( getFelixConfig() );
        framework.start();
        return framework;
    }

    /**
     * Install the bundles at the osgi framework.
     * 
     * @param framework {@link Framework} to install bundles
     * @throws BundleException
     */
    private static void installBundles( Framework framework )
        throws BundleException
    {
        BundleContext context = framework.getBundleContext();
        List<Bundle> installedBundles = new LinkedList<Bundle>();

        installedBundles = installFolderBundles( context, "./bundles" );

        for ( Bundle bundle : installedBundles )
        {
            if ( bundle.getLocation().indexOf( "-ns-" ) <= 0 )
            {
                System.out.println( "Starting " + bundle.getLocation() );
                bundle.start();
            }
            else
            {
                System.out.println( "NOT Starting " + bundle.getLocation() );
            }
        }
    }

    /**
     * Install all the bundles found at a certain folder. It install the bundles sorted by the name of the file. If any
     * subfolder found, then try to install also.
     * 
     * @param context {@link BundleContext} context to install bundles
     * @param folder {@link String} folder path to search bundles
     * @return List<Bundle/> list of installed bundles
     * @throws BundleException
     */
    private static List<Bundle> installFolderBundles( BundleContext context, String folder )
        throws BundleException
    {
        List<Bundle> installedBundles = new LinkedList<Bundle>();
        String[] bundles = new File( folder ).list();
        ArrayList<String> albundles = new ArrayList<String>();
        for ( String bundle : bundles )
        {
            albundles.add( bundle );
        }
        Collections.sort( albundles, new Comparator<String>()
        {

            @Override
            public int compare( String o1, String o2 )
            {
                return ( o1 ).compareTo( o2 );
            }
        } );
        for ( String string : albundles )
        {
            String path = folder + File.separatorChar + string;
            if ( new File( path ).isDirectory() )
            {
                List<Bundle> folderInstalled = installFolderBundles( context, path );
                installedBundles.addAll( folderInstalled );
            }
            else
            {
                String sfile = "file:" + folder + File.separatorChar + string;
                System.out.println( "INSTALLING " + sfile );
                installedBundles.add( context.installBundle( sfile ) );
            }
        }

        return installedBundles;
    }

    private static void copyFile( File source, File dest )
        throws IOException
    {
        FileChannel sourceChannel = null;
        FileChannel destChannel = null;
        try
        {
            sourceChannel = new FileInputStream( source ).getChannel();
            destChannel = new FileOutputStream( dest ).getChannel();
            destChannel.transferFrom( sourceChannel, 0, sourceChannel.size() );
        }
        finally
        {
            sourceChannel.close();
            destChannel.close();
        }
    }

    /**
     * Set the jetty configuration
     */
    private static void setJettyConfig()
    {
        boolean secureProtocol = mc.isMessicSecureProtocol();
        String messicHttpPort = mc.getHttpPort();
        String messicHttpsPort = mc.getHttpsPort();
        String messicPort = ( secureProtocol ? messicHttpsPort : messicHttpPort );

        try
        {

            if ( secureProtocol )
            {
                File ffrom = new File( "./jetty/etc/jetty-selector-ssl.xml" );
                File fto = new File( "./jetty/etc/jetty-selector.xml" );
                copyFile( ffrom, fto );
            }
            else
            {
                File ffrom = new File( "./jetty/etc/jetty-selector-normal.xml" );
                File fto = new File( "./jetty/etc/jetty-selector.xml" );
                copyFile( ffrom, fto );
            }
        }
        catch ( IOException ioe )
        {
            ioe.printStackTrace();
        }

        int port = 0;
        if ( messicPort != null )
        {
            try
            {
                port = Integer.valueOf( messicPort );
            }
            catch ( Exception e )
            {

            }
            if ( port == 0 || !isPortAvailable( port ) )
            {
                port = findValidPort( secureProtocol );
            }
        }
        else
        {
            port = findValidPort( secureProtocol );
        }

        System.setProperty( "messicTimeout", mc.getMessicTimeout() );
        System.setProperty( "messicPort", "" + port );
        System.setProperty( "messicSecurePort", "" + port );
        System.setProperty( "jetty.home", "./jetty" );

        File ftmp = new File( "./jetty/work" );
        if ( ftmp.exists() )
        {
            try
            {
                FileUtils.deleteDirectory( ftmp );
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }
        }
        ftmp.mkdirs();
        // this tmpdir is for the war extraction. It's needed, if not, it get the path from the bundle osgi, and it
        // sometimes contains url paths, like %20%, and so on.
        System.setProperty( "java.io.tmpdir", "./jetty/work" );

        try
        {
            Properties pcurrentPort = new Properties();
            pcurrentPort.put( "currentPort", "" + port );
            pcurrentPort.put( "secured", "" + secureProtocol );
            FileOutputStream fos = new FileOutputStream( "./currentport" );
            pcurrentPort.store( fos, "The last launched port of messic" );
            fos.close();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }

    private static int findValidPort( boolean https )
    {
        if ( OSValidator.isWindows() )
        {
            if ( !https )
            {
                // lets try with port 80
                if ( isPortAvailable( 80 ) )
                {
                    return 80;
                }
            }
            else
            {
                // lets try with port 443
                if ( isPortAvailable( 443 ) )
                {
                    return 443;
                }
            }
        }

        int i = 8080;
        if ( https )
        {
            i = 8543;
        }

        for ( ; i < 9090; i++ )
        {
            if ( isPortAvailable( i ) )
            {
                return i;
            }
        }

        return -1;
    }

    /**
     * From apache camel Checks to see if a specific port is available.
     * 
     * @param port the port to check for availability
     */
    public static boolean isPortAvailable( int port )
    {
        ServerSocket ss = null;
        DatagramSocket ds = null;
        try
        {
            ss = new ServerSocket( port );
            ss.setReuseAddress( true );
            ds = new DatagramSocket( port );
            ds.setReuseAddress( true );
            return true;
        }
        catch ( IOException e )
        {
        }

        finally
        {
            if ( ds != null )
            {
                ds.close();
            }

            if ( ss != null )
            {
                try
                {
                    ss.close();
                }
                catch ( IOException e )
                {
                    /* should not be thrown */
                }
            }
        }

        return false;
    }

    /**
     * Get the Felix configuration based on the config.properties stored at ./felix/conf folder
     * 
     * @return Map<String,String/> configuration properties
     */
    private static Map<String, String> getFelixConfig()
    {
        Map<String, String> config = new HashMap<String, String>();
        // config.put("org.osgi.framework.bootdelegation", "sun.*,com.sun.*");
        Properties p = new Properties();
        try
        {
            p.load( new FileInputStream( new File( "./felix/conf/config.properties" ) ) );
            Enumeration<Object> keys = p.keys();
            while ( keys.hasMoreElements() )
            {
                String key = "" + keys.nextElement();
                String value = p.getProperty( key );
                config.put( key, value );
            }
        }
        catch ( FileNotFoundException e )
        {
            e.printStackTrace();
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
        return config;
    }

}
