/*
 * Copyright (C) 2013 José Amuedo
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import com.mkyong.core.OSValidator;

import sun.tools.jps.JpsOut;

public class Starter
{

    public static void main( String[] args )
        throws InterruptedException, IOException
    {

        if ( !isMessicRunning() )
        {
            launchMessic();
        }
        else
        {
            System.out.println( "RUNNING!!!" );

        }

    }

    private static void launchMessic()
    {
        if ( OSValidator.isWindows() )
        {

        }
        else if ( OSValidator.isMac() )
        {

        }
        else if ( OSValidator.isUnix() )
        {

        }
        else
        {
            System.out.println( "OS not compatible with messic :(" );
        }
    }

    private static boolean isMessicRunning()
        throws IOException
    {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream( baos );
        JpsOut.main( new String[] {}, ps );

        baos.close();
        String str = new String( baos.toByteArray() );
        if ( str.indexOf( "MessicMain" ) >= 0 )
        {
            return true;
        }
        else
        {
            return false;
        }
    }

}
