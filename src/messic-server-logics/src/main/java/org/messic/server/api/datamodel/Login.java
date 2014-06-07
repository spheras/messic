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

    @ApiObjectField( description = "user name" )
    private String userId;

    /**
     * Default constructor
     */
    public Login()
    {
    }

    public static Login sucessLogin( String targetURL, String messicToken, String username )
    {
        Login login = new Login();

        login.sucess = true;
        login.targetURL = targetURL;
        login.messicToken = messicToken;
        login.userId = username;
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
            return "{\"success\":" + this.sucess + ", \"targetUrl\" : \"" + this.targetURL + "\",\"userId\" : \""
                + this.userId + "\" , \"messic_token\":\"" + this.messicToken + "\"}";
        }
        else
        {
            String result = "{\"success\":false, \"message\": \"" + this.message + "\"}";
            return result;
        }
    }
}
