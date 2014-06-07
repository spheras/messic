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
import org.messic.server.datamodel.MDOUser;

@XmlRootElement
@ApiObject(name="User", description="User of Messic")
public class User {
	@ApiObjectField(description="identification number of the user")
    private Long sid;
	@ApiObjectField(description="name of the user")
    private String name;
	@ApiObjectField(description="binary image for the avatar")
    private byte[] avatar;
	@ApiObjectField(description="email of the user")
    private String email;
	@ApiObjectField(description="username of the user")
    private String login;
	@ApiObjectField(description="password of the user")
    private String password;
	@ApiObjectField(description="flag to know if the user is an administrator")
    private Boolean administrator;
	@ApiObjectField(description="Path to store their songs")
    private String storePath;
	    
    /**
     * Default constructor
     */
    public User(){
    	
    }

    public static User transform(MDOUser user){
		return new User(user);
	}
    
    /**
     * Copy constructor
     * @param mdoUser {@link MDOUser} to copy
     */
    public User(MDOUser mdoUser){
        if(mdoUser!=null){
            this.setAdministrator(mdoUser.getAdministrator());
            this.setAvatar(mdoUser.getAvatar());
            this.setEmail(mdoUser.getEmail());
            this.setLogin(mdoUser.getLogin());
            this.setName(mdoUser.getName());
            this.setPassword(mdoUser.getPassword());
            this.setSid(mdoUser.getSid());
            this.setStorePath(mdoUser.getStorePath());
        }
    }
    
    public Long getSid() {
		return sid;
	}
    
	public void setSid(Long sid) {
		this.sid = sid;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public byte[] getAvatar() {
		return avatar;
	}
	
	public void setAvatar(byte[] avatar) {
		this.avatar = avatar;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getLogin() {
		return login;
	}
	
	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public Boolean getAdministrator() {
		return administrator;
	}
	
	public void setAdministrator(Boolean administrator) {
		this.administrator = administrator;
	}
	
	public String getStorePath() {
		return storePath;
	}
	
	public void setStorePath(String storePath) {
		this.storePath = storePath;
	}

}
