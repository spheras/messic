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
package org.messic.android.datamodel;

public class MDMLogin
{
    private boolean success;

    private String targetUrl;

    private String userId;

    private String messic_token;

    /**
     * @return the success
     */
    public boolean isSuccess()
    {
        return success;
    }

    /**
     * @param success the success to set
     */
    public void setSuccess( boolean success )
    {
        this.success = success;
    }

    /**
     * @return the targetUrl
     */
    public String getTargetUrl()
    {
        return targetUrl;
    }

    /**
     * @param targetUrl the targetUrl to set
     */
    public void setTargetUrl( String targetUrl )
    {
        this.targetUrl = targetUrl;
    }

    /**
     * @return the userId
     */
    public String getUserId()
    {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId( String userId )
    {
        this.userId = userId;
    }

    /**
     * @return the messic_token
     */
    public String getMessic_token()
    {
        return messic_token;
    }

    /**
     * @param messic_token the messic_token to set
     */
    public void setMessic_token( String messic_token )
    {
        this.messic_token = messic_token;
    }
}
