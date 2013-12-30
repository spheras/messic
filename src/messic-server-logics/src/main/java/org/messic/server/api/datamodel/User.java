package org.messic.server.api.datamodel;

import javax.xml.bind.annotation.XmlRootElement;

import org.messic.server.datamodel.MDOUser;

@XmlRootElement
public class User {
    private Long sid;
    private String name;
    private byte[] avatar;
    private String email;
    private String login;
    private String password;
    private Boolean administrator;
    private String storePath;
    
    /**
     * Default constructor
     */
    public User(){
    	
    }

    /**
     * Copy constructor
     * @param mdoUser {@link MDOUser} to copy
     */
    public User(MDOUser mdoUser){
    	this.setAdministrator(mdoUser.getAdministrator());
    	this.setAvatar(mdoUser.getAvatar());
    	this.setEmail(mdoUser.getEmail());
    	this.setLogin(mdoUser.getLogin());
    	this.setName(mdoUser.getName());
    	this.setPassword(mdoUser.getPassword());
    	this.setSid(mdoUser.getSid());
    	this.setStorePath(mdoUser.getStorePath());
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
