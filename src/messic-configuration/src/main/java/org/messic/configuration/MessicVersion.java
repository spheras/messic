package org.messic.configuration;


public class MessicVersion
    implements Comparable<MessicVersion>
{

    public MessicVersion( String version )
    {
        this.sversion = version;
        String[] parts = version.split( "\\." );
        this.version = Integer.valueOf( parts[0] );
        this.revision = Integer.valueOf( parts[1] );
        if ( parts[2].indexOf( "-" ) >= 0 )
        {
            String[] subparts = parts[2].split( "-" );
            this.patch = Integer.valueOf( subparts[0] );
            this.semantic = subparts[1];
        }
        else
        {
            this.patch = Integer.valueOf( parts[2] );
        }
    }

    public String sversion;

    public int version;

    public int revision;

    public int patch;

    public String semantic;

    @Override
    public int compareTo( MessicVersion other )
    {
        if ( this.version == other.version )
        {
            if ( this.revision == other.revision )
            {
                if ( this.patch == other.patch )
                {
                    String thisSemantic = this.semantic.trim().toUpperCase();
                    String otherSemantic = other.semantic.trim().toUpperCase();
                    if ( thisSemantic.equals( otherSemantic ) )
                    {
                        return 0;
                    }
                    else
                    {
                        if ( thisSemantic.equals( "ALPHA" ) )
                        {
                            return -1;
                        }
                        else if ( otherSemantic.equals( "ALPHA" ) )
                        {
                            return 1;
                        }
                        else if ( thisSemantic.equals( "BETA" ) )
                        {
                            return -1;
                        }
                        else if ( otherSemantic.equals( "BETA" ) )
                        {
                            return 1;
                        }
                        else
                        {
                            return 0;
                        }
                    }
                }
                else
                {
                    return this.patch - other.patch;
                }
            }
            else
            {
                return this.revision - other.revision;
            }
        }
        else
        {
            return this.version - other.version;
        }
    }
}
