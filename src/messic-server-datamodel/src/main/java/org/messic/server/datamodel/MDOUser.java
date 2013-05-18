package org.messic.server.datamodel;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;

@Entity
public class MDOUser
    implements Serializable, MDO
{
    private static final long serialVersionUID = -7500372633805690738L;

    private Long sid;

    private Integer version;

    /** user */
    private String username;

    /** hash password */
    private String hashpass;

    /** current state of the user */
    private String state;

    /** picture */
    private byte[] avatar;

    /**
     * @return the sid
     */
    @Id
    @GeneratedValue
    public Long getSid()
    {
        return sid;
    }

    /**
     * @param sid the sid to set
     */
    public void setSid( Long sid )
    {
        this.sid = sid;
    }

    /**
     * @return the version
     */
    @Version
    public Integer getVersion()
    {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion( Integer version )
    {
        this.version = version;
    }

    /**
     * @return the username
     */
    public String getUsername()
    {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername( String user )
    {
        this.username = user;
    }

    /**
     * @return the hashpass
     */
    public String getHashpass()
    {
        return hashpass;
    }

    /**
     * @param hashpass the hashpass to set
     */
    public void setHashpass( String hashPass )
    {
        this.hashpass = hashPass;
    }

    /**
     * @return the state
     */
    public String getState()
    {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState( String state )
    {
        this.state = state;
    }

    /**
     * @return the avatar
     */
    public byte[] getAvatar()
    {
        return avatar;
    }

    /**
     * @param avatar the avatar to set
     */
    public void setAvatar( byte[] avatar )
    {
        this.avatar = avatar;
    }

}
