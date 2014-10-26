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
package org.messic.android.controllers.messicdiscovering;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

public class MessicDiscoveringXmlParser
{
    // We don't use namespaces
    private static final String ns = null;

    public MessicServerInstance parse( InputStream in )
        throws XmlPullParserException, IOException
    {
        try
        {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature( XmlPullParser.FEATURE_PROCESS_NAMESPACES, false );
            parser.setInput( in, null );
            parser.nextTag();
            return readMessicInfo( parser );
        }
        finally
        {
            in.close();
        }
    }

    private MessicServerInstance readMessicInfo( XmlPullParser parser )
        throws XmlPullParserException, IOException
    {
        MessicServerInstance md = new MessicServerInstance();

        parser.require( XmlPullParser.START_TAG, ns, "messic" );
        while ( parser.next() != XmlPullParser.END_TAG )
        {
            if ( parser.getEventType() != XmlPullParser.START_TAG )
            {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if ( name.toLowerCase( Locale.US ).equals( "name" ) )
            { 
                if(parser.next() == XmlPullParser.TEXT) {  
                    md.name = parser.getText();
                    parser.next();
                }
            }
            else if ( name.toLowerCase( Locale.US ).equals( "port" ) )
            {
                if(parser.next() == XmlPullParser.TEXT) {  
                    md.port = ( Integer.valueOf( parser.getText() ) );
                    parser.next();
                }
            }
            else if ( name.toLowerCase( Locale.US ).equals( "secure" ) )
            {
                if(parser.next() == XmlPullParser.TEXT) {  
                    md.secured = Boolean.valueOf( parser.getText() );
                    parser.next();
                }
            }
            else if ( name.toLowerCase( Locale.US ).equals( "version" ) )
            {
                if(parser.next() == XmlPullParser.TEXT) {  
                    md.version = parser.getText();
                    parser.next();
                }
            }
        }
        return md;
    }
}