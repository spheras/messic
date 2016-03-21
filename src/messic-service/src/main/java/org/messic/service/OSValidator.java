package org.messic.service;

public class OSValidator
{

    public static void main( String[] args )
    {
        String pathSpec = "*/";
        String path = "/messic/";
        // System.err.println(path.matches( pathSpec ));

        int toffset = path.length() - pathSpec.length() + 1;
        String other = pathSpec;
        int ooffset = 1;
        int len = pathSpec.length() - 1;

        boolean result = path.regionMatches( toffset, other, ooffset, len );
        System.out.println( result );
    }

    private static String OS = System.getProperty( "os.name" ).toLowerCase();

    public static boolean isWindows()
    {

        return ( OS.indexOf( "win" ) >= 0 );

    }

    public static boolean isMac()
    {

        return ( OS.indexOf( "mac" ) >= 0 );

    }

    public static boolean isUnix()
    {

        return ( OS.indexOf( "nix" ) >= 0 || OS.indexOf( "nux" ) >= 0 || OS.indexOf( "aix" ) > 0 );

    }

    public static boolean isSolaris()
    {

        return ( OS.indexOf( "sunos" ) >= 0 );

    }

}