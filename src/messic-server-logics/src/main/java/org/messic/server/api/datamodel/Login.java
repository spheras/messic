package org.messic.server.api.datamodel;

import javax.xml.bind.annotation.XmlRootElement;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

@XmlRootElement
@ApiObject( name = "Login", description = "Login response" )
public class Login
{
    @ApiObjectField( description = "login message" )
    private String message;

    @ApiObjectField( description = "flag to know if the login was sucessfull" )
    private boolean sucess;

    @ApiObjectField( description = "target url trying to acess" )
    private String targetURL;

    @ApiObjectField( description = "generated messic token" )
    private String messicToken;

    /**
     * Default constructor
     */
    public Login()
    {
    }

    public static Login sucessLogin( String targetURL, String messicToken )
    {
        Login login = new Login();

        login.sucess = true;
        login.targetURL = targetURL;
        login.messicToken = messicToken;
        return login;
    }

    public static Login failedLogin( String message )
    {
        Login l = new Login();
        l.message = message;
        return l;
    }

    public String getJSON()
    {
        if ( this.sucess )
        {
            return "{\"success\":" + this.sucess + ", \"targetUrl\" : \"" + this.targetURL + "\", \"messic_token\":\""
                + this.messicToken + "\"}";
        }
        else
        {
            String result = "{\"success\":false, \"message\": \"" + this.message + "\"}";
            return result;
        }
    }
}
