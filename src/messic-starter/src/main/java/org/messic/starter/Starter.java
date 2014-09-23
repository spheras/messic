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
package org.messic.starter;

import java.awt.GraphicsEnvironment;
import java.io.File;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.messic.configuration.MessicConfig;

public class Starter
{
    public static final String STARTER_OPTION_HELP = "help";

    public static final String STARTER_OPTION_HELP2 = "?";

    public static final String STARTER_OPTION_START = "start";

    public static final String STARTER_OPTION_STOP = "stop";

    public static final String STARTER_OPTION_GUI = "gui";

    public static final String STARTER_OPTION_JAVA = "java";

    public static final String STARTER_OPTION_CREATECONFIG = "defaultconfig";

    public static void main( String[] args )
        throws Exception
    {
        try
        {
            CommandLine cmd = readOptions( args );
            if ( cmd.hasOption( STARTER_OPTION_HELP ) || cmd.hasOption( STARTER_OPTION_HELP2 ) )
            {
                // automatically generate the help statement
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp( "messic", createOptions() );
                return;
            }

            boolean showGUI = Boolean.valueOf( cmd.getOptionValue( STARTER_OPTION_GUI, "true" ) );

            if ( showGUI && !GraphicsEnvironment.isHeadless() )
            {
                System.out.println( "[messic-monitor] Starting Messic Monitor" );
                final boolean messicRunning = Util.isMessicRunning();
                showMonitor( messicRunning );
            }
            else
            {
                if ( !showGUI )
                {
                    System.out.println( "Welcome to messic without GUI... ;D " );
                    System.out.println( "····································" );
                    System.out.println();
                }

                if ( cmd.hasOption( STARTER_OPTION_CREATECONFIG ) )
                {
                    MessicConfig mc = new MessicConfig( true );
                    Properties p = mc.getConfiguration();
                    p.setProperty( MessicConfig.MESSIC_MUSICFOLDER, System.getProperty( "user.home" )
                        + File.separatorChar + "messic-data" );
                    mc.setConfiguration( p );
                    mc.save();
                    System.out.println( "The default configuration has been created at the 'conf' folder." );
                    return;
                }

                if ( cmd.hasOption( STARTER_OPTION_STOP ) )
                {
                    System.out.println( "Stopping Messic Service...." );
                    Util.stopMessicService();
                    System.out.println( "Done." );
                    return;
                }
                if ( cmd.hasOption( STARTER_OPTION_START ) )
                {
                    System.out.println( "Launching Messic Service...." );
                    Util.launchMessicService( null, cmd.getOptionValue( STARTER_OPTION_JAVA ) );
                    return;
                }

                // finally, if no options specified, we show the help message
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp( "messic", createOptions() );
                return;
            }
        }
        finally
        {
            System.out.println();
            System.out.println( "bye!" );
        }

    }

    private static CommandLine readOptions( String[] args )
        throws ParseException
    {
        Options options = createOptions();
        CommandLineParser parser = new GnuParser();
        CommandLine cmd = parser.parse( options, args );
        return cmd;
    }

    private static Options createOptions()
    {
        // create Options object
        Options options = new Options();

        // add t option
        options.addOption( STARTER_OPTION_START, false, "start the messic service" );
        options.addOption( STARTER_OPTION_STOP, false, "stop the messic service" );
        options.addOption( STARTER_OPTION_HELP, false, "show this help" );
        options.addOption( STARTER_OPTION_HELP2, false, "show this help" );
        options.addOption( STARTER_OPTION_GUI,
                           true,
                           "if present, it can establish if the Messic Monitor should be shown or not. (true || false).  This is not necessary when the system doesn't have any display available" );
        options.addOption( STARTER_OPTION_CREATECONFIG, false,
                           "if present, messic will delete the current config file (if exist) and create a new one with the default options" );
        options.addOption( STARTER_OPTION_JAVA, true,
                           "specify the java location. (native will try to launch the shell java, at OS location)" );

        return options;
    }

    private static void showMonitor( boolean messicRunning )
        throws Exception
    {
        ProcessMonitorWindow pm = new ProcessMonitorWindow();
        pm.setVisible( true );
    }

}
