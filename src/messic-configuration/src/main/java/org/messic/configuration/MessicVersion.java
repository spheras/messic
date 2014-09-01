package org.messic.configuration;

// version.revision.patch[-semantic[.patch]]
// semantic can be: alpha, beta, rc
// example: 1.0.5-beta.40
// means: major version is 1, minor version is 0.. there have been applied 5 patch, and this is the beta compiled 40.
public class MessicVersion
    implements Comparable<MessicVersion>
{

    @Override
    public String toString()
    {
        return this.sversion;
    }

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
            if ( parts.length == 4 )
            {
                this.semanticPatch = Integer.valueOf( parts[3] );
            }
        }
        else
        {
            this.patch = Integer.valueOf( parts[2] );
            this.semantic = "";
            this.semanticPatch = 0;
        }
    }

    public String sversion;

    public int version;

    public int revision;

    public int patch;

    public String semantic;

    public int semanticPatch;

    @Override
    public boolean equals( Object obj )
    {
        if ( obj instanceof MessicVersion )
        {
            return ( this.compareTo( (MessicVersion) obj ) == 0 ? true : false );
        }
        else
        {
            return false;
        }
    }

    @Override
    public int compareTo( MessicVersion other )
    {
        if ( this.version == other.version )
        {
            if ( this.revision == other.revision )
            {
                if ( this.patch == other.patch )
                {
                    String thisSemantic = ( this.semantic != null ? this.semantic.trim().toUpperCase() : "" );
                    String otherSemantic = ( other.semantic != null ? other.semantic.trim().toUpperCase() : "" );
                    if ( thisSemantic.equals( otherSemantic ) )
                    {
                        if ( this.semanticPatch < other.semanticPatch )
                        {
                            return -1;
                        }
                        else if ( this.semanticPatch > other.semanticPatch )
                        {
                            return 1;
                        }
                        return 0;
                    }
                    else
                    {
                        if ( thisSemantic.equals( "" ) )
                        {

                            // this is a final version
                            return 1;
                        }
                        if ( otherSemantic.equals( "" ) )
                        {
                            // other is a final version
                            return -1;
                        }
                        else if ( thisSemantic.equals( "ALPHA" ) )
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
                        else if ( thisSemantic.equals( "RC" ) )
                        {
                            return -1;
                        }
                        else if ( otherSemantic.equals( "RC" ) )
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
