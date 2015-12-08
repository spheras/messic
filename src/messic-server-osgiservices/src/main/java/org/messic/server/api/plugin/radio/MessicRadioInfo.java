package org.messic.server.api.plugin.radio;

public class MessicRadioInfo
{

    public MessicRadioStatus status = MessicRadioStatus.NONE;

    // this is the port where the music is being cast
    public int publicURLPort;

    // this is the public URL where the client should connect (empty if the same as the messic server)
    public String publicURLHost;

    // this is the last part of the URL, like /mymount
    public String publicURLPath;

    public long songSid = -1l;

    public int songQueuePosition = -1;

    public String songName;

    public String authorName;

    public String albumName;

}
