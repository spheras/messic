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

public class Starter
{

    public static void main( String[] args )
        throws Exception
    {
        System.out.println( "[messic-monitor] Starting Messic Monitor" );

        if ( !GraphicsEnvironment.isHeadless() )
        {
            final boolean messicRunning = Util.isMessicRunning();
            if ( !messicRunning )
            {
                Util.launchMessicService( new MessicLaunchedObserver()
                {

                    @Override
                    public void messicLaunched()
                    {
                        try
                        {
                            showMonitor( messicRunning );
                        }
                        catch ( Exception e )
                        {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                } );
            }
        }
        else
        {
            if ( args.length > 0 )
            {
                if ( args[0].toUpperCase().equals( "STOP" ) )
                {
                    Util.stopMessicService();
                }
                else if ( args[0].toUpperCase().equals( "START" ) )
                {
                    Util.launchMessicService( null);
                }
            }
        }
    }

    private static void showMonitor( boolean messicRunning )
        throws Exception
    {
        ProcessMonitorSwing pm = new ProcessMonitorSwing();
        pm.setVisible( true );

    }

}
