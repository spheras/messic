package org.messic.server.api.musicinfo.wikipedia;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;
import java.util.Properties;

import org.messic.server.api.musicinfo.service.MusicInfoPlugin;
import org.xml.sax.SAXException;

public class MusicInfoWikipediaPlugin implements MusicInfoPlugin
{

	public static final String NAME="WIKIPEDIA";
	public static final String PROVIDER_NAME="Wikipedia";
	public static final String EN_DESCRIPTION="Plugin to obtain information of albums, songs and authors from wikipedia.";
	public static final float VERSION=1.0f;
	public static final float MINIMUM_MESSIC_VERSION=1.0f;
	
	//http://es.wikipedia.org/w/api.php?format=xml&action=query&titles=Radiohead&prop=revisions&rvprop=content&rvparse

	/**
	 * Normalize the query text, just replacing the spaces with underscores
	 * @param query {@link String} query text
	 * @return {@link String} query text normalized
	 */
	private String normalizeQuery(String query){
	    String[] tokens=query.split( " " );
	    StringBuffer buf=new StringBuffer();
	    for (int i=0;i<tokens.length;i++){
	        buf.append( tokens[i] +"_");
	    }
	    if(tokens.length>1){
	        buf.deleteCharAt( buf.length()-1);
	    }
	    return buf.toString();
	}

	public String search(String query) throws IOException, SAXException{
	    return search(new Locale("en","en"),query);
	}

	/**
	 * Do a search over wikipedia api for a certain query string
	 * @param locale {@link Locale} locale for wikipedia search
	 * @param query {@link String} string text to query
	 * @return {@link String} string returned by wikipedia (html text)
	 * @throws IOException
	 * @throws SAXException 
	 */
	private String search(Locale locale,String query) throws IOException, SAXException{
	    String country=locale.getCountry();
	    String nquery=normalizeQuery( query );
	    String surl="http://"+country.toLowerCase()+".wikipedia.org/w/api.php?format=xml&action=query&titles="+nquery+"&prop=revisions&rvprop=content&rvparse";
	    URL url=new URL(surl);
	    URLConnection connection=url.openConnection();
	    InputStream is=connection.getInputStream();
	    byte[] readed=readInputStream( is );
	    String result=new String(readed);
	    WikipediaXMLReader wxr=new WikipediaXMLReader();
	    return wxr.read( result );
	}
	
    /**
     * Read an inputstream and return a byte[] with the whole content of the readed at the inputstream
     * @param is {@link InputStream}
     * @return byte[] content
     * @throws IOException
     */
    public static byte[] readInputStream(InputStream is) throws IOException{
        byte[] buffer=new byte[1024];
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        int cant=is.read( buffer );
        while(cant>0){
            baos.write( buffer, 0, cant );
            cant=is.read( buffer );
        }
        
        return baos.toByteArray();
    }
    

	
	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public Properties getConfiguration() {
		return null;
	}

	@Override
	public void setConfiguration(Properties properties) {
		//no configuration
	}


	@Override
	public String getDescription(Locale locale) {
		//TODO any other language
		return EN_DESCRIPTION;
	}

	@Override
	public String getAuthorInfo(Locale locale, String authorName) {
		try
        {
            return search(locale, authorName);
        }
        catch ( IOException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "ERROR: "+ e.getMessage();
        }
        catch ( SAXException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "ERROR: "+ e.getMessage();
        }
	}

	@Override
	public String getAlbumInfo(Locale locale, String authorName,
			String albumName) {
        try
        {
            return search(locale, albumName+"|"+authorName);
        }
        catch ( IOException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "ERROR: "+ e.getMessage();
        }
        catch ( SAXException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "ERROR: "+ e.getMessage();
        }
	}

	@Override
	public String getSongInfo(Locale locale, String authorName,
			String albumName, String songName) {
        try
        {
            return search(locale, songName+"|"+albumName+"|" + authorName);
        }
        catch ( IOException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "ERROR: "+ e.getMessage();
        }
        catch ( SAXException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "ERROR: "+ e.getMessage();
        }
	}
	
	@Override
	public float getVersion() {
		return VERSION;
	}

	@Override
	public float getMinimumMessicVersion() {
		return MINIMUM_MESSIC_VERSION;
	}

    @Override
    public byte[] getProviderIcon()
    {
        InputStream is=MusicInfoWikipediaPlugin.class.getResourceAsStream( "/org/messic/server/api/musicinfo/wikipedia/Wikipedia-logo-v2.svg.png" );
        try
        {
            return readInputStream( is );
        }
        catch ( IOException e )
        {
            //TODO
            return null;
        }
    }

    @Override
    public String getProviderName()
    {
        return PROVIDER_NAME;
    }

}
