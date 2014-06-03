package org.messic.server.tld;

import org.apache.commons.lang3.StringEscapeUtils;

public class EscapeMessic
{
    public static String escapeHTML(String texto){
        String result=StringEscapeUtils.escapeHtml4( texto );
        return result;
    }

    public static String escapeJS(String texto){
        String result=StringEscapeUtils.escapeEcmaScript( texto );
        return result;
    }
    
    public static String escapeAll(String texto){
        String result=StringEscapeUtils.escapeEcmaScript( StringEscapeUtils.escapeHtml4( texto ) );
        return result;
    }
}
