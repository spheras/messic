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
package org.messic.server.api.musicinfo.wikipedia;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * Example of response with a query like:
 * http://es.wikipedia.org/w/api.php?format=xml&action=query&titles=Radiohead|Carmen%20Linares|espa%C3%B1a&prop=revisions&rvprop=content&rvparse
 * 
 * <api>
 *  <query>
 *      <normalized><n from="españa" to="España"/></normalized>
 *      <pages>
 *          <page pageid="269161" ns="0" title="Carmen Linares">
 *              <revisions><rev xml:space="preserve">HERE IS THE CONTENT</rev></revisions>
 *          </page>
 *          <page pageid="972" ns="0" title="España">
 *              <revisions><rev xml:space="preserve">HERE IS THE CONTENT</rev></revisions>
 *          </page>
 *          <page pageid="19300" ns="0" title="Radiohead">
 *              <revisions><rev xml:space="preserve">HERE IS THE CONTENT</rev></revisions>
 *          </page>
 *       </pages>
 *  </query>
 * </api>
 * 
 * @author spheras
 */
class WikipediaXMLReader
    extends DefaultHandler
{

    private final XMLReader xr;

    private StringBuffer htmlResult=new StringBuffer();

    /**
     * Constructor of the reader
     * 
     * @throws SAXException
     */
    public WikipediaXMLReader()
        throws SAXException
    {
        xr = XMLReaderFactory.createXMLReader();
        xr.setContentHandler( this );
        xr.setErrorHandler( this );
    }

    /**
     * Obtain the htmlresult after a read
     * 
     * @return String
     */
    public String getHtmlResult()
    {
        if(htmlResult.length()>0){
            return htmlResult.toString();
        }else{
            return "Nothing found :(";
        }
    }

    /**
     * Read the response of the wikipedia api and get the html content
     * @param responseXML String response of the wikipedia api
     * @return String html response
     * @throws FileNotFoundException
     * @throws IOException
     * @throws SAXException
     */
    public String read( final String responseXML )
        throws FileNotFoundException, IOException, SAXException
    {
        StringReader sr = new StringReader( responseXML );
        xr.parse( new InputSource( sr ) );
        return getHtmlResult();
    }

    @Override
    public void startDocument()
    {
    }

    @Override
    public void endDocument()
    {
    }

    @Override
    public void characters( char[] ch, int start, int length )
        throws SAXException
    {
        if ( inrev )
        {
            this.htmlResult.append( ch,start,length );
        }
        super.characters( ch, start, length );
    }

    /**
     * Flag to know if we are in an inrev xml
     */
    private boolean inrev = false;

    @Override
    public void startElement( String uri, String name, String qName, Attributes atts )
    {
        inrev = false;
        if ( name.equals( "rev" ) )
        {
            inrev = true;
        }
    }

}