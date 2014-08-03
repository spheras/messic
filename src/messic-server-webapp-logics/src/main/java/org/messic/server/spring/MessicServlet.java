package org.messic.server.spring;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class MessicServlet
    extends DispatcherServlet
{
    /**
     * 
     */
    private static final long serialVersionUID = -6831672472536060528L;

    public MessicServlet()
    {
        super();
        setContextClass( MessicWebApplicationContext.class );
    }

    public MessicServlet( WebApplicationContext wac )
    {
        super( wac );
        setContextClass( MessicWebApplicationContext.class );
    }

}
