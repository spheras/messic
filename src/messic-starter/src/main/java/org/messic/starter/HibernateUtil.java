package org.messic.starter;

import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil
{

    private static final SessionFactory sessionFactory;

    static
    {
        try
        {
            MessicConfig mc = new MessicConfig();
            String musicFolder = mc.getMusicFolder();

            Configuration c = new Configuration().configure();
            c.setProperty( "hibernate.connection.url", "jdbc:h2:" + musicFolder + "/.database/db;DB_CLOSE_DELAY=-1" );
            // Create the SessionFactory from hibernate.cfg.xml
            sessionFactory = c.buildSessionFactory();
        }
        catch ( Throwable ex )
        {
            // Make sure you log the exception, as it might be swallowed
            System.err.println( "Initial SessionFactory creation failed." + ex );
            throw new ExceptionInInitializerError( ex );
        }
    }

    public static SessionFactory getSessionFactory()
    {
        return sessionFactory;
    }

    public static void closeSessionFactory()
    {
        SessionFactory factory = getSessionFactory();
        factory.close();
    }

}